package com.beimi.util.rules.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.CardType;
import com.beimi.core.engine.game.Message;

/**
 * 当前出牌信息
 * 出牌人
 * 牌
 * @author zhangtianyi
 *
 */
public class TakeMaJiangCards extends TakeCards implements Message , java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8718778983090104033L;
	
	private String banker ;
	private boolean allow ;		//符合出牌规则 ， 
	private boolean donot ;		//出 OR 不出
	private String userid ;
	private byte[] cards ;
	private long time ;
	private int type ;		//出牌类型 ： 1:单张 | 2:对子 | 3:三张 | 4:四张（炸） | 5:单张连 | 6:连对 | 7:飞机 : 8:4带2 | 9:王炸
	private CardType cardType ;//出牌的牌型
	
	private String command ;
	
	private boolean sameside ;	//同一伙
	
	private int cardsnum ;	//当前出牌的人 剩下多少张 牌
	
	private String nextplayer ;	// 下一个出牌玩家
	
	
	public TakeMaJiangCards(){}
	
	/**
	 * 默认，自动出牌
	 * @param player
	 */
	public TakeMaJiangCards(Player player){
		this.userid = player.getPlayuser() ;
		this.cards = getAIMostSmall(player, 0) ;
		if(this.cards != null){
			player.setCards(this.removeCards(player.getCards() , cards));
			this.cardType =  ActionTaskUtils.identification(cards);
			this.type = cardType.getCardtype() ;
		}
		this.allow = true ;
		this.cardsnum = player.getCards().length ;
	}
	/**
	 * 最小出牌 ， 管住 last
	 * @param player
	 * @param last
	 */
	public TakeMaJiangCards(Player player , TakeCards last){
		this.userid = player.getPlayuser() ;
		if(last != null){
			this.cards = this.search(player, last) ;
		}else{
			this.cards = getAIMostSmall(player, 0) ;
		}
		if(cards!=null){
			player.setCards(this.removeCards(player.getCards() , cards));
			this.allow = true ;
			this.cardType =  ActionTaskUtils.identification(cards);
			this.type = cardType.getCardtype() ;
		}
		this.cardsnum = player.getCards().length ;
	}
	
	
	/**
	 * 
	 * 玩家出牌，不做校验，传入之前的校验结果
	 * @param player
	 * @param last
	 * @param cards
	 */
	public TakeMaJiangCards(Player player , boolean allow , byte[] playCards){
		this.userid = player.getPlayuser() ;
		if(playCards == null){
			this.cards = getAIMostSmall(player, 0) ;
		}else{
			this.cards = playCards ;
		}
		if(this.cards!=null && this.cards.length > 0){
			player.setCards(this.removeCards(player.getCards() , this.cards));
			this.setCard(this.cards[0]);
		}
		this.cardsnum = player.getCards().length ;
		this.allow = true;
	}
	
	/**
	 * 搜索符合条件的当前最小 牌型
	 * @param player
	 * @param last
	 * @return
	 */
	public byte[] search(Player player , TakeCards lastTakeCards){
		byte[] retValue = null ;
		Map<Integer,Integer> types = ActionTaskUtils.type(player.getCards()) ;
		if(lastTakeCards.getCardType().getTypesize() <= 3){//三带一 、 四带二
			if(lastTakeCards.getCardType().getCardnum() == 4 || lastTakeCards.getCardType().getCardnum() == 3){
				for(int i=lastTakeCards.getCardType().getMincard() + 1; i<14 ; i++){
					if(types.get(i) != null){	//找到能管得住的了 ， 再选 一张到两张配牌
						byte[] supplement = null ;
						Map<Integer,Integer> exist = new HashMap<Integer ,Integer>();
						exist.put(i, i) ;
						if(lastTakeCards.getCardType().getTypesize() == 1){
							supplement = this.getPair(player.getCards(), types , -1, 1, exist) ;
						}else{
							supplement = this.getSingle(player.getCards(), types, -1 , 2, exist) ;
						}
						if(supplement!=null){
							retValue = new byte[types.get(i)] ;
							int length = 0 ;
							for(int inx =0 ; inx < player.getCards().length ; inx++){
								if(player.getCards()[inx] / 4 == i){
									retValue[length++] = player.getCards()[inx] ;
								}
							}
							retValue = ArrayUtils.addAll(retValue, supplement) ;
						}
					}
				}
			}else if(lastTakeCards.getCardType().getCardnum() == 2){	//对子
				retValue = this.getPair(player.getCards(), types, lastTakeCards.getCardType().getMincard() ,1, new HashMap<Integer , Integer>()) ;
			}else if(lastTakeCards.getCardType().getCardnum() == 3){	//对子
				retValue = this.getThree(player.getCards(), types, lastTakeCards.getCardType().getMincard() ,1, new HashMap<Integer , Integer>()) ;
			}else{	//单张
				retValue = this.getSingle(player.getCards(), types, lastTakeCards.getCardType().getMincard(), 1, new HashMap<Integer , Integer>()) ;
			}
		}else{//单顺，双顺， 三顺
			
		}
		return retValue ;
	}
	/**
	 * 找到num对子
	 * @param num
	 * @return
	 */
	public byte[] getPair(byte[] cards , Map<Integer,Integer> types , int mincard , int num , Map<Integer,Integer> exist){
		byte[] retCards = null;
		List<Integer> retValue = new ArrayList<Integer>();
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null && types.get(i) == 2  && retValue.size() < num && (i<0 || i>mincard) && !exist.containsKey(i)){
				retValue.add(i) ;
				exist.put(i, i) ;
			}
			if(retValue.size() == num){
				break ;
			}
		}
		if(retValue.size() == num){
			retCards = new byte[num*2] ;
			int inx = 0 ;
			for(int temp : retValue){
				int times = 0 ;
				for(byte card : cards){
					if(card/4 == temp){
						retCards[inx++] = card ;
						times++;
					}
					if(times == 2){
						break ;
					}
				}
			}
		}
		return retCards ;
	}
	public byte[] getSingle(byte[] cards, Map<Integer,Integer> types , int mincard ,int num , Map<Integer,Integer> exist){
		byte[] retCards = null;
		List<Integer> retValue = new ArrayList<Integer>();
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null && types.get(i) ==1  && retValue.size() < num && (i<0 || i>mincard) && !exist.containsKey(i)){
				retValue.add(i) ;
				exist.put(i, i) ;
			}
			if(retValue.size() == num){
				break ;
			}
		}
		if(retValue.size() < num){	//补充查找
			for(int i=0 ; i<14 ; i++){
				if(types.get(i) != null && types.get(i) >1 && (i<0 || i>mincard)  && retValue.size() < num){
					retValue.add(i) ;
					exist.put(i, i) ;
					if(retValue.size() == num){
						break ;
					}
				}
			}
		}
		if(retValue.size() == num){
			retCards = new byte[num] ;
			int inx = 0 ;
			for(int temp : retValue){
				for(byte card : cards){
					if(card/4 == temp){
						retCards[inx++] = card ;
					}
					if(inx >= num){
						break ;
					}
				}
			}
		}
		return retCards ;
	}
	/**
	 * 找到num对子
	 * @param num
	 * @return
	 */
	public byte[] getThree(byte[] cards , Map<Integer,Integer> types , int mincard , int num , Map<Integer,Integer> exist){
		byte[] retCards = null;
		List<Integer> retValue = new ArrayList<Integer>();
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null && types.get(i) == 2  && retValue.size() < num && (i<0 || i>mincard) && !exist.containsKey(i)){
				retValue.add(i) ;
				exist.put(i, i) ;
			}
			if(retValue.size() == num){
				break ;
			}
		}
		if(retValue.size() == num){
			retCards = new byte[num*3] ;
			int inx = 0 ;
			for(int temp : retValue){
				int times = 0 ;
				for(byte card : cards){
					if(card/4 == temp){
						retCards[inx++] = card ;
						times++;
					}
					if(times == 3){
						break ;
					}
				}
			}
		}
		return retCards ;
	}
//	
//	public byte[] searchBegin(Player player , int card){
//		
//	}
	
	/**
	 * 找到机器人或托管的牌 ， 优先检查是否有 定缺，如果有定缺，就查找定缺的牌
	 * @param cards
	 * @param start
	 * @return
	 */
	public byte[] getAIMostSmall(Player player , int start){
		byte[] retCards = new byte[1] ;
		boolean found = false ;
		if(player.isSelected()){
			for(byte card : player.getCards()){
				if(card/36 == player.getColor()){
					retCards[0] = card ;found = true ; break ;
				}
			}
			
		}
		if(found == false){
			retCards = ArrayUtils.subarray(player.getCards(), player.getCards().length-1, player.getCards().length) ;
		}
		return retCards;
	}
	
	/**
	 * 找到托管玩家或超时玩家的最小的牌 ，不管啥牌，从最小的开始出
	 * @param cards
	 * @param start
	 * @return
	 */
	public byte[] getMostSmall(Player player, int start ){
		byte[] takeCards = null;
		if(player.getCards().length>0){
			takeCards = ArrayUtils.subarray(player.getCards(),player.getCards().length - 1,player.getCards().length) ;
			player.setCards(this.removeCards(player.getCards(), player.getCards().length - 1,player.getCards().length));
		}
		return takeCards ;
	}
	
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
		for(int i=0; i<cards.length && cardsindex < cards.length ; i++){
			boolean found = false ;
			for(int inx = 0 ;inx<playcards.length ; inx++){
				if(cards[i] == playcards[inx]){
					found = true ; break ;
				}
			}
			if(found == false){
				retCards[cardsindex] = cards[i] ;
				cardsindex = cardsindex + 1;
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
}
