package com.beimi.core.engine.game.pva;

import com.beimi.core.BMDataContext;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.service.repository.es.PlayUserESRepository;

/**
 *
 * Personal virtual assets(个人虚拟资产) ， 注意：所有的 设计个人账户虚拟资产的 变更的 操作，
 * 都需要先做校验、事务处理和 账号资产状况的 RSA ， PVA信息需要经常和 订单系统 交互
 * @author iceworld
 *
 */
public abstract class Pva{

	/**
	 * 充值，返回当前账户余额 , 充值后的业务逻辑处理，如果当前有未处理的订单，需要优先处理
	 * @param playuser
	 * @param action , 收入类型 ， 1、充值，2、兑换、3、赢了，4、赠送，6、抽奖，7、接受赠与，8、破产补助
	 * @param amount
	 * @return
	 */
	public abstract PVAOperatorResult income(PlayUserClient playUserClient,String action,int amount);
	
	/**
	 * 消费，返回当前账户余额 ， 消费业务逻辑处理，需要优先验证 当前账户是否有足够的余额用于消费
	 * @param playuser
	 * @param action , 支出类型 ， 1、输了，2、逃跑扣除、3、兑换扣除，4、送好友
	 * @param amount
	 * @return
	 */
	public abstract PVAOperatorResult consume(PlayUserClient playUserClient,String action,int amount) ;
	
	/**
	 * 兑换 ， 兑换的业务逻辑处理，需要验证当前账户是否有足够的余额用于 兑换 ， 兑换扣费完成之后，需要 生成新的余额的 签名信息
	 * @param playuser
	 * @param amount
	 * @param action , 兑换礼品
	 * @param giftid
	 * @return
	 */
	public abstract PVAOperatorResult exchange(PlayUserClient playUserClient ,String action, int amount , String giftid) ;
	
	/**
	 * 验证当前玩家的余额是否有异常
	 * @param playuser
	 * @return
	 */
	public PVAOperatorResult verify(PlayUserClient playUserClient){
		PlayUser playUser = playerUser(playUserClient);
		/**
		 * playUser.getSign();
		 */
		return new PVAOperatorResult(BMDataContext.PVAStatusEnum.OK.toString(), BMDataContext.PVActionEnum.VERIFY.toString(), playUser);
	}
	
	/**
	 * 获得PlayUser
	 * @param playUserClient
	 * @return
	 */
	public PlayUser playerUser(PlayUserClient playUserClient){
		PlayUser playUser = null ;
		if(playUserClient!=null){
			PlayUserESRepository playUserEsRes = BMDataContext.getContext().getBean(PlayUserESRepository.class) ;
			playUser = playUserEsRes.findById(playUserClient.getId()) ;
		}
		return playUser ;
	}
}
