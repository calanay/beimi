package com.beimi.core.engine.game.task;

import org.cache2k.expiry.ValueWithExpiryTime;

import com.beimi.config.web.model.Game;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.Message;
import com.beimi.util.GameUtils;
import com.beimi.util.UKTools;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;

/**
 * 任务抽象类
 * 
 * @author
 *
 */
public abstract class AbstractTask implements ValueWithExpiryTime {

	/**
	 * 根据玩法，找到对应的状态机
	 * @param playway
	 * @param orgi
	 * @return
	 */
	public Game getGame(String playway , String orgi){
		return GameUtils.getGame(playway , orgi) ;
	}
	
	public void sendEvent(String event , Message message , GameRoom gameRoom){
		ActionTaskUtils.sendEvent(event, message, gameRoom);
	}
	
	public Object json(Object data){
		return UKTools.json(data) ;
	}
	/**
	 * 根据当前 ROOM的 玩法， 确定下一步的流程
	 * @param playway
	 * @param currentStatus
	 * @return
	 */
	public String getNextEvent(GamePlayway playway,String currentStatus){
		return "" ;
	}
}
