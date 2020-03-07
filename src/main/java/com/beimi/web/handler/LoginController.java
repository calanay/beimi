package com.beimi.web.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.core.BMDataContext;
import com.beimi.util.Menu;
import com.beimi.util.UKTools;
import com.beimi.web.model.Organ;
import com.beimi.web.model.OrganRole;
import com.beimi.web.model.Role;
import com.beimi.web.model.RoleAuth;
import com.beimi.web.model.User;
import com.beimi.web.model.UserRole;
import com.beimi.web.service.repository.jpa.OrganRepository;
import com.beimi.web.service.repository.jpa.OrganRoleRepository;
import com.beimi.web.service.repository.jpa.RoleAuthRepository;
import com.beimi.web.service.repository.jpa.UserRepository;
import com.beimi.web.service.repository.jpa.UserRoleRepository;

/**
 *
 * @author UK
 * @version 1.0.0
 *
 */
@Controller
public class LoginController extends Handler{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrganRoleRepository organRoleRes ;
	
	@Autowired
	private UserRoleRepository userRoleRes ;
	
	@Autowired
	private RoleAuthRepository roleAuthRes ;
	
	@Autowired
	private OrganRepository organRepository;

    @RequestMapping(value = "/login" , method=RequestMethod.GET)
    @Menu(type = "apps" , subtype = "user" , access = true)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response  , @RequestHeader(value = "referer", required = false) String referer , @Valid String msg) {
    	ModelAndView view = request(super.createRequestPageTempletResponse("redirect:/"));
    	if(request.getSession(true).getAttribute(BMDataContext.USER_SESSION_NAME) ==null){
    		view = request(super.createRequestPageTempletResponse("/login"));
	    	if(!StringUtils.isBlank(request.getParameter("referer"))){
	    		referer = request.getParameter("referer") ;
	    	}
	    	if(!StringUtils.isBlank(referer)){
	    		view.addObject("referer", referer) ;
	    	}
    	}
    	if(!StringUtils.isBlank(msg)){
    		view.addObject("msg", msg) ;
    	}
        return view;
    }
    
    @RequestMapping(value = "/login" , method=RequestMethod.POST)
    @Menu(type = "apps" , subtype = "user" , access = true)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response , @Valid User user ,@Valid String referer) {
    	ModelAndView view = request(super.createRequestPageTempletResponse("redirect:/"));
    	if(request.getSession(true).getAttribute(BMDataContext.USER_SESSION_NAME) ==null){
	        if(user!=null && user.getUsername()!=null){
		    	final User loginUser = userRepository.findByUsernameAndPassword(user.getUsername() , UKTools.md5(user.getPassword())) ;
		        if(loginUser!=null && !StringUtils.isBlank(loginUser.getId())){
		        	loginUser.setLogin(true);
		        	super.setUser(request, loginUser);
		        	if(!StringUtils.isBlank(referer)){
		        		view = request(super.createRequestPageTempletResponse("redirect:"+referer));
			    	}
		        	List<UserRole> userRoleList = userRoleRes.findByOrgiAndUser(loginUser.getOrgi(), loginUser);
		        	if(userRoleList!=null & userRoleList.size()>0){
		        		for(UserRole userRole : userRoleList){
		        			loginUser.getRoleList().add(userRole.getRole()) ;
		        		}
		        	}
		        	if(!StringUtils.isBlank(loginUser.getOrgan())){
		        		Organ organ = organRepository.findByIdAndOrgi(loginUser.getOrgan(), loginUser.getOrgi()) ;
		        		if(organ!=null){
		        			List<OrganRole> organRoleList = organRoleRes.findByOrgiAndOrgan(loginUser.getOrgi(), organ) ;
		        			if(organRoleList.size() > 0){
		        				for(OrganRole organRole : organRoleList){
		        					loginUser.getRoleList().add(organRole.getRole()) ;
		        				}
		        			}
		        		}
		        	}
		        	//获取用户的授权资源
		        	List<RoleAuth> roleAuthList = roleAuthRes.findAll(new Specification<RoleAuth>(){
						@Override
						public Predicate toPredicate(Root<RoleAuth> root, CriteriaQuery<?> query,
								CriteriaBuilder cb) {
							List<Predicate> list = new ArrayList<Predicate>();  
							if(loginUser.getRoleList()!=null && loginUser.getRoleList().size() > 0){
								for(Role role : loginUser.getRoleList()){
									list.add(cb.equal(root.get("roleid").as(String.class), role.getId())) ;
								}
							}
							Predicate[] p = new Predicate[list.size()];  
							cb.and(cb.equal(root.get("orgi").as(String.class), loginUser.getOrgi())) ;
						    return cb.or(list.toArray(p));  
						}}) ;
		        	
		        	loginUser.setRoleAuthList(roleAuthList);//获取用户收取的资源信息
		        	
		        	loginUser.setLastlogintime(new Date());
		        	if(!StringUtils.isBlank(loginUser.getId())){
		        		userRepository.save(loginUser) ;
		        	}
		        }else{
		        	view = request(super.createRequestPageTempletResponse("/login"));
		        	if(!StringUtils.isBlank(referer)){
			    		view.addObject("referer", referer) ;
			    	}
		        	view.addObject("msg", "0") ;
		        }
	        }
    	}
    	return view;
    }
    
    @RequestMapping("/logout")  
    public String logout(HttpServletRequest request  ){  
    	request.getSession().removeAttribute(BMDataContext.USER_SESSION_NAME) ;
         return "redirect:/";
    }  
    
}