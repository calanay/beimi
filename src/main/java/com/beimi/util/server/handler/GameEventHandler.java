
package com.beimi.util.server.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.util.GameUtils;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.client.NettyClients;
import com.beimi.util.rules.model.GameStatus;
import com.beimi.util.rules.model.SearchRoom;
import com.beimi.util.rules.model.SearchRoomResult;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.PlayUserClientESRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import com.beimi.web.service.repository.jpa.PlayUserClientRepository;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

public class GameEventHandler     
{  
	protected SocketIOServer server;
	
	
    @Autowired  
    public GameEventHandler(SocketIOServer server)   
    {  
        this.server = server ;
    }  
    
    /**
     * websocket connection
     * 
     * @param client
     */
    @OnConnect  
    public void onConnect(SocketIOClient client)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
		if(beiMiClient!=null && !StringUtils.isBlank(beiMiClient.getUserid())){
			if(CacheHelper.getRoomMappingCacheBean().getCacheObject(beiMiClient.getUserid(), beiMiClient.getOrgi()) != null){
				ActionTaskUtils.sendEvent("" , beiMiClient.getUserid(), null);
			}
			
		}
    }  
    
    /**
     * 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息  
     * [退出登录]
     * 
     * @param client
     */
    @OnDisconnect  
    public void onDisconnect(SocketIOClient client)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	if(beiMiClient!=null){
    		/**
    		 * 玩家离线
    		 */
    		PlayUserClient playUserClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(beiMiClient.getUserid(), beiMiClient.getOrgi()) ;
    		if(playUserClient!=null){
    			if(BMDataContext.GameStatusEnum.PLAYING.toString().equals(playUserClient.getGamestatus())){
    				GameUtils.updatePlayerClientStatus(beiMiClient.getUserid(), beiMiClient.getOrgi(), BMDataContext.PlayerTypeEnum.OFFLINE.toString());
    			}else{
    				CacheHelper.getApiUserCacheBean().delete(beiMiClient.getUserid(), beiMiClient.getOrgi()) ;
    				if(CacheHelper.getGamePlayerCacheBean().getPlayer(beiMiClient.getUserid(), beiMiClient.getOrgi())!=null){
    					CacheHelper.getGamePlayerCacheBean().delete(beiMiClient.getUserid(), beiMiClient.getOrgi()) ;
    				}
    				CacheHelper.getRoomMappingCacheBean().delete(beiMiClient.getUserid(), beiMiClient.getOrgi()) ;
    				/**
    				 * 玩家退出游戏，需要发送事件给所有玩家，如果房主退出，则房间解散
    				 */
    			}
    			/**
    			 * 退出房间，房卡模式下如果房间还有剩余局数 ， 则不做任何操作，如果无剩余或未开始扣卡，则删除房间
    			 */
    		}
    	}
    }  
    
    /**
     * 进入游戏房间
     * <p>
     * [启动房间后，会启动倒时任务，到时后会自动加入AI机器人]
     * {"token":"308763e20d61441db23722e6da785343","playway":"402888815e21d735015e21d995680000",
     * "orgi":"beimi","extparams":{"gametype":"dizhu","playway":"402888815e21d735015e21d995680000"}}
     * 
     * @param client
     * @param request
     * @param data
     */
    @OnEvent(value = "joinroom")   
    public void onJoinRoom(SocketIOClient client , AckRequest request, String data)  
    {  
    	BeiMiClient beiMiClient = JSON.parseObject(data , BeiMiClient.class) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			/**
			 * Token不为空，并且，验证Token有效，验证完毕即开始进行游戏撮合，房卡类型的
			 * 1、大厅房间处理
			 *    a、从房间队列里获取最近一条房间信息
			 *    b、将token对应玩家加入到房间
			 *    c、如果房间凑齐了玩家，则将房间从等待撮合队列中移除，放置到游戏中的房间信息，如果未凑齐玩家，继续扔到队列
			 *    d、通知房间的所有人，有新玩家加入
			 *    e、超时处理，增加AI进入房间
			 *    f、事件驱动
			 *    g、定时器处理
			 * 2、房卡房间处理
			 * 	  a、创建房间
			 * 	  b、加入到等待中队列
			 */
			Token userToken ;
			if(beiMiClient!=null && !StringUtils.isBlank(token) && (userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, beiMiClient.getOrgi()))!=null){
				//鉴权完毕
				PlayUserClient userClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				
				beiMiClient.setClient(client);
				beiMiClient.setUserid(userClient.getId());
				beiMiClient.setSession(client.getSessionId().toString());
				// 心跳时间
				beiMiClient.setTime(System.currentTimeMillis());
				
				NettyClients.getInstance().putClient(userClient.getId(), beiMiClient);
				
				// 更新当前玩家状态，在线|离线
				userClient.setOnline(true);
				
				// 更新状态
				ActionTaskUtils.updatePlayerClientStatus(userClient, 
						BMDataContext.PlayerTypeEnum.NORMAL.toString());
				
				//玩家用户数据保存发布至Disruptor缓存队列
				UKTools.published(userClient, 
						BMDataContext.getContext().getBean(PlayUserClientESRepository.class), 
						BMDataContext.getContext().getBean(PlayUserClientRepository.class));
				
				BMDataContext.getGameEngine().gameRequest(userToken.getUserid(), 
						beiMiClient.getPlayway(), beiMiClient.getRoom(), beiMiClient.getOrgi(), 
						userClient , beiMiClient);
			}
		}
    }
    
    /**
     * 判断游戏状态
     * [进入房间前请求]
     * {"token":"4ae810dad9784ee7bc54723d81752af1","orgi":"beimi"}
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "gamestatus")   
    public void onGameStatus(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = JSON.parseObject(data , BeiMiClient.class) ;
    	Token userToken ;
    	GameStatus gameStatus = new GameStatus() ;
    	gameStatus.setGamestatus(BMDataContext.GameStatusEnum.NOTREADY.toString());
		if(beiMiClient!=null 
				&& !StringUtils.isBlank(beiMiClient.getToken()) 
				&& (userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(beiMiClient.getToken(), beiMiClient.getOrgi())) != null){
			//鉴权完毕
			PlayUserClient userClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
			if(userClient!=null){
				gameStatus.setGamestatus(BMDataContext.GameStatusEnum.READY.toString());
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(userClient.getId(), userClient.getOrgi()) ;
				if(!StringUtils.isBlank(roomid) && CacheHelper.getBoardCacheBean().getCacheObject(roomid, userClient.getId())!=null){
					gameStatus.setUserid(userClient.getId());
					gameStatus.setOrgi(userClient.getOrgi());

					GameRoom gameRoom = (GameRoom)CacheHelper.getGameRoomCacheBean().getCacheObject(roomid , userClient.getOrgi()) ;
                    GamePlayway gamePlayway = (GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(gameRoom.getPlayway(), userClient.getOrgi()) ;
					gameStatus.setGametype(gamePlayway.getCode());
					gameStatus.setPlayway(gamePlayway.getId());
					gameStatus.setGamestatus(BMDataContext.GameStatusEnum.PLAYING.toString());
					if(gameRoom.isCardroom()){
						gameStatus.setCardroom(true);
					}
				}
			}
		}else{
			gameStatus.setGamestatus(BMDataContext.GameStatusEnum.TIMEOUT.toString());
		}
		System.out.println(JSON.toJSONString(gameStatus));
		
		client.sendEvent(BMDataContext.BEIMI_GAMESTATUS_EVENT, gameStatus);
    }
      
    /**
     * 抢地主事件
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "docatch")   
    public void onCatch(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().actionRequest(roomid, playUser, playUser.getOrgi(), true);
			}
		}
    }
    
    /**
     * 不抢/叫地主事件
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "giveup")   
    public void onGiveup(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().actionRequest(roomid, playUser, playUser.getOrgi(), false);
			}
		}
    }
    
    /**
     * 出牌提示
     * 
     */
    @OnEvent(value = "cardtips")   
    public void onCardTips(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().cardTips(roomid, playUser, playUser.getOrgi(), data);
			}
		}
    }
    
    
    /**
     * 玩家出牌
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "doplaycards")   
    public void onPlayCards(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token) && !StringUtils.isBlank(data)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String[] cards = data.split(",") ;
				
				byte[] playCards = new byte[cards.length] ;
				for(int i= 0 ; i<cards.length ; i++){
					playCards[i] = Byte.parseByte(cards[i]) ;
				}
				BMDataContext.getGameEngine().takeCardsRequest(roomid, userToken.getUserid(), userToken.getOrgi() , false , playCards);
			}
		}
    }
    
    /**
     * 不出牌
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "nocards")   
    public void onNoCards(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().takeCardsRequest(roomid, userToken.getUserid(), userToken.getOrgi() , false , null);
			}
		}
    }
    
    //出牌
    @OnEvent(value = "selectcolor")   
    public void onSelectColor(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().selectColorRequest(roomid, playUser.getId(), userToken.getOrgi() , data);
			}
		}
    }
    
    /**
     * 操作操作
     * [碰、杠]
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "selectaction")   
    public void onActionEvent(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().actionEventRequest(roomid, playUser.getId(), userToken.getOrgi() , data);
			}
		}
    }
    
    /**
     * 继续游戏
     * [明牌开始]
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "restart")   
    public void onRestart(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				BMDataContext.getGameEngine().restartRequest(roomid, playUser , beiMiClient , "true".equals(data));
			}
		}
    }
    
    /**
     * 开始游戏[准备]
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "start")   
    public void onStart(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getGamePlayerCacheBean().getPlayer(userToken.getUserid(), userToken.getOrgi()) ;
				if(playUser!=null){
					BMDataContext.getGameEngine().startGameRequest(playUser.getRoomid(), playUser , userToken.getOrgi() , "true".equals(data)) ;
				}
			}
		}
    }
    
    //抢地主事件
    @OnEvent(value = "recovery")   
    public void onRecovery(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
				BMDataContext.getGameEngine().gameRequest(playUser.getId(), beiMiClient.getPlayway(), beiMiClient.getRoom(), beiMiClient.getOrgi(), playUser , beiMiClient) ;
			}
		}
    }
    
    /**
     * 玩家离开
     * [从一个游戏类型进入另一个游戏类型]
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "leave")   
    public void onLeave(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				GameUtils.updatePlayerClientStatus(beiMiClient.getUserid(), beiMiClient.getOrgi(), BMDataContext.PlayerTypeEnum.LEAVE.toString());
			}
		}
    }
    
    //杂七杂八的指令，混合到一起
    @OnEvent(value = "command")   
    public void onCommand(SocketIOClient client , String data)  
    {  
    	Command command = JSON.parseObject(data , Command.class) ;
		if(command!=null && !StringUtils.isBlank(command.getToken())){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(command.getToken(), BMDataContext.SYSTEM_ORGI) ;
			PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
			if(userToken!=null){
				switch(command.getCommand()){
					case "subsidy" : GameUtils.subsidyPlayerClient(client , playUser, userToken.getOrgi()) ; break ;
				}
			}
		}
    }
    
    //聊天
    @OnEvent(value = "message")   
    public void onMessage(SocketIOClient client , String data)  
    {  
    	BeiMiClient beiMiClient = NettyClients.getInstance().getClient(client.getSessionId().toString()) ;
    	if (beiMiClient == null) {
			return ;
		}
    	String token = beiMiClient.getToken();
		if(!StringUtils.isBlank(token)){
			Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
			if(userToken!=null){
				GameUtils.updatePlayerClientStatus(beiMiClient.getUserid(), beiMiClient.getOrgi(), BMDataContext.PlayerTypeEnum.LEAVE.toString());
			}
		}
    }
    
    
    /**
     * 加入房间
     * [房卡模式]
     * 
     * @param client
     * @param data
     */
    @OnEvent(value = "searchroom")   
    public void onSearchRoom(SocketIOClient client , String data)  
    {  
    	SearchRoom searchRoom = JSON.parseObject(data , SearchRoom.class) ;
    	GamePlayway gamePlayway = null ;
    	SearchRoomResult searchRoomResult = null ;
    	boolean joinRoom = false;
    	if(searchRoom!=null && !StringUtils.isBlank(searchRoom.getUserid())){
    		GameRoomRepository gameRoomRepository = BMDataContext.getContext().getBean(GameRoomRepository.class);
    		PlayUserClient playUser = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(searchRoom.getUserid(), searchRoom.getOrgi()) ;
			if(playUser!=null){
				GameRoom gameRoom = null ;
				String roomid = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(playUser.getId(), playUser.getOrgi()) ;
				if(!StringUtils.isBlank(roomid)){
					gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomid, playUser.getOrgi()) ;
				}else{
					List<GameRoom> gameRoomList = gameRoomRepository.findByRoomidAndOrgi(searchRoom.getRoomid(), playUser.getOrgi()) ;
					if(gameRoomList!=null && gameRoomList.size() > 0){
						GameRoom tempGameRoom = gameRoomList.get(0) ;
						gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(tempGameRoom.getId(), playUser.getOrgi()) ;
					}
				}
				if(gameRoom!=null){
					/**
					 * 将玩家加入到 房间 中来 ， 加入的时候需要处理当前的 房间 已满员或未满员，如果满员，需要检查是否允许围观
					 */
					gamePlayway = (GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(gameRoom.getPlayway(), gameRoom.getOrgi()) ;
					List<PlayUserClient> playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
					if(playerList.size() < gamePlayway.getPlayers()){
						BMDataContext.getGameEngine().joinRoom(gameRoom, playUser, playerList);
						joinRoom = true ;
					}
					/**
					 * 获取的玩法，将玩法数据发送给当前请求的玩家
					 */
				}
			}
    	}
    	if(gamePlayway!=null){
    		//通知客户端
    		if(joinRoom == true){		//加入成功 ， 是否需要输入加入密码？
    			searchRoomResult = new SearchRoomResult(gamePlayway.getId() , gamePlayway.getCode() , BMDataContext.SearchRoomResultType.OK.toString());
    		}else{						//加入失败
    			searchRoomResult = new SearchRoomResult(BMDataContext.SearchRoomResultType.FULL.toString());
    		}
    	}else{ //房间不存在
    		searchRoomResult = new SearchRoomResult(BMDataContext.SearchRoomResultType.NOTEXIST.toString());
    	}
		
		client.sendEvent(BMDataContext.BEIMI_SEARCHROOM_EVENT, searchRoomResult);
    }
}  