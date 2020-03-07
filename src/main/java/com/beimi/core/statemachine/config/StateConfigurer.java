package com.beimi.core.statemachine.config;

/**
 * 游戏状态配置接口
 * 
 * @author 
 *
 * @param <S>
 * @param <E>
 */
public interface StateConfigurer<S, E> {
	
	/**
	 * Specify a initial state {@code S}.
	 *
	 * @param initial the initial state
	 * @return configurer for chaining
	 */
	StateConfigurer<S, E> initial(S initial);
	
	/**
	 * Specify a state {@code S}.
	 *
	 * @param state the state
	 * @return configurer for chaining
	 */
	StateConfigurer<S, E> state(S state);
	
	StateConfigurer<S, E> withStates() throws Exception;
}