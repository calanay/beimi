package com.beimi.util.cache.hazelcast;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheBean;
import com.beimi.util.cache.CacheInstance;
import com.beimi.util.cache.PlayerCacheBean;
import com.beimi.util.cache.hazelcast.impl.ApiUserCache;
import com.beimi.util.cache.hazelcast.impl.GameCache;
import com.beimi.util.cache.hazelcast.impl.GameRoomCache;
import com.beimi.util.cache.hazelcast.impl.OnlineCache;
import com.beimi.util.cache.hazelcast.impl.QueneCache;
import com.beimi.util.cache.hazelcast.impl.SystemCache;
/**
 * Hazlcast缓存实现类
 * 
 * @author admin
 *
 */
public class HazlcastCacheHelper implements CacheInstance{
	/**
	 * 服务类型枚举
	 * @author admin
	 *
	 */
	public enum CacheServiceEnum{
		HAZLCAST_CLUSTER_AGENT_USER_CACHE, 
		HAZLCAST_CLUSTER_AGENT_STATUS_CACHE, 
		HAZLCAST_CLUSTER_QUENE_USER_CACHE,
		/** hazelcast集群系统数据缓存 */
		HAZLCAST_CULUSTER_SYSTEM , 
		HAZLCAST_ONLINE_CACHE , 
		/** hazelcast任务缓存 */
		HAZLCAST_TASK_CACHE, 
		/** hazelcast游戏缓存 */
		HAZLCAST_GAME_CACHE,
		/** hazelcast游戏房间数据缓存 */
		HAZLCAST_GAMEROOM_CACHE , 
		/** 游戏玩家缓存？ */
		GAME_PLAYERS_CACHE , 
		/** IMR指令[用户？]缓存 */
		API_USER_CACHE , 
		/** 队列缓存 */
		QUENE_CACHE;
		
		public String toString(){
			return super.toString().toLowerCase();
		}
	}
	
	@Override
	public CacheBean getOnlineCacheBean() {
		return BMDataContext.getContext().getBean(OnlineCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_ONLINE_CACHE.toString()) ;
	}
	@Override
	public CacheBean getSystemCacheBean() {
		return BMDataContext.getContext().getBean(SystemCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_CULUSTER_SYSTEM.toString()) ;
	}
	@Override
	public CacheBean getGameRoomCacheBean() {
		return BMDataContext.getContext().getBean(GameRoomCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_GAMEROOM_CACHE.toString()) ;
	}
	@Override
	public CacheBean getGameCacheBean() {
		return BMDataContext.getContext().getBean(GameCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_GAME_CACHE.toString()) ;
	}
	@Override
	public CacheBean getApiUserCacheBean() {
		return BMDataContext.getContext().getBean(ApiUserCache.class).getCacheInstance(CacheServiceEnum.API_USER_CACHE.toString()) ;
	}
	@Override
	public QueneCache getQueneCache() {
		// TODO Auto-generated method stub
		return BMDataContext.getContext().getBean(QueneCache.class).getCacheInstance(CacheServiceEnum.QUENE_CACHE.toString()) ;
	}
	@Override
	public PlayerCacheBean getGamePlayerCacheBean() {
		// TODO Auto-generated method stub
		return BMDataContext.getContext().getBean(PlayerCacheBean.class).getCacheInstance(CacheServiceEnum.GAME_PLAYERS_CACHE.toString()) ;
	}
}
