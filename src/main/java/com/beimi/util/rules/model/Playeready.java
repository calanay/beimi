package com.beimi.util.rules.model;

import com.beimi.core.engine.game.Message;

public class Playeready implements Message,java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6085010047550005210L;
	
	public Playeready(String userid , String command){
		this.userid = userid ;
		this.command = command ;
	}
	private String command ;
	
	private String userid ;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
