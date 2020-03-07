package com.beimi.util.cache;

import com.beimi.util.cache.hazelcast.impl.QueneCache;

/**
 * 缓存实例
 * [可实现不同缓存[redis、memcache、Hazlcast]缓存获取，本项目使用Hazlcast缓存]
 * 
 * @author 科
 *
 */
public interface CacheInstance {
	
	/**
	 * 在线用户
	 * @return
	 */
	public CacheBean getOnlineCacheBean();
	
	/**
	 * 系统缓存
	 * @return
	 */
	public CacheBean getSystemCacheBean();
	
	
	/**
	 * 游戏房间
	 * @return
	 */
	public CacheBean getGameRoomCacheBean();
	
	/**
	 * 游戏数据
	 * <p>
	 * <ul>
	 * <li>
	 * key=[roomid,orgi] value={@link com.beimi.util.rules.model.DuZhuBoard} , {@link com.beimi.util.rules.model.MaJiangBoard}
	 * <li>
	 * 
	 * <li>
	 * <li>
	 * <li>
	 * </ul>
	 * 
	 * @return
	 */
	public CacheBean getGameCacheBean();
	
	
	/**
	 * IMR指令
	 * <p>
	 * <ul>
	 * <li>
	 * key=[userid,orgi] value={@link PlayUserClient}
	 * <li>
	 * 
	 * <li>
	 * <li>
	 * <li>
	 * </ul>
	 * 
	 * @return
	 */
	public CacheBean getApiUserCacheBean();
	
	/**
	 * 分布式队列
	 * @return
	 */
	
	public QueneCache getQueneCache();

	public PlayerCacheBean getGamePlayerCacheBean();
	
}