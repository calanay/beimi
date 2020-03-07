package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import com.beimi.core.engine.game.Message;
import com.beimi.util.UKTools;
import com.beimi.util.event.UserEvent;

@Document(indexName = "beimi", type = "bm_game_actrecord")
@Entity
@Table(name = "bm_game_actrecord")
@org.hibernate.annotations.Proxy(lazy = false)
public class ActRecord implements UserEvent ,Message , java.io.Serializable{
	private static final long serialVersionUID = 1L;
    
	private String id = UKTools.getUUID();                   //主键
	
	private String command ;
	
	private String playerid;
	private String orgi ;
	private Date createtime ;
	private String gametype ;
	private String device;
	private int score;
	private String day ;
	private String hour ;
	private String timeslot ;		//补贴时间段 ， 以后扩展备用
	private String rectype ;	//补贴类型  ， 以后扩展备用
	private int frequency ;			//当天第几次补贴 
	
	private boolean enable ;		//是否补贴 
	private int subtimes ;			//补贴次数
	private int subgolds ;			//补贴金币
	
	private int balance ;	//余额
	private int amount ;	//改变的金额
	private String action ;	//操作类型
	
	private String result ;
	private String token ;
	
	private String ruleid ;	//补贴规则ID ， 以后扩展备用
	
	
	public ActRecord() {}
	public ActRecord(String command , String result , String token) {
		this.command = command ;
		this.result = result ;
		this.token = token ;
	}
	
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
	public String getPlayerid() {
		return playerid;
	}
	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}
	
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public int getSubtimes() {
		return subtimes;
	}
	public void setSubtimes(int subtimes) {
		this.subtimes = subtimes;
	}
	public int getSubgolds() {
		return subgolds;
	}
	public void setSubgolds(int subgolds) {
		this.subgolds = subgolds;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getRectype() {
		return rectype;
	}
	public void setRectype(String rectype) {
		this.rectype = rectype;
	}
	public String getRuleid() {
		return ruleid;
	}
	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}
}
