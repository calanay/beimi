package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_account_config")
@org.hibernate.annotations.Proxy(lazy = false)
public class AccountConfig implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 565678041210332017L;
	private String id ;
	private String orgi ;
	private Date createtime = new Date() ;
	private String creater ;
	private String username ;
	private String name ;
	
	private boolean initaccount ; 	//启用账号初始化 数值
	private int initcoins ;			//初始 金币数量
	private int initcards ;			//初始房卡数量
	private int initdiamonds;		//初始钻石数量
	
	private int expdays ;			//有效期时长（天）
	
	private boolean enableask = false;
	private boolean askfirst = false;
	private boolean enablescene = false;
	private boolean scenefirst = false;
	private boolean enablekeyword = false;
	private int keywordnum = 5;
	
	private boolean askqs ;	//询问访客是否解决问题
	private int asktimes ;	//最长多久开始询问 默认 120秒（访客空闲时间超过120秒即断开链接）
	private String asktipmsg ;//询问访客的文本 ， 例如：您的问题是否已经解决？
	private String resolved ;	//已解决的提示文本
	private String unresolved ;	//未解决的提示文本
	private boolean redirectagent ;	//跳转到人工坐席
	private String redirecturl ;	//跳转到其他URL
	private boolean selectskill ;	//跳转到人工坐席之前开启选择技能组
	private String selectskillmsg ;	//选择技能组的提示信息
	
	private String noresultmsg ;
	
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnableask() {
		return enableask;
	}
	public void setEnableask(boolean enableask) {
		this.enableask = enableask;
	}
	public boolean isAskfirst() {
		return askfirst;
	}
	public void setAskfirst(boolean askfirst) {
		this.askfirst = askfirst;
	}
	public boolean isEnablescene() {
		return enablescene;
	}
	public void setEnablescene(boolean enablescene) {
		this.enablescene = enablescene;
	}
	public boolean isScenefirst() {
		return scenefirst;
	}
	public void setScenefirst(boolean scenefirst) {
		this.scenefirst = scenefirst;
	}
	public boolean isEnablekeyword() {
		return enablekeyword;
	}
	public void setEnablekeyword(boolean enablekeyword) {
		this.enablekeyword = enablekeyword;
	}
	public int getKeywordnum() {
		return keywordnum;
	}
	public void setKeywordnum(int keywordnum) {
		this.keywordnum = keywordnum;
	}
	public String getNoresultmsg() {
		return noresultmsg;
	}
	public void setNoresultmsg(String noresultmsg) {
		this.noresultmsg = noresultmsg;
	}
	public boolean isAskqs() {
		return askqs;
	}
	public void setAskqs(boolean askqs) {
		this.askqs = askqs;
	}
	public String getAsktipmsg() {
		return asktipmsg;
	}
	public void setAsktipmsg(String asktipmsg) {
		this.asktipmsg = asktipmsg;
	}
	public String getResolved() {
		return resolved;
	}
	public void setResolved(String resolved) {
		this.resolved = resolved;
	}
	public String getUnresolved() {
		return unresolved;
	}
	public void setUnresolved(String unresolved) {
		this.unresolved = unresolved;
	}
	public boolean isRedirectagent() {
		return redirectagent;
	}
	public void setRedirectagent(boolean redirectagent) {
		this.redirectagent = redirectagent;
	}
	public String getRedirecturl() {
		return redirecturl;
	}
	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getAsktimes() {
		return asktimes;
	}
	public void setAsktimes(int asktimes) {
		this.asktimes = asktimes;
	}
	public boolean isSelectskill() {
		return selectskill;
	}
	public void setSelectskill(boolean selectskill) {
		this.selectskill = selectskill;
	}
	public String getSelectskillmsg() {
		return selectskillmsg;
	}
	public void setSelectskillmsg(String selectskillmsg) {
		this.selectskillmsg = selectskillmsg;
	}
	public boolean isInitaccount() {
		return initaccount;
	}
	public void setInitaccount(boolean initaccount) {
		this.initaccount = initaccount;
	}
	public int getInitcoins() {
		return initcoins;
	}
	public void setInitcoins(int initcoins) {
		this.initcoins = initcoins;
	}
	public int getInitcards() {
		return initcards;
	}
	public void setInitcards(int initcards) {
		this.initcards = initcards;
	}
	public int getInitdiamonds() {
		return initdiamonds;
	}
	public void setInitdiamonds(int initdiamonds) {
		this.initdiamonds = initdiamonds;
	}
	public int getExpdays() {
		return expdays;
	}
	public void setExpdays(int expdays) {
		this.expdays = expdays;
	}
}
