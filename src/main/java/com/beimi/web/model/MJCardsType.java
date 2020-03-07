package com.beimi.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_game_mjcardstype")
@org.hibernate.annotations.Proxy(lazy = false)
public class MJCardsType implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1115593425069549681L;
	
	private String id ;
	private String name ;
	private String code ;
	private Date createtime ;
	
	private String game ;			//游戏类型，  麻将
	private String creater;
	private String username ;
	private Date updatetime ;
	private String parentid ;		//父级ID ， 分类ID
	
	private String cardstypes ;		//胡牌要求的牌型，胡牌牌型的基本要求
	
	
	private String gang ;			//必须有|不得有|无要求
	private String peng ; 			//必须有|不得有|无要求
	private String chi	;			//必须有|不得有|无要求
	
	private String pair ;			//必须有将|不得有将|全部是将|无要求
	
	private boolean self ;			//必须自摸
	
	private boolean last ; 			//海底捞
	private boolean lastgang ;		//杠上花
	
	private int rate 	;	
		
	private String orgi ;
	
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
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOrgi() {
		return orgi;
	}
	public void setOrgi(String orgi) {
		this.orgi = orgi;
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
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getGang() {
		return gang;
	}
	public void setGang(String gang) {
		this.gang = gang;
	}
	public String getPeng() {
		return peng;
	}
	public void setPeng(String peng) {
		this.peng = peng;
	}
	public String getChi() {
		return chi;
	}
	public void setChi(String chi) {
		this.chi = chi;
	}
	public String getPair() {
		return pair;
	}
	public void setPair(String pair) {
		this.pair = pair;
	}
	public boolean isSelf() {
		return self;
	}
	public void setSelf(boolean self) {
		this.self = self;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	public boolean isLastgang() {
		return lastgang;
	}
	public void setLastgang(boolean lastgang) {
		this.lastgang = lastgang;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getCardstypes() {
		return cardstypes;
	}
	public void setCardstypes(String cardstypes) {
		this.cardstypes = cardstypes;
	}
}
