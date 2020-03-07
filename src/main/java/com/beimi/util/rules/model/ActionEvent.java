package com.beimi.util.rules.model;

import com.beimi.core.engine.game.Message;

/**
 * 当前出牌信息
 * 出牌人
 * 牌
 * @author zhangtianyi
 *
 */
public class ActionEvent implements Message , java.io.Serializable{
	private static final long serialVersionUID = 8718778983090104033L;
	
	private String banker ;
	private String userid ;
	private String target ; 	//目标， 杠/碰的 目标
	
	private String actype ;		//杠 的类型 ， 明杠/暗杠/弯杠
	private String action;
	private byte card ;
	
	private byte cardtype ;
	private byte cardvalue ;
	
	private long time ;
	
	
	public ActionEvent(String banker){
		this.banker = banker ;
	}
	public ActionEvent(String banker , String userid, byte card , String action){
		this.userid = userid ;
		this.card = card ; 
		this.action = action ;
		this.banker = banker ;
		this.cardtype = (byte) (card / 36) ;
		this.cardvalue = (byte) (( card % 36 ) /4) ;
	}
	
	private String command ;
	
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public byte getCard() {
		return card;
	}
	public void setCard(byte card) {
		this.card = card;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public byte getCardtype() {
		return cardtype;
	}
	public void setCardtype(byte cardtype) {
		this.cardtype = cardtype;
	}
	public byte getCardvalue() {
		return cardvalue;
	}
	public void setCardvalue(byte cardvalue) {
		this.cardvalue = cardvalue;
	}
	public String getActype() {
		return actype;
	}
	public void setActype(String actype) {
		this.actype = actype;
	}
}
