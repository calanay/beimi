package com.beimi.config.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.beimi.core.BMDataContext;
import com.beimi.util.UKTools;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
  
@org.springframework.context.annotation.Configuration  
public class GameServerConfiguration  
{  	
	@Value("${uk.im.server.host}")  
    private String host;  
  
    @Value("${uk.im.server.port}")  
    private Integer port;
    
    @Value("${web.upload-path}")
    private String path;
    
    @Value("${uk.im.server.threads}")
    private String threads;
    
    private SocketIOServer server ;
    
    @Bean(name="webimport") 
    public Integer getWebIMPort() {   
    	BMDataContext.setWebIMPort(port);
    	return port;   
    }  
    
    @Bean  
    public SocketIOServer socketIOServer() throws NoSuchAlgorithmException, IOException   
    {  
    	Configuration config = new Configuration();
//		config.setHostname("localhost");
		config.setPort(port);
		
//		config.getSocketConfig().setReuseAddress(true);
//		config.setSocketConfig(new SocketConfig());
//		config.setOrigin("http://im.uckefu.com");
		config.setExceptionListener(new BeiMiExceptionListener());
		
		
		File sslFile = new File(path , "ssl/https.properties") ;
        if(sslFile.exists()){
        	Properties sslProperties = new Properties();
        	FileInputStream in = new FileInputStream(sslFile);
        	sslProperties.load(in);
        	in.close();
        	if(!StringUtils.isBlank(sslProperties.getProperty("key-store")) && !StringUtils.isBlank(sslProperties.getProperty("key-store-password"))){
        		config.setKeyStorePassword(UKTools.decryption(sslProperties.getProperty("key-store-password")));
        	    InputStream stream = new FileInputStream(new File(path , "ssl/"+sslProperties.getProperty("key-store")));
        	    config.setKeyStore(stream);
        	}
        }
		
		
//	    config.setSSLProtocol("https");
		int workThreads = !StringUtils.isBlank(threads) && threads.matches("[\\d]{1,6}") ? Integer.parseInt(threads) : 100 ;
		config.setWorkerThreads(workThreads);
		
//		config.setStoreFactory(new HazelcastStoreFactory());
		config.setAuthorizationListener(new AuthorizationListener() {
			public boolean isAuthorized(HandshakeData data) {
				return true;	//其他安全验证策略，IP，Token，用户名
			}
		});
		/**
		 * 性能优化
		 */
		config.getSocketConfig().setReuseAddress(true);
		config.getSocketConfig().setSoLinger(0);
		config.getSocketConfig().setTcpNoDelay(true);
		config.getSocketConfig().setTcpKeepAlive(true);
		
        return server = new SocketIOServer(config);  
    }
    
    @Bean  
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {  
        return new SpringAnnotationScanner(socketServer);  
    }  
    
    @PreDestroy  
    public void destory() { 
		server.stop();
	}
}  