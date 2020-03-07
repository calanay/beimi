package com.beimi.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.core.BMDataContext;
import com.beimi.util.UKTools;
import com.beimi.util.UKView;
import com.beimi.web.model.User;


@Controller
@SessionAttributes
public class Handler {
	public final static int PAGE_SIZE_BG = 1 ;
	public final static int PAGE_SIZE_TW = 20 ;
	public final static int PAGE_SIZE_FV = 50 ;
	public final static int PAGE_SIZE_HA = 100 ;
	
	public User getUser(HttpServletRequest request){
		User user = (User) request.getSession(true).getAttribute(BMDataContext.USER_SESSION_NAME)  ;
		if(user==null){
			user = new User();
			user.setId(UKTools.getContextID(request.getSession().getId())) ;
			user.setUsername(BMDataContext.GUEST_USER+"_"+UKTools.genIDByKey(user.getId())) ;
			user.setOrgi(BMDataContext.SYSTEM_ORGI);
			user.setSessionid(user.getId()) ;
		}else{
			user.setSessionid(user.getId()) ;
		}
		return user ;
	}
	
	public User getIMUser(HttpServletRequest request , String sign){
		User user = (User) request.getSession(true).getAttribute(BMDataContext.IM_USER_SESSION_NAME)  ;
		if(user==null){
			user = new User();
			if(!StringUtils.isBlank(sign)){
				user.setId(sign) ;
			}else{
				user.setId(UKTools.getContextID(request.getSession().getId())) ;
			}
			user.setUsername(BMDataContext.GUEST_USER+"_"+UKTools.genIDByKey(user.getId())) ;
			user.setSessionid(user.getId()) ;
		}else{
			user.setSessionid(UKTools.getContextID(request.getSession().getId())) ;
		}
		return user ;
	}
	
	
	public void setUser(HttpServletRequest request , User user){
		request.getSession(true).removeAttribute(BMDataContext.USER_SESSION_NAME) ;
		request.getSession(true).setAttribute(BMDataContext.USER_SESSION_NAME , user) ;
	}
	

	/**
	 * 创建系统监控的 模板页面
	 * @param page
	 * @return
	 */
	public UKView createAdminTempletResponse(String page) {
		return new UKView("/admin/include/tpl" , page);
	}
	/**
	 * 创建系统监控的 模板页面
	 * @param page
	 * @return
	 */
	public UKView createAppsTempletResponse(String page) {
		return new UKView("/apps/include/tpl" , page);
	}
	
	/**
	 * 创建系统监控的 模板页面
	 * @param page
	 * @return
	 */
	public UKView createEntIMTempletResponse(String page) {
		return new UKView("/apps/entim/include/tpl" , page);
	}
	
	public UKView createRequestPageTempletResponse(String page) {
		return new UKView(page);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public ModelAndView request(UKView data) {
    	return new ModelAndView(data.getTemplet()!=null ? data.getTemplet(): data.getPage() , "data", data) ;
    }

	public int getP(HttpServletRequest request) {
		int page = 0;
		String p = request.getParameter("p") ;
		if(!StringUtils.isBlank(p) && p.matches("[\\d]*")){
			page = Integer.parseInt(p) ;
			if(page > 0){
				page = page - 1 ;
			}
		}
		return page;
	}
	
	public int getPs(HttpServletRequest request) {
		int pagesize = PAGE_SIZE_TW;
		String ps = request.getParameter("ps") ;
		if(!StringUtils.isBlank(ps) && ps.matches("[\\d]*")){
			pagesize = Integer.parseInt(ps) ;
		}
		return pagesize;
	}
	
	public int get50Ps(HttpServletRequest request) {
		int pagesize = PAGE_SIZE_FV;
		String ps = request.getParameter("ps") ;
		if(!StringUtils.isBlank(ps) && ps.matches("[\\d]*")){
			pagesize = Integer.parseInt(ps) ;
		}
		return pagesize;
	}
	
	public String getOrgi(HttpServletRequest request)
	{
		return getUser(request).getOrgi();
	}
}
