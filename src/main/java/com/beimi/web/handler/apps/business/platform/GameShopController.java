package com.beimi.web.handler.apps.business.platform;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.core.BMDataContext;
import com.beimi.util.Menu;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.BeiMiDic;
import com.beimi.web.model.SysDic;
import com.beimi.web.model.Wares;
import com.beimi.web.service.repository.jpa.WaresRepository;

@Controller
@RequestMapping("/apps/shop")
public class GameShopController extends Handler{
	
	@Autowired
	private WaresRepository waresRes ;
	
	@RequestMapping({"/wares"})
	@Menu(type="shop", subtype="wares")
	public ModelAndView account(ModelMap map , HttpServletRequest request , @Valid String type){
		if(!StringUtils.isBlank(type)) {
			SysDic dic = (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(type, BMDataContext.SYSTEM_ORGI) ;
			if(dic!=null) {
				map.addAttribute("dic" , dic) ;
				map.addAttribute("waresList" , waresRes.findByOrgiAndWarestype(super.getOrgi(request), dic.getId() , new PageRequest(super.getP(request), super.getPs(request), Sort.Direction.ASC, "inx"))) ;
			}
		}
		return request(super.createAppsTempletResponse("/apps/business/platform/shop/index"));
	}
	
	@RequestMapping("/add")
    @Menu(type="shop", subtype="wares")
    public ModelAndView add(ModelMap map , HttpServletRequest request, @Valid String type) {
		if(!StringUtils.isBlank(type)) {
			Wares wares = new Wares();
			wares.setWarestype(type);
			map.addAttribute("wares" , wares) ;
			map.addAttribute("type" , type) ;
			
			SysDic dic = (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(type, BMDataContext.SYSTEM_ORGI) ;
			if(dic!=null) {
				map.addAttribute("imagesList" , BeiMiDic.getInstance().getDic("com.dic.shop.warestype" , dic.getId())) ;
			}
		}
        return request(super.createRequestPageTempletResponse("/apps/business/platform/shop/wares/add"));
    }
    
    @RequestMapping("/save")
    @Menu(type="shop", subtype="wares")
    public ModelAndView save(HttpServletRequest request ,@Valid Wares wares) {
    	wares.setOrgi(super.getOrgi(request));
    	wares.setCreater(super.getUser(request).getId());
    	wares.setCreatetime(new Date());
    	waresRes.save(wares) ;
    	
    	CacheHelper.getSystemCacheBean().delete(BMDataContext.BEIMI_GAME_SHOP_WARES , super.getOrgi(request));
    	return request(super.createRequestPageTempletResponse("redirect:/apps/shop/wares.html?type="+wares.getWarestype()));
    }
    
    
    @RequestMapping("/delete")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView userroledelete(HttpServletRequest request ,@Valid String id) {
    	Wares wares= waresRes.findByIdAndOrgi(id, super.getOrgi(request)) ;
    	if(wares!=null){
	    	waresRes.delete(wares) ;
	    	CacheHelper.getSystemCacheBean().delete(BMDataContext.BEIMI_GAME_SHOP_WARES , super.getOrgi(request));
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/apps/shop/wares.html?type="+wares.getWarestype()));
    }
    
    @RequestMapping("/edit")
    @Menu(type="shop", subtype="wares")
    public ModelAndView edit(ModelMap map ,HttpServletRequest request , @Valid String id) {
    	Wares wares = waresRes.findByIdAndOrgi(id, super.getOrgi(request)) ;
    	map.addAttribute("wares", wares) ;
    	SysDic dic = (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(wares.getWarestype(), BMDataContext.SYSTEM_ORGI) ;
		if(dic!=null) {
			map.addAttribute("imagesList" , BeiMiDic.getInstance().getDic("com.dic.shop.warestype" , dic.getId())) ;
		}
        return  request(super.createRequestPageTempletResponse("/apps/business/platform/shop/wares/edit")) ;
    }
    
    @RequestMapping("/update")
    @Menu(type="shop", subtype="wares")
    public ModelAndView update(HttpServletRequest request ,@Valid Wares wares) {
    	Wares temp = waresRes.findByIdAndOrgi(wares.getId(), super.getOrgi(request)) ;
    	if(temp != null){
    		wares.setUpdatetime(new Date());
    		wares.setOrgi(super.getOrgi(request));
    		wares.setCreatetime(temp.getCreatetime());
    		waresRes.save(wares) ;
    		CacheHelper.getSystemCacheBean().delete(BMDataContext.BEIMI_GAME_SHOP_WARES , super.getOrgi(request));
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/apps/shop/wares.html?type="+wares.getWarestype()));
    }
}
