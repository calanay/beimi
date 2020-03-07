package com.beimi.web.handler.api;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beimi.core.BMDataContext;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.handler.Handler;
import com.beimi.web.handler.api.rest.user.ApiRegisterController;
import com.beimi.web.model.PlayUser;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;

@RestController
@RequestMapping("/tokens")
public class ApiLoginController extends Handler{
	
	@Autowired
	private PlayUserESRepository playUserESRes;
	
	@Autowired
	private PlayUserRepository playUserRes ;

    @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(HttpServletRequest request , HttpServletResponse response, @Valid PlayUser playuser) {
    	if(!StringUtils.isBlank(playuser.getMobile()) && !StringUtils.isBlank(playuser.getPassword())){
    		PlayUser tempPlayer = playUserESRes.findByMobileAndPassword(playuser.getMobile(), UKTools.md5(playuser.getPassword())) ;
    		if(tempPlayer == null){
    			playuser = new ApiRegisterController().register(playuser) ;
    		}else{
    			playuser = tempPlayer ;
    		}
    	}
    	ResponseEntity entity = null ;
        if(playuser!=null){
        	playuser.setLogin(true);			//已登录
        	playuser.setOnline(false);		//未在游戏状态
        	playuser.setLastlogintime(new Date());
        	/**
        	 * 消息队列，同时存ES和数据库，或其他持久化数据系统
        	 */
        	UKTools.published(playuser , playUserESRes , playUserRes ,BMDataContext.UserDataEventType.SAVE.toString());
        	/**
        	 * 发送到消息队列，用户登录
        	 */
        	
        	String auth = UKTools.getUUID();
        	CacheHelper.getApiUserCacheBean().put(auth, playuser, BMDataContext.SYSTEM_ORGI);
        	entity = new ResponseEntity<>(auth, HttpStatus.OK) ;
        	response.addCookie(new Cookie("authorization",auth));
        }else{
        	entity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED) ;
        }
        return entity;
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity error(HttpServletRequest request) {
        return new ResponseEntity<>(super.getUser(request) , HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpServletRequest request , @RequestHeader(value="authorization") String authorization) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}