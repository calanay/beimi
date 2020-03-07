package com.beimi.util;

import com.beimi.core.engine.game.pva.GoldPVAImpl;
import com.beimi.core.engine.game.pva.Pva;

public class PvaTools {
	
	private static Pva goldPvaImpl = new GoldPVAImpl();
	/**
	 * 
	 * @return
	 */
	public static Pva getGoldCoins(){
		return goldPvaImpl;
	}
}
