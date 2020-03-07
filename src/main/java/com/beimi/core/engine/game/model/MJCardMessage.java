package com.beimi.core.engine.game.model;

import com.beimi.core.engine.game.Message;

/**
 * 存储牌型
 * 
 * @author iceworld
 *
 */
public class MJCardMessage implements Message,java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean gang ;
	private boolean peng ;
	private boolean chi ;
	private boolean hu;
	
	private boolean deal ;	//是否发牌动作
	
	private byte card ;
	
	private String command;
	private String userid ;
	
	private String takeuser ; //出牌的人
	
	public MJCardMessage(){
		
	}
	public MJCardMessage(boolean gang , boolean peng , boolean hu){
		this.gang = gang ;
		this.peng = peng ;
		this.hu = hu ;
	}
	
	public boolean isGang() {
		return gang;
	}
	public void setGang(boolean gang) {
		this.gang = gang;
	}
	public boolean isPeng() {
		return peng;
	}
	public void setPeng(boolean peng) {
		this.peng = peng;
	}
	public boolean isChi() {
		return chi;
	}
	public void setChi(boolean chi) {
		this.chi = chi;
	}
	public boolean isHu() {
		return hu;
	}
	public void setHu(boolean hu) {
		this.hu = hu;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public byte getCard() {
		return card;
	}
	public void setCard(byte card) {
		this.card = card;
	}
	public String getTakeuser() {
		return takeuser;
	}
	public void setTakeuser(String takeuser) {
		this.takeuser = takeuser;
	}
	public boolean isDeal() {
		return deal;
	}
	public void setDeal(boolean deal) {
		this.deal = deal;
	}
}
