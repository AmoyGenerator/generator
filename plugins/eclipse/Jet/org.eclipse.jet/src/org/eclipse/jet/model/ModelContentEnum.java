package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;

public enum ModelContentEnum {

	/**来自java类*/
	FROM_JAVA("java"),
	/**来自数据库*/ 
	FROM_DB("db"),
	/**来自xml*/
	FROM_XML("xml"),
	/**来自hibernate的配置文件*/
	FROM_XML_HIBERNAT("xml_hibernate"),
	
	/**类型:表*/
	TYPE_TABLE("table"),
	/**类型:视图*/
	TYPE_VIEW("view"),
	
	/**是否为空*/
	PROPERTY_IS_NULL("isNull"),
	/**是否为空*/
	PROPERTY_IS_AUTO_INCREMENT("isAutoIncrement"),
	/**大小*/
	PROPERTY_SIZE("size"),
	
	/**是否为主键*/
	PROPERTY_IS_PK("isPk"),
	/**是否为外键*/
	PROPERTY_IS_FK("isFk"),
	/**外键关联实体*/
	FK_ENTITY_NAME("fkEntityName"),
    /**外键关联实体主键*/
    FK_FIELD_NAME("fkFieldName");
	
	private String value = "";
	
	private ModelContentEnum(String value) {
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
