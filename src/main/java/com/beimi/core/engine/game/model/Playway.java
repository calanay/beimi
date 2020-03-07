package com.beimi.core.engine.game.model;

import com.beimi.web.model.GamePlaywayGroup;
import com.beimi.web.model.GamePlaywayGroupItem;

import java.util.List;

public class Playway implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Playway(String id, String name , String code , int score , int mincoins , int maxcoins , boolean changecard, boolean shuffle){
		this.id = id ; 
		this.name = name ;
		this.score = score ;
		this.code = code ;
		this.mincoins = mincoins ;
		this.maxcoins = maxcoins ;
		this.changecard = changecard ;
		this.shuffle = shuffle ;
	}
	
	private String id;
	private String name ;
	private String code ;
	
	private int score;		//底分
	private int mincoins ;	//最小金币数量
	private int maxcoins ;	//最大金币数量
	
	private boolean changecard ;	//换牌
	
	private int onlineusers ;	//在线用户数
	
	private boolean shuffle ;	//是否洗牌
	private String level ;		//级别
	private String skin ;		//图标颜色
	private String memo ;		//备注

	private boolean free ; 		//开启房卡限免

	private List<GamePlaywayGroup> groups ;

	private List<GamePlaywayGroupItem> items ;

	private String roomtitle ; 	//玩法标题
	private boolean extpro ;	//启用扩展属性配置（房卡游戏中的自定义规则）
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getMincoins() {
		return mincoins;
	}
	public void setMincoins(int mincoins) {
		this.mincoins = mincoins;
	}
	public int getMaxcoins() {
		return maxcoins;
	}
	public void setMaxcoins(int maxcoins) {
		this.maxcoins = maxcoins;
	}
	public boolean isChangecard() {
		return changecard;
	}
	public void setChangecard(boolean changecard) {
		this.changecard = changecard;
	}
	public int getOnlineusers() {
		return onlineusers;
	}
	public void setOnlineusers(int onlineusers) {
		this.onlineusers = onlineusers;
	}
	public boolean isShuffle() {
		return shuffle;
	}
	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public String getRoomtitle() {
		return roomtitle;
	}

	public void setRoomtitle(String roomtitle) {
		this.roomtitle = roomtitle;
	}

	public boolean isExtpro() {
		return extpro;
	}

	public void setExtpro(boolean extpro) {
		this.extpro = extpro;
	}

	public List<GamePlaywayGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<GamePlaywayGroup> groups) {
		this.groups = groups;
	}

	public List<GamePlaywayGroupItem> getItems() {
		return items;
	}

	public void setItems(List<GamePlaywayGroupItem> items) {
		this.items = items;
	}
}
