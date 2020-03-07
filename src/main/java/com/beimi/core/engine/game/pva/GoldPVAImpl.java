package com.beimi.core.engine.game.pva;

import com.beimi.core.BMDataContext;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;

public class GoldPVAImpl extends Pva{
	
	
	/**
	 * 充值
	 * PlayUserClient 仅是 PlayUser的部分字段的镜像，操作个人账号，需要先从数据库存储库中取出PlayUser，
	 * 然后账号的虚拟资产变化只针对PlayUser操作，操作完成后，赋值到 PlayUserClient 然后推送给房间的客户端和当前玩家
	 */
	@Override
	public PVAOperatorResult income(PlayUserClient playUserClient,String action , int amount) {
		PVAOperatorResult result = new PVAOperatorResult(BMDataContext.PVAStatusEnum.OK.toString(), BMDataContext.PVActionEnum.INCOME.toString() , amount);
		playUserClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(playUserClient.getId(), playUserClient.getOrgi()) ;
		/**
		 * 不处理AI
		 */
		if(playUserClient !=null){
			PlayUser playUser = super.playerUser(playUserClient);
			if(amount > 0 && playUser != null){
				result.setPlayer(playUser);
				playUser.setGoldcoins(playUser.getGoldcoins() + amount);
				result.setBalance(playUser.getGoldcoins());					//账户金额变更，需要重新进行RSA签名
				playUserClient.setGoldcoins(playUser.getGoldcoins());
				CacheHelper.getApiUserCacheBean().put(playUserClient.getId(), playUserClient, playUserClient.getOrgi());
				UKTools.published(playUser, BMDataContext.getContext().getBean(PlayUserESRepository.class) , BMDataContext.getContext().getBean(PlayUserRepository.class) );
			}
		}
		return result;
	}

	/**
	 * 消费
	 */
	@Override
	public PVAOperatorResult consume(PlayUserClient playUserClient,String action , int amount) {
		PVAOperatorResult result = new PVAOperatorResult(BMDataContext.PVAStatusEnum.OK.toString(), BMDataContext.PVActionEnum.CONSUME.toString() , amount);
		playUserClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(playUserClient.getId(), playUserClient.getOrgi()) ;
		/**
		 * 不处理AI
		 */
		if(playUserClient !=null){
			PlayUser playUser = super.playerUser(playUserClient);
			if(amount > 0 && playUser != null){
				result.setPlayer(playUser);
				if(playUser.getGoldcoins() >= amount){
					playUser.setGoldcoins(playUser.getGoldcoins() - amount);
					playUserClient.setGoldcoins(playUser.getGoldcoins());
					result.setBalance(playUser.getGoldcoins());				//账户金额变更，需要重新进行RSA签名
					CacheHelper.getApiUserCacheBean().put(playUserClient.getId(), playUserClient, playUserClient.getOrgi());
					UKTools.published(playUser, BMDataContext.getContext().getBean(PlayUserESRepository.class) , BMDataContext.getContext().getBean(PlayUserRepository.class) );
				}else{//
					result = new PVAOperatorResult(BMDataContext.PVAStatusEnum.NOTENOUGH.toString(), BMDataContext.PVActionEnum.CONSUME.toString(), playUser, amount);
				}
			}
		}
		return result ;
	}
	
	/**
	 * 兑换，金币兑换 ， 暂时不做实现，无用 ， 金币是最基本消费 资产类型，无兑换功能
	 */
	@Override
	public PVAOperatorResult exchange(PlayUserClient playUserClient, String action ,int amount, String giftid) {
		return new PVAOperatorResult(BMDataContext.PVAStatusEnum.INVALID.toString(), BMDataContext.PVActionEnum.EXCHANGE.toString(), super.playerUser(playUserClient), amount);
	}
}
