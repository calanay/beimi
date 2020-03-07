package com.beimi.core.statemachine.impl;

import com.beimi.core.statemachine.action.Action;
import com.beimi.core.statemachine.config.ExternalTransitionConfigurer;
import com.beimi.core.statemachine.config.StateMachineTransitionConfigurer;

/**
 * 配置扩展过渡实现
 * 
 * @author 
 *
 * @param <T>
 * @param <S>
 */
public class BeiMiExtentionTransitionConfigurer<T, S> implements ExternalTransitionConfigurer<T, S>{
	private StateMachineTransitionConfigurer<T, S> configure ;
	private T source = null , target = null ;
	private S event = null;
	private Action<T, S> action = null , errorAction = null;

	public BeiMiExtentionTransitionConfigurer(StateMachineTransitionConfigurer<T, S> configure){
		this.configure = configure ;
	}
	
	@Override
	public ExternalTransitionConfigurer<T, S> source(T source) {
		this.source = source ;
		return this;
	}

	@Override
	public ExternalTransitionConfigurer<T, S> event(S event) {
		this.event = event ;
		return this;
	}

	@Override
	public ExternalTransitionConfigurer<T, S> action(Action<T, S> action) {
		this.action = action;
		return this;
	}

	@Override
	public ExternalTransitionConfigurer<T, S> action(Action<T, S> action,
			Action<T, S> error) {
		this.action = action;
		this.errorAction = error ;
		return null;
	}

	@Override
	public StateMachineTransitionConfigurer<T, S> and() {
		configure.apply(this);
		return configure;
	}

	@Override
	public ExternalTransitionConfigurer<T, S> target(T target) {
		this.target = target ;
		return this;
	}

	public StateMachineTransitionConfigurer<T, S> getConfigure() {
		return configure;
	}

	public void setConfigure(StateMachineTransitionConfigurer<T, S> configure) {
		this.configure = configure;
	}

	public T getSource() {
		return source;
	}

	public void setSource(T source) {
		this.source = source;
	}

	public T getTarget() {
		return target;
	}

	public void setTarget(T target) {
		this.target = target;
	}

	public S getEvent() {
		return event;
	}

	public void setEvent(S event) {
		this.event = event;
	}

	public Action<T, S> getAction() {
		return action;
	}

	public void setAction(Action<T, S> action) {
		this.action = action;
	}

	public Action<T, S> getErrorAction() {
		return errorAction;
	}

	public void setErrorAction(Action<T, S> errorAction) {
		this.errorAction = errorAction;
	}
}
