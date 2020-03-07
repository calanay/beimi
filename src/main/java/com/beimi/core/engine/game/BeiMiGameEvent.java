package com.beimi.core.engine.game;

/**
 * 游戏事件
 * <p>
 * 所有棋牌类游戏 的基本状态，
 * 根据游戏类型不同，状态下的事件有所不同
 * 
 * @author chenhao
 *
 */
public enum BeiMiGameEvent {
	/** 创建房间 （仅第一个加入房间的人触发的事件） */
	ENTER,
	/** 成员加入 */
	JOIN,
	/** 自动 , 抢地主 */
	AUTO,
	/** 凑够一桌子 */
	ENOUGH,
	/** 流程处理完毕，开始出牌  */
	RAISEHANDS,
	/** 出牌 */
	PLAYCARDS,
	/** 1、单个玩家打完牌（地主，推到胡）；2、打完桌面的所有牌（血战，血流，德州） */
	ALLCARDS,
	/** 抓牌动作 */
	DEAL,
	
	/** 麻将的特别事件 ， 定缺  */
	SELECT;
}
