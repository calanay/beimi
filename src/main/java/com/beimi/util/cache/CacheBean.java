package com.beimi.util.cache;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

/**
 * 不同功能缓存接口
 * [在线用户、]
 * @author
 *
 */
public interface CacheBean {
	
	/**
	 * 
	 */
	public void put(String key , Object value , String orgi) ;
	
	/**
	 * 
	 */
	public void clear(String orgi);
	
	
	public Object delete(String key , String orgi) ;
	
	public void update(String key , String orgi , Object object) ;
	
	/**
	 * 
	 * @param key
	 * @param orgi
	 * @return
	 */
	public Object getCacheObject(String key, String orgi) ;
	
	
	/**
	 * 
	 * @param key
	 * @param orgi
	 * @return
	 */
	public Object getCacheObject(String key, String orgi,Object defaultValue) ;
	
	/**
	 * 获取所有缓存对象
	 * @param orgi
	 * @return
	 */
	public Collection<?> getAllCacheObject(String orgi) ; 
	
	
	public CacheBean getCacheInstance(String cacheName);
	
	public Object getCache();

	public Lock getLock(String lock, String orgi);
	
	public long getSize();
	
	public long getAtomicLong(String cacheName) ;
	
	public void setAtomicLong(String cacheName , long start) ;	//初始化 发号器
	
}
