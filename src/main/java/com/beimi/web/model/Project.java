package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bm_project")
@org.hibernate.annotations.Proxy(lazy = false)
public class Project implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
    
	private String id;                   //主键
	private String name;                 //项目名称
	private String code;                 //代码
	private String contexturl;           //上下文路径
	private boolean multilang;           //是否支持多语言
	private String demo;                 //备注	, 变更用处，用于记录用户选择的缺省  框架模板
	private String orgi;                 //租户
	private String packagename;
	private String description ;		//项目描述信息
	private String dbid ;
	private String creater ;
	private Date createtime ;
	private Date updatetime ;
	private String teamid ;				//团队ID
	private boolean custom ;
	private boolean accessafterlogin;
	private String params ;
	private String toptemplet ;
	private String lefttemplet ;
	private String color ;				//颜色标识
	
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
	public String getDemo() {
		return demo;
	}
	public void setDemo(String demo) {
		this.demo = demo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getContexturl() {
		return contexturl;
	}
	public void setContexturl(String contexturl) {
		this.contexturl = contexturl;
	}
	public boolean isMultilang() {
		return multilang;
	}
	public void setMultilang(boolean multilang) {
		this.multilang = multilang;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
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
	public boolean isCustom() {
		return custom;
	}
	public void setCustom(boolean custom) {
		this.custom = custom;
	}
	public boolean isAccessafterlogin() {
		return accessafterlogin;
	}
	public void setAccessafterlogin(boolean accessafterlogin) {
		this.accessafterlogin = accessafterlogin;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getToptemplet() {
		return toptemplet;
	}
	public void setToptemplet(String toptemplet) {
		this.toptemplet = toptemplet;
	}
	public String getLefttemplet() {
		return lefttemplet;
	}
	public void setLefttemplet(String lefttemplet) {
		this.lefttemplet = lefttemplet;
	}

	public String getDescription() {
		return description;
	}

	public String getTeamid() {
		return teamid;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
