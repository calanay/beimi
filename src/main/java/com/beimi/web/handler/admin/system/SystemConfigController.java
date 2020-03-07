package com.beimi.web.handler.admin.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.core.BMDataContext;
import com.beimi.util.Menu;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.Secret;
import com.beimi.web.model.SystemConfig;
import com.beimi.web.service.repository.jpa.SecretRepository;
import com.beimi.web.service.repository.jpa.SystemConfigRepository;
import com.beimi.web.service.repository.jpa.TemplateRepository;
import com.corundumstudio.socketio.SocketIOServer;

@Controller
@RequestMapping("/admin/config")
public class SystemConfigController extends Handler{
	
	@Value("${uk.im.server.port}")  
    private Integer port;

	@Value("${web.upload-path}")
    private String path;
	
	@Autowired
	private SocketIOServer server ;
	
	@Autowired
	private SystemConfigRepository systemConfigRes ;
	
	@Autowired
	private SecretRepository secRes ;
	
	@Autowired
	private TemplateRepository templateRes ;
	
    @RequestMapping("/index")
    @Menu(type = "admin" , subtype = "config" , admin = true)
    public ModelAndView index(ModelMap map , HttpServletRequest request , @Valid String execute) throws SQLException {
    	map.addAttribute("server", server) ;
    	map.addAttribute("imServerStatus", BMDataContext.getIMServerStatus()) ;
    	List<Secret> secretConfig = secRes.findByOrgi(super.getOrgi(request)) ;
    	if(secretConfig!=null && secretConfig.size() > 0){
    		map.addAttribute("secret", secretConfig.get(0)) ;
    	}
    	
    	if(!StringUtils.isBlank(execute) && execute.equals("false")){
    		map.addAttribute("execute", execute) ;
    	}
    	if(!StringUtils.isBlank(request.getParameter("msg"))){
    		map.addAttribute("msg", request.getParameter("msg")) ;
    	}
        return request(super.createAdminTempletResponse("/admin/system/config/index"));
    }
    
    @RequestMapping("/stopimserver")
    @Menu(type = "admin" , subtype = "stopimserver" , access = false , admin = true)
    public ModelAndView stopimserver(ModelMap map , HttpServletRequest request , @Valid String confirm) throws SQLException {
    	boolean execute = false ;
    	if(execute = UKTools.secConfirm(secRes, super.getOrgi(request), confirm)){
	    	server.stop();
	    	BMDataContext.setIMServerStatus(false);
    	}
        return request(super.createRequestPageTempletResponse("redirect:/admin/config/index.html?execute="+execute));
    }
    
    /**
     * 危险操作，请谨慎调用 ， WebLogic/WebSphere/Oracle等中间件服务器禁止调用
     * @param map
     * @param request
     * @return
     * @throws SQLException
     */
    @RequestMapping("/stop")
    @Menu(type = "admin" , subtype = "stop" , access = false , admin = true)
    public ModelAndView stop(ModelMap map , HttpServletRequest request , @Valid String confirm) throws SQLException {
    	boolean execute = false ;
    	if(execute = UKTools.secConfirm(secRes, super.getOrgi(request), confirm)){
	    	server.stop();
	    	BMDataContext.setIMServerStatus(false);
	    	System.exit(0);
    	}
    	return request(super.createRequestPageTempletResponse("redirect:/admin/config/index.html?execute="+execute));
    }
    
    
    @RequestMapping("/save")
    @Menu(type = "admin" , subtype = "save" , admin = true)
    public ModelAndView save(ModelMap map , HttpServletRequest request , @Valid SystemConfig config , @RequestParam(value = "keyfile", required = false) MultipartFile keyfile , @Valid Secret secret) throws SQLException, IOException, NoSuchAlgorithmException {
    	SystemConfig systemConfig = systemConfigRes.findByOrgi(super.getOrgi(request)) ;
    	config.setOrgi(super.getOrgi(request));
    	String msg = "0" ;
    	if(StringUtils.isBlank(config.getJkspassword())){
    		config.setJkspassword(null);
    	}
    	if(systemConfig == null){
    		config.setCreater(super.getUser(request).getId());
    		config.setCreatetime(new Date());
    		systemConfig = config ;
    	}else{
    		UKTools.copyProperties(config,systemConfig);
    	}
    	if(config.isEnablessl()){
	    	if(keyfile!=null && keyfile.getBytes()!=null && keyfile.getBytes().length > 0 && keyfile.getOriginalFilename()!=null && keyfile.getOriginalFilename().length() > 0){
		    	FileUtils.writeByteArrayToFile(new File(path , "ssl/"+keyfile.getOriginalFilename()), keyfile.getBytes());
		    	systemConfig.setJksfile(keyfile.getOriginalFilename());
	    	}
	    	Properties prop = new Properties();     
	    	FileOutputStream oFile = new FileOutputStream(new File(path , "ssl/https.properties"));//true表示追加打开
	    	prop.setProperty("key-store-password", UKTools.encryption(systemConfig.getJkspassword())) ;
	    	prop.setProperty("key-store",systemConfig.getJksfile()) ;
	    	prop.store(oFile , "SSL Properties File");
	    	oFile.close();
    	}else if(new File(path , "ssl").exists()){
    		File[] sslFiles = new File(path , "ssl").listFiles() ;
    		for(File sslFile : sslFiles){
    			sslFile.delete();
    		}
    	}
    	
    	if(secret!=null && !StringUtils.isBlank(secret.getPassword())){
	    	List<Secret> secretConfig = secRes.findByOrgi(super.getOrgi(request)) ;
	    	String repassword = request.getParameter("repassword") ;
	    	if(!StringUtils.isBlank(repassword) && repassword.equals(secret.getPassword())){
		    	if(secretConfig!=null && secretConfig.size() > 0){
		    		Secret tempSecret = secretConfig.get(0) ;
		    		String oldpass = request.getParameter("oldpass") ;
		    		if(!StringUtils.isBlank(oldpass) && UKTools.md5(oldpass).equals(tempSecret.getPassword())){
		    			tempSecret.setPassword(UKTools.md5(secret.getPassword()));
		    			msg = "1" ;
		    			tempSecret.setEnable(true);
		    			secRes.save(tempSecret) ;
		    		}else{
			    		msg = "3" ;
			    	}
		    	}else{
		    		secret.setOrgi(super.getOrgi(request));
		    		secret.setCreater(super.getUser(request).getId());
		    		secret.setCreatetime(new Date());
		    		secret.setPassword(UKTools.md5(secret.getPassword()));
		    		secret.setEnable(true);
		    		msg = "1" ;
		    		secRes.save(secret) ;
		    	}
	    	}else{
	    		msg = "2" ;
	    	}
	    	map.addAttribute("msg", msg) ;
    	}
    	systemConfigRes.save(systemConfig) ;
    	
    	CacheHelper.getSystemCacheBean().put("systemConfig", systemConfig , super.getOrgi(request));
    	map.addAttribute("imServerStatus", BMDataContext.getIMServerStatus()) ;
    	
    	return request(super.createRequestPageTempletResponse("redirect:/admin/config/index.html?msg="+msg));
    }
}