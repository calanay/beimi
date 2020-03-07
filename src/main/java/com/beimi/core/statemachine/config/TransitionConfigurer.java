package com.beimi.core.statemachine.config;

import com.beimi.core.statemachine.action.Action;

public interface TransitionConfigurer<T, S, E> extends AbstractTransitionConfigurer<StateMachineTransitionConfigurer<S, E>>{
	/**
	 * Specify a source state {@code S} for this {@link Transition}.
	 *
	 * @param source the source state {@code S}
	 * @return configurer for chaining
	 */
	T source(S source);
	
	/**
	 * 
	 * @param target
	 * @return
	 */
	T target(S target);

	/**
	 * Specify event {@code E} for this {@link Transition} which will be triggered
	 * by a event trigger.
	 *
	 * @param event the event for transition
	 * @return configurer for chaining
	 */
	T event(E event);
	
	/**
	 * Specify {@link Action} for this {@link Transition}.
	 *
	 * @param action the action
	 * @return configurer for chaining
	 */
	T action(Action<S, E> action);

	/**
	 * Specify {@link Action} for this {@link Transition}.
	 *
	 * @param action the action
	 * @param error action that will be called if any unexpected exception is thrown by the action.
	 * @return configurer for chaining
	 */
	T action(Action<S, E> action, Action<S, E> error);
}
