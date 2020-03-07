package com.beimi.util.cache;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.CacheEntry;
import org.cache2k.event.CacheEntryExpiredListener;
import org.cache2k.expiry.ExpiryPolicy;
import org.cache2k.expiry.ValueWithExpiryTime;

import com.beimi.core.engine.game.BeiMiGameTask;
import com.beimi.util.cache.hazelcast.HazlcastCacheHelper;
import com.beimi.util.cache.hazelcast.impl.QueneCache;

/**
 * 缓存帮助[管理]对象
 * 
 * @author
 *
 */
public class CacheHelper {
	private static CacheHelper instance = new CacheHelper();
	private final Cache<String,ValueWithExpiryTime> expireCache ;
	
	public CacheHelper(){
		expireCache = new Cache2kBuilder<String, ValueWithExpiryTime>() {}
			.sharpExpiry(true)
			.eternal(false)
			.expiryPolicy(new ExpiryPolicy<String, ValueWithExpiryTime>() {
				@Override
				public long calculateExpiryTime(String key, ValueWithExpiryTime value,
						long loadTime, CacheEntry<String, ValueWithExpiryTime> oldEntry) {
					return value.getCacheExpiryTime();
				}
			})
		    .addListener(new CacheEntryExpiredListener<String, ValueWithExpiryTime>() {
				@Override
				public void onEntryExpired(Cache<String, ValueWithExpiryTime> cache,
						CacheEntry<String, ValueWithExpiryTime> task) {
					/**
					 * 
					 */
					((BeiMiGameTask)task.getValue()).execute();
				}
			})
	    .build();
	}
	
	/**
	 * 获取缓存实例
	 */
	public static CacheHelper getInstance(){
		return instance ;
	}
	
	private static CacheInstance cacheInstance = new HazlcastCacheHelper();
	
	/**
	 * 房间映射[在线用户]缓存
	 * 
	 * @return
	 */
	public static CacheBean getRoomMappingCacheBean() {
		return cacheInstance!=null ? cacheInstance.getOnlineCacheBean() : null;
	}
	public static CacheBean getSystemCacheBean() {
		return cacheInstance!=null ? cacheInstance.getSystemCacheBean() : null ;
	}
	
	/**
	 * 获得游戏房间管理缓存
	 * 
	 * <p>
	 * <ul>
	 * <li>
	 * key=[roomid,orgi] value={@link GameRoom}
	 * <li>
	 * 
	 * <li>
	 * <li>
	 * <li>
	 * </ul>
	 * 
	 * @return
	 */
	public static CacheBean getGameRoomCacheBean() {
		return cacheInstance!=null ? cacheInstance.getGameRoomCacheBean() : null ;
	}
	
	/**
	 * 游戏玩家缓存
	 * 
	 * @return
	 */
	public static PlayerCacheBean getGamePlayerCacheBean() {
		return cacheInstance!=null ? cacheInstance.getGamePlayerCacheBean() : null ;
	}
	
	public static CacheBean getApiUserCacheBean() {
		return cacheInstance!=null ? cacheInstance.getApiUserCacheBean() : null ;
	}
	
	/**
	 * 存放游戏数据的 ，Board[牌局]
	 * @return
	 */
	public static CacheBean getBoardCacheBean() {
		return cacheInstance!=null ? cacheInstance.getGameCacheBean() : null ;
	}
	
	/**
	 * 游戏房间队列
	 * 
	 * @return {@link QueneCache}
	 */
	public static QueneCache getQueneCache(){
		return cacheInstance!=null ? cacheInstance.getQueneCache() : null ;
	}
	
	public static Cache<String,ValueWithExpiryTime> getExpireCache(){
	    return instance.expireCache;
	}
}