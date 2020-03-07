package com.beimi.config.web.model;

/*
 * Copyright 2015 the original author or authors. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

import com.beimi.core.engine.game.state.GameEvent;
import com.beimi.core.statemachine.impl.BeiMiMachineHandler;
import com.beimi.core.statemachine.impl.MessageBuilder;
import com.beimi.web.model.GameRoom;

/**
 * 游戏对象
 * <p>
 * [分游戏类型[麻将、斗地主、德州]，游戏操作使用状态机机制进行处理]
 * 
 * @author
 *
 */
public class Game { 

	private final BeiMiMachineHandler handler; 

	public Game(BeiMiMachineHandler handler) { 
		this.handler = handler; 
	} 
	
	/**
	 * 游戏状态改变
	 * 
	 * @param gameRoom	游戏房间
	 * @param event		事件		详见：{@link com.beimi.core.engine.game.BeiMiGameEvent} 定义
	 */
	public void change(GameRoom gameRoom , String event) { 
		change(gameRoom, event , 0);
	}
	
	/**
	 * 游戏状态改变
	 * 
	 * @param gameRoom	游戏房间
	 * @param event		事件		详见：{@link com.beimi.core.engine.game.BeiMiGameEvent} 定义
	 * @param interval	间隔时间
	 */
	public void change(GameRoom gameRoom , String event , int interval ) { 
		handler.handleEventWithState(MessageBuilder.withPayload(event).setHeader("room", gameRoom.getId()).setHeader("interval", interval).build(), event);
	}
	
	/**
	 * 游戏状态改变
	 * 
	 * @param gameEvent 详见：{@link com.beimi.core.engine.game.BeiMiGameEvent} 定义
	 */
	public void change(GameEvent gameEvent) { 
		change(gameEvent , 0); 
	} 
	
	/**
	 * 游戏状态改变
	 * 
	 * @param gameEvent		详见：{@link com.beimi.core.engine.game.BeiMiGameEvent} 定义
	 * @param interval		间隔时间
	 */
	public void change(GameEvent gameEvent , int interval ) { 
		handler.handleEventWithState(MessageBuilder.withPayload(gameEvent.getEvent()).setHeader("room", gameEvent.getRoomid()).setHeader("interval", interval).build(), gameEvent.getEvent()); 
	} 
}
