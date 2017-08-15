package com.jmr.service;

import java.util.Map;

import ssh.hibernate.page.ListPage;

public interface IBeanService {
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageCount
	 * @param queryMap
	 * @return
	 */
	public ListPage find(int pageNo, int pageCount, Map<String, Object> queryMap);
	
	/**
	 * 取得单一对象
	 * @param id
	 * @return
	 */
	public Object getById(Integer id);
	
	/**
	 * 保存
	 * @param news
	 */
	public void save(Object object);
	
	/**
	 * 修改
	 * @param news
	 */
	public void update(Object object);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(Integer id);
	
}
