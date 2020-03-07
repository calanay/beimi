package com.beimi.web.handler.apps.business.platform;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.core.BMDataContext;
import com.beimi.util.GameUtils;
import com.beimi.util.Menu;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.BeiMiDic;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;

@Controller
@RequestMapping("/apps/platform")
public class GameRoomController extends Handler{
	
	@Autowired
	private GameRoomRepository gameRoomRes ;
	
	@Autowired
	private PlayUserESRepository playUserRes;
	
	@Autowired
	private GamePlaywayRepository playwayRes;
	
	@RequestMapping({"/gameroom"})
	@Menu(type="platform", subtype="gameroom")
	public ModelAndView gameusers(ModelMap map , HttpServletRequest request , @Valid String id){
		Page<GameRoom> gameRoomList = gameRoomRes.findByOrgi(super.getOrgi(request), new PageRequest(super.getP(request), super.getPs(request) , new Sort(Sort.Direction.DESC, "createtime"))) ;
		List<String> playUsersList = new ArrayList<String>() ;
		for(GameRoom gameRoom : gameRoomList.getContent()){
			List<PlayUserClient> players = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(),gameRoom.getOrgi()) ;
			gameRoom.setPlayers(players.size());
			if(!StringUtils.isBlank(gameRoom.getMaster())){
				playUsersList.add(gameRoom.getMaster()) ;
			}
			if(!StringUtils.isBlank(gameRoom.getPlayway())){
				gameRoom.setGamePlayway((GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(gameRoom.getPlayway(), super.getOrgi(request)));
			}
		}
		if(playUsersList.size() > 0){
			for(GameRoom gameRoom : gameRoomList.getContent()){
				for(PlayUser playUser : playUserRes.findAll(playUsersList) ){
					if(playUser.getId().equals(gameRoom.getMaster())){
						gameRoom.setMasterUser(playUser); break ;
					}
				}
			}
			
		}
		map.addAttribute("gameRoomList", gameRoomList) ;
		
		map.addAttribute("gameModelList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC)) ;
		
		return request(super.createAppsTempletResponse("/apps/business/platform/game/room/index"));
	}
	

	@RequestMapping({"/gameroom/delete"})
	@Menu(type="platform", subtype="gameroom")
	public ModelAndView delete(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String game){
		if(!StringUtils.isBlank(id)){
			GameRoom gameRoom = gameRoomRes.findByIdAndOrgi(id, super.getOrgi(request)) ;
			if(gameRoom!=null){
				gameRoomRes.delete(gameRoom);
			}
			CacheHelper.getExpireCache().remove(gameRoom.getId());
			GameUtils.removeGameRoom(gameRoom.getId(),gameRoom.getPlayway(), super.getOrgi(request));
			CacheHelper.getGameRoomCacheBean().delete(gameRoom.getId(), super.getOrgi(request)) ;
			CacheHelper.getBoardCacheBean().delete(gameRoom.getId(), super.getOrgi(request)) ;
			List<PlayUserClient> playerUsers = CacheHelper.getGamePlayerCacheBean().getCacheObject(id, super.getOrgi(request)) ;
			for(PlayUserClient tempPlayUser : playerUsers){
				CacheHelper.getRoomMappingCacheBean().delete(tempPlayUser.getId(), super.getOrgi(request)) ;
			}
			
			CacheHelper.getGamePlayerCacheBean().clean(gameRoom.getId() , gameRoom.getOrgi()) ;
		}
		return request(super.createRequestPageTempletResponse("redirect:/apps/platform/gameroom.html"));
	}
	
}
