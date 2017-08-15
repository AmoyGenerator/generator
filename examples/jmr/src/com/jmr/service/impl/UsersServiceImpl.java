package com.jmr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashMap;

import ssh.hibernate.page.ListPage;

import com.jmr.dao.IUsersDao;
import com.jmr.entity.Functions;
import com.jmr.entity.Users;
import com.jmr.service.IUsersService;

/**
 * 管理者账号Service实现类
 *
 */
public class UsersServiceImpl implements IUsersService{
	private IUsersDao usersDao;

	public void setUsersDao(IUsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	/**
	 * 取得对象，若沒有，返回null
	 * @param account
	 * @param password
	 * @return
	 */
	public Users get(String account,String password){
		return usersDao.get(account, password);
	}
	
	/**
	 * 保存
	 * @param user
	 */
	public void save(Users user){
		usersDao.save(user);
	}
	
	/**
	 * 修改
	 * @param user
	 */
	public void update(Users user){
		usersDao.update(user);
	}
	
	
	/**
	 * 取得对象
	 * @param user
	 */
	public Users get(Integer id){
		return usersDao.get(id);
	}
	
	
	/**
	 * 单笔删除
	 * @param user
	 */
	public void delete(Integer id){
		usersDao.delete(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void del(Integer[] ids){
		usersDao.del(ids);
	}
	
	/**
	 * 查询
	 * @param pageNo
	 * @param pageCount
	 * @param account
	 * @param name
	 * @return ListPage
	 */
	public ListPage find(int pageNo, int pageCount, String account, String name) {
		return usersDao.find(pageNo, pageCount, account, name);
	}
	
	
	/**
	 * 判断账号是否存在
	 * @param account
	 * @return
	 */
	public boolean isAccountExist(String account){
		return usersDao.isAccountExist(account);
	}
	
	/**
	 * 根据账号，取得Functions信息
	 * @param account
	 * @return Map
	 */
	public Map<Functions,List> getFunctions(){
		Map<Functions,List> map = new LinkedHashMap<Functions,List>();
		Integer parentId = -1;
		List list = usersDao.findFunctions();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Functions functions = (Functions) iterator.next();
			map.put(functions, usersDao.findFunctions2(String.valueOf(functions.getId())));
		}
		return map;
	}
	
	/**
	 * 取到Users单个对象 
	 * @param account
	 * @return
	 */
	public Users getUsers(String account) {
		
		return usersDao.getUsers(account);
	}
	
}
