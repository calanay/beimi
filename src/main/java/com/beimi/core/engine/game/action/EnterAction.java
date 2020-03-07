package com.beimi.core.engine.game.action;

import org.apache.commons.lang3.StringUtils;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.task.CreateAITask;
import com.beimi.core.statemachine.action.Action;
import com.beimi.core.statemachine.impl.BeiMiExtentionTransitionConfigurer;
import com.beimi.core.statemachine.message.Message;
import com.beimi.util.CacheConfigTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.AiConfig;
import com.beimi.web.model.GameRoom;

/**
 * 创建房间的人，房卡模式下的 房主， 大厅模式下的首个进入房间的人
 * 
 * @author iceworld
 *
 */
public class EnterAction<T,S> implements Action<T, S>{
	
	/**
	 * 进入房间后开启 5秒计时模式，计时结束后未撮合玩家成功 的，召唤机器人，
	 * 撮合成功的，立即开启游戏
	 */
	@Override
	public void execute(Message<T> message, BeiMiExtentionTransitionConfigurer<T,S> configurer) {
		String room = (String)message.getMessageHeaders().getHeaders().get("room") ;
		if(!StringUtils.isBlank(room)){
			GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(room, BMDataContext.SYSTEM_ORGI) ; 
			if(gameRoom!=null){
				if(!gameRoom.isCardroom()) {
					AiConfig aiConfig = CacheConfigTools.getAiConfig(gameRoom.getOrgi());
					if (aiConfig.isEnableai()) {
						CacheHelper.getExpireCache().put(gameRoom.getOrgi(), new CreateAITask(aiConfig.getWaittime(), gameRoom, gameRoom.getOrgi()));
					}
				}
				/**
				 * 更新状态
				 */
				gameRoom.setStatus(configurer.getTarget().toString());
				CacheHelper.getGameRoomCacheBean().put(gameRoom.getId(), gameRoom, gameRoom.getOrgi());
			}
		}
	}
}
