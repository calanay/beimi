package com.beimi.util.cache.hazelcast.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.SqlPredicate;

/**
 * 分布式队列缓存
 * <p>
 * 主要用于游戏的撮合部分，游戏的玩法配置是系统级别个参数配置，
 * 代理和分销账号下的 只包含游戏玩家的业务数据，不包括系统级别的配置，无租户相关问题
 * @author iceworld
 *
 */
@Service("quene_cache")
public class QueneCache{
	
	private String cacheName ;
	@Autowired
	public HazelcastInstance hazelcastInstance;	
	
	public HazelcastInstance getInstance(){
		return hazelcastInstance ;
	}
	public QueneCache getCacheInstance(String cacheName){
		this.cacheName = cacheName ;
		return this;
	}
	
	public String getName() {
		return cacheName ;
	}
	
	public void put(GameRoom value, String orgi){
		getInstance().getMap(this.getName()).put(value.getId() , value) ;
	}
	
	/**
	 * 获取可加入游戏房间
	 * [大厅模式]
	 * 
	 * @param playway
	 * @param orgi
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GameRoom poll(String playway , String orgi) {
		GameRoom gameRoom = null;
		// 从Map里获取 
		PagingPredicate<String, GameRoom> pagingPredicate = null ;
		List gameRoomList = new ArrayList();
		// 处理游戏房间
		if(!StringUtils.isBlank(playway)){
			pagingPredicate = new PagingPredicate<String, GameRoom>(  new SqlPredicate( " playway = '" + playway + "'") , 5 );
			gameRoomList.addAll((getInstance().getMap(this.getName())).values(pagingPredicate) ) ;
			while(gameRoomList!=null && gameRoomList.size() > 0){
				GameRoom room = (GameRoom) gameRoomList.remove(0) ;
				
				getInstance().getMap(this.getName()).delete(room.getId());
				
				List<PlayUserClient> players = CacheHelper.getGamePlayerCacheBean().getCacheObject(room.getId(), room.getOrgi()) ;
				if(players.size() < room.getPlayers()){
					gameRoom = room ; break ;
				}
			}
		}
		
		return gameRoom;
	}
	
	
	public void delete(String roomid){
		getInstance().getMap(this.getName()).delete(roomid);
	}
}
