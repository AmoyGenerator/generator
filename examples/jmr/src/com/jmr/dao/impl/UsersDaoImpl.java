package com.jmr.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ssh.hibernate.dao.IBaseDao;
import ssh.hibernate.page.ListPage;

import com.jmr.dao.IUsersDao;
import com.jmr.entity.Users;

/**
 * 管理者账号Dao实现类
 *
 */
public class UsersDaoImpl implements IUsersDao{
	private IBaseDao baseDao;

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void del(Integer[] ids){
		baseDao.delete(ids,"Users", "id");
	}
	
	/**
	 * 取得对象，若没有，返回null
	 * @param account
	 * @param password
	 * @return
	 */
	public Users get(String account,String password){
		String hql = "SELECT u FROM Users u WHERE u.account=:account AND u.password=:password";
		Map map = new HashMap();
		map.put("account", account);
		map.put("password", password);
		List list = baseDao.find(hql, map);
		if(list != null && !list.isEmpty()){
			return (Users) list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 查询功能表
	 * @return
	 */
	public List findFunctions(){
		String hql = " FROM Functions f WHERE f.level=1 ORDER BY f.sort ASC ";
		List list = baseDao.find(hql);
		return list;
	}
	
	/**
	 * 查询功能表2
	 * @param topfuncid
	 * @return
	 */
	public List findFunctions2(String topfuncid){
		String hql = " FROM Functions f WHERE f.level=2 ";
		if(topfuncid!=null && topfuncid != ""){
			hql = hql + " AND f.topfuncid = "+topfuncid+" ";
		}
		hql = hql + " ORDER BY f.sort ASC ";
		List list = baseDao.find(hql);
		return list;
	}
	
	
	/**
	 * 查询
	 * @param pageNo
	 * @param pageCount
	 * @param account
	 * @param name
	 * @return
	 */
	public ListPage find(int pageNo, int pageCount, String account, String name) {
		
		StringBuffer hqlStr=new StringBuffer();
		StringBuffer countStr=new StringBuffer();
		
		hqlStr.append("FROM Users u WHERE 1 = 1 ");
		countStr.append("SELECT COUNT(u.id) FROM Users u WHERE 1 = 1 ");
		
		Map<String,String> map = new HashMap<String,String>();
		
		if(account != null && !account.equals("")){
			hqlStr.append("AND u.account LIKE :account ");
			countStr.append("AND u.account LIKE :account ");
			map.put("account", "%" + account + "%");	
		}		
		if(name != null && !name.equals("")){
			hqlStr.append("AND u.name LIKE :name ");
			countStr.append("AND u.name LIKE :name ");
			map.put("name", "%" + name + "%");	
		}		

		hqlStr.append("ORDER BY u.createDT DESC ");
		
		return baseDao.queryHql(pageNo, pageCount, countStr.toString(), hqlStr.toString(), map);
	}
	
	
	/**
	 * 验证账号唯一性
	 * @param account
	 * @return
	 */
	public boolean isAccountExist(String account){
		boolean b = false;
		String sql = "SELECT u.id FROM Users u WHERE u.account = :account";
		Map<String,String> map = new HashMap<String,String>();
		map.put("account", account);
		List<?> list = baseDao.find(sql, map);
		if(list != null && !list.isEmpty()){
			b = true;
		}
		return b;
	}
	
	/**
	 * 保存
	 * @param user
	 */
	public void save(Users user){
		baseDao.save(user);
	}
	
	/**
	 * 修改
	 * @param user
	 */
	public void update(Users user){
		baseDao.update(user);
	}
	
	
	/**
	 * 取得对象
	 * @param user
	 */
	public Users get(Integer id){
		return (Users) baseDao.get(Users.class, id);
	}
	
	
	/**
	 * 单笔删除
	 * @param user
	 */
	public void delete(Integer id){
		baseDao.delete(Users.class, id);
	}
	
	/**
	 * 取到Users单个对象
	 * @param account
	 * @return
	 */
	public Users getUsers(String account){
		String hql = "SELECT u FROM Users u WHERE u.account=:account ";
		Map map = new HashMap();
		map.put("account", account);
		List list = baseDao.find(hql, map);
		if(list != null && !list.isEmpty()){
			return (Users) list.get(0);
		}else{
			return null;
		}
	}
	

}
