package ssh.hibernate.dao;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;

import app.other.query.RangeQuery;

import ssh.hibernate.page.ListPage;

public class QuerySqlManager {

	public static ListPage query(IBaseDao baseDao, Class<?> clazz, int pageNo, int pageCount, Map<String, Object> queryMap){
		
		Map map = new HashedMap(); 
		StringBuffer hqlStr = new StringBuffer();
		StringBuffer countStr = new StringBuffer();
		String className = clazz.getSimpleName();
		hqlStr.append(" SELECT n FROM " + className + " n WHERE 1=1 ");
		countStr.append(" SELECT count(n.id) FROM " + className + " n WHERE 1=1 ");
		Iterator<Entry<String, Object>> iterator = queryMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			apppendFieldSql(clazz, map, key, value, hqlStr, countStr);
		}
	    hqlStr.append(" ORDER BY n.id DESC ");
		return baseDao.queryHql(pageNo, pageCount, countStr.toString(), hqlStr.toString(), map);
	}
	
	public static void apppendFieldSql(Class<?> clazz, Map map, String key, Object value, StringBuffer hqlStr, StringBuffer countStr){
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			//update by chenyajia 2014-11-23
			if(key.equals(name)){
				if(value instanceof RangeQuery){
					RangeQuery rangeQuery = (RangeQuery) value;
					String start = rangeQuery.getStart();
					if(start != null && !start.trim().isEmpty()){
						String START = name + "_start";
						hqlStr.append(" AND n." + key + " >= :" + START + " ");
						countStr.append(" AND n." + key + " >= :" + START + " ");
						map.put(START, start);
					}
					String end = rangeQuery.getEnd();
					if(end != null && !end.trim().isEmpty()){
						String END = name + "_end";
						hqlStr.append(" AND n." + key + " <= :" + END + " ");
						countStr.append(" AND n." + key + " <= :" + END + " ");
						map.put(END, end);
					}
				}else if(value instanceof String){
					hqlStr.append(" AND n." + key + " = :" + name + " ");
					countStr.append(" AND n." + key + " = :" + name + " ");
					map.put(name, value);
				}
			}
		}
	}
	
}
