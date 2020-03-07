package com.beimi.util.rules.model;

import com.beimi.core.engine.game.Message;
import com.beimi.web.model.GameRoom;

public class RoomReady implements Message{
	private String command ;
	private boolean cardroom ;
	private String roomid ;

	public RoomReady(GameRoom gameRoom){
		this.cardroom = gameRoom.isCardroom() ;
		this.roomid = gameRoom.getRoomid() ;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isCardroom() {
		return cardroom;
	}

	public void setCardroom(boolean cardroom) {
		this.cardroom = cardroom;
	}

	public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
}
