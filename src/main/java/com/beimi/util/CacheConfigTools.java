package com.beimi.util;

import java.util.List;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.AccountConfig;
import com.beimi.web.model.AiConfig;
import com.beimi.web.model.GameConfig;
import com.beimi.web.service.repository.jpa.AccountConfigRepository;
import com.beimi.web.service.repository.jpa.AiConfigRepository;
import com.beimi.web.service.repository.jpa.GameConfigRepository;

/**
 * 用于获取缓存配置
 * @author iceworld
 *
 */
public class CacheConfigTools {
	public static AccountConfig getGameAccountConfig(String orgi){
		AccountConfig config = (AccountConfig) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.getGameAccountConfig(orgi), orgi) ;
		if(config == null){
			AccountConfigRepository accountRes = BMDataContext.getContext().getBean(AccountConfigRepository.class) ;
			List<AccountConfig> gameAccountList = accountRes.findByOrgi(orgi) ;
			if(gameAccountList!=null && gameAccountList.size() >0){
				config = gameAccountList.get(0) ;
			}else{
				config = new AccountConfig() ;
			}
			CacheHelper.getSystemCacheBean().put(BMDataContext.getGameAccountConfig(orgi), config, orgi);
		}
		return config;
	}
	
	public static GameConfig getGameConfig(String orgi){
		GameConfig config = (GameConfig) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.getGameConfig(orgi), orgi) ;
		if(config == null){
			GameConfigRepository gameConfigRes = BMDataContext.getContext().getBean(GameConfigRepository.class) ;
			List<GameConfig> gameConfigList = gameConfigRes.findByOrgi(orgi) ;
			if(gameConfigList!=null && gameConfigList.size() >0){
				config = gameConfigList.get(0) ;
			}else{
				config = new GameConfig() ;
			}
			CacheHelper.getSystemCacheBean().put(BMDataContext.getGameConfig(orgi), config, orgi);
		}
		return config;
	}
	
	public static AiConfig getAiConfig(String orgi){
		AiConfig config = (AiConfig) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.getGameAiConfig(orgi), orgi) ;
		if(config == null){
			AiConfigRepository aiConfigRes = BMDataContext.getContext().getBean(AiConfigRepository.class) ;
			List<AiConfig> gameAccountList = aiConfigRes.findByOrgi(orgi) ;
			if(gameAccountList!=null && gameAccountList.size() >0){
				config = gameAccountList.get(0) ;
			}else{
				config = new AiConfig() ;
			}
			CacheHelper.getSystemCacheBean().put(BMDataContext.getGameAiConfig(orgi), config, orgi);
		}
		return config;
	}
}
