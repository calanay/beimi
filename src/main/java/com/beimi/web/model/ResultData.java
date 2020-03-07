package com.beimi.web.model;

import java.util.List;

import com.beimi.core.engine.game.BeiMiGame;

/**
 * 
 * @author iceworld
 *
 */
public class ResultData implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2710593844038107782L;
	private boolean status ;
	private String msg ;
	private Object data ;
	
	private String gametype ;			// game model : 大厅 | 房卡
	
	private boolean enableai ;			//是否启用AI
	private int waittime ;				//AI等待时长

	private int noaiwaitime ;			//没有AI接入的时候的最大等待时长
	private String noaimsg ;			//没有AI接入的时候达到最大等待时长的 提示消息
	
	private boolean subsidy ;	//启用破产补助
	private int subtimes ;		//破产补助次数
	private int subgolds ;		//破产补助金额
	private String submsg ;		//破产补助提示消息
	private String recmsg ;		//金币不足进行充值的时候的提示
	
	private int lefttimes ;		//当天剩余补助次数
	
	private String welfare ;	//启用的活动列表
	
	private Token token ;
	private List<BeiMiGame> games ;		//游戏配置里选择的游戏类型
	
	public ResultData(boolean status , String msg , Object data){
		this.status = status ;
		this.msg = msg ;
		this.data = data ;
	}
	
	public ResultData(boolean status , String msg , Object data , Token token ){
		this.status = status ;
		this.msg = msg ;
		this.data = data ;
		this.token = token ;
	}
	
	public ResultData(boolean status , String msg , Object data , Token token ,List<BeiMiGame> games){
		this.status = status ;
		this.msg = msg ;
		this.data = data ;
		this.token = token ;
		this.games = games ;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public List<BeiMiGame> getGames() {
		return games;
	}

	public void setGames(List<BeiMiGame> games) {
		this.games = games;
	}

	public boolean isEnableai() {
		return enableai;
	}

	public void setEnableai(boolean enableai) {
		this.enableai = enableai;
	}

	public int getWaittime() {
		return waittime;
	}

	public void setWaittime(int waittime) {
		this.waittime = waittime;
	}

	public int getNoaiwaitime() {
		return noaiwaitime;
	}

	public void setNoaiwaitime(int noaiwaitime) {
		this.noaiwaitime = noaiwaitime;
	}

	public String getNoaimsg() {
		return noaimsg;
	}

	public void setNoaimsg(String noaimsg) {
		this.noaimsg = noaimsg;
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

	public int getLefttimes() {
		return lefttimes;
	}

	public void setLefttimes(int lefttimes) {
		this.lefttimes = lefttimes;
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

	public String getWelfare() {
		return welfare;
	}

	public void setWelfare(String welfare) {
		this.welfare = welfare;
	}
	
}
