package com.jmr.entity;

/**
 * 管理者账号
 *
 */
public class Users extends BaseBean{
    
	private static final long serialVersionUID = 1578453367011881393L;
	
	private Integer id; //流水号	ˇ
	private String account; //管理者账号
	private String password; //密码 DES加密
	private String name; //姓名
	private Integer status; //状态  0-停用, 1-启用
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
