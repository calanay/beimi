package com.beimi.web.handler.admin.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.Organ;
import com.beimi.web.model.OrganRole;
import com.beimi.web.model.Role;
import com.beimi.web.model.User;
import com.beimi.web.service.repository.jpa.OrganRepository;
import com.beimi.web.service.repository.jpa.OrganRoleRepository;
import com.beimi.web.service.repository.jpa.RoleRepository;
import com.beimi.web.service.repository.jpa.UserRepository;

/**
 *
 * @author 程序猿DD
 * @version 1.0.0
 * @blog http://blog.didispace.com
 *
 */
@Controller
@RequestMapping("/admin/organ")
public class OrganController extends Handler{
	
	@Autowired
	private OrganRepository organRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrganRoleRepository organRoleRes ;

    @RequestMapping("/index")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView index(ModelMap map , HttpServletRequest request , @Valid String organ) {
    	List<Organ> organList = organRepository.findAll() ;
    	map.addAttribute("organList", organList);
    	if(organList.size() > 0){
    		Organ organData = null ;
    		if(!StringUtils.isBlank(organ)){
    			for(Organ data : organList){
    				if(data.getId().equals(organ)){
    					map.addAttribute("organData", data);
    					organData = data;
    				}
    			}
    		}else{
    			map.addAttribute("organData", organData = organList.get(0));
    		}
    		if(organData!=null){
    			map.addAttribute("userList", userRepository.findByOrganAndOrgi(organData.getId() , super.getOrgi(request)));
    		}
    	}
    	map.addAttribute("roleList", roleRepository.findAll());
        return request(super.createAdminTempletResponse("/admin/system/organ/index"));
    }
    
    @RequestMapping("/add")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView add(ModelMap map , HttpServletRequest request) {
        return request(super.createRequestPageTempletResponse("/admin/system/organ/add"));
    }
    
    @RequestMapping("/save")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView save(HttpServletRequest request ,@Valid Organ organ) {
    	Organ tempOrgan = organRepository.findByNameAndOrgi(organ.getName(), super.getOrgi(request)) ;
    	String msg = "admin_organ_save_success" ;
    	if(tempOrgan != null){
    		msg =  "admin_organ_save_exist";
    	}else{
    		organ.setOrgi(super.getOrgi(request));
    		organRepository.save(organ) ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/organ/index.html?msg="+msg));
    }
    
    @RequestMapping("/seluser")
    @Menu(type = "admin" , subtype = "seluser" , admin = true)
    public ModelAndView seluser(ModelMap map , HttpServletRequest request , @Valid String organ) {
    	map.addAttribute("userList", userRepository.findByOrgiAndDatastatus(super.getOrgi(request) , false)) ;
    	Organ organData = organRepository.findByIdAndOrgi(organ, super.getOrgi(request)) ;
    	map.addAttribute("userOrganList", userRepository.findByOrganAndOrgi(organ, super.getOrgi(request))) ;
    	map.addAttribute("organ", organData) ;
        return request(super.createRequestPageTempletResponse("/admin/system/organ/seluser"));
    }
    
    
    @RequestMapping("/saveuser")
    @Menu(type = "admin" , subtype = "saveuser" , admin = true)
    public ModelAndView saveuser(HttpServletRequest request ,@Valid String[] users , @Valid String organ) {
    	List<String> userList = new ArrayList<String>();
    	if(users!=null && users.length > 0){
	    	for(String user : users){
	    		userList.add(user) ;
	    	}
	    	List<User> organUserList = userRepository.findAll(userList) ;
	    	for(User user : organUserList){
	    		user.setOrgan(organ);
	    	}
	    	userRepository.save(organUserList) ;
    	}
    	
    	return request(super.createRequestPageTempletResponse("redirect:/admin/organ/index.html?organ="+organ));
    }
    
    @RequestMapping("/user/delete")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView userroledelete(HttpServletRequest request ,@Valid String id , @Valid String organ) {
    	if(id!=null){
	    	User user= userRepository.getOne(id) ;
	    	user.setOrgan(null);
	    	userRepository.save(user) ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/organ/index.html?organ="+organ));
    }
    
    @RequestMapping("/edit")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView edit(ModelMap map ,HttpServletRequest request , @Valid String id) {
    	ModelAndView view = request(super.createRequestPageTempletResponse("/admin/system/organ/edit")) ;
    	view.addObject("organData", organRepository.findByIdAndOrgi(id, super.getOrgi(request))) ;
        return view;
    }
    
    @RequestMapping("/update")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView update(HttpServletRequest request ,@Valid Organ organ) {
    	Organ tempOrgan = organRepository.findByIdAndOrgi(organ.getId(), super.getOrgi(request)) ;
    	String msg = "admin_organ_update_success" ;
    	if(tempOrgan != null){
    		tempOrgan.setName(organ.getName());
    		tempOrgan.setUpdatetime(new Date());
    		tempOrgan.setOrgi(super.getOrgi(request));
    		tempOrgan.setSkill(organ.isSkill());
    		organRepository.save(tempOrgan) ;
    	}else{
    		msg =  "admin_organ_update_not_exist";
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/organ/index.html?msg="+msg));
    }
    
    @RequestMapping("/delete")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView delete(HttpServletRequest request ,@Valid Organ organ) {
    	String msg = "admin_organ_delete" ;
    	if(organ!=null){
	    	organRepository.delete(organ);
    	}else{
    		msg = "admin_organ_not_exist" ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/organ/index.html?msg="+msg));
    }
    
    @RequestMapping("/auth")
    @Menu(type = "admin" , subtype = "organ")
    public ModelAndView auth(ModelMap map ,HttpServletRequest request , @Valid String id) {
    	Organ organData = organRepository.findByIdAndOrgi(id, super.getOrgi(request)) ;
    	map.addAttribute("organData", organData) ;
    	map.addAttribute("roleList", roleRepository.findByOrgi(super.getOrgi(request))) ;
    	
    	map.addAttribute("organRoleList", organRoleRes.findByOrgiAndOrgan(super.getOrgi(request), organData)) ;
    	
        return request(super.createRequestPageTempletResponse("/admin/system/organ/auth"));
    }
    
    @RequestMapping("/auth/save")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView authsave(HttpServletRequest request ,@Valid String id ,@Valid String roles) {
    	Organ organData = organRepository.findByIdAndOrgi(id, super.getOrgi(request)) ;
    	List<OrganRole>  organRoleList = organRoleRes.findByOrgiAndOrgan(super.getOrgi(request), organData) ;
    	organRoleRes.delete(organRoleList);
    	if(!StringUtils.isBlank(roles)){
    		String[] rolesarray = roles.split(",") ;
    		for(String role : rolesarray){
    			OrganRole organRole = new OrganRole();
    			Role tempRole = new Role();
    			tempRole.setId(role);
    			organRole.setRole(tempRole);
    			organRole.setOrgan(organData);
    			organRole.setCreater(super.getUser(request).getId());
    			organRole.setOrgi(super.getOrgi(request));
    			organRole.setCreatetime(new Date());
    			organRoleRes.save(organRole) ;
    		}
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/organ/index.html"));
    }
}