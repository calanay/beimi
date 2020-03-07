package com.beimi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import com.beimi.core.BMDataContext;


@EnableAutoConfiguration
@SpringBootApplication
@EnableAsync
@EnableJpaRepositories("com.beimi.web.service.repository.jpa")
@EnableElasticsearchRepositories("com.beimi.web.service.repository.es")
/**
 * export PATH="/usr/local/jdk8/bin":$PATH
 * nohup java -Xms1240m -Xmx1240m -Xmn450m -XX:PermSize=512M -XX:MaxPermSize=512m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseTLAB  -XX:NewSize=128m  -XX:MaxNewSize=128m -XX:MaxTenuringThreshold=0  -XX:SurvivorRatio=1024 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=60  -Djava.awt.headless=true -XX:+PrintGCDetails  -Xloggc:gc.log  -XX:+PrintGCTimeStamps  -jar beimi-0.8.0.war --server.port=12345 --spring.profiles.active=develop >/data/web_logs/game/beimi.log &
 * 
 * @author
 */
public class Application {
    
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		BMDataContext.setApplicationContext(springApplication.run(args));
	}
	
}