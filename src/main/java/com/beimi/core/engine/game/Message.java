package com.beimi.core.engine.game;

public interface Message {
	/**
	 * 发送到客户端的指令
	 * @return
	 */
	public String getCommand() ; 
	
	/**
	 * 指令
	 * @param command
	 */
	public void setCommand(String command) ;
}
