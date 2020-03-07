package com.beimi.config.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.beimi.config.web.model.Game;
import com.beimi.core.statemachine.BeiMiStateMachine;
import com.beimi.core.statemachine.impl.BeiMiMachineHandler;

/**
 * 游戏状态机处理实现配置类
 *
 * @author
 *
 */
@Configuration
public class BeiMiStateMachineHandlerConfig {
	
	@Resource(name="dizhu")    
	private BeiMiStateMachine<String,String> dizhuConfigure ;
	
	@Resource(name="majiang")    
	private BeiMiStateMachine<String,String> maJiangConfigure ;
	
    @Bean("dizhuGame")
    public Game dizhu() {
        return new Game(new BeiMiMachineHandler(this.dizhuConfigure));
    }
    
    @Bean("majiangGame")
    public Game majiang() {
        return new Game(new BeiMiMachineHandler(this.maJiangConfigure));
    }
}