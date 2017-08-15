package com.jmr.entity;

/**
 * 代码资料
 *
 */
public class Codedesc extends BaseBean{
    
	private static final long serialVersionUID = 1578453367011881393L;
	
	private String type; //类别代码	ˇ
	private String code; //项目代码
	private String name; //项目名称
	private Integer sort; //排序
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	
}
