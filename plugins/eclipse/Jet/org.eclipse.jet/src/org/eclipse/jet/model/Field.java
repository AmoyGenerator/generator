package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Field extends Model {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**是否是新设属性(内置属性)*/ 
  private String isNew;
  /**识别字段ID(唯一)*/
  private String id;
  /**是否被使用*/
  private String isUsed;
  /**字段名*/
  private String name;
  /**默认值*/
  private String defaultValue;
  /**db类型*/
  private String dbType;
  /**jdbc类型*/
  private String jdbcType;
  /**java类型*/
  private String fullType;
  /**java类型*/
  private String type;

  /** data列表 */
  private List<Data> datas = new ArrayList<Data>();

  /** database列表 */
  private List<Database> databases = new ArrayList<Database>();

  /** table列表 */
  private List<Table> tables = new ArrayList<Table>();


//  /** sign列表 */
//  private List<Sign> signs = new ArrayList<Sign>();

  /** userset列表 */
  private List<Userset> usersets = new ArrayList<Userset>();

  public Field(boolean isNew){	
    super(ModelTagEnum.FIELD.getValue());	
    setIsNew(isNew);
  }

  public Field() {
    super(ModelTagEnum.FIELD.getValue());
  }

  public Field(boolean isNew, String id, String isUsed, String dbType, String jdbcType, String fullType, String name, String defaultValue, String type) {
    super(ModelTagEnum.FIELD.getValue());
    setIsNew(isNew);
    setId(id);
    setIsUsed(isUsed);
    setDbType(dbType);
    setJdbcType(jdbcType);
    setFullType(fullType);
    setName(name);
    setDefaultValue(defaultValue);
    setType(type);
  }

  /**
   * 得到entity所在的Group,没有则返回null
   * @return
   */
  public Group getGroup(){
    Entity entity = getEntity();
    if(entity != null){
      entity.getGroup();
    }
    return null;
  }

  /**
   * 得到field的Entity,没有则返回null
   * @return
   */
  public Entity getEntity(){
    if(parent instanceof Entity){
      return (Entity) parent;
    }
    return null;
  }


  public boolean getIsNew() {
    if(isNew != null && isNew.equals("true")){
      return true;
    }else{
      return false;
    }		
  }

  public void setIsNew(boolean value) {
    if(value == true){
      super.setAttr(ModelTagAttrEnum.FIELD_IS_NEW.getValue(), "true");
      this.isNew = "true";
    }else{
      super.setAttr(ModelTagAttrEnum.FIELD_IS_NEW.getValue(), "false");
      this.isNew = "false";
    }	
  }

  public String getId() {
    return id;
  }

  public void setId(String value) {
    super.setAttr(ModelTagAttrEnum.FIELD_ID.getValue(), value);
    id = value;
  }

  public String getIsUsed() {
    return isUsed;
  }

  public String getDbType() {
    return dbType;
  }

  public void setDbType(String value) {
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_DB_TYPE.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_DB_TYPE.getValue());
    }
    dbType = value;
  }

  public void setIsUsed(String value) {
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_IS_USED.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_IS_USED.getValue());
    }
    isUsed = value;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public void setJdbcType(String value) {
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_JDBC_TYPE.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_JDBC_TYPE.getValue());
    }
    jdbcType = value;
  }

  public String getFullType() {
    return fullType;
  }

  public void setFullType(String value) {
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_FULL_TYPE.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_FULL_TYPE.getValue());
    }
    fullType = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String value) {
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_NAME.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_NAME.getValue());
    }
    name = value;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String value) {	
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_DEFAULT_VALUE.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_DEFAULT_VALUE.getValue());
    }
    defaultValue = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String value) {
    if(value != null){
      super.setAttr(ModelTagAttrEnum.FIELD_TYPE.getValue(), value);
    }else{
      super.removeAttr(ModelTagAttrEnum.FIELD_TYPE.getValue());
    }
    type = value;
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

    Data data;
    for (int i = 0; i < datas.size(); i++) {
      data = datas.get(i);
      Document dataDocument = data.getSourceDocument();
      Node dataNode = dataDocument.getDocumentElement();
      Node importNode = document.importNode(dataNode, true);
      element.appendChild(importNode);
    }

    Database database;
    for (int i = 0; i < databases.size(); i++) {
      database = databases.get(i);
      Document databaseDocument = database.getSourceDocument();
      Node databaseNode = databaseDocument.getDocumentElement();
      Node importNode = document.importNode(databaseNode, true);
      element.appendChild(importNode);
    }

    Table table;
    for (int i = 0; i < tables.size(); i++) {
      table = tables.get(i);
      Document tableDocument = table.getSourceDocument();
      Node tableNode = tableDocument.getDocumentElement();
      Node importNode = document.importNode(tableNode, true);
      element.appendChild(importNode);
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

    return document;
  }

  /**
   * 查找字段包含的属性名(只读取自己所含有的属性)
   * @return
   */
  public List<String> getPropertyNames(){
    List<String> names = new ArrayList<String>();
    names.add(ModelTagAttrEnum.FIELD_ID.getValue());
    names.add(ModelTagAttrEnum.FIELD_IS_USED.getValue());
    names.add(ModelTagAttrEnum.FIELD_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_DB_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_JDBC_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_FULL_TYPE.getValue());
    names.add(ModelTagAttrEnum.FIELD_NAME.getValue());
    names.add(ModelTagAttrEnum.FIELD_DEFAULT_VALUE.getValue());
    List<Database> databases = getDatabases();
    for (int i = 0; i < databases.size(); i++) {
      names.add(databases.get(i).getName());
    }
    List<Table> tables = getTables();
    for (int i = 0; i < tables.size(); i++) {
      names.add(tables.get(i).getName());
    }
//    List<Sign> signs = getSigns();
//    for (int i = 0; i < signs.size(); i++) {
//      names.add(signs.get(i).getName());
//    }
    List<Userset> usersets = getUsersets();
    for (int i = 0; i < usersets.size(); i++) {
      names.add(usersets.get(i).getName());
    }
    return names;
  }
  /**
   * 查找字段包含的私有属性名
   * @return
   */
  public List<String> getDataNames(){
    List<String> names = new ArrayList<String>();
    for (int i = 0; i < datas.size(); i++){
      names.add(datas.get(i).getName());
    }
    return names;
  }

  public List<Data> getDatas() {
    return datas;
  }

  /**
   * 根据序号得到元素
   */
  public Data getData(int index){
    return datas.get(index);
  }

  /**
   * 得到第一个元素
   */
  public Data getFirstData(){
    return datas.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Data getLastData(){
    int size = datas.size();
    if(size > 0){
      return datas.get(size - 1);
    }
    return null;
  }

  public void addData(Data data){			
    data.setParent(this);
    datas.add(data);			
  }

  public void add(int index, Data data){
    data.setParent(this);
    datas.add(index, data);
  }

  public void addAllData(Collection<? extends Data> c){
    Iterator<? extends Data> it = c.iterator();
    Data data;
    while(it.hasNext()){
      data = it.next();
      data.setParent(this);
    }
    datas.addAll(c);
  }

  public void addAllData(int index, Collection<? extends Data> c){
    Iterator<? extends Data> it = c.iterator();
    Data data;
    while(it.hasNext()){
      data = it.next();
      data.setParent(this);
    }
    datas.addAll(index, c);
  }

  public void removeData(int index){
    datas.remove(index);
  }

  public void removeData(Data data){
    datas.remove(data);	
  }

  public void removeAllData(Collection<?> c){
    datas.removeAll(c);
  }


  public List<Database> getDatabases() {
    return databases;
  }

  /**
   * 根据序号得到元素
   */
  public Database getDatabase(int index){
    return databases.get(index);
  }

  /**
   * 得到第一个元素
   */
  public Database getFirstDatabase(){
    return databases.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Database getLastDatabase(){
    int size = databases.size();
    if(size > 0){
      return databases.get(size - 1);
    }
    return null;
  }

  public void addDatabase(Database database){           
    database.setParent(this);
    databases.add(database);            
  }

  public void add(int index, Database database){
    database.setParent(this);
    databases.add(index, database);
  }

  public void addAllDatabase(Collection<? extends Database> c){
    Iterator<? extends Database> it = c.iterator();
    Database database;
    while(it.hasNext()){
      database = it.next();
      database.setParent(this);
    }
    databases.addAll(c);
  }

  public void addAllDatabase(int index, Collection<? extends Database> c){
    Iterator<? extends Database> it = c.iterator();
    Database database;
    while(it.hasNext()){
      database = it.next();
      database.setParent(this);
    }
    databases.addAll(index, c);
  }

  public void removeDatabase(int index){
    databases.remove(index);
  }

  public void removeDatabase(Database database){
    databases.remove(database); 
  }

  public void removeAllDatabase(Collection<?> c){
    databases.removeAll(c);
  }


  public List<Table> getTables() {
    return tables;
  }

  /**
   * 根据序号得到元素
   */
  public Table getTable(int index){
    return tables.get(index);
  }

  /**
   * 得到第一个元素
   */
  public Table getFirstTable(){
    return tables.get(0);
  }

  /**
   * 得到最后一个元素
   */
  public Table getLastTable(){
    int size = tables.size();
    if(size > 0){
      return tables.get(size - 1);
    }
    return null;
  }

  public void addTable(Table table){			
    table.setParent(this);
    tables.add(table);			
  }

  public void add(int index, Table table){
    table.setParent(this);
    tables.add(index, table);
  }

  public void addAllTable(Collection<? extends Table> c){
    Iterator<? extends Table> it = c.iterator();
    Table table;
    while(it.hasNext()){
      table = it.next();
      table.setParent(this);
    }
    tables.addAll(c);
  }

  public void addAllTable(int index, Collection<? extends Table> c){
    Iterator<? extends Table> it = c.iterator();
    Table table;
    while(it.hasNext()){
      table = it.next();
      table.setParent(this);
    }
    tables.addAll(index, c);
  }

  public void removeTable(int index){
    tables.remove(index);
  }

  public void removeTable(Table table){
    tables.remove(table);	
  }

  public void removeAllTable(Collection<?> c){
    tables.removeAll(c);
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

  public Userset findUsersetByName(String name){
    if(name == null){
      return null;
    }
    for (int i = 0; i < usersets.size(); i++) {
      Userset userset = usersets.get(i);
      String usersetName = userset.getName();
      if(usersetName != null && usersetName.equals(name)){
         return userset;
      }
    }
    return null;
  }
  
  /**
   * 得到Field的所有属性
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

    Database database = null;
    for (int i = 0; i < databases.size(); i++) {
      database = databases.get(i);
      if(database.getName().equals(key)){
        return database.getText();
      }     
    }

    Table table = null;
    for (int i = 0; i < tables.size(); i++) {
      table = tables.get(i);
      if(table.getName().equals(key)){
        return table.getText();
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
   * 根据key设置Field的属性值
   */
  public void setProperty(String key, String value, boolean create)
  {

    if(ModelTagAttrEnum.FIELD_IS_USED.getValue().equals(key)){
      setIsUsed(value);
      return;
    }else if(ModelTagAttrEnum.FIELD_DB_TYPE.getValue().equals(key)){
      setJdbcType(value);
      return;
    }else if(ModelTagAttrEnum.FIELD_JDBC_TYPE.getValue().equals(key)){
      setJdbcType(value);
      return;
    }else if(ModelTagAttrEnum.FIELD_FULL_TYPE.getValue().equals(key)){
      setFullType(value);
      return;
    }else if(ModelTagAttrEnum.FIELD_NAME.getValue().equals(key)){
      setName(value);
      return;
    }else if(ModelTagAttrEnum.FIELD_DEFAULT_VALUE.getValue().equals(key)){
      setDefaultValue(value);
      return;
    }else if(ModelTagAttrEnum.FIELD_TYPE.getValue().equals(key)){
      setType(value);
      return;
    }

    Database database = null;
    for (int i = 0; i < databases.size(); i++) {
      database = databases.get(i);
      if(database.getName().equals(key)){
        database.setText(value);
        return;
      }     
    }

//    Sign sign = null;
//    for (int i = 0; i < signs.size(); i++) {
//      sign = signs.get(i);
//      if(sign.getName().equals(key)){
//        sign.setText(value);
//        return;
//      }     
//    }

    Userset userset = null;
    for (int i = 0; i < usersets.size(); i++) {
      userset = usersets.get(i);
      if(userset.getName().equals(key)){
        userset.setText(value);
        return;
      }     
      
    }

    if(create){
      addUserset(new Userset(key, value));
    }

  }

  /**
   * 得到Field的所有私有属性
   */
  public Data getData(String key)
  {

    Data data = null;
    for (int i = 0; i < datas.size(); i++) {
      data = datas.get(i);
      if(data.getName().equals(key)){
        return data;
      }     
    }

    return null;        
  }

  /**
   * 根据key设置Field的私有属性值
   */
  public void setData(String key, String value)
  {
    Data data = null;
    for (int i = 0; i < datas.size(); i++) {
      data = datas.get(i);
      if(data.getName().equals(key)){
        data.setText(value);
        return;
      }     
    }
  }

  @Override
  protected List<ProposalType> getByKey(String key)
  {
    List<ProposalType> list = new ArrayList<ProposalType>();

    list.add(new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_KEY, "datas"));
    
    ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_ID.getValue());
    proposalType.addValue(getId());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_IS_USED.getValue());
    proposalType.addValue(getIsUsed());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_TYPE.getValue());
    proposalType.addValue(getType());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_DB_TYPE.getValue());
    proposalType.addValue(getDbType());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_JDBC_TYPE.getValue());
    proposalType.addValue(getJdbcType());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_FULL_TYPE.getValue());
    proposalType.addValue(getFullType());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_NAME.getValue());
    proposalType.addValue(getName());
    proposalType.setNew(false);
    list.add(proposalType);
    proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, ModelTagAttrEnum.FIELD_DEFAULT_VALUE.getValue());
    proposalType.addValue(getDefaultValue());
    proposalType.setNew(false);
    list.add(proposalType);
    List<Database> databases = getDatabases();
    for (int i = 0; i < databases.size(); i++) {
      proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, databases.get(i).getName());
      proposalType.addValue(databases.get(i).getText());
      proposalType.setNew(false);
      list.add(proposalType);
    }
    List<Table> tables = getTables();
    for (int i = 0; i < tables.size(); i++) {
      proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, tables.get(i).getName());
      proposalType.addValue(tables.get(i).getText());
      proposalType.setNew(false);
      list.add(proposalType);
    }
    List<Userset> usersets = getUsersets();
    for (int i = 0; i < usersets.size(); i++) {
      proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, usersets.get(i).getName());
      proposalType.addValue(usersets.get(i).getText());
      proposalType.setNew(true);
      list.add(proposalType);
    }
    return list;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    List<Model> list = new ArrayList<Model>();
    if("datas".equals(key)){
      List<Model> models = new ArrayList<Model>();
      for (int i = 0; i < datas.size(); i++)
      {
        Model data = datas.get(i);
        models.add(data);
      }
      Collect collect = new Collect(models);
      list.add(collect);
    }
    return list;
  }

}
