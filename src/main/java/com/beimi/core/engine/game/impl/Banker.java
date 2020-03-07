package com.beimi.core.engine.game.impl;

import java.io.Serializable;

import com.beimi.core.engine.game.Message;

/**
 * 庄家
 * <p>
 * 上局赢牌的用户，如果是新创建房间，则创建房间用户为庄家
 * @author
 *
 */
public class Banker implements Message,Serializable{
	private static final long serialVersionUID = -9011347288522873348L;
	
	private String command ;
	private String userid ;
	
	public Banker(String userid){
		this.userid = userid ;
	}
	
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