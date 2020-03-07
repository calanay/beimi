package com.beimi.util.rules.model;

import com.beimi.core.engine.game.Message;

/**
 * 当前出牌信息
 * 出牌人
 * 牌
 * @author zhangtianyi
 *
 */
public class SelectColor implements Message , java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8718778983090104033L;
	
	private String banker ;
	private String userid ;
	private int color  = 10;
	private long time ;
	
	
	public SelectColor(String banker){
		this.banker = banker ;
	}
	public SelectColor(String banker , String userid){
		this.userid = userid ;
		this.banker = banker ;
	}
	
	private String command ;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	public String getBanker() {
		return banker;
	}

	public void setBanker(String banker) {
		this.banker = banker;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
