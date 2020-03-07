package com.beimi.core.engine.game;

import org.cache2k.expiry.ValueWithExpiryTime;

public class StateMachineTask implements ValueWithExpiryTime{

	@Override
	public long getCacheExpiryTime() {
		return System.currentTimeMillis()+2000;
	}
}
