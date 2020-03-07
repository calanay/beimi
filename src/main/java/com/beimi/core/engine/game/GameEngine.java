package com.beimi.core.engine.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.state.GameEvent;
import com.beimi.core.engine.game.task.majiang.CreateMJRaiseHandsTask;
import com.beimi.util.GameUtils;
import com.beimi.util.RandomCharUtil;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.client.NettyClients;
import com.beimi.util.rules.model.Action;
import com.beimi.util.rules.model.ActionEvent;
import com.beimi.util.rules.model.Board;
import com.beimi.util.rules.model.DuZhuBoard;
import com.beimi.util.rules.model.JoinRoom;
import com.beimi.util.rules.model.NextPlayer;
import com.beimi.util.rules.model.Player;
import com.beimi.util.rules.model.Playeready;
import com.beimi.util.rules.model.RecoveryData;
import com.beimi.util.rules.model.SelectColor;
import com.beimi.util.rules.model.TakeCards;
import com.beimi.util.server.handler.BeiMiClient;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.service.repository.es.PlayUserClientESRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import com.corundumstudio.socketio.SocketIOServer;

@Service(value="beimiGameEngine")
public class GameEngine {
	
	@Autowired
	protected SocketIOServer server;

	@Resource
	private KieSession kieSession;
	
	/**
	 * 游戏请求
	 * <p>
	 * <ul>
	 * <li>
	 * 1、玩家加入房间
	 * <li>
	 * 2、通知房间其他用户有新成员加入
	 * <li>
	 * 3、判断是否已[发送恢复连接通知消息]/未[建立状态机]在游戏中处理
	 * </ul>
	 * 
	 * @param userid
	 * @param playway
	 * @param room
	 * @param orgi
	 * @param userClient
	 * @param beiMiClient
	 */
	public void gameRequest(String userid ,String playway, String room, String orgi, 
			PlayUserClient userClient , BeiMiClient beiMiClient ){
		GameEvent gameEvent = gameRequest(userClient.getId(), beiMiClient.getPlayway(), 
				beiMiClient, beiMiClient.getOrgi(), userClient);
		if(gameEvent != null){
			// 举手了，表示游戏可以开始了
			if(userClient != null){
				userClient.setGamestatus(BMDataContext.GameStatusEnum.READY.toString());
			}

			/**
			 * 游戏状态 ， 玩家请求 游戏房间，活动房间状态后，发送事件给 StateMachine，由 StateMachine驱动 游戏状态 ， 此处只负责通知房间内的玩家
			 * 1、有新的玩家加入
			 * 2、给当前新加入的玩家发送房间中所有玩家信息（不包含隐私信息，根据业务需求，修改PlayUserClient的字段，剔除掉隐私信息后发送）
			 */
			ActionTaskUtils.sendEvent("joinroom", new JoinRoom(userClient, gameEvent.getIndex(), 
					gameEvent.getGameRoom().getPlayers() , gameEvent.getGameRoom()) , gameEvent.getGameRoom());
			// 发送给单一玩家的消息
			ActionTaskUtils.sendPlayers(beiMiClient, gameEvent.getGameRoom());
			
			// 当前是在游戏中还是未开始
			Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameEvent.getRoomid(), gameEvent.getOrgi());
			if(board != null){
				Player currentPlayer = null;
				for(Player player : board.getPlayers()){
					if(player.getPlayuser().equals(userClient.getId())){
						currentPlayer = player ; break ;
					}
				}
				if(currentPlayer!=null){
					boolean automic = false ;
					if((board.getLast()!=null && board.getLast().getUserid().equals(currentPlayer.getPlayuser())) || (board.getLast() == null && board.getBanker().equals(currentPlayer.getPlayuser()))){
						automic = true ;
					}
					// 恢复连接发送信息给房间用户
					ActionTaskUtils.sendEvent(
							"recovery",
							new RecoveryData(currentPlayer, board.getLasthands(), 
									board.getNextplayer()!=null ? board.getNextplayer().getNextplayer() : null , 
									25 , automic , board), 
							gameEvent.getGameRoom());
				}
			}else{
				//不在游戏中通知状态机 , 此处应由状态机处理异步执行
				GameUtils.getGame(beiMiClient.getPlayway(), gameEvent.getOrgi()).change(gameEvent);
			}
		}
	}
	
	/**
	 * 玩家加入游戏房间[加入已有房间、创建新房间]
	 * <p>
	 * 玩家房间选择， 新请求，游戏撮合， 如果当前玩家是断线重连， 或者是 退出后进入的，则第一步检查是否已在房间
	 * 如果已在房间，直接返回
	 * 
	 * @param userid
	 * @param orgi
	 * @return
	 */
	public GameEvent gameRequest(String userid, String playway, BeiMiClient beiMiClient, String orgi, PlayUserClient playUser){
		String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(userid, orgi) ;
		GamePlayway gamePlayway = (GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(playway, orgi) ;
		// 需排队
		boolean needtakequene = false;
		GameEvent gameEvent = null ;
		
		if(gamePlayway != null){
			gameEvent = new GameEvent(gamePlayway.getPlayers() , gamePlayway.getCardsnum() , orgi) ;
			GameRoom gameRoom = null ;
			
			if(!StringUtils.isBlank(roomid) 
					&& CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) != null){//
				gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;		//直接加入到 系统缓存 （只有一个地方对GameRoom进行二次写入，避免分布式锁）
			}else{
				//房卡游戏 , 创建ROOM
				if(beiMiClient.getExtparams()!=null 
						&& BMDataContext.BEIMI_SYSTEM_ROOM.equals(beiMiClient.getExtparams().get("gamemodel"))){
					gameRoom = this.creatGameRoom(gamePlayway, userid , true , beiMiClient) ;
				}else{	//
					/**
					 * 大厅游戏 ， 撮合游戏 , 发送异步消息，通知RingBuffer进行游戏撮合，撮合算法描述如下：
					 * 1、按照查找
					 * 
					 */
					gameRoom = (GameRoom) CacheHelper.getQueneCache().poll(playway , orgi) ;
					if(gameRoom != null){	
						/**
						 * 修正获取gameroom获取的问题，因为删除房间的时候，为了不损失性能，没有将 队列里的房间信息删除，如果有玩家获取到这个垃圾信息
						 * 则立即进行重新获取房价， 
						 */
						while(CacheHelper.getGameRoomCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) == null){
							gameRoom = (GameRoom) CacheHelper.getQueneCache().poll(playway , orgi) ;
							if(gameRoom == null){
								break ;
							}
						}
					}
					
					//无房间,创建新房间
					if(gameRoom == null){
						gameRoom = this.creatGameRoom(gamePlayway, userid , false , beiMiClient) ;
					}else{
						// 从后往前坐，房主进入以后优先坐在首位
						playUser.setPlayerindex(System.currentTimeMillis());
						// 需要排队
						needtakequene =  true;
					}
				}
			}
			
			if(gameRoom != null){
				// 设置游戏当前已经进行的局数
				gameRoom.setCurrentnum(0);
				// 更新缓存
				CacheHelper.getGameRoomCacheBean().put(gameRoom.getId(), gameRoom, orgi);
				// 如果当前房间到达了最大玩家数量，则不再加入到撮合队列
				List<PlayUserClient> playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
				if(playerList.size() == 0){
					gameEvent.setEvent(BeiMiGameEvent.ENTER.toString());
				}else{	
					gameEvent.setEvent(BeiMiGameEvent.JOIN.toString());
				}
				gameEvent.setGameRoom(gameRoom);
				gameEvent.setRoomid(gameRoom.getId());
				
				// 玩家加入房间[数据保存、建立房间socket连接]
				this.joinRoom(gameRoom, playUser, playerList);
				
				for(PlayUserClient temp : playerList){
					if(temp.getId().equals(playUser.getId())){
						// 设置当前玩家顺序号
						gameEvent.setIndex(playerList.indexOf(temp)); break ;
					}
				}
				
				/**
				 * 如果当前房间到达了最大玩家数量，则不再加入到 撮合队列
				 */
				if(playerList.size() < gamePlayway.getPlayers() && needtakequene == true){
					// 未达到最大玩家数量，加入到游戏撮合 队列，继续撮合
					CacheHelper.getQueneCache().put(gameRoom, orgi);
				}
				
			}
		}
		
		return gameEvent;
	}
	/**
	 * 玩家加入房间
	 * <p>
	 * <ul>
	 * <li>
	 * 1、将玩家数据保存至房间玩家数组中
	 * <li>
	 * 2、游戏玩家加入房间socket
	 * <li>
	 * 3、将玩家数据保存加入游戏玩家缓存中[key为玩家id]
	 * </ul>
	 * 
	 * @param gameRoom
	 * @param playUser
	 * @param playerList
	 */
	public void joinRoom(GameRoom gameRoom , PlayUserClient playUser , List<PlayUserClient> playerList){
		boolean inroom = false ;
		for(PlayUserClient user : playerList){
			if(user.getId().equals(playUser.getId())){
				inroom = true ; break ;
			}
		}
		
		if(inroom == false){
			playUser.setPlayerindex(System.currentTimeMillis());
			playUser.setGamestatus(BMDataContext.GameStatusEnum.READY.toString());
			playUser.setPlayertype(BMDataContext.PlayerTypeEnum.NORMAL.toString());
			playUser.setRoomid(gameRoom.getId());
			playUser.setRoomready(false);
			
			playerList.add(playUser);
			
			NettyClients.getInstance().joinRoom(playUser.getId(), gameRoom.getId());
			// 将用户加入到 room ， MultiCache
			CacheHelper.getGamePlayerCacheBean().put(playUser.getId(), playUser, playUser.getOrgi());
		}
		
		/**
		 *	不管状态如何，玩家一定会加入到这个房间 
		 */
		CacheHelper.getRoomMappingCacheBean().put(playUser.getId(), gameRoom.getId(), playUser.getOrgi());
	}
	
	/**
	 * 抢地主，斗地主
	 * @param roomid

	 * @param orgi
	 * @return
	 */
	public void actionRequest(String roomid, PlayUserClient playUser, String orgi , boolean accept){
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			DuZhuBoard board = (DuZhuBoard) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
			Player player = board.player(playUser.getId()) ;
			board = ActionTaskUtils.doCatch(board, player , accept) ;
			
			ActionTaskUtils.sendEvent("catchresult",new GameBoard(player.getPlayuser() , player.isAccept(), board.isDocatch() , board.getRatio()),gameRoom) ;
			GameUtils.getGame(gameRoom.getPlayway() , orgi).change(gameRoom , BeiMiGameEvent.AUTO.toString() , 15);	//通知状态机 , 继续执行
			
			CacheHelper.getBoardCacheBean().put(gameRoom.getId() , board , gameRoom.getOrgi()) ;
			
			CacheHelper.getExpireCache().put(gameRoom.getRoomid(), ActionTaskUtils.createAutoTask(1, gameRoom));
		}
	}
	
	/**
	 * 开始游戏
	 * <p>
	 * <ul>
	 * <li>
	 * 设置玩家数据缓存
	 * <li>
	 * 如果房间所有人员准备就绪，发牌
	 * <li>
	 * Disruptor 发布方式进行玩家数据保存
	 * <li>
	 * 准备玩牌消息通知
	 * </ul>
	 * 
	 * @param roomid		游戏房间id
	 * @param playUser		游戏玩家用户
	 * @param orgi			orgi {@link BMDataContext#SYSTEM_ORGI}
	 * @param opendeal		是否明牌，是：true, 否：false
	 */
	public void startGameRequest(String roomid, PlayUserClient playUser, String orgi , boolean opendeal){
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			playUser.setRoomready(true);
			if(opendeal == true){
				playUser.setOpendeal(opendeal);
			}
			
			// 设置玩家数据缓存
			CacheHelper.getGamePlayerCacheBean().put(playUser.getId(), playUser, playUser.getOrgi());
			
			// 如果房间所有人员准备就绪，发牌
			ActionTaskUtils.roomReady(gameRoom, GameUtils.getGame(gameRoom.getPlayway() , gameRoom.getOrgi()));
			
			// Disruptor 发布方式进行玩家数据保存
			UKTools.published(playUser, BMDataContext.getContext().getBean(PlayUserClientESRepository.class));
			
			// 准备玩牌消息通知
			ActionTaskUtils.sendEvent(playUser.getId(), new Playeready(playUser.getId() , "playeready"));
		}
	}
	
	
	/**
	 * 提示出牌
	 * 
	 * @param roomid

	 * @param orgi
	 * @return
	 */
	public void cardTips(String roomid, PlayUserClient playUser, String orgi , String cardtips){
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			DuZhuBoard board = (DuZhuBoard) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
			Player player = board.player(playUser.getId()) ;
			
			TakeCards takeCards = null ;
			
			if(!StringUtils.isBlank(cardtips)){
				String[] cards = cardtips.split(",") ;
				byte[] tipCards = new byte[cards.length] ;
				for(int i= 0 ; i<cards.length ; i++){
					tipCards[i] = Byte.parseByte(cards[i]) ;
				}
				takeCards = board.cardtip(player, board.getCardTips(player, tipCards)) ;
			}
			if(takeCards == null || takeCards.getCards() == null){
				if(board.getLast() != null && !board.getLast().getUserid().equals(player.getPlayuser())){	//当前无出牌信息，刚开始出牌，或者出牌无玩家 压
					takeCards = board.cardtip(player, board.getLast()) ;
				}else{
					takeCards = board.cardtip(player, null) ;
				}
			}
			
			if(takeCards.getCards() == null){
				takeCards.setAllow(false);	//没有 管的起的牌
			}
			ActionTaskUtils.sendEvent("cardtips", takeCards ,gameRoom) ;
		}
	}
	
	/**
	 * 出牌，并校验出牌是否合规
	 * 
	 * @param roomid
	 * 
	 * @param auto 是否自动出牌，超时/托管/AI会调用 = true

	 * @param orgi
	 * @return
	 */
	public TakeCards takeCardsRequest(String roomid, String playUserClient, String orgi , boolean auto , byte[] playCards){
		TakeCards takeCards = null ;
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
			if(board!=null){
				Player player = board.player(playUserClient) ;
				if(board.getNextplayer()!=null && player.getPlayuser().equals(board.getNextplayer().getNextplayer()) && board.getNextplayer().isTakecard() == false){
					takeCards = board.takeCardsRequest(gameRoom, board, player, orgi, auto, playCards) ;
				}
			}
		}
		return takeCards ;
	}
	
	/**
	 * 检查是否所有玩家 都已经处于就绪状态，如果所有玩家都点击了 继续开始游戏，则发送一个 ALL事件，继续游戏，
	 * 否则，等待10秒时间，到期后如果玩家还没有就绪，就将该玩家T出去，等待新玩家加入
	 * @param roomid
	 * @param userid
	 * @param orgi
	 * @return
	 */
	public void restartRequest(String roomid , PlayUserClient playerUser, BeiMiClient beiMiClient , boolean opendeal){
		boolean notReady = false ;
		List<PlayUserClient> playerList = null ;
		GameRoom gameRoom = null ;
		if(!StringUtils.isBlank(roomid)){
			gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, playerUser.getOrgi()) ;
			playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
			if(playerList!=null && playerList.size() > 0){
				/**
				 * 有一个 等待 
				 */
				for(PlayUserClient player : playerList){
					if(player.isRoomready() == false){
						notReady = true ; break ;
					}
				}
			}
		}
		if(notReady == true && gameRoom!=null){
			/**
			 * 需要增加一个状态机的触发事件：等待其他人就绪，超过5秒以后未就绪的，直接踢掉，然后等待机器人加入
			 */
			this.startGameRequest(roomid, playerUser, playerUser.getOrgi(), opendeal);
		}else if(playerList == null || playerList.size() == 0 || gameRoom == null){//房间已解散
			BMDataContext.getGameEngine().gameRequest(playerUser.getId(), beiMiClient.getPlayway(), beiMiClient.getRoom(), beiMiClient.getOrgi(), playerUser , beiMiClient) ;
			/**
			 * 结算后重新开始游戏
			 */
			playerUser.setRoomready(true);
			CacheHelper.getGamePlayerCacheBean().put(playerUser.getId(), playerUser, playerUser.getOrgi());
		}
	}
	
	/**
	 * 出牌，并校验出牌是否合规
	 * @param roomid
	 *
	 * @param userid
	 * @param orgi
	 * @return
	 */
	public SelectColor selectColorRequest(String roomid, String userid, String orgi , String color){
		SelectColor selectColor = null ;
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
			if(board!=null){
				//超时了 ， 执行自动出牌
//				Player[] players = board.getPlayers() ;
				/**
				 * 检查是否所有玩家都已经选择完毕 ， 如果所有人都选择完毕，即可开始
				 */
				selectColor = new SelectColor(board.getBanker());
				if(!StringUtils.isBlank(color)){
					if(!StringUtils.isBlank(color) && color.matches("[0-2]{1}")){
						selectColor.setColor(Integer.parseInt(color));
					}else{
						selectColor.setColor(0);
					}
					selectColor.setTime(System.currentTimeMillis());
					selectColor.setCommand("selectresult");
					
					selectColor.setUserid(userid);
				}
				boolean allselected = true ;
				for(Player ply : board.getPlayers()){
					if(ply.getPlayuser().equals(userid)){
						if(!StringUtils.isBlank(color) && color.matches("[0-2]{1}")){
							ply.setColor(Integer.parseInt(color));
						}else{
							ply.setColor(0);
						}
						ply.setSelected(true);
					}
					if(!ply.isSelected()){
						allselected = false ;
					}
				}
				CacheHelper.getBoardCacheBean().put(gameRoom.getId() , board, gameRoom.getOrgi());	//更新缓存数据
				ActionTaskUtils.sendEvent("selectresult", selectColor , gameRoom);	
				/**
				 * 检查是否全部都已经 定缺， 如果已全部定缺， 则发送 开打 
				 */
				if(allselected){
					/**
					 * 重置计时器，立即执行
					 */
					CacheHelper.getExpireCache().put(gameRoom.getId(), new CreateMJRaiseHandsTask(1 , gameRoom , gameRoom.getOrgi()) );
					GameUtils.getGame(gameRoom.getPlayway() , orgi).change(gameRoom , BeiMiGameEvent.RAISEHANDS.toString() , 0);	
				}
			}
		}
		return selectColor ;
	}
	
	/**
	 * 麻将 ， 杠碰吃胡过
	 * @param roomid
	 * 
	 * @param userid
	 * @param orgi
	 * @return
	 */
	public ActionEvent actionEventRequest(String roomid, String userid, String orgi , String action){
		ActionEvent actionEvent = null ;
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
			if(board!=null){
				Player player = board.player(userid) ;
				byte card = board.getLast().getCard() ;
				actionEvent = new ActionEvent(board.getBanker() , userid , card , action);
				if(!StringUtils.isBlank(action) && action.equals(BMDataContext.PlayerAction.GUO.toString())){
					/**
					 * 用户动作，选择 了 过， 下一个玩家直接开始抓牌 
					 * bug，待修复：如果有多个玩家可以碰，则一个碰了，其他玩家就无法操作了
					 */
					board.dealRequest(gameRoom, board, orgi , false , null);
				}else if(!StringUtils.isBlank(action) && action.equals(BMDataContext.PlayerAction.PENG.toString()) && allowAction(card, player.getActions() , BMDataContext.PlayerAction.PENG.toString())){
					Action playerAction = new Action(userid , action , card);
					
					int color = card / 36 ;
					int value = card % 36 / 4 ;
					List<Byte> otherCardList = new ArrayList<Byte>(); 
					for(int i=0 ; i<player.getCards().length ; i++){
						if(player.getCards()[i]/36 == color && (player.getCards()[i]%36) / 4 == value){
							continue ;
						}
						otherCardList.add(player.getCards()[i]) ;
					}
					byte[] otherCards = new byte[otherCardList.size()] ;
					for(int i=0 ; i<otherCardList.size() ; i++){
						otherCards[i] = otherCardList.get(i) ;
					}
					player.setCards(otherCards);
					player.getActions().add(playerAction) ;
					
					board.setNextplayer(new NextPlayer(userid , false));
					
					actionEvent.setTarget(board.getLast().getUserid());
					ActionTaskUtils.sendEvent("selectaction", actionEvent , gameRoom);
					
					CacheHelper.getBoardCacheBean().put(gameRoom.getId() , board, gameRoom.getOrgi());	//更新缓存数据
					
					board.playcards(board, gameRoom, player, orgi);
					
				}else if(!StringUtils.isBlank(action) && action.equals(BMDataContext.PlayerAction.GANG.toString()) && allowAction(card, player.getActions() , BMDataContext.PlayerAction.GANG.toString())){
					if(board.getNextplayer().getNextplayer().equals(userid)){
						card = GameUtils.getGangCard(player.getCards()) ;
						actionEvent = new ActionEvent(board.getBanker() , userid , card , action);
						actionEvent.setActype(BMDataContext.PlayerGangAction.AN.toString());
					}else{
						actionEvent.setActype(BMDataContext.PlayerGangAction.MING.toString());	//还需要进一步区分一下是否 弯杠
					}
					/**
					 * 检查是否有弯杠
					 */
					Action playerAction = new Action(userid , action , card);
					for(Action ac : player.getActions()){
						if(ac.getCard() == card && ac.getAction().equals(BMDataContext.PlayerAction.PENG.toString())){
							ac.setGang(true);
							ac.setType(BMDataContext.PlayerGangAction.WAN.toString());
							playerAction = ac ;
							break ;
						}
					}
					int color = card / 36 ;
					int value = card % 36 / 4 ;
					List<Byte> otherCardList = new ArrayList<Byte>(); 
					for(int i=0 ; i<player.getCards().length ; i++){
						if(player.getCards()[i]/36 == color && (player.getCards()[i]%36) / 4 == value){
							continue ;
						}
						otherCardList.add(player.getCards()[i]) ;
					}
					byte[] otherCards = new byte[otherCardList.size()] ;
					for(int i=0 ; i<otherCardList.size() ; i++){
						otherCards[i] = otherCardList.get(i) ;
					}
					player.setCards(otherCards);
					player.getActions().add(playerAction) ;
					
					actionEvent.setTarget("all");	//只有明杠 是 其他人打出的 ， target 是单一对象
					
					ActionTaskUtils.sendEvent("selectaction", actionEvent , gameRoom);
					
					/**
					 * 杠了以后， 从 当前 牌的 最后一张开始抓牌
					 */
					board.dealRequest(gameRoom, board, orgi , true , userid);
				}else if(!StringUtils.isBlank(action) && action.equals(BMDataContext.PlayerAction.HU.toString())){	//判断下是不是 真的胡了 ，避免外挂乱发的数据
					Action playerAction = new Action(userid , action , card);
					player.getActions().add(playerAction) ;
					GamePlayway gamePlayway = (GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(gameRoom.getPlayway(), gameRoom.getOrgi()) ;
					/**
					 * 不同的胡牌方式，处理流程不同，推倒胡，直接进入结束牌局 ， 血战：当前玩家结束牌局，血流：继续进行，下一个玩家
					 */
					if(gamePlayway.getWintype().equals(BMDataContext.MaJiangWinType.TUI.toString())){		//推倒胡
						GameUtils.getGame(gameRoom.getPlayway() , orgi).change(gameRoom , BeiMiGameEvent.ALLCARDS.toString() , 0);	//打完牌了,通知结算
					}else{ //血战到底
						 if(gamePlayway.getWintype().equals(BMDataContext.MaJiangWinType.END.toString())){		//标记当前玩家的状态 是 已结束
							 player.setEnd(true);
						 }
						 player.setHu(true); 	//标记已经胡了
						 /**
						  * 当前 Player打上标记，已经胡牌了，杠碰吃就不会再有了
						  */
						 /**
						  * 下一个玩家出牌
						  */
						player = board.nextPlayer(board.index(player.getPlayuser())) ;
						/**
						 * 记录胡牌的相关信息，推倒胡 | 血战 | 血流
						 */
						board.setNextplayer(new NextPlayer(player.getPlayuser() , false));
						
						actionEvent.setTarget(board.getLast().getUserid());
						/**
						 * 用于客户端播放 胡牌的 动画 ， 点胡 和 自摸 ，播放不同的动画效果
						 */
						ActionTaskUtils.sendEvent("selectaction", actionEvent , gameRoom);
						CacheHelper.getBoardCacheBean().put(gameRoom.getId() , board, gameRoom.getOrgi());	//更新缓存数据
						
						/**
						 * 杠了以后， 从 当前 牌的 最后一张开始抓牌
						 */
						board.dealRequest(gameRoom, board, orgi , true , player.getPlayuser());
					}
				}
			}
		}
		return actionEvent ;
	}
	/**
	 * 为防止同步数据错误，校验是否允许刚碰牌
	 * @param card
	 * @param actions
	 * @return
	 */
	public boolean allowAction(byte card , List<Action> actions , String actiontype){
		int take_color = card / 36 ;
		int take_value = card%36 / 4 ;
		boolean allow = true ;
		for(Action action : actions){
			int color = action.getCard() / 36 ;
			int value = action.getCard() % 36 / 4 ;
			if(take_color == color && take_value == value && action.getAction().equals(actiontype)){
				allow = false ; break ;
			}
		}
		return allow ;
	}
	
	/**
	 * 出牌，不出牌
	 * @param roomid

	 * @param orgi
	 * @return
	 */
	public void noCardsRequest(String roomid, PlayUserClient playUser, String orgi){
		
	}
	
	/**
	 * 加入房间，房卡游戏
	 * @param roomid

	 * @param orgi
	 * @return
	 */
	public GameRoom joinRoom(String roomid, PlayUserClient playUser, String orgi){
		GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;
		if(gameRoom!=null){
			CacheHelper.getGamePlayerCacheBean().put(gameRoom.getId(), playUser, orgi); //将用户加入到 room ， MultiCache
		}
		return gameRoom ;
	}
	
	/**
	 * 退出房间
	 * 1、房卡模式，userid是房主，则解散房间
	 * 2、大厅模式，如果游戏未开始并且房间仅有一人，则解散房间
	 * @param orgi
	 * @return
	 */
	public GameRoom leaveRoom(PlayUserClient playUser , String orgi){
		GameRoom gameRoom = whichRoom(playUser.getId(), orgi) ;
		if(gameRoom!=null){
			List<PlayUserClient> players = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), orgi) ;
			if(gameRoom.isCardroom()){
				CacheHelper.getGameRoomCacheBean().delete(gameRoom.getId(), gameRoom.getOrgi()) ;
				CacheHelper.getGamePlayerCacheBean().clean(gameRoom.getId() , orgi) ;
				UKTools.published(gameRoom , null , BMDataContext.getContext().getBean(GameRoomRepository.class) , BMDataContext.UserDataEventType.DELETE.toString());
			}else{
				if(players.size() <= 1){
					//解散房间 , 保留 ROOM资源 ， 避免 从队列中取出ROOM
					CacheHelper.getGamePlayerCacheBean().clean(gameRoom.getId() , orgi) ;
				}else{
					CacheHelper.getGamePlayerCacheBean().delete(playUser.getId(), orgi) ;
				}
			}
		}
		return gameRoom;
	}
	
	/**
	 * 当前用户所在的房间
	 * @param userid
	 * @param orgi
	 * @return
	 */
	public GameRoom whichRoom(String userid, String orgi){
		GameRoom gameRoom = null ;
		String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(userid, orgi) ;
		if(!StringUtils.isBlank(roomid)){//
			gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, orgi) ;		//直接加入到 系统缓存 （只有一个地方对GameRoom进行二次写入，避免分布式锁）
		}
		return gameRoom;
	}
	
	/**
	 * 结束 当前牌局

	 * @param orgi
	 * @return
	 */
	public void finished(String roomid, String orgi){
		if(!StringUtils.isBlank(roomid)){//
			CacheHelper.getExpireCache().remove(roomid);
			CacheHelper.getBoardCacheBean().delete(roomid, orgi) ;
		}
	}
	
	/**
	 * 创建新房间 ，需要传入房间的玩法 ， 玩法定义在 系统运营后台，玩法创建后，放入系统缓存 ， 客户端进入房间的时候，传入 玩法ID参数
	 * 
	 * @param playway
	 * @param userid
	 * @return
	 */
	private  GameRoom creatGameRoom(GamePlayway playway , String userid , boolean cardroom , BeiMiClient beiMiClient){
		GameRoom gameRoom = new GameRoom();
		gameRoom.setCreatetime(new Date());
		gameRoom.setRoomid(UKTools.getUUID());
		gameRoom.setUpdatetime(new Date());
		
		if(playway!=null){
			gameRoom.setPlayway(playway.getId());
			gameRoom.setRoomtype(playway.getRoomtype());
			gameRoom.setPlayers(playway.getPlayers());
		}
		gameRoom.setPlayers(playway.getPlayers());
		gameRoom.setCardsnum(playway.getCardsnum());
		gameRoom.setCurpalyers(1);
		gameRoom.setCardroom(cardroom);
		gameRoom.setStatus(BeiMiGameEnum.CRERATED.toString());
		gameRoom.setCardsnum(playway.getCardsnum());
		gameRoom.setCurrentnum(0);
		gameRoom.setCreater(userid);
		gameRoom.setMaster(userid);
		gameRoom.setNumofgames(playway.getNumofgames());   //无限制
		gameRoom.setOrgi(playway.getOrgi());

		/**
		 * 房卡模式启动游戏
		 */
		if(beiMiClient.getExtparams()!=null && BMDataContext.BEIMI_SYSTEM_ROOM.equals(beiMiClient.getExtparams().get("gamemodel"))){
			gameRoom.setRoomtype(BMDataContext.ModelType.ROOM.toString());
			gameRoom.setCardroom(true);
			gameRoom.setExtparams(beiMiClient.getExtparams());
			/**
			 * 产生 房间 ID ， 麻烦的是需要处理冲突 ，准备采用的算法是 先生成一个号码池子，然后重分布是缓存的 Queue里获取
			 */
			gameRoom.setRoomid(RandomCharUtil.getRandomNumberChar(6));

			/**
			 * 分配房间号码 ， 并且，启用 规则引擎，对房间信息进行赋值
			 */
			kieSession.insert(gameRoom) ;
			kieSession.fireAllRules() ;
		}else{
			gameRoom.setRoomtype(BMDataContext.ModelType.HALL.toString());
		}
		
		CacheHelper.getQueneCache().put(gameRoom, playway.getOrgi());	//未达到最大玩家数量，加入到游戏撮合 队列，继续撮合
		
		UKTools.published(gameRoom, null, BMDataContext.getContext().getBean(GameRoomRepository.class) , BMDataContext.UserDataEventType.SAVE.toString());
		
		return gameRoom ;
	}
	
	
	/**
	 * 解散房间 , 解散的时候，需要验证下，当前对象是否是房间的创建人
	 */
	public void dismissRoom(GameRoom gameRoom , String userid,String orgi){
		if(gameRoom.getMaster().equals(userid)){
			CacheHelper.getGamePlayerCacheBean().delete(gameRoom.getId(), orgi) ;
			List<PlayUserClient> players = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), orgi) ;
			for(PlayUserClient player : players){
				/**
				 * 解散房间的时候，只清理 AI
				 */
				if(player.getPlayertype().equals(BMDataContext.PlayerTypeEnum.AI.toString())){
					CacheHelper.getGamePlayerCacheBean().delete(player.getId(), orgi) ;
					CacheHelper.getRoomMappingCacheBean().delete(player.getId(), orgi) ;
				}
			}
			/**
			 * 先不删
			 */
//			UKTools.published(gameRoom, null, BMDataContext.getContext().getBean(GameRoomRepository.class) , BMDataContext.UserDataEventType.DELETE.toString());
		}
	}
	
}