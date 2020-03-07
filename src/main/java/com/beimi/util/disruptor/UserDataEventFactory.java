package com.beimi.util.disruptor;

import com.beimi.util.event.UserDataEvent;
import com.lmax.disruptor.EventFactory;

public class UserDataEventFactory implements EventFactory<UserDataEvent>{

	@Override
	public UserDataEvent newInstance() {
		return new UserDataEvent();
	}
}
