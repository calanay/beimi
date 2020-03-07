package com.beimi.util.rules.model;

import java.util.ArrayList;
import java.util.List;

import com.beimi.core.engine.game.GameBoard;
import com.beimi.core.engine.game.Message;
import com.beimi.core.engine.game.impl.Banker;
import com.beimi.core.engine.game.impl.UserBoard;

/**
 * 恢复数据
 * 
 * @author
 *
 */
public class RecoveryData implements Message{
	private String command ;
	private String userid ;
	private Player player ;
	private byte[] lasthands;
	private TakeCards last ;
	private Banker banker ;
	private String nextplayer ;//正在出牌的玩家
	private CardsInfo[] cardsnum ;
	private int time ;		//计时器剩余时间
	private boolean automic ;	//本轮第一个出牌，不允许出现不出按钮
	private GameBoard data ;
	private int ratio ;
	private byte[] hiscards ;
	
	private SelectColor selectcolor ;
	
	private UserBoard userboard ;
	
	
	public RecoveryData(Player player , byte[] lasthands , String nextplayer , int time , boolean automic , Board board){
		this.player = player ;
		this.userid = player.getPlayuser() ;
		this.lasthands = lasthands ;
		this.nextplayer = nextplayer ;
		this.banker = new Banker(board.getBanker()) ;
		this.time = time ;
		this.automic = automic;
		this.data = new GameBoard(board.getBanker(), board.getRatio()) ;
		
		this.hiscards = player.getHistory() ;
		
		this.ratio = board.getRatio() ;
		
		this.userboard = new UserBoard(board, player.getPlayuser(), "play") ;
		
		this.selectcolor =  new SelectColor(board.getBanker(), player.getPlayuser()) ;
		this.selectcolor.setColor(player.getColor());
		
		this.last = board.getLast() ;
		this.cardsnum = new CardsInfo[board.getPlayers().length - 1];
		List<CardsInfo> tempList = new ArrayList<CardsInfo>();
		for(Player temp : board.getPlayers()){
			if(!temp.getPlayuser().equals(player.getPlayuser())){
				tempList.add(new CardsInfo(temp.getPlayuser() , temp.getCards().length , temp.getHistory() , temp.getActions() , board , temp)) ;
			}
			
		}
		cardsnum = tempList.toArray(this.cardsnum) ;
	}
	
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public byte[] getLasthands() {
		return lasthands;
	}
	public void setLasthands(byte[] lasthands) {
		this.lasthands = lasthands;
	}
	public String getNextplayer() {
		return nextplayer;
	}

	public void setNextplayer(String nextplayer) {
		this.nextplayer = nextplayer;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public boolean isAutomic() {
		return automic;
	}
	public void setAutomic(boolean automic) {
		this.automic = automic;
	}


	public CardsInfo[] getCardsnum() {
		return cardsnum;
	}


	public void setCardsnum(CardsInfo[] cardsnum) {
		this.cardsnum = cardsnum;
	}
	public Banker getBanker() {
		return banker;
	}


	public void setBanker(Banker banker) {
		this.banker = banker;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public TakeCards getLast() {
		return last;
	}


	public void setLast(TakeCards last) {
		this.last = last;
	}


	public GameBoard getData() {
		return data;
	}


	public void setData(GameBoard data) {
		this.data = data;
	}


	public int getRatio() {
		return ratio;
	}


	public void setRatio(int ratio) {
		this.ratio = ratio;
	}


	public UserBoard getUserboard() {
		return userboard;
	}


	public void setUserboard(UserBoard userboard) {
		this.userboard = userboard;
	}


	public SelectColor getSelectcolor() {
		return selectcolor;
	}


	public void setSelectcolor(SelectColor selectcolor) {
		this.selectcolor = selectcolor;
	}


	public byte[] getHiscards() {
		return hiscards;
	}


	public void setHiscards(byte[] hiscards) {
		this.hiscards = hiscards;
	}
}