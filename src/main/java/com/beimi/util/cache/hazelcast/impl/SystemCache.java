package com.beimi.util.cache.hazelcast.impl;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beimi.util.cache.CacheBean;
import com.hazelcast.core.HazelcastInstance;
/**
 * 系统数据缓存
 * 
 * @author
 *
 */
@Service("system_cache")
public class SystemCache implements CacheBean{
	
	@Autowired
	public HazelcastInstance hazelcastInstance;	
	
	private String cacheName ; 
	
	public HazelcastInstance getInstance(){
		return hazelcastInstance ;
	}
	public CacheBean getCacheInstance(String cacheName){
		this.cacheName = cacheName ;
		return this ;
	}
	
	@Override
	public void put(String key, Object value, String orgi) {
		getInstance().getMap(getName()).put(key, value) ;
	}

	@Override
	public void clear(String orgi) {
		getInstance().getMap(getName()).clear();
	}

	@Override
	public Object delete(String key, String orgi) {
		return getInstance().getMap(getName()).remove(key) ;
	}

	@Override
	public void update(String key, String orgi, Object value) {
		getInstance().getMap(getName()).put(key, value);
	}

	@Override
	public Object getCacheObject(String key, String orgi) {
		return getInstance().getMap(getName()).get(key);
	}

	public String getName() {
		return cacheName ;
	}

//	@Override
	public void service() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<?> getAllCacheObject(String orgi) {
		return getInstance().getMap(getName()).keySet();
	}
	@Override
	public Object getCacheObject(String key, String orgi, Object defaultValue) {
		return getCacheObject(key, orgi);
	}
	@Override
	public Object getCache() {
		return getInstance().getMap(cacheName);
	}
	
	@Override
	public Lock getLock(String lock , String orgi) {
		// TODO Auto-generated method stub
		return getInstance().getLock(lock);
	}
	@Override
	public long getSize() {
		return getInstance().getMap(getName()).size();
	}
	@Override
	public long getAtomicLong(String cacheName) {
		return getInstance().getAtomicLong(getName()).incrementAndGet();
	}
	@Override
	public void setAtomicLong(String cacheName, long start) {
		getInstance().getAtomicLong(getName()).set(start);
	}
}
