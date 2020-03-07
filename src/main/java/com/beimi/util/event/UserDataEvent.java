package com.beimi.util.event;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户数据事件
 * 
 * @author
 *
 */
@SuppressWarnings("rawtypes")
public class UserDataEvent{
	private long id ;

	private UserEvent event ;
	private String command ;
	
	private ElasticsearchCrudRepository esRes ;
	private JpaRepository dbRes ;
	
	public ElasticsearchCrudRepository getEsRes() {
		return esRes;
	}
	public void setEsRes(ElasticsearchCrudRepository esRes) {
		this.esRes = esRes;
	}
	public JpaRepository getDbRes() {
		return dbRes;
	}
	public void setDbRes(JpaRepository dbRes) {
		this.dbRes = dbRes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserEvent getEvent() {
		return event;
	}

	public void setEvent(UserEvent event) {
		this.event = event;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
