package com.beimi.core.engine.game.model;


public class SummaryPlayer implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userid ;
	private String username ;
	private int ratio ;
	private int score ;
	private boolean gameover ;//破产了
	private int balance ;	  //玩家账户余额
	private boolean win ;
	private byte[] cards ;
	
	private boolean dizhu ;
	
	public SummaryPlayer(){}
	public SummaryPlayer(String userid , String username , int ratio , int score, boolean win , boolean dizhu){
		this.userid = userid ;
		this.username = username ;
		this.ratio = ratio ; 
		this.score = score ;
		this.win = win ;
		this.dizhu = dizhu ;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getRatio() {
		return ratio;
	}
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isWin() {
		return win;
	}
	public void setWin(boolean win) {
		this.win = win;
	}
	public byte[] getCards() {
		return cards;
	}
	public void setCards(byte[] cards) {
		this.cards = cards;
	}
	public boolean isGameover() {
		return gameover;
	}
	public void setGameover(boolean gameover) {
		this.gameover = gameover;
	}
	public boolean isDizhu() {
		return dizhu;
	}
	public void setDizhu(boolean dizhu) {
		this.dizhu = dizhu;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
}
