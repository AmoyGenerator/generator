package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.model.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Group extends Model{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String name;

  /** 实体对象列表 */
  private List<Entity> entities = new ArrayList<Entity>();

  /** Xml对象列表 */
  private List<Xml> xmls = new ArrayList<Xml>();

  public Group() {
    super(ModelTagEnum.GROUP.getValue());
  }

  public Group(String name) {
    super(ModelTagEnum.GROUP.getValue());
    setName(name);
  }

  /**
   * 设置组名
   * @param name
   */
  public void setName(String value){
    super.setAttr(ModelTagAttrEnum.GROUP_NAME.getValue(), value);
    this.name = value;
  }

  /**
   * 得到组名
   */
  public String getName(){
    return StringUtils.exceptNull(name);
  }

  @Override
  public Document getSourceDocument() {

    Document document = super.getSourceDocument();
    Element element = document.createElement(nodeName);
    element.setUserData("type", this, null);
    document.appendChild(element);

    Attr attr;	
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      element.setAttribute(attr.getKey(), attr.getValue());
    }

    Entity entity;
    for (int i = 0; i < entities.size(); i++) {
      entity = entities.get(i);
      Document entityDocument = entity.getSourceDocument();
      Node entityNode = entityDocument.getDocumentElement();
      Node importNode = document.importNode(entityNode, true);
      importNode.setUserData("type", entity, null);
      element.appendChild(importNode);
    }

    Xml xml;
    for (int i = 0; i < xmls.size(); i++) {
      xml = xmls.get(i);
      Document xmlDocument = xml.getSourceDocument();
      Node xmlNode = xmlDocument.getDocumentElement();
      Node importNode = document.importNode(xmlNode, true);
      importNode.setUserData("type", xml, null);
      element.appendChild(importNode);
    }
    return document;
  }



  @Override
  public Document getDocument()
  {
    Document document = super.getSourceDocument();
    Element element = document.createElement(nodeName);
    element.setUserData("type", this, null);
    document.appendChild(element);

    Attr attr;  
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      element.setAttribute(attr.getKey(), attr.getValue());
    }

    Entity entity;
    for (int i = 0; i < entities.size(); i++) {
      entity = entities.get(i);
      Document entityDocument = entity.getDocument();
      Node entityNode = entityDocument.getDocumentElement();
      Node importNode = document.importNode(entityNode, true);
      importNode.setUserData("type", entity, null);
      element.appendChild(importNode);
    }

    Xml xml;
    for (int i = 0; i < xmls.size(); i++) {
      xml = xmls.get(i);
      Document xmlDocument = xml.getDocument();
      Node xmlNode = xmlDocument.getDocumentElement();
      Node importNode = document.importNode(xmlNode, true);
      importNode.setUserData("type", xml, null);
      element.appendChild(importNode);
    }
    return document;
  }
  
  public Entity getEntityByName(String name){
    if(name == null || name.trim().isEmpty()){
      return null;
    }
    for (int i = 0; i < entities.size(); i++)
    {
       Entity entity = entities.get(i);
       String entityName = entity.getName();
       if(entityName != null && !entityName.trim().isEmpty() && entityName.equals(name)){
         return entity;
       }
    }
    return null;
  }
  
  public Entity getEntityByRelation(String relation){
    if(relation == null || relation.trim().isEmpty()){
      return null;
    }
    for (int i = 0; i < entities.size(); i++)
    {
       Entity entity = entities.get(i);
       String relationName = entity.getRelation();
       if(relationName != null && !relationName.trim().isEmpty() && relationName.equals(relation)){
         return entity;
       }
    }
    return null;
  }
  
  /**
   * 返回所有实体列表
   */
  public List<Entity> getEntities(){
    return entities;
  }

  /**
   * 根据序号得到元素
   * @param index
   */
  public void getEntity(int index){
    entities.get(index);
  }

  /**
   * 根据参数的属性和值得到符合条件的第一个Entity
   * @param attrName
   * @param attrValue
   */
  public Entity getFirstEntityByAttr(String attrName, String attrValue){
    Entity entity;
    for (int i = 0; i < entities.size(); i++) {
      entity = entities.get(i);
      if(entity.getAttr(attrName) != null){
        return entity;
      }
    }
    return null;	
  }

  /**
   * 得到第一个元素
   */	
  public Entity getFirstEntity(){			
    return entities.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Entity getLastEntity(){
    int size = entities.size();
    if(size > 0){
      return entities.get(size-1);
    }
    return null;
  }

  public void addEntity(Entity entity){			
    entity.setParent(this);
    entities.add(entity);			
  }

  public void add(int index, Entity entity){
    entity.setParent(this);
    entities.add(index, entity);
  }

  public void addAllEntity(Collection<? extends Entity> c){
    Iterator<? extends Entity> it = c.iterator();
    Entity entity;
    while(it.hasNext()){
      entity = it.next();
      entity.setParent(this);
    }
    entities.addAll(c);
  }

  public void addAllEntity(int index, Collection<? extends Entity> c){
    Iterator<? extends Entity> it = c.iterator();
    Entity entity;
    while(it.hasNext()){
      entity = it.next();
      entity.setParent(this);
    }
    entities.addAll(index, c);
  }

  public void removeEntity(int index){
    entities.remove(index);
  }

  public void removeEntity(Entity entity){
    entities.remove(entity);	
  }

  public void removeAllEntity(Collection<?> c){
    entities.removeAll(c);
  }

  /**
   * 返回所有xml列表
   */
  public List<Xml> getXmls(){
    return xmls;
  }

  /**
   * 根据序号得到元素
   * @param index
   */
  public void getXml(int index){
    xmls.get(index);
  }

  /**
   * 根据参数的属性和值得到符合条件的第一个xml
   * @param attrName
   * @param attrValue
   */
  public Xml getFirstXmlByAttr(String attrName, String attrValue){
    Xml xml;
    for (int i = 0; i < xmls.size(); i++) {
      xml = xmls.get(i);
      if(xml.getAttr(attrName) != null){
        return xml;
      }
    }
    return null;    
  }

  /**
   * 得到第一个元素
   */ 
  public Xml getFirstXml(){         
    return xmls.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Xml getLastXml(){
    int size = xmls.size();
    if(size > 0){
      return xmls.get(size-1);
    }
    return null;
  }

  public void addXml(Xml xml){           
    xml.setParent(this);
    xmls.add(xml);           
  }

  public void add(int index, Xml xml){
    xml.setParent(this);
    xmls.add(index, xml);
  }

  public void addAllXml(Collection<? extends Xml> c){
    Iterator<? extends Xml> it = c.iterator();
    Xml xml;
    while(it.hasNext()){
      xml = it.next();
      xml.setParent(this);
    }
    xmls.addAll(c);
  }

  public void addAllXml(int index, Collection<? extends Xml> c){
    Iterator<? extends Xml> it = c.iterator();
    Xml xml;
    while(it.hasNext()){
      xml = it.next();
      xml.setParent(this);
    }
    xmls.addAll(index, c);
  }

  public void removeXml(int index){
    xmls.remove(index);
  }

  public void removeXml(Xml xml){
    xmls.remove(xml);    
  }

  public void removeAllXml(Collection<?> c){
    xmls.removeAll(c);
  }

  @Override
  protected List<ProposalType> getByKey(String key)
  {
    return null;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    return null;
  }

}
