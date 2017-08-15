package ssh.hibernate.dao.impl;

import java.util.List;

import ssh.hibernate.dao.IBaseDao;
import ssh.hibernate.dao.ITransManager;






/**
 * 类说明  事务管理
 */
public class TransManagerImpl implements ITransManager{
	
	private IBaseDao baseDao;//基础服务
	//setter注入
	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}
	

	/**
	 * 批量保存
	 * @param list
	 */
	public void batchSave(List list){
		baseDao.save(list);
	}


	

	


}
