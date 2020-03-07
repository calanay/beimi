package com.beimi.util.rules.model;

import java.util.ArrayList;
import java.util.List;

import com.beimi.core.engine.game.Message;
import com.beimi.core.engine.game.model.Summary;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

/**
 * 牌局抽象类
 * 
 * @author
 *
 */
public abstract class Board implements Message,java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id ;
	
	private byte[] cards;	//4个Bit描述一张牌，麻将：136+2/2 = 69 byte ; 扑克 54/2 = 27 byte 
	private Player[] players;//3~10人(4 byte)
	
	private List<Byte>  deskcards ;
	
	private TakeCards last;
	
	private boolean finished ;
	
	private List<Byte> history = new ArrayList<Byte>();
	
	private String winner ;		//赢的玩家
	
	private NextPlayer nextplayer ;
	
	private String command ;
	
	private String room ;		//房间ID（4 byte）
	/** 底牌 */
	private byte[] lasthands;
	
	private byte[] powerful ;	//癞子 ， 麻将里的 单张癞子 ， 地主里的 天地癞子
	
	private int position ;		//地主牌
	
	private boolean docatch ;	//叫地主 OR 抢地主
	/** 倍数， 默认：1*/
	private int ratio = 1;
	private boolean added ;			//已翻倍
	
	// 庄家|地主
	private String banker ;
	private String currplayer ;	//当前出牌人
	private byte currcard ;		//当前出牌
	
	/**
	 * @return
	 */
	public abstract byte[] pollLastHands() ;
	
	/**
	 * 计算倍率， 规则每种游戏不同，斗地主 翻到底牌 大小王 翻倍
	 * @return
	 */
	public abstract int calcRatio() ;
	
	
	public abstract TakeCards takeCards(Player player ,String playerType , TakeCards current);
	
	/**
	 * 找到玩家的 位置
	 * @param board
	 * @param userid
	 * @return
	 */
	public abstract int index(String userid);
	
	
	/**
	 * 计算结算信息
	 * @return
	 */
	public abstract Summary summary(Board board, GameRoom gameRoom , GamePlayway playway);
	
	/**
	 * 是否赢了
	 * @return
	 */
	public abstract boolean isWin() ;
	
	
	/**
	 * 找到下一个玩家
	 * @param board
	 * @param index
	 * @return
	 */
	protected abstract Player next(int index);
	

	public abstract Player nextPlayer(int index);
	
	/**
	 * 玩家随机出牌，能管住当前出牌的 最小牌
	 * @param player
	 * @param current
	 * @return
	 */
	public abstract TakeCards takecard( Player player , boolean allow , byte[] playCards) ;
	
	/**
	 * 当前玩家随机出牌
	 * @param player
	 * @param current
	 * @return
	 */
	public abstract TakeCards takecard(Player player) ;
	
	/**
	 * 出牌请求
	 * @param roomid
	 * @param playUserClient
	 * @param orgi
	 * @param auto
	 * @param playCards
	 * @return
	 */
	public abstract TakeCards takeCardsRequest(GameRoom gameRoom,Board board, Player player, String orgi , boolean auto , byte[] playCards) ;
	
	/**
	 * 发牌动作
	 * @param gameRoom
	 * @param board
	 * @param orgi
	 */
	public abstract void dealRequest(GameRoom gameRoom , Board board , String orgi , boolean reverse, String nextplayer) ;
	
	/**
	 * 下一个出牌玩家
	 * @param board
	 * @param gameRoom
	 * @param player
	 * @param orgi
	 */
	public abstract void playcards(Board board , GameRoom gameRoom ,  Player player , String orgi) ;
	/**
	 * 玩家出牌
	 * @param player
	 * @param cards
	 * @return
	 */
	public abstract TakeCards takecard(Player player , TakeCards last) ;
	
	/**
	 * 找到玩家数据
	 * @param userid
	 * @return
	 */
	public Player player(String userid){
		Player temp = null;
		if(this.players!=null){
			for(Player user : players){
				if(user.getPlayuser()!=null && user.getPlayuser().equals(userid)){
					temp = user ; break ;
				}
			}
		}
		return temp ;
	}
	
	/**
	 * 找到玩家数据
	 * @param userid
	 * @return
	 */
	public PlayUserClient getPlayerClient(List<PlayUserClient> players,String userid){
		PlayUserClient temp = null;
		for(PlayUserClient user : players){
			if(user.getId().equals(userid)){
				temp = user ; break ;
			}
		}
		return temp ;
	}
	
	public byte[] getCards() {
		return cards;
	}
	public void setCards(byte[] cards) {
		this.cards = cards;
	}
	public Player[] getPlayers() {
		return players;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getBanker() {
		return banker;
	}
	public void setBanker(String banker) {
		this.banker = banker;
	}
	public String getCurrplayer() {
		return currplayer;
	}
	public void setCurrplayer(String currplayer) {
		this.currplayer = currplayer;
	}
	public byte getCurrcard() {
		return currcard;
	}
	public void setCurrcard(byte currcard) {
		this.currcard = currcard;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isDocatch() {
		return docatch;
	}
	public void setDocatch(boolean docatch) {
		this.docatch = docatch;
	}
	public int getRatio() {
		return ratio;
	}
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public byte[] getLasthands() {
		return lasthands;
	}

	public void setLasthands(byte[] lasthands) {
		this.lasthands = lasthands;
	}

	public TakeCards getLast() {
		return last;
	}

	public void setLast(TakeCards last) {
		this.last = last;
	}

	public NextPlayer getNextplayer() {
		return nextplayer;
	}

	public void setNextplayer(NextPlayer nextplayer) {
		this.nextplayer = nextplayer;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public byte[] getPowerful() {
		return powerful;
	}

	public void setPowerful(byte[] powerful) {
		this.powerful = powerful;
	}

	public List<Byte> getDeskcards() {
		return deskcards;
	}

	public void setDeskcards(List<Byte> deskcards) {
		this.deskcards = deskcards;
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Byte> getHistory() {
		return history;
	}

	public void setHistory(List<Byte> history) {
		this.history = history;
	}
}
