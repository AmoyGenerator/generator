package com.jmr.entity;

/**
 * 会员基本资料
 *
 */
public class Member extends BaseBean{
	
	public static final String STATUS_YES = "Y";//启用
	public static final String STATUS_NO = "N";//停用
	
	private Integer id; //流水号	ˇ
	private String loginId; //登入帐号
	private String password; //密码  材DES加密储存
	private String name; //姓名
	private String email; //E-Mail
	private String epaper; //订阅电子报  Y:是 N:否
	
	private String status; //状态  Y:启用 N:停用
	private String surplusFilmNum = "0";	//剩余影片点数   默认为0
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEpaper() {
		return epaper;
	}
	public void setEpaper(String epaper) {
		this.epaper = epaper;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSurplusFilmNum() {
		return surplusFilmNum;
	}
	public void setSurplusFilmNum(String surplusFilmNum) {
		this.surplusFilmNum = surplusFilmNum;
	}
	
	
}
