package com.beimi.util.cache;

import java.util.List;

import com.beimi.web.model.PlayUserClient;

public interface PlayerCacheBean {
	/**
	 * 
	 */
	public void put(String key , Object value , String orgi) ;
	
	/**
	 * 
	 * @param key
	 * @param orgi
	 * @return
	 */
	public Object delete(String key , String orgi) ;
	
	/**
	 * 
	 * @param roomid
	 * @param orgi
	 * @return
	 */
	public void clean(String roomid , String orgi) ;
	
	/**
	 * 
	 * @param key
	 * @param orgi
	 * @return
	 */
	public Object getPlayer(String key, String orgi) ;
	
	/**
	 * 获取房间内的所有玩家
	 * @param key
	 * @param orgi
	 * @return
	 */
	public List<PlayUserClient> getCacheObject(String key , String orgi) ;
	
	public Object getCache();

	
	public long getSize();

	public PlayerCacheBean getCacheInstance(String string);
	
}
