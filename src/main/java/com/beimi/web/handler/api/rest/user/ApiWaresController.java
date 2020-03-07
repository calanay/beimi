package com.beimi.web.handler.api.rest.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beimi.core.BMDataContext;
import com.beimi.util.UKTools;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.BeiMiDic;
import com.beimi.web.model.ShopWares;
import com.beimi.web.model.SysDic;
import com.beimi.web.model.Wares;

@RestController
@RequestMapping("/api/wares")
public class ApiWaresController extends Handler{

	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ShopWares>> get(HttpServletRequest request) {
		List<SysDic> shopWaresList = BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SHOP_WARES_TYPE_DIC) ;
		
		List<ShopWares> data = new ArrayList<ShopWares>();
		for(SysDic dic : shopWaresList) {
			
			ShopWares shopWares = new ShopWares();
			shopWares.setName(dic.getName());
			shopWares.setCode(dic.getCode());
			if(dic.isHaschild() == false) {
				for(Wares wares : UKTools.getWaresList()) {
					if(wares.getWarestype().equals(dic.getId())) {
						shopWares.getWares().add(wares) ;
					}
				}
			}else {
				shopWares.setStatus("0");
			}
			data.add(shopWares);
		}
    	return new ResponseEntity<>(data, HttpStatus.OK);
    }
}