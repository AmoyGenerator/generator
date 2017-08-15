package com.jmr.dao;

import java.util.Map;

import ssh.hibernate.page.ListPage;

public interface IBeanDao {
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageCount
	 * @param code
	 * @param status
	 * @param subjectCE
	 * @param startDate
	 * @param endDate
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
	 * 删除
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 保存
	 * @param news
	 */
	public void save(Object object);
	
	/**
	 * 修改
	 * @param news
	 */
	public void update(Object news);
}
