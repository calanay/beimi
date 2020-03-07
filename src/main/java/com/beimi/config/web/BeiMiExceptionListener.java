package com.beimi.config.web;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;

public class BeiMiExceptionListener extends ExceptionListenerAdapter {
	private static final Logger log = LoggerFactory.getLogger(BeiMiExceptionListener.class);

    @Override
    public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
    	if(e instanceof IOException){
    		log.info(e.getMessage());
    	}else{
    		log.error(e.getMessage(), e);
    	}
    	client.disconnect();
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {
    	if(e instanceof IOException){
    		log.info(e.getMessage());
    	}else{
    		log.error(e.getMessage(), e);
    	}
    	client.disconnect();
    }

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {
    	if(e instanceof IOException){
    		log.info(e.getMessage());
    	}else{
    		log.error(e.getMessage(), e);
    	}
    	client.disconnect();
    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
    	if(e instanceof IOException){
    		log.info(e.getMessage());
    	}else{
    		log.error(e.getMessage(), e);
    	}
    	ctx.close();
        return true;
    }
}
