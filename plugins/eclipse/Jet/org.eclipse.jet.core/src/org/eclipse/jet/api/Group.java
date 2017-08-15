package org.eclipse.jet.api;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

public class Group {

	private String name;

	/** 实体对象列表 */
	private List<Entity> entities = new ArrayList<Entity>();

	/** Xml对象列表 */
	private List<Xml> xmls = new ArrayList<Xml>();

	/** 模型对象列表 */
	private List<Object> models = new ArrayList<Object>(); 
	
	private Object selectedModel;
	
	/** Group的xml document */
	private Document document;
	
	public Group() {
		
	}
	
	public Group(String name) {
		this.name = name;
	}

	public void addEntity(Entity entity){
		entities.add(entity);
		models.add(entity);
	}
	
	public void addXml(Xml xml){
		xmls.add(xml);
		models.add(xml);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public List<Xml> getXmls() {
		return xmls;
	}

	public void setXmls(List<Xml> xmls) {
		this.xmls = xmls;
	}

	public Entity entityByName(String name){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Entity entity : entities) {
			String entityName = entity.getName();
			if(entityName != null && name.equals(entityName)){
				return entity;
			}
		}
		return null;
	}
	
	public Entity ebn(String name){
		return entityByName(name);
	}
	
	public Entity entityByRelation(String relation){
		if(relation == null || relation.trim().isEmpty()){
			return null;
		}
		for (Entity entity : entities) {
			String entityRelation = entity.getRelation();
			if(entityRelation != null && relation.equals(entityRelation)){
				return entity;
			}
		}
		return null;
	}
	
	public Entity ebr(String relation){
		return entityByRelation(relation);
	}
	
	public Xml xmlByName(String name){
		if(name == null || name.trim().isEmpty()){
			return null;
		}
		for (Xml xml : xmls) {
			String xmlName = xml.getName();
			if(xmlName != null && name.equals(xmlName)){
				return xml;
			}
		}
		return null;
	}
	
	public Xml xbn(String name){
		return xmlByName(name);
	}
	
	public Xml xmlByRelation(String relation){
		if(relation == null || relation.trim().isEmpty()){
			return null;
		}
		for (Xml xml : xmls) {
			String xmlRelation = xml.getRelation();
			if(xmlRelation != null && relation.equals(xmlRelation)){
				return xml;
			}
		}
		return null;
	}
	
	public Xml xbr(String relation){
		return xmlByRelation(relation);
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	public List<Object> getModels(){
		return models;
	}
	
	public Object getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Object selectedModel) {
		this.selectedModel = selectedModel;
	}

	public Object findModel(String name){
		if(models == null || name == null){
			return null;
		}
		for (int i = 0; i < models.size(); i++) {
			Object object = models.get(i);
			if(object instanceof Entity){
				Entity entity = (Entity) object;
				String entityName = entity.getName();
				if(entityName != null && entityName.equals(name)){
					return entity;
				}
			}else if(object instanceof Xml){
				Xml xml = (Xml) object;
				String xmlName = xml.getName();
				if(xmlName != null && xmlName.equals(name)){
					return xml;
				}
			}
		}
		return null;
	}
	
}
