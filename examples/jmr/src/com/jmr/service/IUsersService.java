package com.jmr.service;

import java.util.List;
import java.util.Map;

import ssh.hibernate.page.ListPage;

import com.jmr.entity.Functions;
import com.jmr.entity.Users;

/**
 * 管理者账号Service接口
 *
 */
public interface IUsersService {
	
	/**
	 * 取得对象，若沒有，返回null
	 * @param account
	 * @param password
	 * @return
	 */
	 Users get(String account,String password);
	
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
	 * 批量删除
	 * @param ids
	 */
	 void del(Integer[] ids);
	
	
	/**
	 * 单笔删除
	 * @param user
	 */
	 void delete(Integer id);
	
	
	/**
	 * 查询
	 * @param pageNo
	 * @param pageCount
	 * @param account
	 * @param name
	 * @return ListPage
	 */
	 ListPage find(int pageNo, int pageCount, String account, String name);
	
	
	/**
	 * 判断账号是否存在
	 * @param account
	 * @return
	 */
	 boolean isAccountExist(String account);
	
	/**
	 * 根据账号，取得Functions信息 
	 * @param account
	 * @return Map
	 */
	public Map<Functions,List> getFunctions();
	
	/**
	 * 取到Users单个对象
	 * @param account
	 * @return
	 */
	public Users getUsers(String account);
	
}
