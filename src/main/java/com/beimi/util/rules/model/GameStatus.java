package com.beimi.util.rules.model;

import com.beimi.core.engine.game.Message;

public class GameStatus implements Message{
	private String command ;
	private String gamestatus ;
	
	private String userid ;
	private String orgi ;
	private String gametype ;
	private String playway ;
	private boolean cardroom ; 
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getGamestatus() {
		return gamestatus;
	}
	public void setGamestatus(String gamestatus) {
		this.gamestatus = gamestatus;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrgi() {
		return orgi;
	}

	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public String getPlayway() {
		return playway;
	}

	public void setPlayway(String playway) {
		this.playway = playway;
	}
	public boolean isCardroom() {
		return cardroom;
	}
	public void setCardroom(boolean cardroom) {
		this.cardroom = cardroom;
	}
	
}
