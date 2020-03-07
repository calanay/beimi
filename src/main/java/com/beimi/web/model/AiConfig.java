package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_game_ai")
@org.hibernate.annotations.Proxy(lazy = false)
public class AiConfig implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 565678041210332017L;
	private String id ;
	private String orgi ;
	private Date createtime = new Date() ;
	private String creater ;
	private String username ;
	private String name ;
	
	private boolean enableai ;//启用AI
	private int waittime = 5; //玩家等待时长
	
	private int initcoins ;			//初始 金币数量
	private int initcards ;			//初始房卡数量
	private int initdiamonds;		//初始钻石数量
	
	private String exitcon ;		//机器人退出条件
	private int maxai ;				//最大AI数量
	
	private boolean dicinfo ;		//从字典获取 AI的用户昵称、头像信息
	private boolean aichat	;		//启用 AI自动聊天功能
	
	
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnableai() {
		return enableai;
	}
	public void setEnableai(boolean enableai) {
		this.enableai = enableai;
	}
	public int getWaittime() {
		return waittime;
	}
	public void setWaittime(int waittime) {
		this.waittime = waittime;
	}
	public int getInitcoins() {
		return initcoins;
	}
	public void setInitcoins(int initcoins) {
		this.initcoins = initcoins;
	}
	public int getInitcards() {
		return initcards;
	}
	public void setInitcards(int initcards) {
		this.initcards = initcards;
	}
	public int getInitdiamonds() {
		return initdiamonds;
	}
	public void setInitdiamonds(int initdiamonds) {
		this.initdiamonds = initdiamonds;
	}
	public String getExitcon() {
		return exitcon;
	}
	public void setExitcon(String exitcon) {
		this.exitcon = exitcon;
	}
	public int getMaxai() {
		return maxai;
	}
	public void setMaxai(int maxai) {
		this.maxai = maxai;
	}
	public boolean isDicinfo() {
		return dicinfo;
	}
	public void setDicinfo(boolean dicinfo) {
		this.dicinfo = dicinfo;
	}
	public boolean isAichat() {
		return aichat;
	}
	public void setAichat(boolean aichat) {
		this.aichat = aichat;
	}
	
}
