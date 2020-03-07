package com.beimi.core.engine.game.model;

import java.util.ArrayList;
import java.util.List;

public class Type implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Type(String id , String name , String code){
		this.id = id ;
		this.name = name ;
		this.code = code ;
	}
	private String id ;
	private String name ;
	private String code ;
	private List<Playway> playways = new ArrayList<Playway>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<Playway> getPlayways() {
		return playways;
	}
	public void setPlayways(List<Playway> playways) {
		this.playways = playways;
	}
}
