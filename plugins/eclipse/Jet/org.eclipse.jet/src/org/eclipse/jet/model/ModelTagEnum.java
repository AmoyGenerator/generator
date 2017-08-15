package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型标签Enum
 * @author xyu
 *
 */
public enum ModelTagEnum {
		
	//xml标签
	/**编辑器文件中所在的元素*/
	MODEL("model"),
	/**组,group元素做为模型的根节点只有一个*/
	GROUP("group"),
	/**实体,entity元素有1或n个,但entity节点的参数name唯一*/
	ENTITY("entity"),
	/**实体身中包含的字段*/
	FIELD("field"),
	/**database*/
	DATABASE("database"),
	/**data*/
    DATA("data"),
	/**table*/
	TABLE("table"),
	/**sign*/
	SIGN("sign"),
	/**userset*/
	USERSET("userset"),
	/**column*/
	COLUMN("column"),
	/**select*/
	SELECT("select"),
	/**value*/
	VALUE("value"),
	/**xml文件*/
    XML("xml"),
    /**xml文件*/
    XML_PATH("path")
    ;
	
	private String value = "";
	
	private ModelTagEnum(String value) {
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
