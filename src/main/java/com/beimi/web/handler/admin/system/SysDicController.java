package com.beimi.web.handler.admin.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.SysDic;
import com.beimi.web.service.repository.jpa.SysDicRepository;

@Controller
@RequestMapping("/admin/sysdic")
public class SysDicController extends Handler{
	
	
	@Autowired
	private SysDicRepository sysDicRes;

    @RequestMapping("/index")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView index(ModelMap map , HttpServletRequest request) {
    	map.addAttribute("sysDicList", sysDicRes.findByParentid( "0" , new PageRequest(super.getP(request), super.getPs(request) , Direction.DESC , "createtime")));
        return request(super.createAdminTempletResponse("/admin/system/sysdic/index"));
    }
    
    @RequestMapping("/add")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView add(ModelMap map , HttpServletRequest request) {
        return request(super.createRequestPageTempletResponse("/admin/system/sysdic/add"));
    }
    
    @RequestMapping("/save")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView save(HttpServletRequest request ,@Valid SysDic dic) {
    	List<SysDic> sysDicList = sysDicRes.findByCodeOrName(dic.getCode() , dic.getName()) ;
    	String msg = null ;
    	if(sysDicList.size() == 0){
    		dic.setParentid("0");
    		dic.setHaschild(true);
    		dic.setCreater(super.getUser(request).getId());
    		dic.setCreatetime(new Date());
    		sysDicRes.save(dic) ;
    		reloadSysDicItem(dic , request);
    	}else{
    		msg = "exist" ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/index.html"+(msg!=null ? "?msg="+msg : "")));
    }
    
    @RequestMapping("/edit")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView edit(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String p) {
    	map.addAttribute("sysDic", sysDicRes.findById(id)) ;
    	map.addAttribute("p", p) ;
        return request(super.createRequestPageTempletResponse("/admin/system/sysdic/edit"));
    }
    
    @RequestMapping("/update")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView update(HttpServletRequest request ,@Valid SysDic dic, @Valid String p) {
    	List<SysDic> sysDicList = sysDicRes.findByCodeOrName(dic.getCode() , dic.getName()) ;
    	if(sysDicList.size() == 0 || (sysDicList.size() ==1 && sysDicList.get(0).getId().equals(dic.getId()))){
    		SysDic sysDic = sysDicRes.findById(dic.getId()) ;
    		sysDic.setName(dic.getName());
    		sysDic.setCode(dic.getCode());
    		sysDic.setCtype(dic.getCtype());
    		sysDic.setIconskin(dic.getIconskin());
    		sysDic.setIconstr(dic.getIconstr());
    		sysDic.setDescription(dic.getDescription());
			sysDic.setDefaultvalue(dic.isDefaultvalue());
    		sysDicRes.save(sysDic) ;
    		
    		reloadSysDicItem(sysDic, request);
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/index.html?p="+p));
    }
    
    @RequestMapping("/delete")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView delete(ModelMap map , HttpServletRequest request , @Valid String id, @Valid String p) {
    	SysDic sysDic = sysDicRes.findById(id);
    	sysDicRes.delete(sysDicRes.findByDicid(id) );
    	sysDicRes.delete(sysDic);
    	
    	reloadSysDicItem(sysDic, request);
    	
        return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/index.html?p="+p));
    }
    
    @RequestMapping("/dicitem")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitem(ModelMap map , HttpServletRequest request , @Valid String id) {
    	map.addAttribute("sysDic", sysDicRes.findById(id)) ;
    	map.addAttribute("sysDicList", sysDicRes.findByParentid( id , new PageRequest(super.getP(request), super.getPs(request) , Direction.DESC , "createtime")));
        return request(super.createAdminTempletResponse("/admin/system/sysdic/dicitem"));
    }
    
    @RequestMapping("/dicitem/add")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitemadd(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String p) {
    	map.addAttribute("sysDic", sysDicRes.findById(id)) ;
    	map.addAttribute("p", p) ;
        return request(super.createRequestPageTempletResponse("/admin/system/sysdic/dicitemadd"));
    }
    
    @RequestMapping("/dicitem/save")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitemsave(HttpServletRequest request ,@Valid SysDic dic , @Valid String p) {
    	List<SysDic> sysDicList = sysDicRes.findByDicidAndName(dic.getDicid() ,  dic.getName()) ;
    	String msg = null ;
    	if(sysDicList.size() == 0){
    		dic.setHaschild(true);
    		dic.setOrgi(super.getOrgi(request));
    		dic.setCreater(super.getUser(request).getId());
    		dic.setCreatetime(new Date());
    		sysDicRes.save(dic) ;
    		reloadSysDicItem(dic, request);
    	}else{
    		msg = "exist" ;
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/dicitem.html?id="+dic.getParentid()+(msg!=null ? "&p="+p+"&msg="+msg : "")));
    }
    
    private void reloadSysDicItem(SysDic dic , HttpServletRequest request ){
    	CacheHelper.getSystemCacheBean().put(dic.getId(), dic,super.getOrgi(request));
		if(dic.getDicid()!=null){
			SysDic root = (SysDic) CacheHelper.getSystemCacheBean().getCacheObject(dic.getDicid(), super.getOrgi(request)) ;
			List<SysDic> sysDicList = sysDicRes.findByDicid(dic.getDicid()) ;
			CacheHelper.getSystemCacheBean().put(root.getCode(), sysDicList,super.getOrgi(request));
		}else if(dic.getParentid().equals("0")){
			List<SysDic> sysDicList = sysDicRes.findByDicid(dic.getId()) ;
			CacheHelper.getSystemCacheBean().put(dic.getCode(), sysDicList,super.getOrgi(request));
		}
    }
    
    @RequestMapping("/dicitem/batadd")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitembatadd(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String p) {
    	map.addAttribute("sysDic", sysDicRes.findById(id)) ;
    	map.addAttribute("p", p) ;
        return request(super.createRequestPageTempletResponse("/admin/system/sysdic/batadd"));
    }
    
    @RequestMapping("/dicitem/batsave")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitembatsave(HttpServletRequest request ,@Valid SysDic sysDic , 
    		@Valid String content , @Valid String p) {
    	String[] dicitems = content.split("[\n\r\n]");
		int count = 0 ;
		for(String dicitem : dicitems){
			String[] dicValues = dicitem.split("[\t, ;]{1,}");
			if(dicValues.length == 2 && dicValues[0].length()>0 && dicValues[1].length() >0){
				SysDic dic = new SysDic() ;
				dic.setOrgi(super.getOrgi(request));
				
				dic.setName(dicValues[0]);
				dic.setCode(dicValues[1]);
				dic.setCreater(super.getUser(request).getId());
				dic.setParentid(sysDic.getParentid());
				dic.setDicid(sysDic.getDicid());
				dic.setSortindex(++count);
				dic.setCreatetime(new Date());
				dic.setUpdatetime(new Date());
				if(sysDicRes.findByDicidAndName(dic.getDicid(), dic.getName()).size() == 0){
					sysDicRes.save(dic) ;
				}
				
			}
		}
		reloadSysDicItem(sysDicRes.getOne(sysDic.getParentid()), request);
		
    	return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/dicitem.html?id="+sysDic.getParentid()+"&p="+p));
    }
    
    @RequestMapping("/dicitem/edit")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitemedit(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String p) {
    	map.addAttribute("sysDic", sysDicRes.findById(id)) ;
    	map.addAttribute("p", p) ;
        return request(super.createRequestPageTempletResponse("/admin/system/sysdic/dicitemedit"));
    }
    
    @RequestMapping("/dicitem/update")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitemupdate(HttpServletRequest request ,@Valid SysDic dic, @Valid String p) {
    	List<SysDic> sysDicList = sysDicRes.findByDicidAndName(dic.getDicid() , dic.getName()) ;
    	if(sysDicList.size() == 0 || (sysDicList.size() ==1 && sysDicList.get(0).getId().equals(dic.getId()))){
    		SysDic sysDic = sysDicRes.findById(dic.getId()) ;
    		sysDic.setName(dic.getName());
    		sysDic.setCode(dic.getCode());
    		sysDic.setCtype(dic.getCtype());
    		sysDic.setOrgi(super.getOrgi(request));
    		sysDic.setIconskin(dic.getIconskin());
    		sysDic.setIconstr(dic.getIconstr());
    		sysDic.setDiscode(dic.isDiscode());
    		sysDic.setDescription(dic.getDescription());
			sysDic.setDefaultvalue(dic.isDefaultvalue());
			sysDic.setHaschild(dic.isHaschild());
    		sysDicRes.save(sysDic) ;
    		
    		reloadSysDicItem(sysDic, request);
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/dicitem.html?id="+dic.getParentid()+"&p="+p));
    }
    
    @RequestMapping("/dicitem/delete")
    @Menu(type = "admin" , subtype = "sysdic")
    public ModelAndView dicitemdelete(ModelMap map , HttpServletRequest request , @Valid String id, @Valid String p) {
    	sysDicRes.delete(sysDicRes.findByDicid(id));
    	SysDic dic = sysDicRes.getOne(id) ;
    	sysDicRes.delete(dic);
    	reloadSysDicItem(dic, request);
        return request(super.createRequestPageTempletResponse("redirect:/admin/sysdic/dicitem.html?id="+dic.getParentid()+"&p="+p));
    }
    
}