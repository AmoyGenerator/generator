package org.eclipse.jet.api;

import java.util.ArrayList;
import java.util.List;

public class Field {

	/** 识别字段ID(唯一) */
	private String id;
	/** 字段名 */
	private String name;
	/** 是否被使用 */
	private Boolean isUsed = true;
	/** 类型 */
	private String type;
	/** db类型 */
	private String dbType;
	/** jdbc类型 */
	private String jdbcType;
	/** java类型 */
	private String fullType;

	private Boolean isNull;
	
	private String defaultValue;
	
	private Boolean isAutoIncrement;
	
	private Integer size;
	
	private Boolean isPk;
	
	private Boolean isFk;
	
	private String fkEntityName;
	
	private String fkFieldName;
	
	/** 是否是新设属性(内置属性) */
	private Boolean isNew = false;

	private Entity parent;

	private List<Property> properties = new ArrayList<Property>();

	private List<Data> datas = new ArrayList<Data>();

	public Field() {
		super();
		init();
	}
	
	public Field(String id, String name, Boolean isNew) {
		super();
		this.id = id;
		this.name = name;
		this.isNew = isNew;
		init();
	}

	public Field(String id, String name, String type, Boolean isNew) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.isNew = isNew;
		init();
	}

	public Field(String id, String name, Boolean isUsed, String type,
			String dbType, String jdbcType, String fullType, Boolean isNew) {
		super();
		this.id = id;
		this.name = name;
		this.isUsed = isUsed;
		this.type = type;
		this.dbType = dbType;
		this.jdbcType = jdbcType;
		this.fullType = fullType;
		this.isNew = isNew;
		init();
	}

	private void init() {
		Property idProperty = new Property(ApiEnum.FIELD_ID.getValue(), id, false);
		Property nameProperty = new Property(ApiEnum.FIELD_NAME.getValue(), name, false);
		Property isUsedProperty = new Property(ApiEnum.FIELD_IS_USED.getValue(), isUsed.toString(),
				false);
		Property typeProperty = new Property(ApiEnum.FIELD_TYPE.getValue(), type, false);

		Property dbTypeProperty = new Property(ApiEnum.FIELD_DB_TYPE.getValue(), dbType, false);
		Property jdbcTypeProperty = new Property(ApiEnum.FIELD_JDBC_TYPE.getValue(), jdbcType, false);
		Property fullTypeProperty = new Property(ApiEnum.FIELD_FULL_TYPE.getValue(), fullType, false);

		Property isNewProperty = new Property(ApiEnum.FIELD_IS_NEW.getValue(), isNew.toString(), false);

		properties.add(idProperty);
		properties.add(nameProperty);
		properties.add(isUsedProperty);
		properties.add(typeProperty);
		properties.add(dbTypeProperty);
		properties.add(jdbcTypeProperty);
		properties.add(fullTypeProperty);
		properties.add(isNewProperty);
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}

	public void putProperty(Property property) {

		if (property == null) {
			return;
		}
		String name = property.getName();
		if (name == null || name.trim().isEmpty()) {
			return;
		}

		if (!replaceProperty(property)) {
			properties.add(property);
		}

	}

	private boolean replaceProperty(Property property) {
		if (property == null) {
			return false;
		}
		String name = property.getName();
		if (name == null || name.trim().isEmpty()) {
			return false;
		}
		for (int i = 0; i < properties.size(); i++) {
			Property p = properties.get(i);
			String propertyName = p.getName();
			if (propertyName != null && propertyName.equals(name)) {
				properties.set(i, property);
				return true;
			}
		}
		return false;
	}

	public void putData(Data data) {

		if (data == null) {
			return;
		}
		String name = data.getName();
		if (name == null || name.trim().isEmpty()) {
			return;
		}

		if (!replaceData(data)) {
			datas.add(data);
		}

	}

	private boolean replaceData(Data data) {
		if (data == null) {
			return false;
		}
		String name = data.getName();
		if (name == null || name.trim().isEmpty()) {
			return false;
		}
		for (int i = 0; i < datas.size(); i++) {
			Data d = datas.get(i);
			String dataName = d.getName();
			if (dataName != null && dataName.equals(name)) {
				datas.set(i, data);
				return true;
			}
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getFullType() {
		return fullType;
	}

	public void setFullType(String fullType) {
		this.fullType = fullType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsNull() {
		return isNull;
	}

	public void setIsNull(Boolean isNull) {
		this.isNull = isNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getIsAutoIncrement() {
		return isAutoIncrement;
	}

	public void setIsAutoIncrement(Boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getIsPk() {
		return isPk;
	}

	public void setIsPk(Boolean isPk) {
		this.isPk = isPk;
	}

	public Boolean getIsFk() {
		return isFk;
	}

	public void setIsFk(Boolean isFk) {
		this.isFk = isFk;
	}

	public String getFkEntityName() {
		return fkEntityName;
	}

	public void setFkEntityName(String fkEntityName) {
		this.fkEntityName = fkEntityName;
	}

	public String getFkFieldName() {
		return fkFieldName;
	}

	public void setFkFieldName(String fkFieldName) {
		this.fkFieldName = fkFieldName;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public Object property(String name) {
		if (name == null || name.trim().isEmpty()) {
			return null;
		}
		for (Property property : properties) {
			String propertyName = property.getName();
			if (propertyName != null && name.equals(propertyName)) {
				return property.getValue();
			}
		}
		return null;
	}

	public List<Property> defaultProperties() {
		List<Property> defaultProperties = new ArrayList<Property>();
		for (Property property : properties) {
			if (!property.getIsNew()) {
				defaultProperties.add(property);
			}
		}
		return defaultProperties;
	}

	public List<Property> newProperties() {
		List<Property> newProperties = new ArrayList<Property>();
		for (Property property : properties) {
			if (property.getIsNew()) {
				newProperties.add(property);
			}
		}
		return newProperties;
	}

	public Object data(String name) {
		if (name == null || name.trim().isEmpty()) {
			return null;
		}
		for (Data data : datas) {
			String dataName = data.getName();
			if (dataName != null && name.equals(dataName)) {
				return data.getValue();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Field [id=" + id + ", name=" + name + ", type=" + type
				+ ", isUsed=" + isUsed + "]";
	}

}
