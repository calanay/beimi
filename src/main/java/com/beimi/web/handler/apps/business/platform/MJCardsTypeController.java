package com.beimi.web.handler.apps.business.platform;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.web.handler.Handler;
import com.beimi.web.service.repository.jpa.AccountConfigRepository;
import com.beimi.web.service.repository.jpa.AiConfigRepository;
import com.beimi.web.service.repository.jpa.GameConfigRepository;

@Controller
@RequestMapping("/apps/platform")
public class MJCardsTypeController extends Handler{
	
	@Autowired
	private AccountConfigRepository accountRes ;
	
	@Autowired
	private GameConfigRepository gameConfigRes ;
	
	@Autowired
	private AiConfigRepository aiConfigRes ;
	
	@RequestMapping({"/mjcardstype"})
	@Menu(type="platform", subtype="mjcardstype")
	public ModelAndView index(ModelMap map , HttpServletRequest request){
		
		return request(super.createAppsTempletResponse("/apps/business/platform/game/mjcardstype/index"));
	}
}
