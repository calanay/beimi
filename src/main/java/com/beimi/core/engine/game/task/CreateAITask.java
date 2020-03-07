package com.beimi.core.engine.game.task;

import java.util.List;

import org.cache2k.expiry.ValueWithExpiryTime;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.BeiMiGameTask;
import com.beimi.util.GameUtils;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.PlayUserClient;

public class CreateAITask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask{

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateAITask(long timer , GameRoom gameRoom, String orgi){
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
		//执行生成AI
		GameUtils.removeGameRoom(gameRoom.getId(), gameRoom.getPlayway(), orgi);
		List<PlayUserClient> playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
		/**
		 * 清理 未就绪玩家
		 */
		for(int i=0 ; i<playerList.size() ; ){
			PlayUserClient player = playerList.get(i) ;
			if(!player.getGamestatus().equals(BMDataContext.GameStatusEnum.READY.toString())){
				playerList.remove(i) ;
				CacheHelper.getGamePlayerCacheBean().delete(player.getId(), orgi) ;
				continue ;
			}
			i++;
		}
		int aicount = gameRoom.getPlayers() - playerList.size() ;
		if(aicount>0){
			for(int i=0 ; i<aicount ; i++){
				PlayUserClient playerUser = GameUtils.create(new PlayUser() , BMDataContext.PlayerTypeEnum.AI.toString()) ;
				playerUser.setPlayerindex(System.currentTimeMillis());	//按照加入房间的时间排序，有玩家离开后，重新发送玩家信息列表，重新座位
				playerUser.setRoomid(gameRoom.getId());
				playerUser.setRoomready(true);
				
				CacheHelper.getGamePlayerCacheBean().put(playerUser.getId(), playerUser, orgi); //将用户加入到 room ， MultiCache
				playerList.add(playerUser) ;
			}
			
			ActionTaskUtils.sendPlayers(gameRoom, playerList);
			
			/**
			 * 发送一个 Enough 事件
			 */
			ActionTaskUtils.roomReady(gameRoom, super.getGame(gameRoom.getPlayway(), orgi));
		}
	}
}
