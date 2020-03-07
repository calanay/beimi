package com.beimi.core.engine.game.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.beimi.core.engine.game.iface.ChessGame;
import com.beimi.util.rules.model.Board;
import com.beimi.util.rules.model.MaJiangBoard;
import com.beimi.util.rules.model.Player;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUserClient;

/**
 * 麻将游戏发牌实现类
 * 
 * @author
 *
 */
public class MaJiangGame implements ChessGame{
	
	/**
	 * 开始麻将游戏 ， 麻将牌 生成规则（总共136张牌，还不会玩 带 春夏秋冬 梅兰竹菊 的玩法，暂不处理） ， 
	 * 1~108:1万~9万小计 36 ， 1筒~9筒小计36，1条~9条小计36     0-107
	 * 东南西北中发白  7*4 = 28张							   -4~-32,
	 * 前端 癞子牌（宝牌） 统一 用 0 表示
	 * 癞子牌确定规则：通常是 最后一张牌 牌面 +1
	 * @return
	 */
	public Board process(List<PlayUserClient> playUsers, GameRoom gameRoom, GamePlayway playway, String banker, int cardsnum){
		Board board = new MaJiangBoard() ;
		board.setCards(null);
		List<Byte> temp = new ArrayList<Byte>(136) ;
		for(int i= 0 ; i<108 ; i++){
			temp.add((byte)i) ;
		}
		/**
		 * 血流/战玩法 ， 无风 ，广东麻将， 有风 ， 需要根据配置的玩法 获取
		 */
		if(playway.isWind()){
			for(int i= -4 ; i>-32 ; i--){
				temp.add(0 , (byte)i) ;
			}
		}
		
		/**
		 * 洗牌次数，参数指定，建议洗牌次数 为1次，多次洗牌的随机效果更好，例如：7次
		 */
		for(int i = 0 ; i<playway.getShuffletimes()+1 ; i++){
			Collections.shuffle(temp);
		}
		byte[] cards = new byte[136] ;
		for(int i=0 ; i<temp.size() ; i++){
			cards[i] = temp.get(i) ;
		}
		
		board.setCards(cards);
		board.setRatio(2); 	//默认番 ： 2
		
		/**
		 * 以下为定癞子牌(根据玩法需要)
		 */
		int random = (byte)new Random().nextInt(6) ;		//骰子 0~6
		board.setPosition(random);	
		
		byte[] powerful = new byte[1];
		if(cards[cards.length - 2] >=0){
			if(cards[cards.length - 2]/4 % 9 == 8){
				powerful[0] = (byte) (cards[cards.length - 2]/4 - 8) ; //癞子牌， 万筒条牌面 ， +1
			}else{
				powerful[0] = (byte) (cards[cards.length - 2]/4 + 1) ; //癞子牌， 万筒条牌面 ， +1
			}
		}else{//东南西北风， 中发白 ， 是中的 跳过
			if(cards[cards.length - 2] / 4 == -3){
				powerful[0] = -2 ;
			}else if(cards[cards.length - 2] / 4 == -1){
				powerful[0] = -7 ;
			}else{
				powerful[0] = (byte) (cards[cards.length - 2] / 4 + 1) ;
			}
		}
		board.setPowerful(powerful);	//填癞子牌
		Player[] players = new Player[playUsers.size()];
		
		int inx = 0 ;
		for(PlayUserClient playUser : playUsers){
			Player player = new Player(playUser.getId()) ;
			if(player.getPlayuser().equals(banker)){
				player.setCards(new byte[cardsnum+1]);	//庄家
				player.setBanker(true);
			}else{
				player.setCards(new byte[cardsnum]);
			}
			players[inx++] = player ;
		}
		
		/**
		 * 切墩 ， 每次 4张， 发够 12张，然后再挑一张牌 ， 切墩 跳过了 骰子
		 */
		for(int i = 0 ; i<3; i++){
			for(int j = 0 ; j<gameRoom.getPlayers(); j++){
				for(int cs=0 ; cs < 4 ; cs++){
					players[j].getCards()[i*4+cs] = temp.remove(0) ;
				}
			}
		}
		
		/**
		 * 挑牌，庄 挑 1
		 */
		for(int i=0 ; i< players.length ; i++){
			if(players[i].isBanker()){
				players[i].getCards()[12] = temp.remove(0) ;
				players[i].getCards()[13] = temp.remove(1) ;
			}else{
				players[i].getCards()[12] = temp.remove(0) ;
			}
		}
		
		for(Player tempPlayer : players){
			Arrays.sort(tempPlayer.getCards());
		}
		board.setDeskcards(temp);	//待打 的麻将 牌
		board.setRoom(gameRoom.getId());
		
		Player tempbanker = players[0];
		if(!StringUtils.isBlank(banker)){
			for(int i= 0 ; i<players.length ; i++){
				Player player = players[i] ;
				if(player.getPlayuser().equals(banker)){
					tempbanker = player ; break ;
				}
			}
			
		}
		board.setPlayers(players);
		
		if(tempbanker!=null){
			board.setBanker(tempbanker.getPlayuser());
		}
		
		return board;
	}

}