package com.beimi.util.rules.model;

public class SearchRoomResult implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8077510029073026136L;
	private String id ;		//玩法ID
	private String code ;	//游戏类型
	private String roomid ; //房间ID
	private String result ;	//
	
	
	public SearchRoomResult(){}
	
	public SearchRoomResult(String result){
		this.result = result ;
	}
	
	public SearchRoomResult(String id , String code ,String result){
		this.id = id;
		this.code = code;
		this.result = result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
