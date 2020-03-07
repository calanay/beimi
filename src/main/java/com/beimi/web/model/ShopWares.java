package com.beimi.web.model;

import java.util.ArrayList;
import java.util.List;

public class ShopWares implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name ;
	private String code ;
	private String tag ;
	private String status ;
	
	private List<Wares> wares = new ArrayList<Wares>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<Wares> getWares() {
		return wares;
	}

	public void setWares(List<Wares> wares) {
		this.wares = wares;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
