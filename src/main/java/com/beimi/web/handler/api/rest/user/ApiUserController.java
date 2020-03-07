package com.beimi.web.handler.api.rest.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beimi.util.MessageEnum;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.ResultData;
import com.beimi.web.service.repository.es.PlayUserESRepository;

@RestController
@RequestMapping("/api/player")
public class ApiUserController extends Handler{

	@Autowired
	private PlayUserESRepository playUserESRes;

	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResultData> get(HttpServletRequest request , @RequestParam String id) {
    	PlayUser player = null ;
    	if(!StringUtils.isBlank(id) && playUserESRes.exists(id)){
    		player = playUserESRes.findOne(id) ;
    	}
    	return new ResponseEntity<>(new ResultData( player!=null , player != null ? MessageEnum.USER_GET_SUCCESS : MessageEnum.USER_NOT_EXIST, player), HttpStatus.OK);
    }
}