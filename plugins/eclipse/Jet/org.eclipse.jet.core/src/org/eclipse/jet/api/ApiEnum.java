package org.eclipse.jet.api;

import java.util.ArrayList;
import java.util.List;

public enum ApiEnum {

	ENTITY_NAME("name"),
	ENTITY_RELATION("relation"),
	ENTITY_FROM("from"),
	ENTITY_TYPE("type"),
	
	FIELD_ID("id"),
	FIELD_NAME("name"),
	FIELD_IS_USED("isUsed"),
	FIELD_TYPE("type"),
	
	FIELD_DB_TYPE("dbType"),
	FIELD_JDBC_TYPE("jdbcType"),
	FIELD_FULL_TYPE("fullType"),
	
	FIELD_IS_NULL("isNull"),
	FIELD_DEFAULT_VALUE("defaultValue"),
	FIELD_IS_AUTO_INCREMENT("isAutoIncrement"),
	FIELD_SIZE("size"),
	
	FIELD_IS_PK("isPk"),
	FIELD_IS_FK("isFk"),
	FIELD_FK_ENTITY_NAME("fkEntityName"),
	FIELD_FK_FIELD_NAME("fkFieldName"),

	FIELD_IS_NEW("isNew")
	;
	
	private String value = "";
	
	private ApiEnum(String value) {
		this.value = value;
	}
  
	public String getValue(){
		return this.value;
	}
		
	public static List<String> getNames(){
		Enum<?>[] enums = values();
		List<String> names = new ArrayList<String>();
		for(Enum<?> e: enums){
			names.add(e.name());
		}	
		return names;
	}
	

}
