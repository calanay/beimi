package com.beimi.util.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beimi.core.BMDataContext;
import com.beimi.util.server.handler.GameEventHandler;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * websocket server runner
 * 
 * @author 科
 *
 */
@Component  
public class ServerRunner implements CommandLineRunner {  
    private final SocketIOServer server;
    private final SocketIONamespace gameSocketNameSpace ;
    
    @Autowired  
    public ServerRunner(SocketIOServer server) {  
        this.server = server;  
        gameSocketNameSpace = server.addNamespace(BMDataContext.NameSpaceEnum.GAME.getNamespace());
    }
    
    @Bean(name="gameNamespace")
    public SocketIONamespace getGameSocketIONameSpace(SocketIOServer server ){
    	gameSocketNameSpace.addListeners(new GameEventHandler(server));
    	return gameSocketNameSpace  ;
    }
    
    public void run(String... args) throws Exception { 
        server.start();  
        BMDataContext.setIMServerStatus(true);	//IMServer 启动成功
    }  
}  