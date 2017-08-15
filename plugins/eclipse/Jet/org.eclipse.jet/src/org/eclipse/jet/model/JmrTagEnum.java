package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;

public enum JmrTagEnum {
	//xml标签
	/**根标签*/
	JMR("jmr");
	
	private String value = "";
	
	private JmrTagEnum(String value) {
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
