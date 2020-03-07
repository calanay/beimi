package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_gameconfig")
@org.hibernate.annotations.Proxy(lazy = false)
public class GameConfig implements java.io.Serializable{
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
	
	private String gamemodel = "hall";	//游戏模式  ： 大厅 （默认）： 房卡
	
	private String gametype ;	//玩家默认进入的游戏类型 ， 大厅模式下启用的游戏类型
	
	private String hallgametype ;//房卡模式下启用的游戏类型
	
	private int maxuser = 10 ;	
	
	private int initmaxuser = 10 ;
	
	private String sessionmsg ;	
	private String distribution ;
	private boolean lastagent;	
	private boolean sessiontimeout;			//匹配玩家等到超时后 是否 解散房间
	private int timeout = 30;				//匹配玩家的超时时长
	private String timeoutmsg ;				//配牌玩家超时后的 提示消息
	private boolean resessiontimeout;
	private int retimeout = 120;	
	private String retimeoutmsg ;	
	private boolean satisfaction ;	
	
	private boolean hourcheck ;		
	private String workinghours ;	
	private String notinwhmsg ;

	private boolean anysdk ;	//启用 AnySDK配置
	private String oauthserver; //AnySDK 服务器地址
	private String appkey ; 	//APPKEY
	private String appsecret ;	//
	private String privatekey ;	//

	private boolean anysdkpay ;	//启用 AnySDK支付
	private boolean anysdkshare ;	//启用 AnySDK分享
	private boolean anysdklogin ;	//启用 AnySDK登录验证
	
	private boolean subsidy ;	//启用破产补助
	private int subtimes ;		//破产补助次数
	private int subgolds ;		//破产补助金额
	private String submsg ;		//破产补助的提示消息
	private String recmsg ;		//金币不足的提示，和破产补助提示 同时判断，如果还有破产补助，就不会提示这个
	private String subovermsg;	//补助次数用完的提示消息	
	
	private String nosubmsg ;	//未启用破产补助的提示消息
	
	private String welfare ;	//启用的活动列表
	
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
	public String getSessionmsg() {
		return sessionmsg;
	}
	public void setSessionmsg(String sessionmsg) {
		this.sessionmsg = sessionmsg;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public boolean isLastagent() {
		return lastagent;
	}
	public void setLastagent(boolean lastagent) {
		this.lastagent = lastagent;
	}
	public boolean isSessiontimeout() {
		return sessiontimeout;
	}
	public void setSessiontimeout(boolean sessiontimeout) {
		this.sessiontimeout = sessiontimeout;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getTimeoutmsg() {
		return timeoutmsg;
	}
	public void setTimeoutmsg(String timeoutmsg) {
		this.timeoutmsg = timeoutmsg;
	}
	public boolean isResessiontimeout() {
		return resessiontimeout;
	}
	public void setResessiontimeout(boolean resessiontimeout) {
		this.resessiontimeout = resessiontimeout;
	}
	public int getRetimeout() {
		return retimeout;
	}
	public void setRetimeout(int retimeout) {
		this.retimeout = retimeout;
	}
	public String getRetimeoutmsg() {
		return retimeoutmsg;
	}
	public void setRetimeoutmsg(String retimeoutmsg) {
		this.retimeoutmsg = retimeoutmsg;
	}
	public boolean isSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(boolean satisfaction) {
		this.satisfaction = satisfaction;
	}
	public int getMaxuser() {
		return maxuser;
	}
	public void setMaxuser(int maxuser) {
		this.maxuser = maxuser;
	}
	public int getInitmaxuser() {
		return initmaxuser;
	}
	public void setInitmaxuser(int initmaxuser) {
		this.initmaxuser = initmaxuser;
	}
	public String getWorkinghours() {
		return workinghours;
	}
	public void setWorkinghours(String workinghours) {
		this.workinghours = workinghours;
	}
	public String getNotinwhmsg() {
		return notinwhmsg;
	}
	public void setNotinwhmsg(String notinwhmsg) {
		this.notinwhmsg = notinwhmsg;
	}
	public boolean isHourcheck() {
		return hourcheck;
	}
	public void setHourcheck(boolean hourcheck) {
		this.hourcheck = hourcheck;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public String getGamemodel() {
		return gamemodel;
	}
	public void setGamemodel(String gamemodel) {
		this.gamemodel = gamemodel;
	}
	public String getHallgametype() {
		return hallgametype;
	}
	public void setHallgametype(String hallgametype) {
		this.hallgametype = hallgametype;
	}

	public boolean isAnysdk() {
		return anysdk;
	}

	public void setAnysdk(boolean anysdk) {
		this.anysdk = anysdk;
	}

	public String getOauthserver() {
		return oauthserver;
	}

	public void setOauthserver(String oauthserver) {
		this.oauthserver = oauthserver;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	public boolean isAnysdkpay() {
		return anysdkpay;
	}

	public void setAnysdkpay(boolean anysdkpay) {
		this.anysdkpay = anysdkpay;
	}

	public boolean isAnysdkshare() {
		return anysdkshare;
	}

	public void setAnysdkshare(boolean anysdkshare) {
		this.anysdkshare = anysdkshare;
	}

	public boolean isAnysdklogin() {
		return anysdklogin;
	}

	public void setAnysdklogin(boolean anysdklogin) {
		this.anysdklogin = anysdklogin;
	}
	public boolean isSubsidy() {
		return subsidy;
	}
	public void setSubsidy(boolean subsidy) {
		this.subsidy = subsidy;
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
	public String getSubmsg() {
		return submsg;
	}
	public void setSubmsg(String submsg) {
		this.submsg = submsg;
	}
	public String getRecmsg() {
		return recmsg;
	}
	public void setRecmsg(String recmsg) {
		this.recmsg = recmsg;
	}
	public String getSubovermsg() {
		return subovermsg;
	}
	public void setSubovermsg(String subovermsg) {
		this.subovermsg = subovermsg;
	}
	public String getNosubmsg() {
		return nosubmsg;
	}
	public void setNosubmsg(String nosubmsg) {
		this.nosubmsg = nosubmsg;
	}
	public String getWelfare() {
		return welfare;
	}
	public void setWelfare(String welfare) {
		this.welfare = welfare;
	}
}
