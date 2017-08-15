package ssh.struts1;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import app.j2ee.util.RequestUtil;
import app.other.query.RangeQuery;

public class ParamsManager {

	public static Map<String, Object> createParams(Class<?> clazz, HttpServletRequest request){

		Map<String, Object> map = new HashMap<String, Object>(); 
		createFieldParams(clazz, request, map);
		return map;
	}
	
	public static void createFieldParams(Class<?> clazz, HttpServletRequest request, Map<String, Object> map){
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			RangeQuery rangeQuery = RequestUtil.isRangeQuery(request, name);
			if(rangeQuery != null){
				map.put(name, rangeQuery);
			}else{
				String param = RequestUtil.getParaTrim(request, name);
				if(param != null && !param.isEmpty()){
					map.put(name, param);
				}
			}
		}
	}
	
	public static void forwordParams(HttpServletRequest request, Map<String, Object> map){
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			request.setAttribute(key, value);
		}
		//add by xyu 2016-3-18
		Enumeration enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			Object key = (Object) enumeration.nextElement();
			if(key instanceof String && !map.containsKey(key)){
				request.setAttribute((String)key, request.getParameter((String)key));
			}
		}
	}
	
	public static void main(String[] args) {
		String str = "{start:'ab',end:''}";
		JSONObject jsonobject = JSONObject.fromObject(str);
		System.out.println(jsonobject);
		RangeQuery rangeQuery = (RangeQuery) JSONObject.toBean(jsonobject, RangeQuery.class);
		System.out.println(rangeQuery.getStart());
	}
	
}
