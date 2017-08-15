package com.jmr.entity;

/**
 * 功能表
 *
 */
public class Functions extends BaseBean{
    
	private static final long serialVersionUID = 1578453367011881393L;
	
	private Integer id; //流水号	ˇ
	private String name; //功能名称
	private String link; //连接位置
	private Integer sort; //功能排列顺序
	private Integer topfuncid; //上层功能流水号
	private Integer level; //层级
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getTopfuncid() {
		return topfuncid;
	}
	public void setTopfuncid(Integer topfuncid) {
		this.topfuncid = topfuncid;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	
	
}
