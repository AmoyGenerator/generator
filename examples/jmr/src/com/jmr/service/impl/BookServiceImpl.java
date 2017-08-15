package com.jmr.service.impl;

import java.util.Map;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import ssh.hibernate.page.ListPage;
import com.jmr.dao.IBookDao;
import com.jmr.service.IBookService;

/**
 * Service implementation class
 *
 */
 public class BookServiceImpl implements IBookService{
 	private IBookDao bookDao;
	private TransactionTemplate transactionTemplate;
	
	//Transaction
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public IBookDao getBookDao() {
		return bookDao;
	}
	
	public void setBookDao(IBookDao bookDao) {
		this.bookDao = bookDao;
	}
		
	/**
	 * Paging query
	 * @param pageNo
	 * @param pageCount
	 * @param queryMap
	 * @return
	 */
	public ListPage find(int pageNo, int pageCount, Map queryMap) {
		return bookDao.find(pageNo, pageCount, queryMap);
	}
	
	/**
	 * Get object by id
	 * @param id
	 * @return
	 */
	 public Object getById(Integer id) {
		return bookDao.getById(id);
	}
	
	/**
	 * delete
	 * @param id
	 */
	 public void delete(final Integer id) {
		transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				bookDao.delete(id);
				return null;
			}
		});
	}
		
	/**
	 * save
	 * @param news
	 */
	 public void save(Object object) {
		bookDao.save(object);
	}
	
	/**
	 * update
	 * @param news
	 */
	 public void update(Object object) {
		bookDao.update(object);
	}
	
}
 
 