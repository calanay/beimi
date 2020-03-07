package com.beimi.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;

public class BeiMiDic<K,V> extends HashMap<K,V>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2110217015030751243L;
	private static BeiMiDic<Object, Object> uKeFuDic = new BeiMiDic<Object, Object>();
	
	public static BeiMiDic<?, ?> getInstance(){
		return uKeFuDic ;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDic> getSysDic(String key){
		return (List<SysDic>) CacheHelper.getSystemCacheBean().getCacheObject(key, BMDataContext.SYSTEM_ORGI)  ;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		Object obj = CacheHelper.getSystemCacheBean().getCacheObject(String.valueOf(key), BMDataContext.SYSTEM_ORGI) ;
		if(obj!=null && obj instanceof List){
			obj = getDic((String) key) ;
		}else if(obj == null && (String.valueOf(key)).endsWith(".subdic") && (String.valueOf(key)).lastIndexOf(".subdic") > 0){
			String id = (String.valueOf(key)).substring(0  , (String.valueOf(key)).lastIndexOf(".subdic")) ;
			SysDic dic = (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(id, BMDataContext.SYSTEM_ORGI) ;
			if(dic!=null){
				SysDic sysDic = (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(dic.getDicid(), BMDataContext.SYSTEM_ORGI) ;
				obj = getDic(sysDic.getCode(), dic.getParentid()) ;
			}
		}
		return (V) obj;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDic> getDic(String code){
		List<SysDic> dicList = new ArrayList<SysDic>() ;
		List<SysDic> sysDicList = (List<SysDic>) CacheHelper.getSystemCacheBean().getCacheObject(code, BMDataContext.SYSTEM_ORGI)  ;
		if(sysDicList!=null){
			for(SysDic dic : sysDicList){
				if(dic.getParentid().equals(dic.getDicid())){
					dicList.add(dic) ;
				}
			}
		}
		return dicList ;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDic> getDic(String code , String id){
		List<SysDic> dicList = new ArrayList<SysDic>() ;
		List<SysDic> sysDicList = (List<SysDic>) CacheHelper.getSystemCacheBean().getCacheObject(code, BMDataContext.SYSTEM_ORGI)  ;
		if(sysDicList!=null){
			for(SysDic dic : sysDicList){
				if(dic.getParentid().equals(id)){
					dicList.add(dic) ;
				}
			}
		}
		return dicList ;
	}
	
	
	
	public List<SysDic> getEpt(){
		return new ArrayList<SysDic>() ;
	}
	
	public SysDic getDicItem(String id){
		return (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(id, BMDataContext.SYSTEM_ORGI) ;
	}
}
