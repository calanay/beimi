package com.beimi.core.engine.game;

public class GameBoard implements Message , java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -907633644768054042L;
	
	public GameBoard(String userid, boolean docatch, boolean grab , int ratio){
		this.userid = userid ;
		this.docatch = docatch ;
		this.ratio = ratio ;
		this.grab = grab ;
	}
	
	public GameBoard(String userid, byte[] lasthands, int ratio){
		this.userid = userid ;
		this.lasthands = lasthands ;
		this.ratio = ratio ;
	}
	
	public GameBoard(String userid,int ratio){
		this.userid = userid ;
		this.ratio = ratio ;
	}
	
	public GameBoard(String userid , String banker ,int ratio){
		this.userid = userid ;
		this.ratio = ratio ;
		this.banker = banker ;
	}
	
	private byte[] lasthands ;
	private String userid ;
	private boolean docatch ;
	private boolean grab ;
	private int ratio ;
	
	private String banker ;
	
	private String command ;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public boolean isGrab() {
		return grab;
	}
	public void setGrab(boolean grab) {
		this.grab = grab;
	}

	public byte[] getLasthands() {
		return lasthands;
	}

	public void setLasthands(byte[] lasthands) {
		this.lasthands = lasthands;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getBanker() {
		return banker;
	}

	public void setBanker(String banker) {
		this.banker = banker;
	}
}
