package com.beimi.core.statemachine.impl;

import org.apache.log4j.Logger;

import com.beimi.core.statemachine.BeiMiStateMachine;
import com.beimi.core.statemachine.action.Action;
import com.beimi.core.statemachine.message.Message;

/**
 * 贝密状态机处理类
 * <p>
 * 用于处理游戏状态发生变化动作
 * 
 * @author chenghao
 *
 */
public class BeiMiMachineHandler {
	
	private BeiMiStateMachine<String,String> config ;
	
	public BeiMiMachineHandler(BeiMiStateMachine<String,String> config){
		this.config = config ;
	}
	
	/**
	 * Handle event with entity.
	 * 
	 * <p>
	 * <ul>
	 * <li>
	 * 1、获得当前事件的Transition
	 * <li>
	 * 2、处理Action
	 * <li>
	 * 3、校验当前状态是否是从 上一个状态 转变来的（不做实现）
	 * <li>
	 * 4、变更状态到下一步
	 *</ul>
	 *
	 * @param event the event
	 * @param state the state
	 * @return true if event was accepted
	 */
	public boolean handleEventWithState(Message<String> event, String state) {
		BeiMiExtentionTransitionConfigurer<String, String> transition = config.getTransitions().transition(state) ;
		
		if(transition!=null){
			Action<String, String> action = transition.getAction() ;
			if(action!=null){
				/**
				 * 1、任务的执行应该为异步执行，可以考虑放入 RingBuffer中处理
				 * 2、启用异步线程并增加线程池处理
				 */
				action.execute(event , transition);
				/**
				 * 修改当前状态，并持久化
				 */
			}else{
				//抛出异常
				Logger.getLogger(this.getClass()).info("Transition's Action is null");
			}
		}
		
		return config!=null;
	}
	
}