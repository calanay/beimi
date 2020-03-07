package com.beimi.util.rules.model;

import com.beimi.core.engine.game.Message;

/**
 * 发牌数据对象
 * 
 * @author
 *
 */
public class DealCard implements Message{
	private String command ;
	private String userid ;
	private int deskcards ;
	private byte card ;		//发给当前玩家的时候， 需要有牌面值 ， 发给其他玩家的时候，则不能有牌面值
	
	private boolean action ;	//是否有 杠碰吃胡
	
	private String type ;		//胡的类型 ， 杠的类型
	
	private int color;	//定缺的是啥
	
	public DealCard(String userid , int deskcards){
		this.userid = userid ; 
		this.deskcards = deskcards ;
	}
	
	public DealCard(String userid , int deskcards ,int color, byte card , boolean action){
		this.userid = userid ; 
		this.deskcards = deskcards ;
		this.card = card ;
		this.color = color ;
		this.action = action ;
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
	public int getDeskcards() {
		return deskcards;
	}
	public void setDeskcards(int deskcards) {
		this.deskcards = deskcards;
	}
	public byte getCard() {
		return card;
	}
	public void setCard(byte card) {
		this.card = card;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
