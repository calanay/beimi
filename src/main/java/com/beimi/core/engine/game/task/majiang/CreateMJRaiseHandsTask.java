package com.beimi.core.engine.game.task.majiang;

import org.cache2k.expiry.ValueWithExpiryTime;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.BeiMiGameEvent;
import com.beimi.core.engine.game.BeiMiGameTask;
import com.beimi.core.engine.game.GameBoard;
import com.beimi.core.engine.game.task.AbstractTask;
import com.beimi.util.GameUtils;
import com.beimi.util.cache.CacheHelper;
import com.beimi.util.rules.model.Board;
import com.beimi.util.rules.model.NextPlayer;
import com.beimi.util.rules.model.Player;
import com.beimi.util.rules.model.SelectColor;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

public class CreateMJRaiseHandsTask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask{

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateMJRaiseHandsTask(long timer , GameRoom gameRoom, String orgi){
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
		/**
		 * 
		 * 检查是否所有人都已经定缺，如果定缺完毕，则通知庄家开始出牌，如果有未完成定缺的，则自动选择
		 */
		Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
		Player banker = null ;
		for(Player player : board.getPlayers()){
			if(player.getPlayuser().equals(board.getBanker())){
				banker = player ;
			}
			if(!player.isSelected()){
				SelectColor color = new SelectColor( board.getBanker(), player.getPlayuser()) ;
				color.setColor(GameUtils.selectColor(player.getCards()));
				ActionTaskUtils.sendEvent("selectresult" , color , gameRoom);
				player.setColor(color.getColor()); 
				player.setSelected(true);break ;
			}
		}
		if(banker!=null){
			board.setNextplayer(new NextPlayer(board.getBanker(), false));
			CacheHelper.getBoardCacheBean().put(gameRoom.getId() , board, gameRoom.getOrgi());	//更新缓存数据
			/**
			 * 发送一个通知，告诉大家 ， 开始出牌了
			 */
			sendEvent("lasthands", new GameBoard(banker.getPlayuser(), board.getBanker() , board.getRatio()) , gameRoom) ;
			
			/**
			 * 更新牌局状态
			 */
			CacheHelper.getBoardCacheBean().put(gameRoom.getId(), board, orgi);
			/**
			 * 发送一个 开始打牌的事件 ， 判断当前出牌人是 玩家还是 AI，如果是 AI，则默认 1秒时间，如果是玩家，则超时时间是25秒
			 */
			PlayUserClient playUserClient = ActionTaskUtils.getPlayUserClient(gameRoom.getId(), banker.getPlayuser(), orgi) ;
			
			if(BMDataContext.PlayerTypeEnum.NORMAL.toString().equals(playUserClient.getPlayertype())){
				super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.PLAYCARDS.toString() , 8);	//应该从 游戏后台配置参数中获取
			}else{
				super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.PLAYCARDS.toString() ,3);	//应该从游戏后台配置参数中获取
			}
		}
	}
}
