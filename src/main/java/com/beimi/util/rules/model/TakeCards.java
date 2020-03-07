package com.beimi.util.rules.model;

import com.beimi.core.engine.game.CardType;
import com.beimi.core.engine.game.Message;

/**
 * 当前出牌信息
 * 出牌人
 * 牌
 * @author zhangtianyi
 *
 */
public abstract class TakeCards implements Message , java.io.Serializable{
	private static final long serialVersionUID = 8718778983090104033L;
	
	private String banker ;
	private boolean allow ;		//符合出牌规则 ， 
	private boolean donot ;		//出 OR 不出
	private String userid ;
	private byte[] cards ;
	private byte card ;			//麻将出牌
	
	private boolean over ;		//已结束
	
	private boolean bomb ;		//炸
	
	private long time ;
	private int type ;		//出牌类型 ： 1:单张 | 2:对子 | 3:三张 | 4:四张（炸） | 5:单张连 | 6:连对 | 7:飞机 : 8:4带2 | 9:王炸
	private CardType cardType ;//出牌的牌型
	
	private String command ;
	
	private boolean sameside ;	//同一伙
	
	private int cardsnum ;	//当前出牌的人 剩下多少张 牌
	
	private String nextplayer ;		// 下一个出牌玩家
	private boolean automic ;		//是否允许不出牌过
	private byte nextplayercard ;	//下一个玩家翻到的新牌
	
	/**
	 * 移除出牌
	 * @param cards
	 * @param start
	 * @param end
	 * @return
	 */
	public byte[] removeCards(byte[] cards , int start , int end){
		byte[] retCards = new byte[cards.length - (end - start)] ;
		int inx = 0 ;
		for(int i=0; i<cards.length ; i++){
			if(i<start || i >= end){
				retCards[inx++] = cards[i] ;
			}
		}
		return retCards ;
	}
	
	/**
	 * 移除出牌
	 * @param cards
	 * @param start
	 * @param end
	 * @return
	 */
	public byte[] removeCards(byte[] cards , byte[] playcards){
		byte[] retCards = new byte[cards.length - playcards.length] ;
		int cardsindex = 0 ;
		for(int i=0; i<cards.length ; i++){
			boolean found = false ;
			for(int inx = 0 ;inx<playcards.length ; inx++){
				if(cards[i] == playcards[inx]){
					found = true ; break ;
				}
			}
			if(found == false){
				retCards[cardsindex++] = cards[i] ;
			}
		}
		return retCards ;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public byte[] getCards() {
		return cards;
	}
	public void setCards(byte[] cards) {
		this.cards = cards;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCardsnum() {
		return cardsnum;
	}
	public void setCardsnum(int cardsnum) {
		this.cardsnum = cardsnum;
	}
	public String getNextplayer() {
		return nextplayer;
	}
	public void setNextplayer(String nextplayer) {
		this.nextplayer = nextplayer;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public boolean isAllow() {
		return allow;
	}

	public void setAllow(boolean allow) {
		this.allow = allow;
	}

	public boolean isDonot() {
		return donot;
	}

	public void setDonot(boolean donot) {
		this.donot = donot;
	}

	public boolean isSameside() {
		return sameside;
	}

	public void setSameside(boolean sameside) {
		this.sameside = sameside;
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

	public byte getCard() {
		return card;
	}

	public void setCard(byte card) {
		this.card = card;
	}

	public byte getNextplayercard() {
		return nextplayercard;
	}

	public void setNextplayercard(byte nextplayercard) {
		this.nextplayercard = nextplayercard;
	}

	public boolean isAutomic() {
		return automic;
	}

	public void setAutomic(boolean automic) {
		this.automic = automic;
	}

	public boolean isBomb() {
		return bomb;
	}

	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}
}
