package com.beimi.core.statemachine.impl;

import java.util.HashMap;
import java.util.Map;

import com.beimi.core.statemachine.config.ExternalTransitionConfigurer;
import com.beimi.core.statemachine.config.StateMachineTransitionConfigurer;

public class BeiMiTransitionConfigurer<T,S> implements StateMachineTransitionConfigurer<T,S>{
	
	private Map<S, BeiMiExtentionTransitionConfigurer<T, S>> transitions = new HashMap<S,BeiMiExtentionTransitionConfigurer<T,S>>();
	
	@Override
	public ExternalTransitionConfigurer<T, S> withExternal() throws Exception {
		return new BeiMiExtentionTransitionConfigurer<T, S>(this);
	}

	@Override
	public void apply(BeiMiExtentionTransitionConfigurer<T, S> transition) {
		transitions.put(transition.getEvent(), transition);
	}

	@Override
	public BeiMiExtentionTransitionConfigurer<T, S> transition(T event) {
		return transitions.get(event);
	}
}
