package com.beimi.util.rules.model;

public class NextPlayer implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -154983225778796701L;
	
	private String nextplayer ;
	private boolean takecard ;
	
	public NextPlayer(String nextplayer , boolean takecard){
		this.nextplayer = nextplayer ;
		this.takecard = takecard ;
	}
	
	public String getNextplayer() {
		return nextplayer;
	}
	public void setNextplayer(String nextplayer) {
		this.nextplayer = nextplayer;
	}
	public boolean isTakecard() {
		return takecard;
	}
	public void setTakecard(boolean takecard) {
		this.takecard = takecard;
	}
}
