package ssh.hibernate.dao;

import java.util.List;

/**
 * 类说明:事务管理接口
 */
public interface ITransManager {
	
	/**
	 * 批量保存
	 * @param list
	 */
	void batchSave(List list);

	
}
