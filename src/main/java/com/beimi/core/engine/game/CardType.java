package com.beimi.core.engine.game;

public class CardType implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxcard ;	//最大的牌
	private int cardtype ;	//牌型
	private int typesize ;	//不同牌数量
	private boolean king ;	//王炸
	private boolean bomb;	//炸弹
	private int mincard ; 	//最小的牌
	private int cardnum ;	//最大牌张数  ， JJJQ，= 3
	private byte maxcardvalue ;
	
	public CardType(){}
	public CardType(int maxcard , int cardtype , boolean king , boolean bomb){
		this.maxcard = maxcard ;
		this.cardtype = cardtype ;
		this.king = king ;
		this.bomb = bomb ;
	}
	
	public int getMaxcard() {
		return maxcard;
	}
	public void setMaxcard(int maxcard) {
		this.maxcard = maxcard;
	}
	public int getCardtype() {
		return cardtype;
	}
	public void setCardtype(int cardtype) {
		this.cardtype = cardtype;
	}
	public boolean isKing() {
		return king;
	}
	public void setKing(boolean king) {
		this.king = king;
	}
	public boolean isBomb() {
		return bomb;
	}
	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}
	public int getTypesize() {
		return typesize;
	}
	public void setTypesize(int typesize) {
		this.typesize = typesize;
	}
	public int getMincard() {
		return mincard;
	}
	public void setMincard(int mincard) {
		this.mincard = mincard;
	}
	public int getCardnum() {
		return cardnum;
	}
	public void setCardnum(int cardnum) {
		this.cardnum = cardnum;
	}
	public byte getMaxcardvalue() {
		return maxcardvalue;
	}
	public void setMaxcardvalue(byte maxcardvalue) {
		this.maxcardvalue = maxcardvalue;
	}
}
