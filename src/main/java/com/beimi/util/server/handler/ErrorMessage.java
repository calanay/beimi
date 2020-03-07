package com.beimi.util.server.handler;

import com.beimi.core.engine.game.Message;

public class ErrorMessage implements java.io.Serializable,Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String command ;
	private String result ;
	private String token ;
	
	public ErrorMessage(String command , String result , String token) {
		this.command = command ;
		this.result = result ;
		this.token = token ;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
