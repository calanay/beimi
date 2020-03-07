package com.beimi.core.engine.game;

/**
 * 游戏房间状态
 * <p>
 * 游戏的基本状态，开局->等待玩家（AI）->凑齐一桌子->打牌->结束
 * 
 * @author
 *
 */
public enum BeiMiGameEnum {
	/** 无状态  */
	NONE,
	/** 进入房间  */
	CRERATED,	
	/** 开局  */
	BEGIN ,	
	/** 等待玩家  */
	WAITTING,	
	/** 凑齐一桌子  */
	READY,		
	/** 翻底牌  */
	LASTHANDS,
	/** 打牌  */
	PLAY,
	/** 结束  */
	END;
}