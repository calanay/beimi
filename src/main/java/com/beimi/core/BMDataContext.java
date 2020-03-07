package com.beimi.core;

import org.springframework.context.ApplicationContext;
import java.util.*;
import com.beimi.core.engine.game.GameEngine;

/**
 * 贝密数据上下文
 * 
 * @author
 *
 */
public class BMDataContext {
	public static final String USER_SESSION_NAME = "user";
	public static final String GUEST_USER = "guest";
	public static final String IM_USER_SESSION_NAME = "im_user";
	public static final String GUEST_USER_ID_CODE = "BEIMIGUESTUSEKEY" ;
	public static final String SERVICE_QUENE_NULL_STR = "service_quene_null" ;
	public static final String DEFAULT_TYPE = "default"	;		//默认分类代码
	public static final String BEIMI_SYSTEM_DIC = "com.dic.system.template";
	public static final String BEIMI_SYSTEM_GAME_TYPE_DIC = "com.dic.game.type";
	public static final String BEIMI_SYSTEM_GAME_WELFARETYPE_DIC = "com.dic.game.welfare.type";
	public static final String BEIMI_SHOP_WARES_TYPE_DIC = "com.dic.shop.warestype";
	public static final String BEIMI_SYSTEM_GAME_SCENE_DIC = "com.dic.scene.item";

	public static final String BEIMI_SYSTEM_GAME_CARDTYPE_DIC = "com.dic.game.dizhu.cardtype";

	public static final String BEIMI_SYSTEM_GAME_ROOMTITLE_DIC = "com.dic.game.room.title";
	
	/** 消息通知事件[给客户端用户发消息] */
	public static final String BEIMI_MESSAGE_EVENT = "command" ;
	public static final String BEIMI_PLAYERS_EVENT = "players" ;
	
	public static final String BEIMI_GAMESTATUS_EVENT = "gamestatus" ;
	
	public static final String BEIMI_SEARCHROOM_EVENT = "searchroom" ;
	
	public static final String BEIMI_GAME_PLAYWAY = "game_playway";
	
	public static final String BEIMI_GAME_SHOP_WARES = "game_shop_wares";
	
	public static final String BEIMI_SYSTEM_AUTH_DIC = "com.dic.auth.resource";

	public static final String BEIMI_SYSTEM_ROOM = "room" ;
	
	/** 系统orgi标识：beimi */
	public static String SYSTEM_ORGI = "beimi" ;
	
	private static int WebIMPort = 9081 ;
	/** IM服务状态 :　运行:true, 未运行: false， 默认false */
	private static boolean imServerRunning = false;
	
	private static ApplicationContext applicationContext ;

	public static Map<String , Boolean> model = new HashMap<String,Boolean>();
	
	
	private static GameEngine gameEngine ;
	
	public static int getWebIMPort() {
		return WebIMPort;
	}

	public static void setWebIMPort(int webIMPort) {
		WebIMPort = webIMPort;
	}
	
	public static void setApplicationContext(ApplicationContext context){
		applicationContext = context ;
	}
	
	public static void setGameEngine(GameEngine engine){
		gameEngine = engine ;
	}
	
	/**
	 * 根据ORGI找到对应 游戏配置
	 * @param orgi
	 * @return
	 */
	public static String getGameAccountConfig(String orgi){
		return BMDataContext.ConfigNames.ACCOUNTCONFIG.toString()+"_"+orgi ;
	}
	
	/**
	 * 根据ORGI找到对应 游戏配置
	 * @param orgi
	 * @return
	 */
	public static String getGameConfig(String orgi){
		return BMDataContext.ConfigNames.GAMECONFIG.toString()+"_"+orgi ;
	}
	
	/**
	 * 根据ORGI找到对应 游戏配置
	 * @param orgi
	 * @return
	 */
	public static String getGameAiConfig(String orgi){
		return BMDataContext.ConfigNames.AICONFIG.toString()+"_"+orgi ;
	}
	
	/**
	 * 应用上下文
	 * [spring {@link ApplicationContext}]
	 * 
	 * @return
	 */
	public static ApplicationContext getContext(){
		return applicationContext ;
	}
	
	public static GameEngine getGameEngine(){
		return gameEngine; 
	}
	
	/**
	 * 系统级的加密密码 ， 从CA获取
	 * @return
	 */
	public static String getSystemSecrityPassword(){
		return "BEIMI" ;
	}
	
	/**
	 * 命名空间？
	 * 
	 * @author
	 *
	 */
	public enum NameSpaceEnum{
		SYSTEM("/bm/system"),
		GAME("/bm/game");
		
		private String namespace ;
		
		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		NameSpaceEnum(String namespace){
			this.namespace = namespace ;
		}
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 * 模型类型枚举
	 * [房间、大厅]
	 * 
	 * @author
	 *
	 */
	public enum ModelType{
		/** 房间  */
		ROOM,
		/** 大厅 */
		HALL;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 * 行为记录类型
	 * [补贴、转盘、签到、连续登录]
	 * 
	 * @author
	 *
	 */
	public enum ActRecordType{
		/** 补贴 */
		SUBSIDY,
		/** 转盘 */
		TURN,
		/** 签到 */
		SIGN,
		/** 连续登录 */
		LOGIN;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	
	public enum ConfigNames{
		GAMECONFIG,
		AICONFIG,
		ACCOUNTCONFIG,
		PLAYWAYCONFIG,
		PLAYWAYGROUP,
		PLAYWAYGROUPITEM;
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 * 用户数据操作事件类型
	 * [保存、更新、删除]
	 * 
	 * @author
	 *
	 */
	public enum UserDataEventType{
		SAVE,
		UPDATE,
		DELETE;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum PlayerAction{
		GANG,
		PENG,
		HU,
		CHI,
		GUO;
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum CommandMessageType{
		SUBSIDY,
		SUBSIDYFAILD,
		PVACHANGE;
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum PlayerGangAction{
		MING,		//明杠
		AN,			//暗杠
		WAN;		//弯杠
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum GameTypeEnum{
		MAJIANG,
		DIZHU,
		DEZHOU;
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 * 游戏玩家类型枚举
	 * 
	 * @author
	 *
	 */
	public enum PlayerTypeEnum{
		/** AI[机器人]玩家  */
		AI,
		/** 普通玩家  */
		NORMAL,
		/** 托管玩家  */
		OFFLINE,
		/** 离开房间的玩家  */
		LEAVE;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 * 游戏状态枚举
	 * 
	 * @author
	 *
	 */
	public enum GameStatusEnum{
		/** 准备好了 */
		READY,
		/** 准备好 */
		NOTREADY,
		/** 管理[托管] */
		MANAGED,
		/** 游戏中 */
		PLAYING,
		/** 登录会话过期 */
		TIMEOUT;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 * 牌类型
	 * 
	 * @author 科
	 *
	 */
	public enum CardsTypeEnum{
		ONE(1),		//单张      3~K,A,2
		TWO(2),		//一对	 3~K,A,2
		THREE(3),	//三张	 3~K,A,2
		FOUR(4),	//三带一	 AAA+K
		FORMTWO(41),	//三带对	 AAA+K
		FIVE(5),	//单顺	连子		10JQKA
		SIX(6),		//双顺	连对		JJQQKK
		SEVEN(7),	//三顺	飞机		JJJQQQ
		EIGHT(8),	//飞机	带翅膀	JJJ+QQQ+K+A
		EIGHTONE(81),	//飞机	带翅膀	JJJ+QQQ+KK+AA
		NINE(9),	//四带二			JJJJ+Q+K
		NINEONE(91),	//四带二对			JJJJ+QQ+KK
		TEN(10),	//炸弹			JJJJ
		ELEVEN(11);	//王炸			0+0
		
		private int type ;
		
		CardsTypeEnum(int type){
			this.type = type ;
		} 
		

		public int getType() {
			return type;
		}


		public void setType(int type) {
			this.type = type;
		}
	}
	
	public enum MessageTypeEnum{
		JOINROOM,
		MESSAGE, 
		END,
		TRANS, STATUS , AGENTSTATUS , SERVICE, WRITING;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum SearchRoomResultType{
		NOTEXIST,  //房间不存在
		FULL, 		//房间已满员
		OK,			//加入成功
		DISABLE,	//房间启用了 禁止非邀请加入
		INVALID;	//房主已离开房间
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum MaJiangWinType{
		TUI,
		RIVER,
		END,
		LOST;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum PVActionEnum{
		INCOME,	//
		CONSUME,
		EXCHANGE,
		VERIFY;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	public enum PVAStatusEnum{
		OK,
		NOTENOUGH,
		FAILD,
		NOTEXIST,
		INVALID;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	/**
	 * 收入类型： 1、充值，2、兑换、3、赢了，4、赠送，5、抽奖，6、接受赠与
	 */
	public enum PVAInComeActionEnum{
		RECHARGE,
		EXCHANGE,
		WIN,
		WELFARE,
		PRIZE,
		GIFT;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 *  支出 ：1、输了，2、逃跑扣除、3、兑换扣除，4、送好友
	 */
	public enum PVAConsumeActionEnum{
		LOST,
		ESCAPE,
		DEDUCTION,
		SEND,
		SUBSIDY;
		
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}


	public static void setIMServerStatus(boolean running){
		imServerRunning = running ;
	}
	public static boolean getIMServerStatus(){
		return imServerRunning;
	}
	
}
