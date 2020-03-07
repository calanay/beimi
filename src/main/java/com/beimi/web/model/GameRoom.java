package com.beimi.web.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.beimi.util.UKTools;
import com.beimi.util.event.UserEvent;

/**
 * 游戏房间对象
 * 
 * @author
 *
 */
@Entity
@Table(name = "bm_game_room")
@org.hibernate.annotations.Proxy(lazy = false)
public class GameRoom implements UserEvent, java.io.Serializable, Comparable<GameRoom>{
	private static final long serialVersionUID = -8988042477190235024L;
	
	private String id = UKTools.getUUID();
	private String name ;
	private String code ;
	/**
	 * 房间ID，房卡游戏的 房间ID是 6位数字，其他为 UUID
	 */
	private String roomid;
	/**
	 * 是否比赛房间
	 */
	private boolean matchmodel;
	/**
	 * 赛事ID
	 */
	private String matchid;
	/**
	 * 比赛场次
	 */
	private int matchscreen;
	/**
	 * 比赛类型
	 */
	private String matchtype;
	/**
	 * 最后赢的人 ， 可多人 ， 逗号隔开
	 */
	private String lastwinner;	
	
	
	private Date createtime ;
	private String parentid ;
	private String typeid ;
	private String creater;
	private String username ;
	/**
	 * 当前状态
	 */
	private String status;
	
	private Date updatetime;
	private String orgi;
	private String area;
	/**
	 * 游戏类型 ： 麻将：地主：德州
	 */
	private String game;
	/**
	 * 最大游戏人数
	 */
	private int players;
	/**
	 * 发牌数量
	 */
	private int cardsnum;
	/**
	 * 游戏房间当前人数
	 */
	private int curpalyers;
	/**
	 * 房主 ，开设房间的人 或第一个进入的人
	 */
	private String master;
	/**
	 * 房间类型：[房卡、大厅]
	 */
	private String roomtype;
	/**
	 * 是否房卡模式
	 */
	private boolean cardroom; 
	/**
	 * 玩法
	 */
	private String playway;
	/**
	 * 局数
	 */
	private int numofgames;
	/**
	 * 已完局数
	 */
	private int currentnum;
	/**
	 * 房间的创建人
	 */
	private PlayUser masterUser;
	/**
	 * 房间玩法
	 */
	private GamePlayway gamePlayway;
	/**
	 * 房卡模式下的自定义参数
	 */
	private Map<String,String> extparams;
	
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "assigned")	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public int getPlayers() {
		return players;
	}
	public void setPlayers(int players) {
		this.players = players;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	public String getPlayway() {
		return playway;
	}
	public void setPlayway(String playway) {
		this.playway = playway;
	}
	public int getNumofgames() {
		return numofgames;
	}
	public void setNumofgames(int numofgames) {
		this.numofgames = numofgames;
	}
	public int getCurrentnum() {
		return currentnum;
	}
	public void setCurrentnum(int currentnum) {
		this.currentnum = currentnum;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCurpalyers() {
		return curpalyers;
	}
	public void setCurpalyers(int curpalyers) {
		this.curpalyers = curpalyers;
	}
	public boolean isCardroom() {
		return cardroom;
	}
	public void setCardroom(boolean cardroom) {
		this.cardroom = cardroom;
	}
	@Transient
	public PlayUser getMasterUser() {
		return masterUser;
	}
	public void setMasterUser(PlayUser masterUser) {
		this.masterUser = masterUser;
	}
	@Transient
	public GamePlayway getGamePlayway() {
		return gamePlayway;
	}
	public void setGamePlayway(GamePlayway gamePlayway) {
		this.gamePlayway = gamePlayway;
	}
	public boolean isMatchmodel() {
		return matchmodel;
	}
	public void setMatchmodel(boolean matchmodel) {
		this.matchmodel = matchmodel;
	}
	public String getMatchid() {
		return matchid;
	}
	public void setMatchid(String matchid) {
		this.matchid = matchid;
	}
	public int getMatchscreen() {
		return matchscreen;
	}
	public void setMatchscreen(int matchscreen) {
		this.matchscreen = matchscreen;
	}
	public String getMatchtype() {
		return matchtype;
	}
	public void setMatchtype(String matchtype) {
		this.matchtype = matchtype;
	}
	public int getCardsnum() {
		return cardsnum;
	}
	public void setCardsnum(int cardsnum) {
		this.cardsnum = cardsnum;
	}
	public String getLastwinner() {
		return lastwinner;
	}
	public void setLastwinner(String lastwinner) {
		this.lastwinner = lastwinner;
	}

	@Transient
	public Map<String, String> getExtparams() {
		return extparams;
	}

	public void setExtparams(Map<String, String> extparams) {
		this.extparams = extparams;
	}

	@Override
	public int compareTo(GameRoom o) {
		return this.currentnum - o.getCurpalyers();
	}
}