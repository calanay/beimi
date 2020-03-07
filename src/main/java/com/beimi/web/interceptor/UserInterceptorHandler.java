package com.beimi.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.beimi.core.BMDataContext;
import com.beimi.util.Menu;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.SystemConfig;
import com.beimi.web.model.BeiMiDic;
import com.beimi.web.model.User;

public class UserInterceptorHandler extends HandlerInterceptorAdapter {
	
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	boolean filter = false; 
    	if(handler instanceof HandlerMethod){
	        User user = (User) request.getSession(true).getAttribute(BMDataContext.USER_SESSION_NAME) ;
	        HandlerMethod  handlerMethod = (HandlerMethod ) handler ;
	        Menu menu = handlerMethod.getMethod().getAnnotation(Menu.class) ;
	        if(user != null || (menu!=null && menu.access()) || handlerMethod.getBean() instanceof BasicErrorController){
	        	filter = true;
	        }
	        
	        if(!filter){
	        	response.sendRedirect("/login.html");
	        }
    	}
        return filter ; 
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse response, Object arg2,
            ModelAndView view) throws Exception {
    	User user = (User) arg0.getSession().getAttribute(BMDataContext.USER_SESSION_NAME) ;
    	if( view!=null){
	    	if(user!=null){
				view.addObject("user", user) ;
				view.addObject("schema",arg0.getScheme()) ;
				view.addObject("hostname",arg0.getServerName()) ;
				view.addObject("port",arg0.getServerPort()) ;
				
				HandlerMethod  handlerMethod = (HandlerMethod ) arg2 ;
				Menu menu = handlerMethod.getMethod().getAnnotation(Menu.class) ;
				if(menu!=null){
					view.addObject("subtype", menu.subtype()) ;
					view.addObject("maintype", menu.type()) ;
					view.addObject("typename", menu.name()) ;
				}
				view.addObject("orgi", user.getOrgi()) ;
			}
	    	
	    	view.addObject("sessionid", UKTools.getContextID(arg0.getSession().getId())) ;
			/**
			 * WebIM共享用户
			 */
			User imUser = (User) arg0.getSession().getAttribute(BMDataContext.IM_USER_SESSION_NAME) ;
			if(imUser == null && view!=null){
				imUser = new User();
				imUser.setUsername(BMDataContext.GUEST_USER) ;
				imUser.setId(UKTools.getContextID(arg0.getSession(true).getId())) ;
				imUser.setSessionid(imUser.getId()) ;
				view.addObject("imuser", imUser) ;
			}
			
			if(arg0.getParameter("msg") != null){
				view.addObject("msg", arg0.getParameter("msg")) ;
			}
			view.addObject("uKeFuDic", BeiMiDic.getInstance()) ;	//处理系统 字典数据 ， 通过 字典code 获取
			SystemConfig systemConfig = (SystemConfig) CacheHelper.getSystemCacheBean().getCacheObject("systemConfig", BMDataContext.SYSTEM_ORGI) ; 
			if(systemConfig != null){
				view.addObject("systemConfig", systemConfig)  ;
			}else{
				view.addObject("systemConfig", new SystemConfig())  ;
			}
			view.addObject("gameTypeList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC)) ;
			
			view.addObject("shopWaresTypeList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SHOP_WARES_TYPE_DIC)) ;
    	}
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}