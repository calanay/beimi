package com.beimi.util.rules.model;

import java.util.ArrayList;
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
public class TakeDiZhuCards extends TakeCards implements Message , java.io.Serializable{

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
	
	
	public TakeDiZhuCards(){}
	
	/**
	 * 默认，自动出牌
	 * @param player
	 */
	public TakeDiZhuCards(Player player){
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
	public TakeDiZhuCards(Player player , TakeCards last){
		this(player, last, true) ;
	}
	
	/**
	 * 最小出牌 ， 管住 last
	 * @param player
	 * @param last
	 */
	public TakeDiZhuCards(Player player , TakeCards last , boolean take){
		this.userid = player.getPlayuser() ;
		if(last != null){
			this.cards = this.search(player, last) ;
		}else{
			this.cards = getAIMostSmall(player, 0) ;
		}
		if(cards!=null){
			if(take == true){
				player.setCards(this.removeCards(player.getCards() , cards));
			}
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
	public TakeDiZhuCards(Player player , boolean allow , byte[] playCards){
		this.userid = player.getPlayuser() ;
		if(playCards == null){
			this.cards = getAIMostSmall(player, 0) ;
		}else{
			this.cards = playCards ;
		}
		if(this.cards!=null){
			player.setCards(this.removeCards(player.getCards() , this.cards));
			this.cardType =  ActionTaskUtils.identification(cards);
			this.type = cardType.getCardtype() ;
		}
		this.cardsnum = player.getCards().length ;
		this.allow = true;
	}
	

	/**
	 * 
	 * 玩家出牌，不做校验，传入之前的校验结果
	 * @param player
	 * @param last
	 * @param cards
	 */
	public TakeDiZhuCards(Player player , byte[] tipcards){
		this.userid = player.getPlayuser() ;
		this.cards = tipcards ;
		if(this.cards!=null){
			this.cardType =  ActionTaskUtils.identification(cards);
			this.type = cardType.getCardtype() ;
		}
		this.cardsnum = player.getCards().length ;
		this.allow = true;
	}
	
	
	/**
	 * 搜索符合条件的当前最小 牌型 , 机器人出牌 ， 只处理到 三带一，其他以后在扩展
	 * @param player
	 * @param last
	 * @return
	 */
	public byte[] search(Player player , TakeCards lastTakeCards){
		byte[] retValue = null ;
		Map<Integer,Integer> types = ActionTaskUtils.type(player.getCards()) ;
		if(lastTakeCards!=null && lastTakeCards.getCardType()!=null){
			switch(lastTakeCards.getCardType().getCardtype()){
				case 1 : //单张
					retValue = this.getSingle(player.getCards(), types, lastTakeCards.getCardType().getMaxcard(), 1) ;
					break ;
				case 2 : //一对儿
					retValue = this.getPair(player.getCards(), types, lastTakeCards.getCardType().getMaxcard() ,1) ;
					break ;
				case 3 : //三张
					retValue = this.getThree(player.getCards(), types, lastTakeCards.getCardType().getMaxcard() ,1) ;
					break ;
				case 4 : //三带一
					retValue = this.getThree(player.getCards(), types, lastTakeCards.getCardType().getMaxcard() ,1) ;
					if(retValue!=null && retValue.length == 3){
						byte[] supplement = null ;
						if(lastTakeCards.getCards().length == 4){	//三带一
							supplement = this.getSingle(player.getCards(), types, -1 , 1) ;
						}else if(lastTakeCards.getCards().length == 5){	//三带一对儿
							supplement = this.getPair(player.getCards(), types , -1, 1) ;
						}else if(lastTakeCards.getCards().length == 6){	//三顺
							supplement = this.getThree(player.getCards(), types , -1, 1) ;
						}
						if(supplement!=null){
							retValue = ArrayUtils.addAll(retValue, supplement) ;
						}else{
							retValue = null ;
						}
					}
					break ;
			}
		}
		return retValue ;
	}
	/**
	 * 找到num对子
	 * @param num
	 * @return
	 */
	public byte[] getPair(byte[] cards , Map<Integer,Integer> types , int mincard , int num ){
		byte[] retCards = null;
		List<Integer> retValue = new ArrayList<Integer>();
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null && types.get(i) == 2  && retValue.size() < num && (i<0 || i>mincard)){
				retValue.add(i) ;
			}
			if(retValue.size() >= num){
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
	public byte[] getSingle(byte[] cards, Map<Integer,Integer> types , int mincard ,int num ){
		byte[] retCards = null;
		List<Integer> retValue = new ArrayList<Integer>();
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null && types.get(i) ==1  && retValue.size() < num && (i>mincard || i == 13)){
				retValue.add(i) ;
			}
			if(retValue.size() >= num){
				break ;
			}
		}
		if(retValue.size() == num){
			retCards = new byte[num] ;
			int inx = 0 ;
			for(int temp : retValue){
				for(byte card : cards){
					if(temp == 13 && mincard == 13){
						if(card == 53){
							retCards[inx++] = card ;
						}
					}else{
						if(card/4 == temp){
							retCards[inx++] = card ;
						}
					}
					if(inx >= num){
						break ;
					}
				}
			}
			if(inx == 0){
				retCards = null ;
			}
		}
		return retCards ;
	}
	/**
	 * 找到num对子
	 * @param num
	 * @return
	 */
	public byte[] getThree(byte[] cards , Map<Integer,Integer> types , int mincard , int num){
		byte[] retCards = null;
		List<Integer> retValue = new ArrayList<Integer>();
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null && types.get(i) == 3  && retValue.size() < num && (i<0 || i>mincard)){
				retValue.add(i) ;
			}
			if(retValue.size() >= num){
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
	 * 找到机器人或托管的最小的牌
	 * @param cards
	 * @param start
	 * @return
	 */
	public byte[] getAIMostSmall(Player player , int start){
		Map<Integer,Integer> types = ActionTaskUtils.type(player.getCards()) ;
		int value = getMinCards(types , false) ;
		/**
		 * 可以增加机器人 出 4带二 / 连串 / 飞机 / 顺子 等牌型的功能 
		 */
		if(value < 0 ){	//炸弹或对王也行
			value = getMinCards(types , true) ;
		}
		int num = types.get(value) ;
		byte[] takeCards = getSubCards(player.getCards(), value, num);
		
		if(takeCards!=null && takeCards.length == 3){
			byte[] supplement = this.getSingle(player.getCards(), types, -1 , 1) ;
			if(supplement == null){
				this.getPair(player.getCards(), types, -1 , 2) ;
			}
			if(supplement!=null){
				takeCards = ArrayUtils.addAll(takeCards, supplement) ;
			}
		}
		return takeCards;
	}
	/**
	 * 
	 * @param types
	 * @return
	 */
	private int getMinCards(Map<Integer,Integer> types , boolean includebang){
		int value = -1 ;
		for(int i=0 ; i<14 ; i++){
			if(types.get(i) != null){
				if(includebang){
					value = i ;
				}else if(types.get(i) != 4 && i != 13){	//对王和炸弹 不优先出牌
					value = i ;
				}
				break ;
			}
		}
		return value ;
	}
	
	/**
	 * 找到匹配的牌
	 * @param cards
	 * @param value
	 * @param num
	 * @return
	 */
	private byte[] getSubCards(byte[] cards,int value,int num){
		byte[] takeCards = new byte[num];
		int index = 0 ;
		for(int i=0 ; i<cards.length ; i++){
			if(cards[i]/4 == value){
				takeCards[index++] = cards[i] ;
			}
		}
		return takeCards ;
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
		List<Byte> tempArray = new ArrayList<Byte>();
		for(int i=0; i<cards.length ; i++){
			boolean found = false ;
			for(int inx = 0 ;inx<playcards.length ; inx++){
				if(cards[i] == playcards[inx]){
					found = true ; break ;
				}
			}
			if(found == false){
				tempArray.add(cards[i]);
			}
		}
		byte[] retCards = new byte[tempArray.size()] ;
		for(int i=0 ; i<tempArray.size() ; i++){
			retCards[i] = tempArray.get(i) ;
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
