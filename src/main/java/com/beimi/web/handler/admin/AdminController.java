package com.beimi.web.handler.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.web.handler.Handler;
import com.beimi.web.service.repository.jpa.SysDicRepository;
import com.beimi.web.service.repository.jpa.UserRepository;

@Controller
public class AdminController extends Handler{
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRepository userRes;
	
//	@Autowired
//	private KieContainer kieContainer;
	
	@Autowired
	private SysDicRepository sysDicRes ;
	
    @RequestMapping("/admin/content")
    @Menu(type = "admin" , subtype = "content")
    public ModelAndView content(ModelMap map , HttpServletRequest request) {
    	
//    	KieSession kieSession = kieContainer.newKieSession() ;
//    	long start = System.currentTimeMillis() ;
//    	kieSession.insert(super.getUser(request));
//		int ruleFiredCount = kieSession.fireAllRules();
//		System.out.println(ruleFiredCount);
//		kieSession.dispose();
//		System.out.println(System.currentTimeMillis() - start);
    	return request(super.createAdminTempletResponse("/admin/desktop/index"));
    }
}