package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;


/**
 * 模型标签参数Enum
 * @author xyu
 *
 */
public enum ModelTagAttrEnum {
	
	//xml标签参数
	/**组名*/
	GROUP_NAME("name"),
	/**table(表名),java(实体名)*/
	ENTITY_NAME("name"),
	/**关系*/
	ENTITY_RELATION("relation"),
	/**来源 jdbc,java,xml,hibernatexml*/
	ENTITY_FROM("from"),
	/**类型,table,view*/
	ENTITY_TYPE("type"),
	/**xml(根元素名)*/
    XML_NAME("name"),
    /**关系*/
    XML_RELATION("relation"),
    
	/**body的attribute中default属性*/
	/**是否是新设属性*/
	FIELD_IS_NEW("isNew"),
	/**识别字段ID(唯一)*/
    FIELD_ID("id"),
	/**是否使用*/
	FIELD_IS_USED("isUsed"),
	/**数据库字段类型*/
    FIELD_DB_TYPE("dbType"),
	/**jdbc字段类型*/
    FIELD_JDBC_TYPE("jdbcType"),
    /**java字段类型*/
    FIELD_FULL_TYPE("fullType"),
	/**字段名*/
	FIELD_NAME("name"),
	/**字段默认值*/
	FIELD_DEFAULT_VALUE("defaultValue"),
	/**类型*/
	FIELD_TYPE("type"),
	
	/**data节点属性名*/
    DATA_NAME("name"),
	/**表或视图节点属性名*/
	DATABASE_NAME("name"),
	/**表节点属性名*/
	TABLE_NAME("name"),
	/**标记的属性名*/
	SIGN_NAME("name"),
	/**自定义的属性名*/
	USERSET_NAME("name"),
	/**自定义的值*/
    USERSET_VALUE("value"),
	/**列名*/
	COLUMN_NAME("name"),
	/**列类型*/
	COLUMN_TYPE("type"),
	/**默认值*/	
	COLUMN_DEFAULT_VALUE("defaultValue");

	
	private String value = "";
	
	private ModelTagAttrEnum(String value) {
		this.value = value;
	}
  
	public String getValue(){
		return this.value;
	}
		
	/**
	 * 返回所有的enum对象
	 * @return
	 */
	public static List<String> getNames(){
		Enum<?>[] enums = values();
		List<String> names = new ArrayList<String>();
		for(Enum<?> e: enums){
			names.add(e.name());
		}	
		return names;
	}
	
}
