package com.beimi.web.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bm_wares")
@org.hibernate.annotations.Proxy(lazy = false)
public class Wares implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1115593425069549681L;
	
	private String id ;
	private String name ;
	private String code ;
	
	/**
	 * 之所以将 商品和SKU分开，是因为，处理金币、钻石、房卡以外的，某些有限量的商品需要有库存限制，例如兑换的商品，先到先得
	 * 通过SKU来控制库存，能够更好的管理商城里的商品销售，此外，根据租户自己选择销售的商品 ， 例如：
	 * 几点到几点卖什 么、卖的东西轮流在切换；什么日子特别卖什么；每周的什么时候卖什么，当玩家满足某 个条件的时候，可以购买什么
	 * 再例如：玩家有超级贝卡的时候，可以8折的价格购买商城内的所有物品
	 * 
	 * 
	 */
	private int price ;			//建议价格 ， 由SKU定价和库存
	private int stock ;
	
	private int quantity ;		//兑换数量，1钻石兑换 5000 金币数量
	private String imageurl ;
	private String warestype;
	
	private String recomtype ;
	
	private String pmethod ;	//购买方式
	private String shopid ;		//店铺ID ， 备用
	private int spu ;			//SPU销量
	private String payment ;	//支付方式
	
	private int inx;			//商品排序位置
	
	private Date createtime ;
	private String creater;
	private String username ;
	
	private Date starttime;
	private Date endtime ;
	
	
	private String description	;//商品备注描述信息
	
	private Date updatetime ;
	private String orgi ;
	
	private String status ;		//状态，启用，禁用
	
	private boolean enablesku ;		//是否启用SKU
	
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
	public int getInx() {
		return inx;
	}
	public void setInx(int inx) {
		this.inx = inx;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getWarestype() {
		return warestype;
	}
	public void setWarestype(String warestype) {
		this.warestype = warestype;
	}
	public String getPmethod() {
		return pmethod;
	}
	public void setPmethod(String pmethod) {
		this.pmethod = pmethod;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public int getSpu() {
		return spu;
	}
	public void setSpu(int spu) {
		this.spu = spu;
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
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getRecomtype() {
		return recomtype;
	}
	public void setRecomtype(String recomtype) {
		this.recomtype = recomtype;
	}
	public boolean isEnablesku() {
		return enablesku;
	}
	public void setEnablesku(boolean enablesku) {
		this.enablesku = enablesku;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
