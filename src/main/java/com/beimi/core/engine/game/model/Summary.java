package com.beimi.core.engine.game.model;

import java.util.ArrayList;
import java.util.List;

import com.beimi.core.engine.game.Message;

public class Summary implements Message,java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String game ;		//房间ID
	private String board;		//场次 ID
	private int ratio ;			//倍率
	private String command ;	
	private boolean finished = true ;
	private boolean gameRoomOver ;	
	
	private int score ;			//总分
	private List<SummaryPlayer> players = new ArrayList<SummaryPlayer>() ;
	
	public Summary(){}
	public Summary(String game , String board , int ratio , int score){
		this.game = game ;
		this.board = board ;
		this.ratio = ratio ; 
		this.score = score ;
	}
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getBoard() {
		return board;
	}
	public void setBoard(String board) {
		this.board = board;
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
	public List<SummaryPlayer> getPlayers() {
		return players;
	}
	public void setPlayers(List<SummaryPlayer> players) {
		this.players = players;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public boolean isGameRoomOver() {
		return gameRoomOver;
	}
	public void setGameRoomOver(boolean gameRoomOver) {
		this.gameRoomOver = gameRoomOver;
	}
}
