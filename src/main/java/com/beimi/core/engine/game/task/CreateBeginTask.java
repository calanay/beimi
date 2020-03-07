package com.beimi.core.engine.game.task;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cache2k.expiry.ValueWithExpiryTime;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.BeiMiGameEvent;
import com.beimi.core.engine.game.BeiMiGameTask;
import com.beimi.core.engine.game.impl.Banker;
import com.beimi.core.engine.game.impl.UserBoard;
import com.beimi.util.GameUtils;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.rules.model.Board;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

/**
 * 创建游戏开始任务[发牌]
 * 
 * @author
 *
 */
public class CreateBeginTask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask{

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateBeginTask(long timer , GameRoom gameRoom, String orgi){
		super();
		this.timer = timer ;
		this.gameRoom = gameRoom ;
		this.orgi = orgi ;
	}
	
	@Override
	public long getCacheExpiryTime() {
		return System.currentTimeMillis()+timer*1000;	//5秒后执行
	}
	
	/**
	 * 顺手 把牌发了，注：此处应根据 GameRoom的类型获取 发牌方式
	 */
	public void execute(){
		List<PlayUserClient> playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), orgi) ;
		
		// 是否在房间中[根据房间中用户是否有赢牌的用户ID来判断，如果有就是已在房间中，否则设置最后赢牌的玩家为庄家]
		boolean inroom = false;
		
		if(!StringUtils.isBlank(gameRoom.getLastwinner())){
			for(PlayUserClient player : playerList){
				// 房间中用户是否有赢牌的用户ID来判断
				if(player.getId().equals(gameRoom.getLastwinner())){
					inroom = true ;
				}
			}
		}
		
		// 设置最后赢牌的玩家为庄家
		if(inroom == false){
			gameRoom.setLastwinner(playerList.get(0).getId());
		}
		
		//通知所有玩家新的庄
		ActionTaskUtils.sendEvent("banker",  new Banker(gameRoom.getLastwinner()), gameRoom);
		
		// 根据游戏类型及玩法发牌
		Board board = GameUtils.playGame(playerList, gameRoom, gameRoom.getLastwinner(), gameRoom.getCardsnum());
		
		CacheHelper.getBoardCacheBean().put(gameRoom.getId(), board, gameRoom.getOrgi());
		
		for(Object temp : playerList){
			PlayUserClient playerUser = (PlayUserClient) temp ;
			playerUser.setGamestatus(BMDataContext.GameStatusEnum.PLAYING.toString());
			/**
			 * 更新状态到 PLAYING
			 */
			if(CacheHelper.getApiUserCacheBean().getCacheObject(playerUser.getId(), playerUser.getOrgi())!=null){
				CacheHelper.getApiUserCacheBean().put(playerUser.getId(), playerUser, orgi);
			}
			/**
			 * 每个人收到的 牌面不同，所以不用 ROOM发送广播消息，而是用 遍历房间里所有成员发送消息的方式
			 */
			ActionTaskUtils.sendEvent(playerUser, new UserBoard(board , playerUser.getId() , "play"));
		}
		
		CacheHelper.getGameRoomCacheBean().put(gameRoom.getId(), gameRoom, gameRoom.getOrgi());
		
		
		/**
		 * 发送一个 Begin 事件
		 * 通知状态机 , 此处应由状态机处理异步执行
		 */
		super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.AUTO.toString() , 2);
	}
}
