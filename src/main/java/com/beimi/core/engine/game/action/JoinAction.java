package com.beimi.core.engine.game.action;

import org.apache.commons.lang3.StringUtils;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.statemachine.action.Action;
import com.beimi.core.statemachine.impl.BeiMiExtentionTransitionConfigurer;
import com.beimi.core.statemachine.message.Message;
import com.beimi.util.GameUtils;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.GameRoom;

/**
 * 创建房间的人，房卡模式下的 房主， 大厅模式下的首个进入房间的人
 * 
 * @author iceworld
 *
 */
public class JoinAction<T,S> implements Action<T, S>{
	
	/**
	 * JOIN事件，检查是否 凑齐一桌子，如果凑齐了，直接开始，并取消计时器
	 * 如果不够一桌子，啥也不做，等人齐、等计时器到时间事件
	 * 撮合成功的，立即开启游戏
	 * 通知所有成员的消息在 GameEventHandler里处理了
	 * 
	 */
	@Override
	public void execute(Message<T> message, BeiMiExtentionTransitionConfigurer<T,S> configurer) {
		String room = (String)message.getMessageHeaders().getHeaders().get("room") ;
		if(!StringUtils.isBlank(room)){
			GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(room, BMDataContext.SYSTEM_ORGI) ; 
			if(gameRoom!=null){
				/**
				 * 发送一个 Enough 事件
				 */
				ActionTaskUtils.roomReady(gameRoom, GameUtils.getGame(gameRoom.getPlayway() , gameRoom.getOrgi()));
			}
		}
	}
	
}