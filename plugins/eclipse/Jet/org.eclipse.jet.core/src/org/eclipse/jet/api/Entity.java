package org.eclipse.jet.api;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	/**
	 * this is name
	 */
	private String name;
	private String relation;
	private String from;
	private String type;

	
	private Group parent;

	private List<Property> properties = new ArrayList<Property>();

	private List<Field> fields = new ArrayList<Field>();

	public Entity(String name, String relation, String from, String type) {
		super();
		this.name = name;
		this.relation = relation;
		this.from = from;
		this.type = type;
		init();
	}

	private void init(){
		Property nameProperty = new Property(ApiEnum.ENTITY_NAME.getValue(), name, false);
		Property relationProperty = new Property(ApiEnum.ENTITY_RELATION.getValue(), relation, false);
		Property fromProperty = new Property(ApiEnum.ENTITY_FROM.getValue(), from, false);
		Property typeProperty = new Property(ApiEnum.ENTITY_TYPE.getValue(), type, false);
		properties.add(nameProperty);
		properties.add(relationProperty);
		properties.add(fromProperty);
		properties.add(typeProperty);
	}

	public void putProperty(Property property){

		if(property == null){
			return;
		}
		String name = property.getName();
		if(name == null || name.trim().isEmpty()){
			return;
		}

		if(!replaceProperty(property)){
			properties.add(property);
		}

	}

	private boolean replaceProperty(Property property){
		if(property == null){
			return false;
		}
		String name = property.getName();
		if(name == null || name.trim().isEmpty()){
			return false;
		}
		for (int i = 0; i < properties.size(); i++) {
			Property p = properties.get(i);
			String propertyName = p.getName();
			if(propertyName != null && propertyName.equals(name)){
				properties.set(i, property);
				return true;
			}
		}
		return false;
	}

	public void putField(Field field){

		if(field == null){
			return;
		}
		String id = field.getId();
		if(id == null || id.trim().isEmpty()){
			return;
		}

		if(!replaceField(field)){
			fields.add(field);
		}

	}

	private boolean replaceField(Field field){
		if(field == null){
			return false;
		}
		String id = field.getId();
		if(id == null || id.trim().isEmpty()){
			return false;
		}
		for (int i = 0; i < fields.size(); i++) {
			Field f = fields.get(i);
			String fieldId = f.getId();
			if(fieldId != null && fieldId.equals(id)){
				fields.set(i, field);
				return true;
			}
		}
		return false;
	}

	/**
	 * return entity name
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Group getParent() {
		return parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Object property(String name){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Property property : properties) {
			String propertyName = property.getName();
			if(propertyName != null && name.equals(propertyName)){
				return property.getValue();
			}
		}
		return null;
	}

	public List<Property> defaultProperties(){
		List<Property> defaultProperties = new ArrayList<Property>();
		for (Property property : properties) {
			if(!property.getIsNew()){
				defaultProperties.add(property);
			}
		}
		return defaultProperties;
	}

	public List<Property> newProperties(){
		List<Property> newProperties = new ArrayList<Property>();
		for (Property property : properties) {
			if(property.getIsNew()){
				newProperties.add(property);
			}
		}
		return newProperties;
	}

	/**
	 * get field by name
	 * @param name
	 * @return
	 */
	public Field field(String name){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Field field : fields) {
			String fieldName = field.getName();
			if(fieldName != null && name.equals(fieldName)){
				return field;
			}
		}
		return null;
	}

	public Field field(String name, int i){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Field field : fields) {
			String fieldName = field.getName();
			if(fieldName != null && name.equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	public Field field(String name, String str){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Field field : fields) {
			String fieldName = field.getName();
			if(fieldName != null && name.equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	public Field field(String name, boolean test){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Field field : fields) {
			String fieldName = field.getName();
			if(fieldName != null && name.equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	public Field fieldById(String id){
		if(id == null || id.trim().isEmpty()){
			return null;
		}
		for (Field field : fields) {
			String fieldId = field.getId();
			if(fieldId != null && id.equals(fieldId)){
				return field;
			}
		}
		return null;
	}

	public Field fbi(String id){
		return fieldById(id);
	}

	@Override
	public String toString() {
		return "Entity [name=" + name + ", relation=" + relation + ", from="
				+ from + ", type=" + type + "]";
	}
	
}
