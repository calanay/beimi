package com.beimi.core.engine.game.pva;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.Message;
import com.beimi.web.model.PlayUser;

public class PVAOperatorResult implements Message{
	private String status ;	//操作状态 ， 成功|失败|等待|未知|无权限|非法操作|用户不存在
	private int balance ;	//余额
	private int amount ;	//改变的金额
	private String action ;	//操作类型
	private String message ;//操作提示消息
	private String command ;//指令
	
	public PVAOperatorResult(String status, String action , PlayUser playUser){
		this(status, action, playUser, 0 , null) ;
	}
	
	public PVAOperatorResult(String status, String action , int amount){
		this(status, action, null , amount , null) ;
	}
	
	public PVAOperatorResult(String status, String action , PlayUser playUser , int amount){
		this(status, action, playUser, amount , null) ;
	}
	/**
	 * 
	 * @param status
	 * @param action
	 * @param balance
	 * @param message
	 */
	public PVAOperatorResult(String status, String action , PlayUser playUser ,int amount, String message){
		this.action = action ;
		if(playUser!=null){
			this.status = status ;
			this.balance = playUser.getGoldcoins() ;
		}else{
			this.status = BMDataContext.PVAStatusEnum.NOTEXIST.toString();
			this.balance = 0 ;
		}
		this.message = message ;
	}
	
	public void setPlayer(PlayUser playUser){
		if(playUser!=null){
			this.balance = playUser.getGoldcoins() ;
		}else{
			this.status = BMDataContext.PVAStatusEnum.NOTEXIST.toString();
			this.balance = 0 ;
		}
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
