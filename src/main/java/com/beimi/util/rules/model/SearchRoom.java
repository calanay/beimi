package com.beimi.util.rules.model;

public class SearchRoom implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7161988199372214969L;
	private String token ;
	private String roomid ;
	private String orgi ;
	private String userid ;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
