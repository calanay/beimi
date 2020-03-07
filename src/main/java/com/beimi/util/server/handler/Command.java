package com.beimi.util.server.handler;

public class Command implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7231534341030590077L;
	private String command ;
	private String data ;
	private String token ;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
