package com.beimi.util.server.handler;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.Map;

public class BeiMiClient{
	private String token ;
	/** 玩的方式 ？*/
	private String playway ;
	/**
	 *	开放服务网关倡议（OSGi，Open Service Gateway Initiative）?
	 *  [data:　beimi]
	 **/
	private String orgi ;
	private String room ;
	/**
	 * 心跳时间
	 */
	private long time ;
	
	private String userid ;
	
	private String session ;
	
	
	private SocketIOClient client;

	private Map<String,  String> extparams ;
	
	public BeiMiClient(){
		
	}
	
	public String getSession() {
		return session;
	}


	public void setSession(String session) {
		this.session = session;
	}


	public String getPlayway() {
		return playway;
	}

	public void setPlayway(String playway) {
		this.playway = playway;
	}

	public String getOrgi() {
		return orgi;
	}

	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}

	public SocketIOClient getClient() {
		return client;
	}

	public void setClient(SocketIOClient client) {
		this.client = client;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Map<String, String> getExtparams() {
		return extparams;
	}

	public void setExtparams(Map<String, String> extparams) {
		this.extparams = extparams;
	}
}
