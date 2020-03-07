package com.beimi.core.engine.game.task.majiang;

import java.util.List;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.BeiMiGameEvent;
import com.beimi.core.engine.game.BeiMiGameTask;
import com.beimi.core.engine.game.task.AbstractTask;
import com.beimi.util.GameUtils;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.rules.model.Board;
import com.beimi.util.rules.model.Player;
import com.beimi.util.rules.model.SelectColor;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

/**
 * 定缺
 * @author iceworld
 *
 */
public class CreateSelectTask extends AbstractTask implements BeiMiGameTask{

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateSelectTask(long timer , GameRoom gameRoom, String orgi){
		super();
		this.timer = timer ;
		this.gameRoom = gameRoom ;
		this.orgi = orgi ;
	}
	@Override
	public long getCacheExpiryTime() {
		return System.currentTimeMillis()+timer*1000;	//5秒后执行
	}
	
	public void execute(){
		Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
		/**
		 * 通知状态机 ， 大家开始定缺， 流程引导 ， 可以通过配置选择是否需要此节点流程
		 */
		ActionTaskUtils.sendEvent("selectcolor" , new SelectColor(board.getBanker()) , gameRoom);
		
		List<PlayUserClient> playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
		for(PlayUserClient player : playerList){
			if(!BMDataContext.PlayerTypeEnum.NORMAL.toString().equals(player.getPlayertype())){//AI定缺 ， 并通知玩家
				/**
				 * 定缺算法 , 万筒条 
				 */
				for(Player ply : board.getPlayers()){
					if(ply.getPlayuser().equals(player.getId())){
						SelectColor color = new SelectColor( board.getBanker(), player.getId()) ;	
						color.setColor(GameUtils.selectColor(ply.getCards()));
						ActionTaskUtils.sendEvent("selectresult" , color , gameRoom);
						
						ply.setColor(color.getColor()); 
						ply.setSelected(true);
						
						break ;
					}
				}
			}
		}
		CacheHelper.getBoardCacheBean().put(gameRoom.getId() , board, gameRoom.getOrgi());	//更新缓存数据
		
		super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.RAISEHANDS.toString());	
	}
}
