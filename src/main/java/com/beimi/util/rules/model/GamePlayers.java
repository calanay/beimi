package com.beimi.util.rules.model;

import java.util.List;

import com.beimi.core.engine.game.Message;
import com.beimi.web.model.PlayUserClient;

public class GamePlayers implements Message{
	private int maxplayers ;
	private String command ;
	private List<PlayUserClient> player ;
	
	public GamePlayers(int maxplayers , List<PlayUserClient> player ,String command){
		this.maxplayers = maxplayers ;
		this.player = player ;
		this.command = command ;
	}
	
	public int getMaxplayers() {
		return maxplayers;
	}
	public void setMaxplayers(int maxplayers) {
		this.maxplayers = maxplayers;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public List<PlayUserClient> getPlayer() {
		return player;
	}
	public void setPlayer(List<PlayUserClient> player) {
		this.player = player;
	}
}
