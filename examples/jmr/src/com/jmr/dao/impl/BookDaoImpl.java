package com.jmr.dao.impl;

import java.util.Map;
import ssh.hibernate.dao.IBaseDao;
import ssh.hibernate.dao.QuerySqlManager;
import ssh.hibernate.page.ListPage;
import com.jmr.dao.IBookDao;
import com.jmr.entity.Book;

/**
 * Dao implementation class
 *
 */

public class BookDaoImpl implements IBookDao{
	
	private IBaseDao baseDao;

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	/**
	 * 
	 * @param pageNo 
	 * @param pageCount
	 * @param queryMap 
	 * @return
	 */
	public ListPage find(int pageNo, int pageCount, Map queryMap) {
		return QuerySqlManager.query(baseDao, Book.class, pageNo, pageCount, queryMap);
	}
	
	/**
	 * Get Object
	 * @param id
	 * @return
	 */
	public Book getById(Integer id) {
		return (Book)baseDao.get(Book.class, id);
	}
		
	/**
	 * Delete
	 * @param id
	 */
	public void delete(Integer id) {
		baseDao.delete(Book.class, id);
	}
	
	/**
	 * Save
	 */
	public void save(Object object) {
		baseDao.save(object);
	}
	
	/**
	 * Update
	 */
	public void update(Object object) {
		baseDao.update(object);
	}

}

