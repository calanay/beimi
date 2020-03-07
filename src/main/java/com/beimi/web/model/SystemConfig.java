package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_systemconfig")
@org.hibernate.annotations.Proxy(lazy = false)
public class SystemConfig implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8675632756915176657L;
	private String id ;
	private String name ;
	private String title ;	
	
	private String theme = "01";	//默认绿色
	
	private String code ;
	private String orgi ;
	private String description;
	private String memo ;
	private String creater;
	private Date createtime;
	private Date updatetime;
	private String loglevel ;
	private boolean enablessl ;
	private String jksfile ;
	private String jkspassword ;
	private String mapkey ;
	private boolean workorders ;	
	
	private boolean callout ;	
	private boolean auth ;			//启用权限控制
	
	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getLoglevel() {
		return loglevel;
	}
	public void setLoglevel(String loglevel) {
		this.loglevel = loglevel;
	}
	
	public boolean isEnablessl() {
		return enablessl;
	}
	public void setEnablessl(boolean enablessl) {
		this.enablessl = enablessl;
	}
	public String getJksfile() {
		return jksfile;
	}
	public void setJksfile(String jksfile) {
		this.jksfile = jksfile;
	}
	public String getJkspassword() {
		return jkspassword;
	}
	public void setJkspassword(String jkspassword) {
		this.jkspassword = jkspassword;
	}
	
	public String getMapkey() {
		return mapkey;
	}
	public void setMapkey(String mapkey) {
		this.mapkey = mapkey;
	}
	
	public boolean isWorkorders() {
		return workorders;
	}
	public void setWorkorders(boolean workorders) {
		this.workorders = workorders;
	}
	
	public boolean isCallout() {
		return callout;
	}
	public void setCallout(boolean callout) {
		this.callout = callout;
	}
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
