package com.beimi.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;

@Controller
public class ApplicationController extends Handler{

	@RequestMapping("/")
	@Menu(type = "apps" , subtype = "index" , access = false)
    public ModelAndView admin(HttpServletRequest request) {
        return request(super.createRequestPageTempletResponse("/apps/index"));
    }
}