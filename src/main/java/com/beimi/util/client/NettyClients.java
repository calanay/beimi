package com.beimi.util.client;

import com.beimi.util.server.handler.BeiMiClient;

/**
 * Netty Client管理
 * 
 * @author 
 *
 */
public class NettyClients {
	
	
	private static NettySystemClient systemClients = new NettySystemClient();
	
	public static NettySystemClient getInstance(){
		return systemClients ;
	}
	
	public void putGameEventClient(String id , BeiMiClient gameClient){
		systemClients.putClient(id, gameClient);
	}
	public void removeGameEventClient(String id){
		systemClients.removeClient(id);
	}
	public void sendGameEventMessage(String id , String event , Object data){
		systemClients.getClient(id).getClient().sendEvent(event, data);
	}
	
	public void joinRoom(String id , String roomid){
		systemClients.getClient(id).getClient().joinRoom(roomid);
	}
}
