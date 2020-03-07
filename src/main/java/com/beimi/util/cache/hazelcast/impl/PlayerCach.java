package com.beimi.util.cache.hazelcast.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.PlayerCacheBean;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.SqlPredicate;

@Service("multi_cache")
public class PlayerCach implements PlayerCacheBean{
	
	@Autowired
	public HazelcastInstance hazelcastInstance;	
	
	public HazelcastInstance getInstance(){
		return hazelcastInstance ;
	}

	public void clean(String roomid ,String orgi) {
		List<PlayUserClient> palyers = getCacheObject(roomid , orgi );
		if(palyers!=null && palyers.size() > 0){
			for(PlayUserClient player : palyers){
				this.delete(player.getId() , orgi) ;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PlayUserClient> getCacheObject(String key, String orgi) {
		PagingPredicate<String, GameRoom> pagingPredicate = null ;
		List playerList = new ArrayList();
		pagingPredicate = new PagingPredicate<String, GameRoom>(  new SqlPredicate( " roomid = '" + key + "'") , 100 );
		playerList.addAll((getInstance().getMap(getName())).values(pagingPredicate) ) ;
		return playerList;
	}

	public String getName() {
		return BMDataContext.BEIMI_GAME_PLAYWAY ;
	}
	@Override
	public void put(String key, Object value, String orgi) {
		getInstance().getMap(getName()).put(key, value) ;
	}
	@Override
	public Object delete(String key, String orgi) {
		return getInstance().getMap(getName()).remove(key) ;
	}
	@Override
	public Object getPlayer(String key, String orgi) {
		return getInstance().getMap(getName()).get(key) ;
	}
	
	@Override
	public Object getCache() {
		return getInstance().getMap(getName());
	}
	@Override
	public long getSize() {
		return getInstance().getMap(getName()).size();
	}

	@Override
	public PlayerCacheBean getCacheInstance(String string) {
		return this;
	}
}