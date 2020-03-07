package com.beimi.core.engine.game.impl;

import java.io.Serializable;

import com.beimi.core.engine.game.Message;
import com.beimi.util.rules.model.Board;
import com.beimi.util.rules.model.Player;

/**
 * 用户牌局
 * 
 * @author
 *
 */
public class UserBoard implements Message,Serializable{
	private static final long serialVersionUID = -1224310911110772375L;
	
	private Player player ;
	private Player[] players ;
	private int deskcards ;	//剩下多少张牌
	private String command ;
	
	/**
	 * 发给玩家的牌，开启特权后可以将某个其他玩家的牌 显示出来
	 * @param board
	 * @param curruser
	 */
	public UserBoard(Board board , String curruser , String command){
		players = new Player[board.getPlayers().length-1] ;
		this.command = command ;
		if(board.getDeskcards()!=null){
			this.deskcards = board.getDeskcards().size() ;
		}
		int inx = 0 ;
		for(Player temp : board.getPlayers()){
			if(temp.getPlayuser().equals(curruser)){
				player = temp ;
			}else{
				Player clonePlayer = temp.clone() ;
				clonePlayer.setDeskcards(clonePlayer.getCards().length);
				clonePlayer.setCards(null);	//克隆对象，然后将 其他玩家手里的牌清空
				players[inx++] = clonePlayer;
			}
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getDeskcards() {
		return deskcards;
	}

	public void setDeskcards(int deskcards) {
		this.deskcards = deskcards;
	}
}