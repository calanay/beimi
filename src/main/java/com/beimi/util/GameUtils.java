package com.beimi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;

import com.beimi.config.web.model.Game;
import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.BeiMiGame;
import com.beimi.core.engine.game.Message;
import com.beimi.core.engine.game.iface.ChessGame;
import com.beimi.core.engine.game.impl.DizhuGame;
import com.beimi.core.engine.game.impl.MaJiangGame;
import com.beimi.core.engine.game.model.MJCardMessage;
import com.beimi.core.engine.game.model.Playway;
import com.beimi.core.engine.game.model.Type;
import com.beimi.core.engine.game.pva.PVAOperatorResult;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.rules.model.Action;
import com.beimi.util.rules.model.Board;
import com.beimi.util.rules.model.Player;
import com.beimi.web.model.AccountConfig;
import com.beimi.web.model.AiConfig;
import com.beimi.web.model.BeiMiDic;
import com.beimi.web.model.GameConfig;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GamePlaywayGroup;
import com.beimi.web.model.GamePlaywayGroupItem;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.model.ActRecord;
import com.beimi.web.model.SysDic;
import com.beimi.web.service.repository.es.ActRecordESRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayGroupItemRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayGroupRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import com.corundumstudio.socketio.SocketIOClient;

/**
 * 游戏支持工具类
 * 
 * @author
 *
 */
public class GameUtils {
	private static Map<String,ChessGame> games = new HashMap<String,ChessGame>();
	
	static{
		games.put("dizhu", new DizhuGame()) ;
		games.put("majiang", new MaJiangGame()) ;
	}
	
	/**
	 * 获得具体游戏实现对象
	 * <p>
	 * 具体配置详见：{@link com.beimi.config.web.BeiMiStateMachineHandlerConfig}
	 * 
	 * @param playway
	 * @param orgi
	 * @return
	 */
	public static Game getGame(String playway ,String orgi){
		GamePlayway gamePlayway = (GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(playway, orgi) ;
		
		Game game = null ;
		if(gamePlayway!=null){
			SysDic dic = (SysDic)CacheHelper.getSystemCacheBean().getCacheObject(gamePlayway.getGame(), gamePlayway.getOrgi()) ;
			if(dic.getCode().equals("dizhu") || gamePlayway.getCode().equals("dizhu")){
				game = (Game) BMDataContext.getContext().getBean("dizhuGame");
			}else if(dic.getCode().equals("majiang") || gamePlayway.getCode().equals("majiang")){
				game = (Game) BMDataContext.getContext().getBean("majiangGame");
			}
		}

		return game;
	}
	
	/**
	 * 移除GameRoom
	 * @param gameRoom
	 * @param orgi
	 */
	public static void removeGameRoom(String roomid,String playway,String orgi){
		CacheHelper.getQueneCache().delete(roomid);
	}
	
	/**
	 * 更新玩家状态
	 * 
	 * @param userid
	 * @param orgi
	 */
	public static void updatePlayerClientStatus(String userid , String orgi , String status){
		PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userid, orgi) ;
		if(playUser != null){
			playUser.setPlayertype(status);
			CacheHelper.getApiUserCacheBean().put(userid, playUser, orgi);
			
			if(playUser != null 
					&& !BMDataContext.GameStatusEnum.PLAYING.toString().equals(playUser.getGamestatus())){
				playUser = (PlayUserClient) CacheHelper.getGamePlayerCacheBean().getPlayer(userid, orgi) ;
				
				CacheHelper.getGamePlayerCacheBean().delete(userid, orgi);
				CacheHelper.getRoomMappingCacheBean().delete(userid, orgi);
				
				// 检查，如果房间没真人玩家了并且当前玩家是房主 ，就可以解散房间了
				if(playUser!= null && !StringUtils.isBlank(playUser.getRoomid())){
					GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(playUser.getRoomid(), orgi) ;
					if(gameRoom.getMaster().equals(playUser.getId())){
						// 解散房间，应该需要一个专门的 方法来处理，别直接删缓存了，这样不好！！！
						BMDataContext.getGameEngine().dismissRoom(gameRoom, userid, orgi);
					}
				}
			}
		}
	}
	
	public static Message subsidyPlayerClient(SocketIOClient client , PlayUserClient playUser , String orgi) {
		Message message = null ;
		if(playUser!=null){
			GameConfig gameConfig = (GameConfig) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.getGameConfig(orgi) , orgi);
			int score = 0  ;
			ActRecord actRecord = new ActRecord();
			if(gameConfig!=null && gameConfig.isSubsidy()) {
				actRecord.setEnable(gameConfig.isSubsidy());
				actRecord.setSubgolds(gameConfig.getSubgolds());
				actRecord.setSubtimes(gameConfig.getSubtimes());
				actRecord.setToken(playUser.getId());
				/**
				 * 启用了 破产补助功能，需要校验改玩家当天是否还有申请破产补助的资格 ， 无论是否有资格，都需要给玩家一个回复消息，
				 * 如果有申请资格，需要查询破产补助记录表，按天，则直接补助，并通知玩家 PVA信息更新，如果没有资格，则更新PVA信息，并给出提示消息
				 */
				
				ActRecordESRepository actRecordRes = BMDataContext.getContext().getBean(ActRecordESRepository.class) ;
				int times = actRecordRes.countByPlayeridAndOrgiAndDayAndRectype(playUser.getId(), orgi, UKTools.getDay() , BMDataContext.ActRecordType.SUBSIDY.toString())  ;
				if(times <= gameConfig.getSubtimes()) { //允许补助
					actRecord = createActRecord(playUser, BMDataContext.ActRecordType.SUBSIDY.toString(), orgi, times+1, gameConfig.getSubgolds()) ;
					actRecord.setCommand(BMDataContext.CommandMessageType.SUBSIDY.toString());
					
					message = actRecord ;
					score = actRecord.getScore() ;
					/**
					 * 需要记录下交互日志 ， 更新玩家的 PVA消息
					 */
					PVAOperatorResult result = PvaTools.getGoldCoins().income(playUser , BMDataContext.PVAConsumeActionEnum.SUBSIDY.toString(), score) ;
					if(result!=null) {
						actRecord.setAction(result.getAction());
						actRecord.setAmount(result.getAmount());
						actRecord.setBalance(result.getBalance());
						
						UKTools.published(actRecord, actRecordRes);
					}
				}else {
					message = actRecord ;
					actRecord.setCommand(BMDataContext.CommandMessageType.SUBSIDYFAILD.toString());
					actRecord.setResult(gameConfig.getSubovermsg());
				}
			}
			if(message == null){
				message = actRecord ;
				actRecord.setEnable(false);
				actRecord.setCommand(BMDataContext.CommandMessageType.SUBSIDYFAILD.toString());
				actRecord.setResult(gameConfig.getNosubmsg());
			}
			/**
			 * 发送 破产补助的消息
			 */
			client.sendEvent(message.getCommand() , message);
		}
		return message;
	}
	/**
	 * 创建事件
	 * @param playUser
	 * @param recType
	 * @param orgi
	 * @param times
	 * @param score
	 * @return
	 */
	public static ActRecord createActRecord(PlayUserClient playUser  , String recType , String orgi , int times , int score) {
		ActRecord actRecord = new ActRecord();
		actRecord.setCreatetime(new Date());
		actRecord.setDay(UKTools.getDay());
		actRecord.setPlayerid(playUser.getId());
		actRecord.setOrgi(orgi);
		actRecord.setFrequency(times);
		actRecord.setScore(score);
			
		actRecord.setRectype(recType);	//记录类型 ， 每天能够 领取的 补贴 次数
		return actRecord ;
	}
	/**
	 * 创建一个AI玩家
	 * @param player
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static PlayUserClient create(PlayUser player,String playertype) {
		return create(player, null , null , playertype) ;
	}
	
	/**
	 * 开始游戏，根据玩法创建游戏对局
	 * <p>
	 * [根据玩法发牌]
	 * 
	 * @param playUsers		房间游戏用户
	 * @param gameRoom		房间
	 * @param banker		
	 * @param cardsnum		牌张数
	 * 
	 * @return {@link Board}
	 */
	public static Board playGame(List<PlayUserClient> playUsers , GameRoom gameRoom , String banker , int cardsnum){
		Board board = null ;
		GamePlayway gamePlayWay = (GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(gameRoom.getPlayway(), gameRoom.getOrgi()) ;
		if(gamePlayWay!=null){
			ChessGame chessGame = games.get(gamePlayWay.getCode());
			if(chessGame!=null){
				board = chessGame.process(playUsers, gameRoom, gamePlayWay , banker, cardsnum);
			}
		}
		
		return board;
	}
	
	/**
	 * 创建一个普通玩家
	 * @param player
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static PlayUserClient create(PlayUser player , IP ipdata , HttpServletRequest request ) throws IllegalAccessException, InvocationTargetException{
		return create(player, ipdata, request, BMDataContext.PlayerTypeEnum.NORMAL.toString()) ;
	}
	
	/**
	 * 反转牌的排序为大牌在前
	 * 
	 * @param cards
	 * @return
	 */
	public static byte[] reverseCards(byte[] cards) {  
		byte[] target_cards = new byte[cards.length];  
		for (int i = 0; i < cards.length; i++) {  
			// 反转后数组的第一个元素等于源数组的最后一个元素：  
			target_cards[i] = cards[cards.length - i - 1];  
		}  
		
		return target_cards;  
	}  
	
	/**
	 * 注册用户
	 * @param player
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static PlayUserClient create(PlayUser player , IP ipdata , HttpServletRequest request , String playertype){
		PlayUserClient playUserClient = null ;
		if(player!= null){
    		if(StringUtils.isBlank(player.getUsername())){
    			player.setUsername("Guest_"+Base62.encode(UKTools.getUUID().toLowerCase()));
    		}
    		if(!StringUtils.isBlank(player.getPassword())){
    			player.setPassword(UKTools.md5(player.getPassword()));
    		}else{
    			player.setPassword(UKTools.md5(RandomKey.genRandomNum(6)));//随机生成一个6位数的密码 ，备用
    		}
    		player.setPlayertype(playertype);	//玩家类型
    		player.setCreatetime(new Date());
    		player.setUpdatetime(new Date());
    		player.setLastlogintime(new Date());
    		
    		BrowserClient client = UKTools.parseClient(request) ;
    		player.setOstype(client.getOs());
    		player.setBrowser(client.getBrowser());
    		if(request!=null){
	    		String usetAgent = request.getHeader("User-Agent") ;
	    		if(!StringUtils.isBlank(usetAgent)){
	    			if(usetAgent.length() > 255){
	    				player.setUseragent(usetAgent.substring(0,250));
	    			}else{
	    				player.setUseragent(usetAgent);
	    			}
	    		}
    		}
    		if(ipdata!=null){
	    		player.setRegion(ipdata.getRegion());
				player.setCountry(ipdata.getCountry());
				player.setProvince(ipdata.getProvince());
				player.setCity(ipdata.getCity());
				player.setIsp(ipdata.getIsp());
    		}
			
    		
    		player.setOrgi(BMDataContext.SYSTEM_ORGI);
    		AiConfig aiConfig = CacheConfigTools.getAiConfig(player.getOrgi()) ;
    		
			if(BMDataContext.PlayerTypeEnum.AI.toString().equals(playertype) && aiConfig != null){
				player.setGoldcoins(aiConfig.getInitcoins());
    			player.setCards(aiConfig.getInitcards());
    			player.setDiamonds(aiConfig.getInitdiamonds());
			}else{
	    		AccountConfig config = CacheConfigTools.getGameAccountConfig(BMDataContext.SYSTEM_ORGI) ;
	    		if(config!=null){
	    			player.setGoldcoins(config.getInitcoins());
	    			player.setCards(config.getInitcards());
	    			player.setDiamonds(config.getInitdiamonds());
	    		}
			}
    		
    		if(!StringUtils.isBlank(player.getId())){
    			playUserClient  = new PlayUserClient() ;
    			try {
					BeanUtils.copyProperties(playUserClient , player);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
    		}
    	}
		return playUserClient ;
	}
	
	/**
	 * 获取游戏全局配置，后台管理界面上的配置功能
	 * @param orgi
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<GamePlayway> playwayConfig(String gametype,String orgi){
		List<GamePlayway> gamePlayList = (List<GamePlayway>) CacheHelper.getSystemCacheBean().getCacheObject(gametype+"."+BMDataContext.ConfigNames.PLAYWAYCONFIG.toString(), orgi) ;
		if(gamePlayList == null){
			gamePlayList = BMDataContext.getContext().getBean(GamePlaywayRepository.class).findByOrgiAndTypeid(orgi, gametype , new Sort(Sort.Direction.ASC, "sortindex")) ;
			CacheHelper.getSystemCacheBean().put(gametype+"."+BMDataContext.ConfigNames.PLAYWAYCONFIG.toString() , gamePlayList , orgi) ;
		}
		return gamePlayList ;
	}
	/**
	 * 获取房卡游戏的自定义配置，后台管理界面上的配置功能
	 * @param orgi
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<GamePlaywayGroup> playwayGroupsConfig(String orgi){
		List<GamePlaywayGroup> gamePlaywayGroupsList = (List<GamePlaywayGroup>) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.ConfigNames.PLAYWAYGROUP.toString(), orgi) ;
		if(gamePlaywayGroupsList == null){
			gamePlaywayGroupsList = BMDataContext.getContext().getBean(GamePlaywayGroupRepository.class).findByOrgi(orgi, new Sort(Sort.Direction.ASC, "sortindex")) ;
			CacheHelper.getSystemCacheBean().put(BMDataContext.ConfigNames.PLAYWAYGROUP.toString() , gamePlaywayGroupsList , orgi) ;
		}
		return gamePlaywayGroupsList ;
	}

	/**
	 * 获取房卡游戏的自定义配置，后台管理界面上的配置功能
	 * @param orgi
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<GamePlaywayGroupItem> playwayGroupItemConfig(String orgi){
		List<GamePlaywayGroupItem> gamePlaywayGroupsList = (List<GamePlaywayGroupItem>) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.ConfigNames.PLAYWAYGROUPITEM.toString(), orgi) ;
		if(gamePlaywayGroupsList == null){
			gamePlaywayGroupsList = BMDataContext.getContext().getBean(GamePlaywayGroupItemRepository.class).findByOrgi(orgi, new Sort(Sort.Direction.ASC, "sortindex")) ;
			CacheHelper.getSystemCacheBean().put(BMDataContext.ConfigNames.PLAYWAYGROUPITEM.toString() , gamePlaywayGroupsList , orgi) ;
		}
		return gamePlaywayGroupsList ;
	}



	/**
	 * 
	 * @param gametype
	 * @param orgi
	 */
	public static void cleanPlaywayCache(String gametype,String orgi){
		CacheHelper.getSystemCacheBean().delete(gametype+"."+BMDataContext.ConfigNames.PLAYWAYCONFIG.toString(), orgi) ;
	}
	/**
	 * 封装Game信息，基于缓存操作
	 * @param gametype
	 * @return
	 */
	public static List<BeiMiGame> games(String gametype){
		List<BeiMiGame> beiMiGameList = new ArrayList<BeiMiGame>();
		if(!StringUtils.isBlank(gametype)){
			/**
			 * 找到游戏配置的 模式 和玩法，如果多选，则默认进入的是 大厅模式，如果是单选，则进入的是选场模式
			 */
			String[] games = gametype.split(",") ;
			for(String game : games){
				BeiMiGame beiMiGame = new BeiMiGame();
				for(SysDic sysDic : BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC)){
					if(sysDic.getId().equals(game)){
						beiMiGame.setName(sysDic.getName());
						beiMiGame.setId(sysDic.getId());
						beiMiGame.setCode(sysDic.getCode());
						
						List<SysDic> gameModelList = BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC, game) ;
						for(SysDic gameModel : gameModelList){
							Type type = new Type(gameModel.getId(), gameModel.getName() , gameModel.getCode()) ;
							beiMiGame.getTypes().add(type) ;
							List<GamePlayway> gamePlaywayList = playwayConfig(gameModel.getId(), gameModel.getOrgi()) ;

							List<GamePlaywayGroup> gamePlaywayGroups = playwayGroupsConfig(gameModel.getOrgi()) ;
							List<GamePlaywayGroupItem> gamePlaywayGroupItems = playwayGroupItemConfig(gameModel.getOrgi()) ;


							for(GamePlayway gamePlayway : gamePlaywayList){
								Playway playway = new Playway(gamePlayway.getId(), gamePlayway.getName() , gamePlayway.getCode(), gamePlayway.getScore() , gamePlayway.getMincoins(), gamePlayway.getMaxcoins(), gamePlayway.isChangecard() , gamePlayway.isShuffle()) ;
								playway.setLevel(gamePlayway.getTypelevel());

								playway.setGroups(new ArrayList<GamePlaywayGroup>());
								playway.setItems(new ArrayList<GamePlaywayGroupItem>());

								for(GamePlaywayGroup group : gamePlaywayGroups){
									if(group.getPlaywayid().equals(gamePlayway.getId())){
										playway.getGroups().add(group) ;
									}
								}

								for(GamePlaywayGroupItem item : gamePlaywayGroupItems){
									if(item.getPlaywayid().equals(gamePlayway.getId())){
										playway.getItems().add(item) ;
									}
								}

								playway.setSkin(gamePlayway.getTypecolor());
								playway.setMemo(gamePlayway.getMemo());
                                playway.setRoomtitle(gamePlayway.getRoomtitle());
								playway.setFree(gamePlayway.isFree());
								playway.setExtpro(gamePlayway.isExtpro());
								type.getPlayways().add(playway) ;
							}
						}
						beiMiGameList.add(beiMiGame) ;
					}
				}
			}
		}
		return beiMiGameList ;
	}
	
	public static void main(String[] args){
		long start = System.nanoTime() ;
		byte[] cards = new byte[]{21,22,24,28,88,92,96} ;
		byte takecard = 20 ;
		List<Byte> test = new ArrayList<Byte>();
		for(byte temp : cards){
			test.add(temp) ;
		}
		test.add(takecard) ;
		Collections.sort(test);
		for(byte temp : test){
			int value = (temp%36) / 4 ;			//牌面值
			int rote = temp / 36 ;				//花色
			System.out.print(value+1);
			if(rote == 0){
				System.out.print("万,");
			}else if(rote == 1){
				System.out.print("筒,");
			}else if(rote == 2){
				System.out.print("条,");
			}
		}
		Player player = new Player("USER1") ;
		player.setColor(3);
		player.setActions(new ArrayList<Action>());
		
		for(int i=0 ; i<5000000 ; i++){
			GameUtils.processMJCard(player, cards, takecard, false) ;
		}
		long end = System.nanoTime() - start ;
		System.out.println("判断500W次胡牌花费时间："+(end)+"纳秒，约等于："+ end/1000000f+"ms");
	}
	/**
	 * 麻将的出牌判断，杠碰吃胡
	 * @param cards
	 * @param card
	 * @param deal	是否抓牌
	 * @return
	 */
	public static MJCardMessage processMJCard(Player player,byte[] cards , byte takecard , boolean deal){
		MJCardMessage mjCard = new MJCardMessage();
		mjCard.setCommand("action");
		mjCard.setUserid(player.getPlayuser());
		Map<Integer, Byte> data = new HashMap<Integer, Byte>();
		boolean que = false ;
		if(cards.length > 0){
			for(byte temp : cards){
				int value = (temp%36) / 4 ;			//牌面值
				int rote = temp / 36 ;				//花色
				int key = value + 9 * rote ;		//
				if(rote == player.getColor()){
					que = true ;
				}
				if(data.get(key) == null || rote == player.getColor()){
					data.put(key , (byte)1) ;
				}else{
					data.put(key, (byte)(data.get(key)+1)) ;
				}
				
				if(data.get(key) == 4 && deal == true){	//自己发牌的时候，需要先判断是否有杠牌
					mjCard.setGang(true);
					mjCard.setCard(temp);
				}
			}
			/**
			 * 检查是否有 杠碰
			 */
			int value = (takecard %36)/4 ;
			int key = value + 9*(takecard/36) ;
			Byte card = data.get(key) ;
			if(card!=null){
				if(card ==2 && deal == false){
					//碰
					mjCard.setPeng(true);
					mjCard.setCard(takecard);
				}else if(card == 3){
					//明杠
					mjCard.setGang(true);
					mjCard.setCard(takecard);
				}
			}
			
			/**
			 * 检查是否有弯杠 , 碰过 AND 自己抓了一张碰过的牌
			 */
			int rote = takecard  / 36 ;
			if(deal == true && rote!= player.getColor() ){
				for(Action action : player.getActions()){
					if(action.getCard() == takecard && action.getAction().equals(BMDataContext.PlayerAction.PENG.toString())){
						//
						mjCard.setGang(true); break ;
					}
				}
			}
			/**
			 * 后面胡牌判断使用
			 */
			if(data.get(key) == null){
				data.put(key , (byte)1) ;
			}else{
				data.put(key, (byte)(data.get(key)+1)) ;
			}
		}
		if(que == false){
			/**
			 * 检查是否有 胡 , 胡牌算法，先移除 对子
			 */
			List<Byte> pairs = new ArrayList<Byte>();
			List<Byte> others = new ArrayList<Byte>();
			List<Byte> kezi = new ArrayList<Byte>();
			/**
			 * 处理玩家手牌
			 */
			for(byte temp : cards){
				int key = (((temp%36) / 4) + 9 * (int)(temp / 36)) ;			//字典编码
				if(data.get(key) == 1 ){
					others.add(temp) ;
				}else if(data.get(key) == 2){
					pairs.add(temp) ;
				}else if(data.get(key) == 3){
					kezi.add(temp) ;
				}
			}
			/**
			 * 处理一个单张
			 */
			{
				int key = (((takecard%36) / 4) + 9 * (int)(takecard / 36)) ;			//字典编码
				if(data.get(key) == 1 ){
					others.add(takecard) ;
				}else if(data.get(key) == 2){
					pairs.add(takecard) ;
				}else if(data.get(key) == 3){
					kezi.add(takecard) ;
				}
			}
			/**
			 * 是否有胡
			 */
			processOther(others);
			
			if(others.size() == 0){
				if(pairs.size() == 2 || pairs.size() == 14){//有一对，胡
					mjCard.setHu(true);
				}else{	//然后分别验证 ，只有一种特殊情况，的 3连对，可以组两个顺子，也可以胡 ， 其他情况就呵呵了
					
				}
			}else if(pairs.size() > 2){	//对子的牌大于>2张，否则肯定是不能胡的
				//检查对子里 是否有额外多出来的 牌，如果有，则进行移除
				for(int i=0 ; i<pairs.size() ; i++){
					if(i%2==0){
						others.add(pairs.get(i)) ;
					}
				}
				processOther( others);
				
				for(int i=0 ; i<pairs.size() ; i++){
					if(i%2==1){
						others.add(pairs.get(i)) ;
					}
				}
				
				processOther(others);
				
				/**
				 * 检查 others
				 */
				/**
				 * 最后一次，检查所有的值都是 2，就胡了
				 */
				if(others.size() == 2 && getKey(others.get(0)) == getKey(others.get(1))){
					mjCard.setHu(true);
				}else{	//还不能胡？
					
				}
			}else if(pairs.size() == 0){
				for(Byte temp : kezi){
					others.add(temp) ;
					processOther(others);
					if(others.size() == 0){
						mjCard.setHu(true);
						break ;
					}else{
						others.remove(temp) ;
					}
				}
			}
		}
		if(mjCard.isHu()){
			mjCard.setCard(takecard);
			System.out.println("胡牌了");
			for(byte temp : cards){
				System.out.print(temp+",");
			}
			System.out.println(takecard);
		}
		return mjCard;
	}
	
	private static void processOther(List<Byte> others){
		Collections.sort(others);
		for(int i=0 ; i<others.size() && others.size() >(i+2) ; ){
			byte color = (byte) (others.get(i) / 36) ;							//花色
			byte key = getKey(others.get(i));
			byte nextcolor = (byte) (others.get(i) / 36) ;							//花色
			byte nextkey = getKey(others.get(i+1));
			if(color == nextcolor && nextkey == key+1){
				nextcolor = (byte) (others.get(i+2) / 36) ;							//花色
				nextkey = getKey(others.get(i+2));
				if(color == nextcolor && nextkey == key+2){		//数字，移除掉
					others.remove(i+2) ;
					others.remove(i+1) ;
					others.remove(i) ;
				}else{
					i = i+2 ;
				}
			}else{
				i = i+1 ; 	//下一步
			}
		}
	}
	
	public static byte getKey(byte card){
		byte value = (byte) ((card%36) / 4) ;			//牌面值
		int rate = card / 36 ;							//花色
		byte key = (byte) (value + 9 * rate) ;			//字典编码
		return key ;
	}
	
	/**
	 * 麻将的出牌判断，杠碰吃胡
	 * @param cards
	 * @param card
	 * @param deal	是否抓牌
	 * @return
	 */
	public static Byte getGangCard(byte[] cards){
		Byte card = null ;
		Map<Integer, Byte> data = new HashMap<Integer, Byte>();
		for(byte temp : cards){
			int value = (temp%36) / 4 ;			//牌面值
			int rote = temp / 36 ;				//花色
			int key = value + 9 * rote ;		//
			if(data.get(key) == null){
				data.put(key , (byte)1) ;
			}else{
				data.put(key, (byte)(data.get(key)+1)) ;
			}
			if(data.get(key) == 4){	//自己发牌的时候，需要先判断是否有杠牌
				card = temp ;
				break ;
			}
		}
		
		return card;
	}
	/**
	 * 定缺方法，计算最少的牌
	 * @param cards
	 * @return
	 */
	public static int selectColor(byte[] cards){
		Map<Integer, Byte> data = new HashMap<Integer, Byte>();
		for(byte temp : cards){
			int key = temp / 36 ;				//花色
			if(data.get(key) == null){
				data.put(key , (byte)1) ;
			}else{
				data.put(key, (byte)(data.get(key)+1)) ;
			}
		}
		int color = 0 , cardsNum = 0 ;
		if(data.get(0)!=null){
			cardsNum = data.get(0) ;
			if(data.get(1) == null){
				color = 1 ;
			}else{
				if(data.get(1) < cardsNum){
					cardsNum = data.get(1) ;
					color = 1 ;
				}
				if(data.get(2)==null){
					color = 2 ;
				}else{
					if(data.get(2) < cardsNum){
						cardsNum = data.get(2) ;
						color = 2 ;
					}
				}
			}
		}
		return color ;
	}
}
