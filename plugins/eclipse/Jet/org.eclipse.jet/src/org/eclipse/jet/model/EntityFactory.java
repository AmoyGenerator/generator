package org.eclipse.jet.model;

import java.util.HashMap;



public class EntityFactory {
	//池容器
	private static HashMap<String,Entity> pool = new HashMap<String,Entity>();
    
	/**
	 * 过时的方法
	 * @return
	 */
	@Deprecated
	public static Entity getEntity(){
		return new Entity();
	}
	
	//从池中获得对象
	public static Entity getEntity(String key){
		//设置返回对象
		Entity result = null;
		//池中没有该对象，则建立，并放入池中
		if(!pool.containsKey(key)){

			result = new EntityPool(key);
			pool.put(key, result);
		}else{
			result =  pool.get(key);
		}
		return result;
	}
}
