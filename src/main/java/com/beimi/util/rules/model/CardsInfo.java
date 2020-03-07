package com.beimi.util.rules.model;

import java.util.List;

public class CardsInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4465163177787451309L;
	private String userid;
	private int cardsnum ;
	private byte[] hiscards ;
	private List<Action> actions ;
	
	private SelectColor selectcolor ; 
	
	public CardsInfo(String userid, int cardsnum){
		this.userid = userid ;
		this.cardsnum = cardsnum ;
	}
	
	public CardsInfo(String userid, int cardsnum,byte[] hiscards , List<Action> actions , Board board , Player player){
		this.userid = userid ;
		this.cardsnum = cardsnum ;
		this.hiscards = hiscards ;
		this.actions = actions ;
		this.selectcolor = new SelectColor(board.getBanker(),userid) ;
		this.selectcolor.setColor(player.getColor());
		this.selectcolor.setCommand("selectresult");
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getCardsnum() {
		return cardsnum;
	}
	public void setCardsnum(int cardsnum) {
		this.cardsnum = cardsnum;
	}

	public byte[] getHiscards() {
		return hiscards;
	}

	public void setHiscards(byte[] hiscards) {
		this.hiscards = hiscards;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public SelectColor getSelectcolor() {
		return selectcolor;
	}

	public void setSelectcolor(SelectColor selectcolor) {
		this.selectcolor = selectcolor;
	}
}
