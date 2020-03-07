package com.beimi.core.statemachine.config;

public interface StateMachineStateConfigurer<S , E> {
	/**
	 * Gets a configurer for states.
	 * 
	 * @return {@link StateConfigurer} for chaining
	 * @throws Exception if configuration error happens
	 */
	StateConfigurer<S, E> withStates() throws Exception;
}
