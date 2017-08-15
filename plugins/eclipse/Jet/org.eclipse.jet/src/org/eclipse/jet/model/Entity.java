package org.eclipse.jet.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.model.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Entity extends Model{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String name;
  private String relation;
  private String from;
  private String type;

//  /** sign列表 */
//  private List<Sign> signs = new ArrayList<Sign>();

  /** sign列表 */
  private List<Userset> usersets = new ArrayList<Userset>();

  /** 字段列表 */
  private List<Field> fields = new ArrayList<Field>();

  /** 列列表 */
  private List<Column> columns = new ArrayList<Column>();

  public Entity(){
    super(ModelTagEnum.ENTITY.getValue());	
  }

  public Entity(String name, String relation, String from, String type){
    super(ModelTagEnum.ENTITY.getValue());
    setName(name);
    setRelation(relation);
    setFrom(from);
    setType(type);	
  }

  /**
   * 得到entity所在的Group,没有则返回null
   * @return
   */
  public Group getGroup(){
    if(parent instanceof Group){
      return (Group) parent;
    }
    return null;
  }

  /**
   * 得到实体名
   */
  public String getName(){
    return StringUtils.exceptNull(name);
  }

  /**
   * 设置实体名
   * @param name
   */
  public void setName(String value){
    super.setAttr(ModelTagAttrEnum.ENTITY_NAME.getValue(), value);
    this.name = value;
  }

  /**
   * 得到实体关系
   */
  public String getRelation(){
    return StringUtils.exceptNull(relation);
  }

  /**
   * 设置实体关系
   * @param relation
   */
  public void setRelation(String value){
    super.setAttr(ModelTagAttrEnum.ENTITY_RELATION.getValue(), value);
    this.relation = value;
  }

  /**
   * 得到实体来源
   */
  public String getFrom(){
    return StringUtils.exceptNull(from);
  }

  /**
   * 设置实体来源
   * @param from
   */
  public void setFrom(String value){
    super.setAttr(ModelTagAttrEnum.ENTITY_FROM.getValue(), value);
    this.from = value;
  }

  /**
   * 得到实体来源
   */
  public String getType(){
    return StringUtils.exceptNull(type);
  }

  /**
   * 设置实体类型
   * @param type
   */
  public void setType(String value){
    super.setAttr(ModelTagAttrEnum.ENTITY_TYPE.getValue(), value);
    this.type = value;
  }

  @Override
  public Document getSourceDocument() {

    Document document = super.getSourceDocument();
    Element element = document.createElement(nodeName);
    document.appendChild(element);

    Attr attr;	
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      element.setAttribute(attr.getKey(), attr.getValue());
    }

//    Sign sign;
//    for (int i = 0; i < signs.size(); i++) {
//      sign = signs.get(i);
//      Document signDocument = sign.getDocument();
//      Node signNode = signDocument.getDocumentElement();
//      Node importNode = document.importNode(signNode, true);
//      element.appendChild(importNode);
//    }

    Userset userset;
    for (int i = 0; i < usersets.size(); i++) {
      userset = usersets.get(i);
      Document usersetDocument = userset.getSourceDocument();
      Node usersetNode = usersetDocument.getDocumentElement();
      Node importNode = document.importNode(usersetNode, true);
      element.appendChild(importNode);
    }

    Field field;
    for (int i = 0; i < fields.size(); i++) {
      field = fields.get(i);
      Document fieldDocument = field.getSourceDocument();
      Node fieldNode = fieldDocument.getDocumentElement();
      Node importNode = document.importNode(fieldNode, true);
      element.appendChild(importNode);
    }

    Column column;
    for (int i = 0; i < columns.size(); i++) {
      column = columns.get(i);
      Document columnDocument = column.getSourceDocument();
      Node columnNode = columnDocument.getDocumentElement();
      Node importNode = document.importNode(columnNode, true);
      element.appendChild(importNode);
    }

    return document;
  }

  @Override
  public Document getDocument()
  {
    return getSourceDocument();
  }

  /**
   * 得到entity包含的列名
   * @return
   */
  public List<String> getColumnNames(){

    List<String> names = new ArrayList<String>();
    names.add(ModelTagAttrEnum.FIELD_ID.getValue());
    names.add(ModelTagAttrEnum.FIELD_IS_USED.getValue());
    names.add(ModelTagAttrEnum.FIELD_DB_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_JDBC_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_FULL_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_NAME.getValue());
    names.add(ModelTagAttrEnum.FIELD_DEFAULT_VALUE.getValue());
    names.add(ModelTagAttrEnum.FIELD_TYPE.getValue());
    //根据from和type判断需要添加的固定属性名
    if(from.equals(ModelContentEnum.FROM_DB.getValue())){
      names.add(ModelContentEnum.PROPERTY_IS_NULL.getValue());
      names.add(ModelContentEnum.PROPERTY_IS_AUTO_INCREMENT.getValue());
      names.add(ModelContentEnum.PROPERTY_SIZE.getValue());
    }
    if(type.equals(ModelContentEnum.TYPE_TABLE.getValue())){
      names.add(ModelContentEnum.PROPERTY_IS_PK.getValue());
      names.add(ModelContentEnum.PROPERTY_IS_FK.getValue());
      names.add(ModelContentEnum.FK_ENTITY_NAME.getValue());
      names.add(ModelContentEnum.FK_FIELD_NAME.getValue());
    }

//    List<Sign> signs = getSigns();
//    for (int i = 0; i < signs.size(); i++) {
//      names.add(signs.get(i).getName());
//    }
    List<Userset> usersets = getUsersets();
    for (int i = 0; i < usersets.size(); i++) {
      names.add(usersets.get(i).getName());
    }

    //添加动态属性名
    Field field;	
    Set<String> hasAddedList = new LinkedHashSet<String>();
    //	List<Sign> signs;
    Sign sign;
    //	List<Userset> usersets;
    Userset userset;
    for (int i = 0; i < fields.size(); i++) {
      field = fields.get(i);
//      signs = field.getSigns();
//      for (int j = 0; j < signs.size(); j++) {
//        sign = signs.get(j);
//        hasAddedList.add(sign.getName());
//      }
      usersets = field.getUsersets();
      for (int j = 0; j < usersets.size(); j++) {
        userset = usersets.get(j);
        hasAddedList.add(userset.getName());
      }
    }

    names.addAll(hasAddedList);

    return names;
  }

  /**
   * 得到字段列表
   * @return
   */
  public List<Field> getFields() {
    return fields;
  }

  /**
   * 根据序号得到元素
   */
  public Field getField(int index){
    return fields.get(index);
  }

  /**
   * 根据序号得到元素
   */
  public Field getFieldById(String id){
    if(id == null || id.trim().isEmpty()){
      return null;
    }
    for (int i = 0; i < fields.size(); i++)
    {
      Field field = fields.get(i);
      String fieldId = field.getId();
      if(fieldId != null && fieldId.equals(id)){
        return field;
      }
    }
    return null;
  }

  /**
   * 根据名称得到元素
   */
  public Field getFieldByName(String name){
    if(name == null || name.trim().isEmpty()){
      return null;
    }
    for (int i = 0; i < fields.size(); i++)
    {
      Field field = fields.get(i);
      String fieldName = field.getName();
      if(fieldName != null && fieldName.equals(name)){
        return field;
      }
    }
    return null;
  }

  /**
   * 得到第一个元素
   */
  public Field getFirstField(){
    return fields.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Field getLastField(){
    int size = fields.size();
    if(size > 0){
      return fields.get(size - 1);
    }
    return null;
  }

  public Field getPreviousField(Field field){
    for (int i = 0; i < fields.size(); i++) {
      if(i > 0 && fields.get(i) == field){
        return fields.get(i - 1);
      } 
    }
    return null;
  }

  public Field getNextField(Field field){   
    for (int i = 0; i < fields.size() - 1; i++) {
      if(fields.get(i) == field){
        return fields.get(i + 1);
      } 
    }
    return null;
  }

  public void addField(Field field){			
    field.setParent(this);
    fields.add(field);			
  }

  public void add(int index, Field field){
    field.setParent(this);
    fields.add(index, field);
  }

  public void addAllField(Collection<? extends Field> c){
    Iterator<? extends Field> it = c.iterator();
    Field field;
    while(it.hasNext()){
      field = it.next();
      field.setParent(this);
    }
    fields.addAll(c);
  }

  public void addAllField(int index, Collection<? extends Field> c){
    Iterator<? extends Field> it = c.iterator();
    Field field;
    while(it.hasNext()){
      field = it.next();
      field.setParent(this);
    }
    fields.addAll(index, c);
  }

  public void removeField(int index){
    fields.remove(index);
  }

  public void removeField(Field field){
    fields.remove(field);	
  }

  public void removeAllField(Collection<?> c){
    fields.removeAll(c);
  }

  /**
   * 上移元素
   * @param field
   * @return
   */
  public boolean replaceUpField(Field field){

    Integer index = null;

    for (int i = 0; i < fields.size(); i++) {
      if(fields.get(i) == field){
        index = i;
        break;
      }
    }

    if(index != null){
      Field replaceField = fields.get(index - 1);
      if(replaceField != null){
        fields.remove(field);
        fields.add(index - 1, field);
        return true;
      }
    }	
    return false;
  }

  /**
   * 下移元素
   * @param field
   * @return
   */
  public boolean replaceDownField(Field field){

    Integer index = null;

    for (int i = 0; i < fields.size(); i++) {
      if(fields.get(i) == field){
        index = i;
        break;
      }
    }

    if(index != null){
      Field replaceField = fields.get(index);
      if(replaceField != null){
        fields.remove(field);
        fields.add(index + 1, field);
        return true;
      }
    }
    return false;	
  }

//  public List<Sign> getSigns() {
//    return signs;
//  }

//  /**
//   * 根据序号得到元素
//   */
//  public Sign getSign(int index){
//    return signs.get(index);
//  }

//  /**
//   * 得到第一个元素
//   */
//  public Sign getFirstSign(){
//    return signs.get(0);
//  }

//  /**
//   * 得到最后一个元素
//   */
//  public Sign getLastSign(){
//    int size = signs.size();
//    if(size > 0){
//      return signs.get(size - 1);
//    }
//    return null;
//  }

//  public void addSign(Sign sign){			
//    sign.setParent(this);
//    signs.add(sign);			
//  }

//  public void add(int index, Sign sign){
//    sign.setParent(this);
//    signs.add(index, sign);
//  }

//  public void addAllSign(Collection<? extends Sign> c){
//    Iterator<? extends Sign> it = c.iterator();
//    Sign sign;
//    while(it.hasNext()){
//      sign = it.next();
//      sign.setParent(this);
//    }
//    signs.addAll(c);
//  }

//  public void addAllSign(int index, Collection<? extends Sign> c){
//    Iterator<? extends Sign> it = c.iterator();
//    Sign sign;
//    while(it.hasNext()){
//      sign = it.next();
//      sign.setParent(this);
//    }
//    signs.addAll(index, c);
//  }

//  public void removeSign(int index){
//    signs.remove(index);
//  }

//  public void removeSign(Sign sign){
//    signs.remove(sign);	
//  }

//  public void removeAllSign(Collection<?> c){
//    signs.removeAll(c);
//  }


  public List<Userset> getUsersets() {
    return usersets;
  }

  /**
   * 根据序号得到元素
   */
  public Userset getUserset(int index){
    return usersets.get(index);
  }

  /**
   * 得到第一个元素
   */
  public Userset getFirstUserset(){
    return usersets.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Userset getLastUserset(){
    int size = usersets.size();
    if(size > 0){
      return usersets.get(size - 1);
    }
    return null;
  }

  public void addUserset(Userset userset){			
    userset.setParent(this);
    usersets.add(userset);			
  }

  public void add(int index, Userset userset){
    userset.setParent(this);
    usersets.add(index, userset);
  }

  public void addAllUserset(Collection<? extends Userset> c){
    Iterator<? extends Userset> it = c.iterator();
    Userset userset;
    while(it.hasNext()){
      userset = it.next();
      userset.setParent(this);
    }
    usersets.addAll(c);
  }

  public void addAllUserset(int index, Collection<? extends Userset> c){
    Iterator<? extends Userset> it = c.iterator();
    Userset userset;
    while(it.hasNext()){
      userset = it.next();
      userset.setParent(this);
    }
    usersets.addAll(index, c);
  }

  public void removeUserset(int index){
    usersets.remove(index);
  }

  public void removeUserset(Userset userset){
    usersets.remove(userset);	
  }

  public void removeAllUserset(Collection<?> c){
    usersets.removeAll(c);
  }

  /**
   * 得到列列表
   * @return
   */
  public List<Column> getColumns() {
    return columns;
  }

  /**
   * 根据序号得到列
   */
  public Column getColumn(String name){
    if(name == null){
      return null;
    }

    for (int i = 0; i < columns.size(); i++)
    {
      Column column = columns.get(i);
      if (column.getName().equals(name))
      {
        return column;
      } 
    }
    return null;

  }

  /**
   * 根据序号得到列
   */
  public Column getColumn(int index){
    return columns.get(index);
  }

  /**
   * 得到第一个元素
   */
  public Column getFirstColumn(){
    return columns.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Column getLastColumn(){
    int size = columns.size();
    if(size > 0){
      return columns.get(size - 1);
    }
    return null;
  }

  public void addColumn(Column column){			
    column.setParent(this);
    columns.add(column);
  }

  public void add(int index, Column column){
    column.setParent(this);
    columns.add(index, column);
  }

  public void addAllColumn(Collection<? extends Column> c){
    Iterator<? extends Column> it = c.iterator();
    Column column;
    while(it.hasNext()){
      column = it.next();
      column.setParent(this);
    }
    columns.addAll(c);
  }

  public void addAllColumn(int index, Collection<? extends Column> c){
    Iterator<? extends Column> it = c.iterator();
    Column column;
    while(it.hasNext()){
      column = it.next();
      column.setParent(this);
    }
    columns.addAll(index, c);
  }

  public void removeColumn(int index){
    //删除对应的userset
    Column column = columns.get(index);
    String columnName = column.getName();
    Field field;
    Userset userset;
    List<Userset> usersets;
    for (int i = 0; i < fields.size(); i++) {
      field = fields.get(i);
      usersets = field.getUsersets();
      for (int j = 0; j < usersets.size(); j++) {
        userset = usersets.get(i);
        if(userset.getName().equals(columnName)){
          field.removeUserset(userset);
        }
      }
    }
    columns.remove(index);
  }

  public void removeColumn(Column column){
    columns.remove(column);	
  }

  public void removeAllColumn(Collection<?> c){
    columns.removeAll(c);
  }

  /**
   * 根据列名得到字段的所有值
   * @return
   */
  public Map<String, String> getColumnData(String columnName){
    Map<String, String> map = new LinkedHashMap<String, String>(); 
    for (int i = 0; i < fields.size(); i++)
    {
      Field field = fields.get(i);
      String fieldId = field.getId();
      String columnValue = field.getProperty(columnName);
      map.put(fieldId, columnValue);
    }
    return map;
  }

  public void setColumnData(String columnName, Map<String, String> map){
    if(map != null){
      Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
      while (iterator.hasNext())
      {
        Map.Entry<String, String> entry = iterator.next();
        String key = entry.getKey();
        String value = entry.getValue();
        for (int i = 0; i < fields.size(); i++)
        {
          Field field = fields.get(i);
          String fieldId = field.getId();
          if(fieldId.equals(key)){
            field.setProperty(columnName, value, true);
            break;
          }
        }
      }
    }
  }

  /**
   * 得到Entity的所有属性
   */
  public String getProperty(String key)
  {

    Attr attr = null;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      if(attr.getKey().equals(key)){
        return attr.getValue();
      }     
    }

//    Sign sign = null;
//    for (int i = 0; i < signs.size(); i++) {
//      sign = signs.get(i);
//      if(sign.getName().equals(key)){
//        return sign.getText();
//      }     
//    }

    Userset userset = null;
    for (int i = 0; i < usersets.size(); i++) {
      userset = usersets.get(i);
      if(userset.getName().equals(key)){
        return userset.getText();
      }     
    }
    return null;	    
  }


  /**
   * 得到Entity的所有属性
   */
  public Object getFieldObject(String key)
  {
    Field field = getFieldById(key);
    return field;
  }

  /**
   * 根据key设置Entity的属性值
   */
  public void setProperty(String key, String value)
  {
    Attr attr = null;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      if(attr.getKey().equals(key)){
        attr.setValue(value);
        break;
      }     
    }

//    Sign sign = null;
//    for (int i = 0; i < signs.size(); i++) {
//      sign = signs.get(i);
//      if(sign.getName().equals(key)){
//        sign.setText(value);
//        break;
//      }     
//    }

    Userset userset = null;
    for (int i = 0; i < usersets.size(); i++) {
      userset = usersets.get(i);
      if(userset.getName().equals(key)){
        userset.setText(value);
        break;
      }     
    }
  }

  /**
   * 得到Entity的所有属性
   */
  public List<String> getPropertyNames()
  {
    List<String> names = new ArrayList<String>();
    Attr attr = null;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      names.add(attr.getKey());
    }

//    Sign sign = null;
//    for (int i = 0; i < signs.size(); i++) {
//      sign = signs.get(i);
//      names.add(sign.getName());
//    }

    Userset userset = null;
    for (int i = 0; i < usersets.size(); i++) {
      userset = usersets.get(i);
      names.add(userset.getName());
    }
    return names;        
  }

  @Override
  public Entity clone() {
    try {
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream oo = new ObjectOutputStream(bo);
      oo.writeObject(this);
      ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
      ObjectInputStream oi = new ObjectInputStream(bi);
      return (Entity) oi.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this;
  }

  @Override
  protected List<ProposalType> getByKey(String key)
  {

    List<ProposalType> list = new ArrayList<ProposalType>();

    list.add(new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_KEY, "fields"));

    Attr attr = null;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, attr.getKey());
      proposalType.addValue(attr.getValue());
      proposalType.setNew(false);
      list.add(proposalType);
    }

    Userset userset = null;
    for (int i = 0; i < usersets.size(); i++) {
      userset = usersets.get(i);
      ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, userset.getName());
      proposalType.addValue(userset.getText());
      proposalType.setNew(true);
      list.add(proposalType);
    }

    return list;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    List<Model> list = new ArrayList<Model>();
//    List<Field> fields = getFields();
//    for (int i = 0; i < fields.size(); i++)
//    {
//      Field field = fields.get(i);
//      String id = field.getId();
//      if(id != null && id.equals(key)){
//        list.add(field);
//      }
//    }
    if("fields".equals(key)){
      List<Model> models = new ArrayList<Model>();
      for (int i = 0; i < fields.size(); i++)
      {
        Model field = fields.get(i);
        models.add(field);
      }
      Collect collect = new Collect(models);
      list.add(collect);
    }
    return list;
  }

}
