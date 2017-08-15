package ssh.hibernate.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import ssh.hibernate.page.ListPage;



/** 
 * 类说明 ：DAO接口
 */
public interface IBaseDao {
	
	/**
	 * 储存
	 * @param object
	 */
	public void save(Object object);	
	
	/**
	 * 批次储存
	 * @param objectList
	 */
	public void save(List objectList);

	/**
	 * 更新
	 * @param object
	 */
	public void update(Object object);
	
	/**
	 * 储存活更新
	 * 
	 * @param object
	 */
	void saveOrUpdate(Object object);

	/**
	 * hql语句更新
	 * @param queryString sql语句
	 */
	public void updateByHql(final String queryString);
	
	/**
	 * hql语句更新
	 * @param queryString sql语句
	 * @param parameters 参数
	 */
	public void updateByHql(final String queryString, final Object[] parameters);
	
	/**
	 * hql语句更新
	 * @param queryString	sql语句        
	 * @param parameters	参数      
	 */
	public void updateByHql(final String queryString, final Map parameters);
	
	/**
	 * sql语句更新
	 * @param queryString sql语句
	 * @param parameters 参数
	 */
	public void updateBySql(final String queryString, final Object[] parameters);
	
	/**
	 * sql语句更新
	 * @param queryString	sql语句      
	 * @param parameters	参数     
	 */
	public void updateBySql(final String queryString, final Map parameters);

	/**
	 * del
	 * @param object
	 */
	public void delete(Object object);
	
	
	/**
	 * 根据对象del
	 * @param clazz 对象类型
	 * @param id 主建
	 */
	public void delete(Class clazz, Serializable id);

	/**
	 * 根据类型删除全部物件
	 * @param clazz		对象类型  
	 * @return 
	 */
	public void deleteAll(final Class clazz);

	/**
	 * 根据类型删除
	 * @param queryString		Hql查询语句         
	 * @param parameters		参数        
	 * @return 
	 */
	public void deleteByHql(final String queryString, final Object[] parameters);
	
	/**
	 * hql语句删除
	 * @param queryString		hql语句   
	 * @param parameters		参数        
	 * @return Integer
	 */
	public void deleteByHql(final String queryString, final Map parameters);
	

    /**
     * 单条删除
     * @param className 类别
     * @param dwKey 主键名称
     * @param id 主键值
     */
	public void delete(final String className, final String dwKey,final String id);
	

	/**
	 * 批次删除 delete from javaBeanName where id not in (: ids)
	 * 
	 * @param ids
	 * @param javaBeanName
	 * @param id
	 * @return
	 */
	void deleteIdNotIn(final Object[] ids, final String javaBeanName,final String id, final String coditions);
	
	/**
	 * 批次删除 delete from javaBeanName where id in (: ids)
	 * 
	 * @param ids
	 * @param javaBeanName
	 * @param id
	 * @return
	 */
	public void deleteIdIn(final Object[] ids, final String javaBeanName,final String id, final String coditions);
	
	/**
	 * 批次删除
	 * delete from javaBeanName where id in (: ids)
	 * @param ids			
	 * @param javaBeanName	
	 * @param id			
	 * @return				
	 */
	public void delete(final Object[] ids, final String javaBeanName, final String id);
	
	/**
	 * 批次删除
	 * delete from javaBeanName where id in (: ids)
	 * @param ids			
	 * @param javaBeanName	
	 * @param id			
	 * @return			
	 */
    public void delete(final List ids, final String javaBeanName, final String id);
	
	
	/**
	 * 取得某个类型的全部物件列表
	 * 
	 * @param clazz 类型   
	 * @return 物件集合
	 */
	public List getList(Class clazz);

	/**
	 * 根据类型和对象id载入
	 * @param clazz		类型    
	 * @param id		对象id      
	 * @return 目标物件
	 */
	public Object load(Class clazz, Serializable id);

	/**
	 * 根据类型和对象id从资料库取得对象
	 * @param clazz	类型    
	 * @param id		对象id        
	 * @return 目标物件
	 */
	public Object get(Class clazz, Serializable id);

	/**
	 * 根据查询语句和查询参数从资料库取得一个对象
	 * @param queryString	查询语句           
	 * @param parameters	参数          
	 * @return Object 单个对象
	 */
	public Object get(final String queryString, final Object[] parameters);

	/**
	 * 命名查询
	 * @param queryName		命名查询语句        
	 * @return 对象列表
	 */
	public List findByNamedQuery(final String queryName);

	/**
	 * 依据单个参数做命名查询
	 * @param query				命名查询语句          
	 * @param parameter			单个查询参数   
	 * @return 对象列表
	 */
	public List findByNamedQuery(final String queryName, final Object parameter);

	/**
	 * 依据参数阵列做命名查询
	 * @param query  		命名查询语句        
	 * @param parameters  	查询参数阵列       
	 * @return 对象列表
	 */
	public List findByNamedQuery(final String queryName, final Object[] parameters);

	/**
	 * 查询全部(hql)
	 * @param query  查询语句    
	 * @return 对象列表
	 */
	public List find(final String queryString);

	/**
	 * 带参数查询全部(hql)
	 * @param queryString  查询语句   
	 * @param parameters   查询参数   
	 * @return 对象列表
	 */
	public List find(String queryString, Object[] parameters);
	
	
	
	
	
	/**
	 * 带参数查询全部(hql)
	 * @param queryString  查询语句 
	 * @param parameters   查询参数     
	 * @return 对象列表
	 */
	public List find(final String queryString, final Map parameters);
	
	/**
	 * 带参数查询全部(hql)
	 * @param queryString  查询语句 
	 * @param parameters   查询参数  
	 * @param cache   是否用缓存    
	 * @return 对象列表
	 */
	public List find(final String queryString, final Map parameters,final boolean cache);
	
	/**
	 * 带参数查询全部 (sql语句)
	 * @param queryString	查询语句
	 * @param parameters	查询参数
	 * @return    			对象列表
	 */
	public List findBySql (final String queryString, final Object[] parameters);
	
	/**
	 * 带参数查询全部 (sql语句)
	 * @param queryString	查询语句
	 * @param parameters	查询参数
	 * @return    			对象列表
	 */
	public List findBySql (final String queryString, final Map parameters);

	/**
	 * 分页查询(hql查询)
	 * @param pageNo		页数		
	 * @param pageSize		一页几笔资料
	 * @param countSql		查询总数语句
	 * @param querySql		查询语句
	 * @param parameters	参数
	 * @return 			LisPage 分页封装
	 */
	public ListPage queryHql(int pageNo, int pageSize, String countSql, String querySql, Map parameters);
	

	
	/**
	 * 分页查询(sql查询)
	 * @param pageNo		页数		
	 * @param pageSize		一页几条记录
	 * @param countSql		查询总数语句
	 * @param querySql		查询语句
	 * @param parameters	参数
	 * @return 			LisPage 分页封装
	 */
	public ListPage querySql(int pageNo, int pageSize, String countSql, String querySql, Map parameters);

	/** 
	 * 依参数查询资料(SQL)，前几笔
	 * @param sql			查询语法
	 * @param values		参数值(Object[])
	 * @param firstResult	从第几笔抓取
	 * @param maxResults	该页show几笔
	 * @param clazz			类别
	 * @param sqlName
	 * @return 资料列表
	 */
	public List findBySql(final String sql,final Object[] values, final int firstResult, final int maxResults, final Class clazz, final String sqlName);
	
	/**
	 *  依参数查询资料(Sql),显示前几笔
	 * @param sql  			查询语法
	 * @param values  		参数值(Object[]) 
	 * @param firstResult	从第几笔开始
	 * @param maxResults 	该页show出多少笔
	 * @return    			资料列表
	 */
	public List findBySql(final String sql, final Object[] values,final int firstResult, final int maxResults);
	
	/** 
	 * 依参数查询资料(hql)，显示前几笔
	 * @param hql			查询语法
	 * @param values		参数值(Object[])
	 * @param firstResult	从第几笔抓取
	 * @param maxResults	该页show几笔
	 * @param clazz			类别
	 * @param sqlName
	 * @return 资料列表
	 */
	public List findByHql(final String hql,final Object[] values,final int firstResult,final int maxResults);
	
    /**
	 * 取得总数(hql)  
	 * @param hql  查询语法     
	 * @return 总笔数
	 */
	public int getCount(String hql);
    
	/**
	 * 依参数查询，取得总数	(hql)
	 * @param hql  		查询语法 
	 * @param values   	参数值(Object[])       
	 * @return 总笔数
	 */
	public int getCount(final String hql, final Object[] values);
	
	/**
	 * 依参数查询资料笔数(sql) 
	 * @param sql  		查询语法=
	 * @param values    参数值(Object[])     
	 * @return 总笔数 
	 */
	public int getCountBySql(final String sql, final Object[] values);
	

	/**
	 * 取得hibernateTemplate
	 * @return
	 */
    public HibernateTemplate getHibernateTemplate();
    
    /**
	 * 随机取得记录 (sql语句)
	 * @return 对象列表
	 */
	public List getRecordRandomly(final String queryString, final Map parameters);
	
    /**
     * 取得session
     * @return
     */
	public Session getHibernateSession();
}
