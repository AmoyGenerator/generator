package com.jmr.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ssh.hibernate.page.ListPage;

import com.jmr.entity.Users;

/**
 * 管理者账号Dao接口
 *
 */
public interface IUsersDao {
	
	/**
	 * 取得对象，若沒有，返回null
	 * @param account
	 * @param password
	 * @return
	 */
	public Users get(String account,String password);
	
	/**
	 * 查询功能表
	 * @return
	 */
	public List findFunctions();
	
	/**
	 * 查询功能表2
	 * @param topfuncid
	 * @return
	 */
	public List findFunctions2(String topfuncid);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	 void del(Integer[] ids);
	 
	/**
	 * 查询
	 * @param pageNo
	 * @param pageCount
	 * @param account
	 * @param name
	 * @return
	 */
	 ListPage find(int pageNo, int pageCount, String account, String name);
		
		
		/**
	 * 验证账号唯一性
	 * @param account
	 * @return
	 */
	 boolean isAccountExist(String account);
		
	/**
	 * 保存
	 * @param user
	 */
	 void save(Users user);
		
	/**
	 * 修改
	 * @param user
	 */
	 void update(Users user);
		
		
	/**
	 * 取得对象
	 * @param user
	 */
	 Users get(Integer id);
		
		
	/**
	 * 单笔删除
	 * @param user
	 */
	 void delete(Integer id);
	 
	/**
	 * 取到Users单个对象
	 * @param account
	 * @return
	 */
	public Users getUsers(String account);
	
}
