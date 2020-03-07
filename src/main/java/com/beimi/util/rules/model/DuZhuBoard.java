package com.beimi.util.rules.model;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.ActionTaskUtils;
import com.beimi.core.engine.game.BeiMiGameEvent;
import com.beimi.core.engine.game.CardType;
import com.beimi.core.engine.game.model.Summary;
import com.beimi.core.engine.game.model.SummaryPlayer;
import com.beimi.core.engine.game.pva.PVAOperatorResult;
import com.beimi.util.GameUtils;
import com.beimi.util.PvaTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

/**
 * 斗地主牌局
 * <p>
 * 用于描述当前牌局的内容，实现斗地主逻辑
 * <ul>
 * <li> 
 * 1、随机排序生成的 当前 待起牌（麻将、德州有/斗地主无）
 * <li>
 * 2、玩家 手牌
 * <li>
 * 3、玩家信息
 * <li>
 * 4、当前牌
 * <li>
 * 5、当前玩家
 * <li>
 * 6、房间/牌桌信息
 * <li>
 * 7、其他附加信息
 * </ul>
 * 数据结构内存占用 78 byte ， 一副牌序列化到 数据库 占用的存储空间约为 78 byt， 数据库字段长度约为 20
 * </p>
 * 
 * @author iceworld
 *
 */
public class DuZhuBoard extends Board implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6143646772231515350L;

	/**
	 * 翻底牌 ， 斗地主
	 */
	@Override
	public byte[] pollLastHands() {
		return ArrayUtils.subarray(this.getCards() , this.getCards().length - 3 , this.getCards() .length);
	}

	/**
	 * 暂时不做处理，根据业务规则修改，例如：底牌有大王翻两倍，底牌有小王 翻一倍，底牌是顺子 翻两倍 ====
	 */
	@Override
	public int calcRatio() {
		return 1;
	}

	@Override
	public TakeCards takeCards(Player player , String playerType, TakeCards current) {
		return new TakeDiZhuCards(player);
	}
	
	
	/**
	 * 找到玩家
	 * @param board
	 * @param userid
	 * @return
	 */
	public Player player(String userid){
		Player target = null ;
		for(Player temp : this.getPlayers()){
			if(temp.getPlayuser().equals(userid)){
				target = temp ; break ;
			}
		}
		return target ;
	}
	
	/**
	 * 找到玩家的 位置
	 * @param board
	 * @param userid
	 * @return
	 */
	public int index(String userid){
		int index = 0;
		for(int i=0 ; i<this.getPlayers().length ; i++){
			Player temp = this.getPlayers()[i] ;
			if(temp.getPlayuser().equals(userid)){
				index = i ; break ;
			}
		}
		return index ;
	}
	
	
	/**
	 * 找到下一个玩家
	 * @param board
	 * @param index
	 * @return
	 */
	public Player next(int index){
		Player catchPlayer = null;
		if(index == (this.getPlayers().length - 1)){	//fixed
			index = -1 ;
		}
		for(int i = index + 1 ; i<this.getPlayers().length ; ){
			Player player = this.getPlayers()[i] ;
			if(player.isDocatch() == false){
				catchPlayer = player ;
				break ;
			}else if(player.isRandomcard()){	//重新遍历一遍，发现找到了地主牌的人，终止查找
				break ;
			}else if(i == (this.getPlayers().length - 1)){
				i = 0; continue ;
			}
			i++ ;
		}
		return catchPlayer;
	}
	

	public Player nextPlayer(int index) {
		if(index == (this.getPlayers().length - 1)){
			index = 0 ;
		}else{
			index = index + 1 ;
		}
		return this.getPlayers()[index];
	}
	/**
	 * 
	 * @param player
	 * @param current
	 * @return
	 */
	public TakeCards takecard( Player player , boolean allow , byte[] playCards) {
		return new TakeDiZhuCards(player , allow , playCards);
	}
	
	/**
	 * 当前玩家随机出牌，能管住当前出牌的 最小牌
	 * @param player
	 * @param current
	 * @return
	 */
	public TakeCards takecard(Player player) {
		return new TakeDiZhuCards(player);
	}
	
	/**
	 * 当前玩家随机出牌，能管住当前出牌的 最小牌
	 * @param player
	 * @param current
	 * @return
	 */
	public TakeCards takecard(Player player , TakeCards last) {
		return new TakeDiZhuCards(player, last);
	}
	
	/**
	 * 当前玩家随机出牌，能管住当前出牌的 最小牌
	 * @param player
	 * @param current
	 * @return
	 */
	public TakeCards cardtip(Player player , TakeCards last) {
		return new TakeDiZhuCards(player, last , false);
	}
	/**
	 * 顺序提示玩家出牌
	 * @param player
	 * @param tipcards
	 * @return
	 */
	public TakeCards getCardTips(Player player , byte[] tipcards) {
		return new TakeDiZhuCards(player , tipcards);
	}

	@Override
	public boolean isWin() {
		boolean win = false ;
		if(this.getLast()!=null && this.getLast().getCardsnum() == 0){//出完了
			win = true ;
		}
		return win;
	}

	@Override
	public TakeCards takeCardsRequest(GameRoom gameRoom , Board board, Player player,
			String orgi, boolean auto, byte[] playCards) {
		TakeCards takeCards = null ;
		boolean automic = false ;
		//超时了 ， 执行自动出牌
		if((auto == true || playCards != null)){
			CardType playCardType = null ;
			if(playCards!=null && playCards.length > 0){
				playCardType = ActionTaskUtils.identification(playCards) ;
			}
			if(playCardType == null || playCardType.getCardtype() > 0){
				if(board.getLast() == null || board.getLast().getUserid().equals(player.getPlayuser())){	//当前无出牌信息，刚开始出牌，或者出牌无玩家 压
					/**
					 * 超时处理，如果当前是托管的或玩家超时，直接从最小的牌开始出，如果是 AI，则 需要根据AI级别（低级/中级/高级） 计算出牌 ， 目前先不管，直接从最小的牌开始出
					 */
					takeCards = board.takecard(player , true , playCards) ;
				}else{
					if(playCards == null){
						takeCards = board.takecard(player , board.getLast()) ;
					}else{
						CardType lastCardType = ActionTaskUtils.identification(board.getLast().getCards()) ;
						if(playCardType.getCardtype() >0 && ActionTaskUtils.allow(playCardType, lastCardType)){//合规，允许出牌
							takeCards = board.takecard(player , true , playCards) ;
						}//不合规的牌 ， 需要通知客户端 出牌不符合规则 ， 此处放在服务端判断，防外挂
					}
				}
			}
		}else{
			takeCards = new TakeDiZhuCards();
			takeCards.setUserid(player.getPlayuser());
		}
		if(takeCards!=null){		//通知出牌
			takeCards.setCardsnum(player.getCards().length);
			takeCards.setAllow(true);
			if(takeCards.getCards()!=null){
				Arrays.sort(takeCards.getCards());
			}
			
			if(takeCards.getCards()!=null){
				board.setLast(takeCards);
				takeCards.setDonot(false);	//出牌
			}else{		
				takeCards.setDonot(true);	//不出牌
			}
			if(takeCards.getCardType()!=null && (takeCards.getCardType().getCardtype() == BMDataContext.CardsTypeEnum.TEN.getType() || takeCards.getCardType().getCardtype() == BMDataContext.CardsTypeEnum.ELEVEN.getType())){
				takeCards.setBomb(true);
				ActionTaskUtils.doBomb(board, true);
				ActionTaskUtils.sendEvent("ratio", new BoardRatio(takeCards.isBomb(), false , board.getRatio()), gameRoom);	
			}
			
			Player next = board.nextPlayer(board.index(player.getPlayuser())) ;
			if(next!=null){
				takeCards.setNextplayer(next.getPlayuser());
				board.setNextplayer(new NextPlayer(next.getPlayuser(), false));

				if(board.getLast() != null && board.getLast().getUserid().equals(next.getPlayuser())){	//当前无出牌信息，刚开始出牌，或者出牌无玩家 压
					automic = true ;
				}
				takeCards.setAutomic(automic);
			}
			if(board.isWin()){//出完了
				board.setWinner(player.getPlayuser());
				takeCards.setOver(true);
			}
			/**
			 * 放到 Board的列表里去，如果是不洗牌玩法，则直接将出牌结果 重新发牌
			 */
			if(takeCards.getCards()!=null && takeCards.getCards().length > 0){
				for(byte temp : takeCards.getCards()){
					board.getHistory().add(temp) ;
				}
			}
			
			CacheHelper.getBoardCacheBean().put(gameRoom.getId(), board, gameRoom.getOrgi());
			/**
			 * 判断下当前玩家是不是和最后一手牌 是一伙的，如果是一伙的，手机端提示 就是 不要， 如果不是一伙的，就提示要不起
			 */
			if(player.getPlayuser().equals(board.getBanker())){ //当前玩家是地主
				takeCards.setSameside(false);
			}else{
				if(board.getLast().getUserid().equals(board.getBanker())){ //最后一把是地主出的，然而我却不是地主
					takeCards.setSameside(false);	
				}else{
					takeCards.setSameside(true);
				}
			}
			/**
			 * 移除定时器，然后重新设置
			 */
			CacheHelper.getExpireCache().remove(gameRoom.getRoomid());
			
			
			if(takeCards.getCards()!=null && takeCards.getCards().length == 1){
				takeCards.setCard(takeCards.getCards()[0]);
			}
			
			ActionTaskUtils.sendEvent("takecards", takeCards , gameRoom);	
			
			/**
			 * 牌出完了就算赢了
			 */
			if(board.isWin()){//出完了
				GameUtils.getGame(gameRoom.getPlayway() , orgi).change(gameRoom , BeiMiGameEvent.ALLCARDS.toString() , 0);	//赢了，通知结算
				takeCards.setNextplayer(null);
			}else{
				PlayUserClient nextPlayUserClient = ActionTaskUtils.getPlayUserClient(gameRoom.getId(), takeCards.getNextplayer(), orgi) ;
				if(nextPlayUserClient!=null){
					if(BMDataContext.PlayerTypeEnum.NORMAL.toString().equals(nextPlayUserClient.getPlayertype())){
						GameUtils.getGame(gameRoom.getPlayway() , orgi).change(gameRoom , BeiMiGameEvent.PLAYCARDS.toString() , 25);	//应该从 游戏后台配置参数中获取
					}else{
						GameUtils.getGame(gameRoom.getPlayway() , orgi).change(gameRoom , BeiMiGameEvent.PLAYCARDS.toString() , 3);	//应该从游戏后台配置参数中获取
					}
				}
			}
		}else{
			takeCards = new TakeDiZhuCards();
			takeCards.setAllow(false);
			ActionTaskUtils.sendEvent("takecards", takeCards , gameRoom);	
		}
		return takeCards;
	}

	@Override
	public void dealRequest(GameRoom gameRoom, Board board, String orgi , boolean reverse, String nextplayer) {
		/**
		 * 斗地主无发牌动作
		 */
	}

	@Override
	public void playcards(Board board, GameRoom gameRoom, Player player,
			String orgi) {
	}

	@Override
	public Summary summary(Board board, GameRoom gameRoom , GamePlayway playway) {
		Summary summary = new Summary(gameRoom.getId() , board.getId() , board.getRatio() , board.getRatio() * playway.getScore());
		int dizhuScore = 0 ;
		boolean dizhuWin = board.getWinner().equals(board.getBanker()) ;
		
		List<PlayUserClient> players = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
		
		PlayUserClient dizhuPlayerUser = getPlayerClient(players, board.getBanker());
		int temp = summary.getScore() * (board.getPlayers().length - 1) ;
		SummaryPlayer dizhuSummaryPlayer = null ;
		PVAOperatorResult result = null ;
		boolean gameRoomOver = false ;	//解散房价
		
		for(Player player : board.getPlayers()){
			PlayUserClient playUser = getPlayerClient(players, player.getPlayuser());
			SummaryPlayer summaryPlayer = new SummaryPlayer(player.getPlayuser() , playUser.getUsername() , board.getRatio() , board.getRatio() * playway.getScore() , false , player.getPlayuser().equals(board.getBanker())) ;
			/**
			 * 找到对应的玩家结算信息
			 */
			if(player.getPlayuser().equals(board.getBanker())){
				dizhuSummaryPlayer = summaryPlayer ;
			}
			if(dizhuWin){
				if(player.getPlayuser().equals(board.getBanker())){
					summaryPlayer.setWin(true);
				}else{
					/**
					 * 扣 农民的 金币 , 扣除金币的时候需要最好做一下金币的校验，例如：签名验证是否由系统修改的 金币余额，并记录金币扣除的日志，用于账号账单信息
					 */
					if(playUser.getGoldcoins() <= summaryPlayer.getScore()){
						summaryPlayer.setScore(playUser.getGoldcoins());//还有多少，扣你多少	
						summaryPlayer.setGameover(true);				//金币不够了，破产，重新充值或领取奖励恢复状态
						gameRoomOver = true ;
					}
					dizhuScore = dizhuScore  + summaryPlayer.getScore() ;
					result = PvaTools.getGoldCoins().consume(playUser , BMDataContext.PVAConsumeActionEnum.LOST.toString(), summaryPlayer.getScore()) ;
					summaryPlayer.setBalance(result.getBalance());
				}
			}else{	//地主输了
				if(!player.getPlayuser().equals(board.getBanker())){
					summaryPlayer.setWin(true);
					if(dizhuPlayerUser.getGoldcoins() < temp){	//金币不够扣
						summaryPlayer.setScore(dizhuPlayerUser.getGoldcoins() / (board.getPlayers().length - 1));
						gameRoomOver = true ;
					}
					dizhuScore = dizhuScore  + summaryPlayer.getScore() ;
					
					/**
					 * 应该共用一个 扣除个人虚拟资产的 全局对象，用于处理个人虚拟资产
					 */
					result = PvaTools.getGoldCoins().income(playUser, BMDataContext.PVAInComeActionEnum.WIN.toString(), summaryPlayer.getScore()) ;
					summaryPlayer.setBalance(result.getBalance());
				}
			}
			summaryPlayer.setCards(player.getCards()); //未出完的牌
			summary.getPlayers().add(summaryPlayer) ;
		}
		if(dizhuSummaryPlayer!=null){
			dizhuSummaryPlayer.setScore(dizhuScore);
			if(dizhuWin){
				result = PvaTools.getGoldCoins().income(dizhuPlayerUser, BMDataContext.PVAInComeActionEnum.WIN.toString(), dizhuScore) ;
			}else{
				result = PvaTools.getGoldCoins().consume(dizhuPlayerUser, BMDataContext.PVAConsumeActionEnum.LOST.toString(), dizhuScore) ;
			}
			dizhuSummaryPlayer.setBalance(result.getBalance());
		}
		summary.setGameRoomOver(gameRoomOver);	//有玩家破产，房间解散
		/**
		 * 上面的 Player的 金币变更需要保持 数据库的日志记录 , 机器人的 金币扣完了就出局了
		 */
		return summary;
	}
}
