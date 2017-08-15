package ssh.hibernate.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ssh.hibernate.dao.IBaseDao;
import ssh.hibernate.page.CommQueryCondition;
import ssh.hibernate.page.ListPage;





/**
 * 类别说明:基础服务实现类
 */
public class BaseDaoImpl extends HibernateDaoSupport implements
		IBaseDao {

	/**
	 * 储存
	 * 
	 * @param object
	 */
	public void save(Object object) {
		this.getHibernateTemplate().save(object);
	}

	/**
	 * 批量储存
	 * 
	 * @param objectList
	 */
	public void save(List objectList) {
		if (objectList != null) {
			for (Iterator it = objectList.iterator(); it.hasNext();) {
				this.save(it.next());
			}
		}
	}

	/**
	 * 更新
	 * 
	 * @param object
	 */
	public void update(Object object) {
		this.getHibernateTemplate().update(object);
	}
	
	/**
	 * 储存或更新
	 * 
	 * @param object
	 */
	public void saveOrUpdate(Object object) {
		this.getHibernateTemplate().saveOrUpdate(object);
	}

	/**
	 * hql语句更新
	 * 
	 * @param queryString
	 *            sql语句
	 */
	public void updateByHql(final String queryString) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * hql语句更新
	 * 
	 * @param queryString
	 *            sql语句
	 * @param parameters
	 *            参数
	 */
	public void updateByHql(final String queryString, final Object[] parameters) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * hql语句更新
	 * 
	 * @param queryString
	 *            sql语句
	 * @param parameters
	 *            参数
	 */
	public void updateByHql(final String queryString, final Map parameters) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * sql语句更新
	 * 
	 * @param queryString
	 *            sql语句
	 * @param parameters
	 *            参数
	 */
	public void updateBySql(final String queryString, final Object[] parameters) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(queryString);
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * sql语句更新
	 * 
	 * @param queryString
	 *            sql语句
	 * @param parameters
	 *            参数
	 */
	public void updateBySql(final String queryString, final Map parameters) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(queryString);
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * del
	 * 
	 * @param object
	 */
	public void delete(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	/**
	 * 根据对象del
	 * 
	 * @param clazz
	 *            对象类型
	 * @param id
	 *            主建
	 */
	public void delete(Class clazz, Serializable id) {
		this.getHibernateTemplate().delete(load(clazz, id));
	}

	/**
	 * 根据类型删除全部物件
	 * 
	 * @param clazz
	 *            对象类型
	 * @return
	 */
	public void deleteAll(final Class clazz) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery("DELETE " + clazz.getName());
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * 根据类型删除
	 * 
	 * @param queryString
	 *            Hql查询语句
	 * @param parameters
	 *            参数
	 * @return
	 */
	public void deleteByHql(final String queryString, final Object[] parameters) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * hql语句删除
	 * 
	 * @param queryString
	 *            hql语句
	 * @param parameters
	 *            参数
	 * @return Integer
	 */
	public void deleteByHql(final String queryString, final Map parameters) {
		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * 单条删除
	 * 
	 * @param className
	 *            类名
	 * @param dwKey
	 *            主键名称
	 * @param id
	 *            主键值
	 */
	public void delete(final String className, final String dwKey,
			final String id) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM ").append(className).append(" WHERE ")
						.append(dwKey).append("=:id");
				session.createQuery(sql.toString()).setString("id", id)
						.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 批量删除 delete from javaBeanName where id in (: ids)
	 * 
	 * @param ids
	 * @param javaBeanName
	 * @param id
	 * @return
	 */
	public void delete(final Object[] ids, final String javaBeanName,
			final String id) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM ").append(javaBeanName).append(
						" WHERE ").append(id).append(" IN (:ids)");
				session.createQuery(sql.toString())
						.setParameterList("ids", ids).executeUpdate();
				return null;
			}
		});
	}
	
	/**
	 * 批量删除 delete from javaBeanName where id not in (: ids)
	 * 
	 * @param ids
	 * @param javaBeanName
	 * @param id
	 * @return
	 */
	public void deleteIdNotIn(final Object[] ids, final String javaBeanName,
			final String id, final String coditions) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM ").append(javaBeanName).append(
						" WHERE ").append(id).append(" NOT IN (:ids)").append(coditions);
				session.createQuery(sql.toString())
						.setParameterList("ids", ids).executeUpdate();
				return null;
			}
		});
	}
	
	/**
	 * 批量删除 delete from javaBeanName where id in (: ids)
	 * 
	 * @param ids
	 * @param javaBeanName
	 * @param id
	 * @return
	 */
	public void deleteIdIn(final Object[] ids, final String javaBeanName,
			final String id, final String coditions) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM ").append(javaBeanName).append(
						" WHERE ").append(id).append(" IN (:ids)").append(coditions);
				session.createQuery(sql.toString())
						.setParameterList("ids", ids).executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 批量删除 delete from javaBeanName where id in (: ids)
	 * 
	 * @param ids
	 * @param javaBeanName
	 * @param id
	 * @return
	 */
	public void delete(final List ids, final String javaBeanName,
			final String id) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM ").append(javaBeanName).append(
						" WHERE ").append(id).append(" IN (:ids)");
				session.createQuery(sql.toString())
						.setParameterList("ids", ids).executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 获得某个类型的全部物件列表
	 * 
	 * @param clazz
	 *            类型
	 * @return 物件集合
	 */
	public List getList(Class clazz) {
		return getHibernateTemplate().find("FROM " + clazz.getName());
	}

	/**
	 * 根据类型和对象id载入
	 * 
	 * @param clazz
	 *            类型
	 * @param id
	 *            对象id
	 * @return 目标物件
	 */
	public Object load(Class clazz, Serializable id) {
		return this.getHibernateTemplate().load(clazz, id);
	}

	/**
	 * 根据类型和对象id从资料库取得对象
	 * 
	 * @param clazz
	 *            类型
	 * @param id
	 *            对象id
	 * @return 目标物件
	 */
	public Object get(Class clazz, Serializable id) {
		return this.getHibernateTemplate().get(clazz, id);
	}

	/**
	 * 根据查询语句和查询参数从资料库取得一个对象
	 * 
	 * @param queryString
	 *            查询语句
	 * @param parameters
	 *            参数
	 * @return Object 单个对象
	 */
	public Object get(final String queryString, final Object[] parameters) {
		List list = getHibernateTemplate().find(queryString, parameters);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 命名查询
	 * 
	 * @param queryName
	 *            命名查询语句
	 * @return 对象列表
	 */
	public List findByNamedQuery(final String queryName) {
		return getHibernateTemplate().findByNamedQuery(queryName);
	}

	/**
	 * 依据单个参数做命名查询
	 * 
	 * @param query
	 *            命名查询语句
	 * @param parameter
	 *            单个查询参数
	 * @return 对象列表
	 */
	public List findByNamedQuery(final String queryName, final Object parameter) {
		return getHibernateTemplate().findByNamedQuery(queryName, parameter);
	}

	/**
	 * 依据参数阵列做命名查询
	 * 
	 * @param query
	 *            命名查询语句
	 * @param parameters
	 *            查询参数阵列
	 * @return 对象列表
	 */
	public List findByNamedQuery(final String queryName,
			final Object[] parameters) {
		return getHibernateTemplate().find(queryName, parameters);
	}

	/**
	 * 查询全部(hql)
	 * 
	 * @param query
	 *            查询语句
	 * @return 对象列表
	 */
	public List find(final String queryString) {
		return this.getHibernateTemplate().find(queryString);
	}

	/**
	 * 带参数查询全部(hql)
	 * 
	 * @param queryString
	 *            查询语句
	 * @param parameters
	 *            查询参数
	 * @return 对象列表
	 */
	public List find(String queryString, Object[] parameters) {
		return getHibernateTemplate().find(queryString, parameters);
	}

	/**
	 * 带参数查询全部(hql)
	 * 
	 * @param queryString
	 *            查询语句
	 * @param parameters
	 *            查询参数
	 * @return 对象列表
	 */
	public List find(final String queryString, final Map parameters) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(queryString);
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				return query.list();
			}
		});
	}
	
	/**
	 * 带参数查询全部(hql)
	 * 
	 * @param queryString
	 *            查询语句
	 * @param parameters
	 *            查询参数
	 * @param cache   是否用缓存  
	 * @return 对象列表
	 */
	public List find(final String queryString, final Map parameters, final boolean cache) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(queryString);
				if(cache){
					query.setCacheable(true); //开启查询缓存
				}
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				return query.list();
			}
		});
	}


	/**
	 * 带参数查询全部 (sql語句)
	 * 
	 * @param queryString
	 *            查询语句
	 * @param parameters
	 *            查询参数
	 * @return 对象列表
	 */
	public List findBySql(final String queryString, final Object[] parameters) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(queryString);
				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						query.setParameter(i, parameters[i]);
					}
				}
				return query.list();
			}
		});
	}

	/**
	 * 带参数查询全部 (sql语句)
	 * 
	 * @param queryString
	 *            查询语句
	 * @param parameters
	 *            查询参数
	 * @return 对象列表
	 */
	public List findBySql(final String queryString, final Map parameters) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(queryString);
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				return query.list();
			}
		});
	}

	/**
	 * 分页查询(hql查询)
	 * 
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            一页几条记录
	 * @param countSql
	 *            查询总数语句
	 * @param querySql
	 *            查询语句
	 * @param parameters
	 *            参数
	 * @return LisPage 分页封装
	 */
	public ListPage queryHql(final int pageNo, final int pageSize,
			final String countSql, final String querySql, final Map parameters) {
		return (ListPage) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						CommQueryCondition mqc = new CommQueryCondition();
						mqc.setPrepareHql(querySql);
						mqc.setCountHql(countSql);
						mqc.setConditions(parameters);
						// Session session = getSession();
						ListPage listPage = null;
						try {
							String hql = mqc.getPrepareHql();
							if (hql != null) {
								
								Query query = session.createQuery(mqc
										.getCountHql());
								mqc.launchParamValues(query);
								List list = query.list();
								String totalCountStr = (list.get(0)).toString();
								int totalCount = Integer
										.parseInt(totalCountStr);
								int totalPageCount = (totalCount -1)/pageSize + 1; //总页数
								int tempPageNo = pageNo;
								if(pageNo > totalPageCount){
									tempPageNo = totalPageCount;
								}
								int firstResultIndex = (tempPageNo - 1) * pageSize;
								if (totalCount == 0
										|| firstResultIndex > totalCount) {
									listPage = ListPage.EMPTY_PAGE;
									listPage.setCurrentPageSize(pageSize);
									listPage.setDataList(new ArrayList());
								} else {
									query = session.createQuery(hql);
									mqc.launchParamValues(query);
									query.setFirstResult(firstResultIndex);
									query.setMaxResults(pageSize);
									List result = query.list();
									listPage = new ListPage();
									listPage.setCurrentPageNo(tempPageNo);
									listPage.setCurrentPageSize(pageSize);
									listPage.setTotalSize(totalCount);
									listPage.setDataList(result);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							return ListPage.EMPTY_PAGE;
						} finally {
							// session.close();
						}
						return listPage;
					}
				});
	}

	/**
	 * 分页查询(sql查询)
	 * 
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            一页几条记录
	 * @param countSql
	 *            查询总数语句
	 * @param querySql
	 *            查询语句
	 * @param parameters
	 *            参数
	 * @return LisPage 分页封装
	 */
	public ListPage querySql(final int pageNo, final int pageSize,
			final String countSql, final String querySql, final Map parameters) {
		return (ListPage) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						CommQueryCondition mqc = new CommQueryCondition();
						mqc.setPrepareHql(querySql);
						mqc.setCountHql(countSql);
						mqc.setConditions(parameters);
						// Session session = getSession();
						ListPage listPage = null;
						try {
							String hql = mqc.getPrepareHql();
							if (hql != null) {
								Query query = session.createSQLQuery(mqc
										.getCountHql());
								mqc.launchParamValues(query);
								List list = query.list();
								String totalCountStr = (list.get(0)).toString();
								int totalCount = Integer
										.parseInt(totalCountStr);
								int totalPageCount = (totalCount -1)/pageSize + 1; //总页数
								int tempPageNo = pageNo;
								if(pageNo > totalPageCount){
									tempPageNo = totalPageCount;
								}
								int firstResultIndex = (pageNo - 1) * pageSize;
								
								if (totalCount == 0
										|| firstResultIndex > totalCount) {
									listPage = ListPage.EMPTY_PAGE;
									listPage.setCurrentPageSize(pageSize);
									listPage.setDataList(new ArrayList());
								} else {
									query = session.createSQLQuery(hql);
									mqc.launchParamValues(query);
									query.setFirstResult(firstResultIndex);
									query.setMaxResults(pageSize);
									List result = query.list();
									listPage = new ListPage();
									listPage.setCurrentPageNo(tempPageNo);
									listPage.setCurrentPageSize(pageSize);
									listPage.setTotalSize(totalCount);
									listPage.setDataList(result);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							return ListPage.EMPTY_PAGE;
						} finally {
							// session.close();
						}
						return listPage;
					}
				});
	}

	/**
	 * 依参数查询资料(SQL)，显示钱几笔
	 * 
	 * @param sql
	 *            查询语法
	 * @param values
	 *            参数值(Object[])
	 * @param firstResult
	 *            从第几笔抓取
	 * @param maxResults
	 *            该页秀几笔
	 * @param clazz
	 *            类别
	 * @param sqlName
	 * @return 资料列表
	 */
	public List findBySql(final String sql, final Object[] values,
			final int firstResult, final int maxResults, final Class clazz,
			final String sqlName) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity(sqlName, clazz);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
				return query.list();
			}
		});
	}

	/**
	 * 依参数查询资料(Sql),显示前几笔
	 * 
	 * @param sql
	 *            查询语法
	 * @param values
	 *            参数值(Object[])
	 * @param firstResult
	 *            从第几笔开始
	 * @param maxResults
	 *            该页秀出多少笔
	 * @return 资料列表
	 */
	public List findBySql(final String sql, final Object[] values,
			final int firstResult, final int maxResults) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) {

				SQLQuery query = session.createSQLQuery(sql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
				return query.list();
			}
		});
	}

	/**
	 * 依参数查询资料(hql)，显示前几笔
	 * 
	 * @param hql
	 *            查询语法
	 * @param values
	 *            参数值(Object[])
	 * @param firstResult
	 *            从第几笔抓取
	 * @param maxResults
	 *            该页秀几笔
	 * @param clazz
	 *            类别
	 * @param sqlName
	 * @return 资料列表
	 */
	public List findByHql(final String hql, final Object[] values,
			final int firstResult, final int maxResults) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
				return query.list();
			}

		});
	}

	/**
	 * 取得总数(hql)
	 * 
	 * @param hql
	 *            查询语法
	 * @return 总笔数
	 */
	public int getCount(String hql) {
		Long total = (Long) getHibernateTemplate().find(hql).listIterator()
				.next();
		return total.intValue();
	}

	/**
	 * 依参数查询，取得总数 (hql)
	 * 
	 * @param hql
	 *            查询语法
	 * @param values
	 *            参数值(Object[])
	 * @return 总笔数
	 */
	public int getCount(final String hql, final Object[] values) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) {
				Query query = session.createQuery(hql);
				List temp = new ArrayList();
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				Long total = (Long) query.list().iterator().next();
				temp.add(total.intValue());
				return temp;
			}
		});
		return (Integer) list.get(0);
	}

	/**
	 * 依参数查询资料笔数(sql)
	 * 
	 * @param sql
	 *            查询语法
	 * @param values
	 *            参数值(Object[])
	 * @return 总笔数
	 */
	public int getCountBySql(final String sql, final Object[] values) {
		List tmp = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(sql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		});
		return Integer.valueOf(tmp.get(0).toString());
	}
	
	/**
	 * 随机取得记录 (hql语句)
	 * @return 对象列表
	 */
	public List getRecordRandomly(final String queryString, final Map parameters) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(queryString);
				CommQueryCondition mqc = new CommQueryCondition();
				mqc.setConditions(parameters);
				mqc.launchParamValues(query);
				return query.list();
			}
		});
	}

	/**
	 * 取得session
	 * 
	 * @return
	 */
	public Session getHibernateSession() {
		return this.getSession();
	}

}

