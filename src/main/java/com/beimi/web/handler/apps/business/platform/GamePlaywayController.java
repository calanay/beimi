package com.beimi.web.handler.apps.business.platform;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.beimi.web.model.GamePlaywayGroup;
import com.beimi.web.model.GamePlaywayGroupItem;
import com.beimi.web.service.repository.jpa.GamePlaywayGroupItemRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayGroupRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.beimi.web.service.repository.jpa.GameModelRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;

@Controller
@RequestMapping("/apps/platform")
public class GamePlaywayController extends Handler{
	
	@Autowired
	private GameModelRepository gameModelRes ;
	
	@Autowired
	private GamePlaywayRepository playwayRes ;

	@Autowired
	private GamePlaywayGroupRepository gamePlaywayGroupRes ;

	@Autowired
	private GamePlaywayGroupItemRepository gamePlaywayGroupItemRes;

	@RequestMapping({"/playway"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView playway(ModelMap map , HttpServletRequest request , @Valid String id){
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(id)) ;
		map.addAttribute("gameModelList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC, id)) ;
		
		map.addAttribute("playwayList", playwayRes.findByOrgi(super.getOrgi(request) , new Sort(Sort.Direction.ASC, "sortindex"))) ;
		
		return request(super.createAppsTempletResponse("/apps/business/platform/game/playway/index"));
	}
	
	@RequestMapping({"/playway/add"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView add(ModelMap map , HttpServletRequest request , @Valid String id, @Valid String typeid){
		
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(id)) ;
		map.addAttribute("gameModelList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC, id)) ;
		map.addAttribute("sceneList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_SCENE_DIC)) ;

        map.addAttribute("cardTypeList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_CARDTYPE_DIC)) ;

		map.addAttribute("dicList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_ROOMTITLE_DIC)) ;
		
		map.addAttribute("typeid", typeid) ;
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/add"));
	}
	
	@RequestMapping("/playway/save")
    @Menu(type = "admin" , subtype = "user")
    public ModelAndView save(HttpServletRequest request ,@Valid GamePlayway playway) {
		playway.setOrgi(super.getOrgi(request));
		playway.setCreater(super.getUser(request).getId());
		playway.setCreatetime(new Date());
		playway.setUpdatetime(new Date());
		playwayRes.save(playway) ;
		
		/**
		 * 清除缓存
		 */
		if(!StringUtils.isBlank(playway.getTypeid())){
			GameUtils.cleanPlaywayCache(playway.getTypeid(), super.getOrgi(request));
		}
		
		CacheHelper.getSystemCacheBean().put(playway.getId(), playway, playway.getOrgi());
    	return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway.html?id="+playway.getGame()));
    }
	
	
	@RequestMapping({"/playway/edit"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView edit(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String game){
		
		map.addAttribute("playway", playwayRes.findByIdAndOrgi(id, super.getOrgi(request))) ;
		
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(game)) ;
		map.addAttribute("gameModelList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC, game)) ;

		map.addAttribute("cardTypeList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_CARDTYPE_DIC)) ;
		
		map.addAttribute("sceneList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_SCENE_DIC)) ;

		map.addAttribute("dicList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_ROOMTITLE_DIC)) ;
		
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/edit"));
	}
	
	@RequestMapping("/playway/update")
    @Menu(type = "admin" , subtype = "user")
    public ModelAndView update(HttpServletRequest request ,@Valid GamePlayway playway) {
		GamePlayway tempPlayway = playwayRes.findByIdAndOrgi(playway.getId(), super.getOrgi(request)) ;
		if(tempPlayway!=null){
			playway.setOrgi(super.getOrgi(request));
			playway.setCreater(tempPlayway.getCreater());
			playway.setCreatetime(tempPlayway.getCreatetime());
			playway.setUpdatetime(new Date());
			playwayRes.save(playway) ;
			/**
			 * 清除修改之前的缓存
			 */
			GameUtils.cleanPlaywayCache(tempPlayway.getTypeid(), super.getOrgi(request));
			
			/**
			 * 清除修改之后的缓存
			 */
			GameUtils.cleanPlaywayCache(playway.getTypeid(), super.getOrgi(request));
			
			
			CacheHelper.getSystemCacheBean().put(playway.getId(), playway, playway.getOrgi());
		}
    	return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway.html?id="+playway.getGame()));
    }

	@RequestMapping({"/playway/extpro"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView extpro(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String game){
		map.addAttribute("playway", playwayRes.findByIdAndOrgi(id, super.getOrgi(request))) ;
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(game)) ;
		map.addAttribute("groupList" , gamePlaywayGroupRes.findByOrgiAndPlaywayid( super.getOrgi(request), id , new Sort(Sort.Direction.ASC, "sortindex")));
		map.addAttribute("groupItemList" , gamePlaywayGroupItemRes.findByOrgiAndPlaywayid( super.getOrgi(request), id , new Sort(Sort.Direction.ASC, "sortindex")));
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/extpro"));
	}

	@RequestMapping({"/playway/addgroup"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView addgroup(ModelMap map , HttpServletRequest request , @Valid String playwayid , @Valid String game){
		map.addAttribute("playway", playwayRes.findByIdAndOrgi(playwayid, super.getOrgi(request))) ;
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(game)) ;
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/addgroup"));
	}

	@RequestMapping({"/playway/savegroup"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView savegroup(ModelMap map , HttpServletRequest request , @Valid GamePlaywayGroup gamePlaywayGroup){
		if(gamePlaywayGroup!=null && !StringUtils.isBlank(gamePlaywayGroup.getName())){
			int count = gamePlaywayGroupRes.countByNameAndPlaywayidAndOrgi(gamePlaywayGroup.getName(), gamePlaywayGroup.getPlaywayid() , super.getOrgi(request)) ;
			if(count == 0){
				gamePlaywayGroup.setOrgi(super.getOrgi(request));
				gamePlaywayGroup.setCreater(super.getUser(request).getId());
				gamePlaywayGroupRes.save(gamePlaywayGroup) ;

                CacheHelper.getSystemCacheBean().delete(BMDataContext.ConfigNames.PLAYWAYGROUP.toString(), super.getOrgi(request)) ;
			}
		}
		return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway/extpro.html?id="+gamePlaywayGroup.getPlaywayid()+"&game="+gamePlaywayGroup.getGame()));
	}

	@RequestMapping({"/playway/editgroup"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView editgroup(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String playwayid, @Valid String game){
		map.addAttribute("playway", playwayRes.findByIdAndOrgi(playwayid, super.getOrgi(request))) ;
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(game)) ;
		map.addAttribute("group", gamePlaywayGroupRes.findByIdAndOrgi(id, super.getOrgi(request))) ;
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/editgroup"));
	}

	@RequestMapping({"/playway/updategroup"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView updategroup(ModelMap map , HttpServletRequest request , @Valid GamePlaywayGroup gamePlaywayGroup){
		if(gamePlaywayGroup!=null && !StringUtils.isBlank(gamePlaywayGroup.getId())){
			int count = gamePlaywayGroupRes.countByNameAndPlaywayidAndOrgiNotAndId(gamePlaywayGroup.getName(), gamePlaywayGroup.getPlaywayid() , super.getOrgi(request) , gamePlaywayGroup.getId()) ;
			if(count == 0){
				gamePlaywayGroup.setOrgi(super.getOrgi(request));
				gamePlaywayGroup.setCreater(super.getUser(request).getId());
                gamePlaywayGroup.setUpdatetime(new Date());
				gamePlaywayGroupRes.save(gamePlaywayGroup) ;
                /***
                 *
                 */
                CacheHelper.getSystemCacheBean().delete(BMDataContext.ConfigNames.PLAYWAYGROUP.toString(), super.getOrgi(request)) ;
			}
		}
		return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway/extpro.html?id="+gamePlaywayGroup.getPlaywayid()+"&game="+gamePlaywayGroup.getGame()));
	}

    @RequestMapping({"/playway/deletegroup"})
    @Menu(type="platform", subtype="playway")
    public ModelAndView deletegroup(ModelMap map , HttpServletRequest request , @Valid GamePlaywayGroup gamePlaywayGroup){
        if(gamePlaywayGroup!=null && !StringUtils.isBlank(gamePlaywayGroup.getId())){
            gamePlaywayGroupRes.delete(gamePlaywayGroup);
            gamePlaywayGroupItemRes.deleteByPlaywayidAndOrgi(gamePlaywayGroup.getId() , super.getOrgi(request));

            CacheHelper.getSystemCacheBean().delete(BMDataContext.ConfigNames.PLAYWAYGROUP.toString(), super.getOrgi(request)) ;
            CacheHelper.getSystemCacheBean().delete(BMDataContext.ConfigNames.PLAYWAYGROUPITEM.toString(), super.getOrgi(request)) ;
        }
        return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway/extpro.html?id="+gamePlaywayGroup.getPlaywayid()+"&game="+gamePlaywayGroup.getGame()));
    }


	@RequestMapping({"/playway/additem"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView addgroupitem(ModelMap map , HttpServletRequest request , @Valid String playwayid , @Valid String game , @Valid String groupid){
		map.addAttribute("playway", playwayRes.findByIdAndOrgi(playwayid, super.getOrgi(request))) ;
		map.addAttribute("game", BeiMiDic.getInstance().getDicItem(game)) ;
		map.addAttribute("group", gamePlaywayGroupRes.findByIdAndOrgi(groupid , super.getOrgi(request))) ;
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/addgroupitem"));
	}

	@RequestMapping({"/playway/savegroupitem"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView savegroupitem(ModelMap map , HttpServletRequest request , @Valid GamePlaywayGroupItem gamePlaywayGroupItem){
		if(gamePlaywayGroupItem!=null && !StringUtils.isBlank(gamePlaywayGroupItem.getName())){
			int nameCount = gamePlaywayGroupItemRes.countByNameAndPlaywayidAndOrgi(gamePlaywayGroupItem.getName(), gamePlaywayGroupItem.getPlaywayid() , super.getOrgi(request)) ;
			/**
			 * 不能重复
			 */
			if(nameCount == 0){
				gamePlaywayGroupItem.setOrgi(super.getOrgi(request));
				gamePlaywayGroupItem.setCreater(super.getUser(request).getId());
				gamePlaywayGroupItemRes.save(gamePlaywayGroupItem) ;
                CacheHelper.getSystemCacheBean().delete(BMDataContext.ConfigNames.PLAYWAYGROUPITEM.toString(), super.getOrgi(request)) ;
			}
		}
		return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway/extpro.html?id="+gamePlaywayGroupItem.getPlaywayid()+"&game="+gamePlaywayGroupItem.getGame()));
	}

    @RequestMapping({"/playway/edititem"})
    @Menu(type="platform", subtype="playway")
    public ModelAndView addgroupitem(ModelMap map , HttpServletRequest request , @Valid String playwayid , @Valid String game , @Valid String groupid , @Valid String id){
        map.addAttribute("playway", playwayRes.findByIdAndOrgi(playwayid, super.getOrgi(request))) ;
        map.addAttribute("game", BeiMiDic.getInstance().getDicItem(game)) ;
        map.addAttribute("group", gamePlaywayGroupRes.findByIdAndOrgi(groupid , super.getOrgi(request))) ;
        map.addAttribute("groupItem", gamePlaywayGroupItemRes.findByIdAndOrgi(id , super.getOrgi(request))) ;
        return request(super.createRequestPageTempletResponse("/apps/business/platform/game/playway/editgroupitem"));
    }

    @RequestMapping({"/playway/updategroupitem"})
    @Menu(type="platform", subtype="playway")
    public ModelAndView updategroupitem(ModelMap map , HttpServletRequest request , @Valid GamePlaywayGroupItem gamePlaywayGroupItem){
        if(gamePlaywayGroupItem!=null && !StringUtils.isBlank(gamePlaywayGroupItem.getName())){
            int nameCount = gamePlaywayGroupItemRes.countByNameAndPlaywayidAndOrgiNotAndId(gamePlaywayGroupItem.getName(), gamePlaywayGroupItem.getPlaywayid() , super.getOrgi(request) , gamePlaywayGroupItem.getId()) ;
            /**
             * 不能重复
             */
            if(nameCount == 0){
                gamePlaywayGroupItem.setUpdatetime(new Date());
                gamePlaywayGroupItem.setOrgi(super.getOrgi(request));
                gamePlaywayGroupItem.setCreater(super.getUser(request).getId());
                gamePlaywayGroupItemRes.save(gamePlaywayGroupItem) ;
                /**
                 *
                 */
                CacheHelper.getSystemCacheBean().delete(BMDataContext.ConfigNames.PLAYWAYGROUPITEM.toString(), super.getOrgi(request)) ;
            }
        }
        return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway/extpro.html?id="+gamePlaywayGroupItem.getPlaywayid()+"&game="+gamePlaywayGroupItem.getGame()));
    }

    @RequestMapping({"/playway/deleteitem"})
    @Menu(type="platform", subtype="playway")
    public ModelAndView deletegroupitem(ModelMap map , HttpServletRequest request , @Valid GamePlaywayGroupItem gamePlaywayGroupItem){
        if(gamePlaywayGroupItem!=null && !StringUtils.isBlank(gamePlaywayGroupItem.getId())){
            gamePlaywayGroupItemRes.delete(gamePlaywayGroupItem);
        }
        return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway/extpro.html?id="+gamePlaywayGroupItem.getPlaywayid()+"&game="+gamePlaywayGroupItem.getGame()));
    }
	

	@RequestMapping({"/playway/delete"})
	@Menu(type="platform", subtype="playway")
	public ModelAndView delete(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String game){
		if(!StringUtils.isBlank(id)){
			GamePlayway tempPlayway = playwayRes.findByIdAndOrgi(id, super.getOrgi(request)) ;
			if(tempPlayway!=null){
				playwayRes.delete(tempPlayway);
			}
			/**
			 * 清除缓存
			 */
			GameUtils.cleanPlaywayCache(tempPlayway.getTypeid(), super.getOrgi(request));
		}
		return request(super.createRequestPageTempletResponse("redirect:/apps/platform/playway.html?id="+game));
	}
	
}
