package com.beimi.core.engine.game.iface;

import java.util.List;

import com.beimi.util.rules.model.Board;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

/**
 * 棋牌游戏接口API
 * @author iceworld
 *
 */
public interface ChessGame {
	
	/**
	 * 创建一局新游戏[发牌]
	 * 
	 * @return
	 */
	public Board process(List<PlayUserClient> playUsers , GameRoom gameRoom ,GamePlayway playway ,  String banker , int cardsnum) ;
}
