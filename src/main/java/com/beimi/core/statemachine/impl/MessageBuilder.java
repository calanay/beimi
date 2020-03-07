package com.beimi.core.statemachine.impl;
import com.beimi.core.statemachine.message.GenericMessage;
import com.beimi.core.statemachine.message.Message;
import com.beimi.core.statemachine.message.MessageHeaders;

/**
 * The default message builder; creates immutable {@link GenericMessage}s.
 * Named MessageBuilder instead of DefaultMessageBuilder for backwards
 * compatibility.
 *
 * @author Arjen Poutsma
 * @author Mark Fisher
 * @author Oleg Zhurakousky
 * @author Dave Syer
 * @author Gary Russell
 * @author Artem Bilan
 */
public final class MessageBuilder<T>  {

	private final T payload;

	private MessageHeaders readOnlyHeaders = new MessageHeaders();


	/**
	 * Private constructor to be invoked from the static factory methods only.
	 */
	private MessageBuilder(T payload) {
		this.payload = payload;
	}

	public T getPayload() {
		return this.payload;
	}

	public MessageBuilder<T> setHeader(String headerName, Object headerValue) {
		this.readOnlyHeaders.getHeaders().put(headerName, headerValue) ;
		return this;
	}
	
	/**
	 * Create a builder for a new {@link Message} instance with the provided payload.
	 *
	 * @param payload the payload for the new message
	 * @param <T> The type of the payload.
	 * @return A MessageBuilder.
	 */
	public static <T> MessageBuilder<T> withPayload(T payload) {
		return new MessageBuilder<T>(payload);
	}

	public Message<T> build() {
		return new GenericMessage<T>(this.payload, this.readOnlyHeaders);
	}


}