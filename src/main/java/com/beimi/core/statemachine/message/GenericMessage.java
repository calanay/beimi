package com.beimi.core.statemachine.message;

import java.io.Serializable;
import java.util.Map;

public class GenericMessage<T> implements Message<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 663603304301142068L;
	
	private final T payload;
	
	private final MessageHeaders headers;

	public GenericMessage(T payload, Map<String, Object> headers) {
		this(payload, new MessageHeaders(headers));
	}
	
	public GenericMessage(T payload, MessageHeaders headers) {
		this.payload = payload;
		this.headers = headers;
	}

	public MessageHeaders getMessageHeaders() {
		return headers;
	}
	@Override
	public T getPayload() {
		return payload;
	}
}