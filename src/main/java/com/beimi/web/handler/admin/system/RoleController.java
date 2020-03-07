package com.beimi.web.handler.admin.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.core.BMDataContext;
import com.beimi.util.Menu;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.Role;
import com.beimi.web.model.RoleAuth;
import com.beimi.web.model.SysDic;
import com.beimi.web.model.BeiMiDic;
import com.beimi.web.model.User;
import com.beimi.web.model.UserRole;
import com.beimi.web.service.repository.jpa.RoleAuthRepository;
import com.beimi.web.service.repository.jpa.RoleRepository;
import com.beimi.web.service.repository.jpa.SysDicRepository;
import com.beimi.web.service.repository.jpa.UserRepository;
import com.beimi.web.service.repository.jpa.UserRoleRepository;

@Controller
@RequestMapping("/admin/role")
public class RoleController extends Handler{
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRoleRepository userRoleRes;
	
	@Autowired
	private RoleAuthRepository roleAuthRes ;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SysDicRepository sysDicRes; 

    @RequestMapping("/index")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView index(ModelMap map , HttpServletRequest request , @Valid String role) {
    	List<Role> roleList = roleRepository.findAll() ;
    	map.addAttribute("roleList", roleList);
    	if(roleList.size() > 0){
    		Role roleData = null ;
    		if(!StringUtils.isBlank(role)){
    			for(Role data : roleList){
    				if(data.getId().equals(role)){
    					roleData = data ;
    					map.addAttribute("roleData", data);
    				}
    			}
    		}else{
    			map.addAttribute("roleData", roleData = roleList.get(0));
    		}
    		if(roleData!=null){
    			map.addAttribute("userRoleList", userRoleRes.findByOrgiAndRole(super.getOrgi(request), roleData, new PageRequest(super.getP(request), super.getPs(request))) );
    		}
    	}
        return request(super.createAdminTempletResponse("/admin/system/role/index"));
    }
    
    @RequestMapping("/add")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView add(ModelMap map , HttpServletRequest request) {
        return request(super.createRequestPageTempletResponse("/admin/system/role/add"));
    }
    
    @RequestMapping("/save")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView save(HttpServletRequest request ,@Valid Role role) {
    	Role tempRole = roleRepository.findByNameAndOrgi(role.getName(), super.getOrgi(request)) ;
    	String msg = "admin_role_save_success" ;
    	if(tempRole != null){
    		msg =  "admin_role_save_exist";
    	}else{
    		role.setOrgi(super.getOrgi(request));
    		role.setCreater(super.getUser(request).getId());
    		role.setCreatetime(new Date());
    		role.setUpdatetime(new Date());
    		roleRepository.save(role) ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/role/index.html?msg="+msg));
    }
    
    @RequestMapping("/seluser")
    @Menu(type = "admin" , subtype = "seluser" , admin = true)
    public ModelAndView seluser(ModelMap map , HttpServletRequest request , @Valid String role) {
    	map.addAttribute("userList", userRepository.findByOrgiAndDatastatus(super.getOrgi(request) , false)) ;
    	Role roleData = roleRepository.findByIdAndOrgi(role, super.getOrgi(request)) ;
    	map.addAttribute("userRoleList", userRoleRes.findByOrgiAndRole(super.getOrgi(request) , roleData)) ;
    	map.addAttribute("role", roleData) ;
        return request(super.createRequestPageTempletResponse("/admin/system/role/seluser"));
    }
    
    
    @RequestMapping("/saveuser")
    @Menu(type = "admin" , subtype = "saveuser" , admin = true)
    public ModelAndView saveuser(HttpServletRequest request ,@Valid String[] users , @Valid String role) {
    	Role roleData = roleRepository.findByIdAndOrgi(role, super.getOrgi(request)) ;
    	List<UserRole> userRoleList = userRoleRes.findByOrgiAndRole(super.getOrgi(request) , roleData) ;
    	if(users!=null && users.length > 0){
	    	for(String user : users){
	    		boolean exist = false ;
	    		for(UserRole userRole : userRoleList){
	    			if(user.equals(userRole.getUser().getId())){
	    				exist = true ; continue ;
	    			}
	    		}
	    		if(exist == false) {
					UserRole userRole = new UserRole() ;
					userRole.setUser(new User(user));
					userRole.setRole(new Role(role));
					userRole.setOrgi(super.getOrgi(request));
					userRole.setCreater(super.getUser(request).getId());
					userRoleRes.save(userRole) ;
	    		}
			}
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/role/index.html?role="+role));
    }
    
    @RequestMapping("/user/delete")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView userroledelete(HttpServletRequest request ,@Valid String id , @Valid String role) {
    	if(role!=null){
	    	userRoleRes.delete(id);
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/role/index.html?role="+role));
    }
    
    @RequestMapping("/edit")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView edit(ModelMap map , HttpServletRequest request ,@Valid String id) {
    	ModelAndView view = request(super.createRequestPageTempletResponse("/admin/system/role/edit")) ;
    	view.addObject("roleData", roleRepository.findByIdAndOrgi(id, super.getOrgi(request))) ;
        return view;
    }
    
    @RequestMapping("/update")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView update(HttpServletRequest request ,@Valid Role role) {
    	Role tempRole = roleRepository.findByIdAndOrgi(role.getId(), super.getOrgi(request)) ;
    	String msg = "admin_role_update_success" ;
    	if(tempRole != null){
    		tempRole.setName(role.getName());
    		tempRole.setUpdatetime(new Date());
    		roleRepository.save(tempRole) ;
    	}else{
    		msg =  "admin_role_update_not_exist";
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/role/index.html?msg="+msg));
    }
    
    @RequestMapping("/delete")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView delete(HttpServletRequest request ,@Valid Role role) {
    	String msg = "admin_role_delete" ;
    	if(role!=null){
    		userRoleRes.delete(userRoleRes.findByOrgiAndRole(super.getOrgi(request), role));
	    	roleRepository.delete(role);
    	}else{
    		msg = "admin_role_not_exist" ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/role/index.html?msg="+msg));
    }
    
    @RequestMapping("/auth")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView auth(ModelMap map , HttpServletRequest request , @Valid String id) {
    	SysDic sysDic = sysDicRes.findByCode(BMDataContext.BEIMI_SYSTEM_AUTH_DIC) ;
    	if(sysDic!=null){
    		map.addAttribute("resourceList", sysDicRes.findByDicid(sysDic.getId())) ;
    	}
    	map.addAttribute("sysDic", sysDic) ;
    	Role role = roleRepository.findByIdAndOrgi(id, super.getOrgi(request)) ;
    	map.addAttribute("role", role) ;
    	map.addAttribute("roleAuthList", roleAuthRes.findByRoleidAndOrgi(role.getId(), super.getOrgi(request))) ;
        return request(super.createRequestPageTempletResponse("/admin/system/role/auth"));
    }
    
    @RequestMapping("/auth/save")
    @Menu(type = "admin" , subtype = "role")
    public ModelAndView authsave(HttpServletRequest request ,@Valid String id ,@Valid String menus) {
    	List<RoleAuth>  roleAuthList = roleAuthRes.findByRoleidAndOrgi(id, super.getOrgi(request)) ;
    	roleAuthRes.delete(roleAuthList);
    	if(!StringUtils.isBlank(menus)){
    		String[] menuarray = menus.split(",") ;
    		for(String menu : menuarray){
    			RoleAuth roleAuth = new RoleAuth();
    			roleAuth.setRoleid(id);
    			roleAuth.setDicid(menu);
    			roleAuth.setCreater(super.getUser(request).getId());
    			roleAuth.setOrgi(super.getOrgi(request));
    			roleAuth.setCreatetime(new Date());
    			SysDic sysDic = BeiMiDic.getInstance().getDicItem(menu) ;
    			roleAuth.setName(sysDic.getName());
    			roleAuth.setDicvalue(sysDic.getCode());
    			roleAuthRes.save(roleAuth) ;
    		}
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/role/index.html"));
    }
}