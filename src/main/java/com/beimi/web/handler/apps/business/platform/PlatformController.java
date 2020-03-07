package com.beimi.web.handler.apps.business.platform;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.web.handler.Handler;

@Controller
@RequestMapping("/apps/platform")
public class PlatformController extends Handler{
	
	@RequestMapping({"/index"})
	@Menu(type="apps", subtype="content")
	public ModelAndView content(ModelMap map , HttpServletRequest request){
		return request(super.createAppsTempletResponse("/apps/business/platform/desktop/index"));
	}
}
