package com.beimi.web.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "bm_game_groupitem")
@org.hibernate.annotations.Proxy(lazy = false)
public class GamePlaywayGroupItem implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2593399616448881368L;
	private String id ;
	private String name ;
	private String code ;
	private String title ;
	private String groupid;
	private String memo ;
	private String playwayid ;
	private String game ;
	private String orgi ;
	private String status ;

	private String type ;
	private boolean defaultvalue ;
	private String value ;
	private String creater ;
	private Date createtime = new Date();
	private Date updatetime = new Date();
	private int sortindex ;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgi() {
		return orgi;
	}

	public void setOrgi(String orgi) {
		this.orgi = orgi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPlaywayid() {
		return playwayid;
	}

	public void setPlaywayid(String playwayid) {
		this.playwayid = playwayid;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(boolean defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public int getSortindex() {
        return sortindex;
    }

    public void setSortindex(int sortindex) {
        this.sortindex = sortindex;
    }

}
