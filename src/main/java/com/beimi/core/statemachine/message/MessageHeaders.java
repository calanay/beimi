package com.beimi.core.statemachine.message;

import java.util.HashMap;
import java.util.Map;

public class MessageHeaders {
	private final Map<String, Object> headers ;
	
	public MessageHeaders(){
		this.headers = new HashMap<String, Object>() ;
	}
	
	public MessageHeaders(Map<String, Object> headers){
		if(headers!=null){
			this.headers = headers ;
		}else{
			this.headers = new HashMap<String, Object>() ;
		}
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}
}
