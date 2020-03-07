package com.beimi.config.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.GameEngine;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.Generation;
import com.beimi.web.model.SysDic;
import com.beimi.web.model.SystemConfig;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import com.beimi.web.service.repository.jpa.GenerationRepository;
import com.beimi.web.service.repository.jpa.SysDicRepository;
import com.beimi.web.service.repository.jpa.SystemConfigRepository;

@Component
public class StartedEventListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Resource
	private GameEngine gameEngine ;
	
	private SysDicRepository sysDicRes;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	if(BMDataContext.getContext() == null){
    		BMDataContext.setApplicationContext(event.getApplicationContext());
    	}
    	BMDataContext.setGameEngine(gameEngine);
    	
    	sysDicRes = event.getApplicationContext().getBean(SysDicRepository.class) ;
    	List<SysDic> sysDicList = sysDicRes.findAll() ;
    	
    	for(SysDic dic : sysDicList){
    		CacheHelper.getSystemCacheBean().put(dic.getId(), dic, dic.getOrgi());
			if(dic.getParentid().equals("0")){
				List<SysDic> sysDicItemList = new ArrayList<SysDic>();
				for(SysDic item : sysDicList){
					if(item.getDicid()!=null && item.getDicid().equals(dic.getId())){
						sysDicItemList.add(item) ;
					}
				}
				CacheHelper.getSystemCacheBean().put(dic.getCode(), sysDicItemList, dic.getOrgi());
			}
		}
    	/**
    	 * 加载系统全局配置
    	 */
    	SystemConfigRepository systemConfigRes = event.getApplicationContext().getBean(SystemConfigRepository.class) ;
    	SystemConfig config = systemConfigRes.findByOrgi(BMDataContext.SYSTEM_ORGI) ;
    	if(config != null){
    		CacheHelper.getSystemCacheBean().put("systemConfig", config, BMDataContext.SYSTEM_ORGI);
    	}
    	
    	
    	GamePlaywayRepository playwayRes = event.getApplicationContext().getBean(GamePlaywayRepository.class) ;
    	List<GamePlayway> gamePlaywayList = playwayRes.findAll() ;
    	if(gamePlaywayList != null){
    		for(GamePlayway playway : gamePlaywayList){
    			CacheHelper.getSystemCacheBean().put(playway.getId(), playway, playway.getOrgi());
    		}
    	}
    	
    	GameRoomRepository gameRoomRes = event.getApplicationContext().getBean(GameRoomRepository.class) ;
    	List<GameRoom> gameRoomList = gameRoomRes.findAll() ;
    	if(gameRoomList!= null){
    		for(GameRoom gameRoom : gameRoomList){
    			if(gameRoom.isCardroom()){
    				gameRoomRes.delete(gameRoom);//回收房卡房间资源
    			}else{
    				CacheHelper.getQueneCache().put(gameRoom, gameRoom.getOrgi());
    				CacheHelper.getGameRoomCacheBean().put(gameRoom.getId(), gameRoom, gameRoom.getOrgi());
    			}
    		}
    	}
    	
    	GenerationRepository generationRes = event.getApplicationContext().getBean(GenerationRepository.class) ;
    	List<Generation> generationList = generationRes.findAll() ;
    	for(Generation generation : generationList){
    		CacheHelper.getSystemCacheBean().setAtomicLong(BMDataContext.ModelType.ROOM.toString(), generation.getStartinx());
    	}
    }
}