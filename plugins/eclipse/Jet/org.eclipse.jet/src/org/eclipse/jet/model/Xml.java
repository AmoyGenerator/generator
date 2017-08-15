package org.eclipse.jet.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.model.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Xml extends Model implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String name;
  private String relation;

  /** 路径 */
  private String path;

  /** 扩展名是否是xml */
  private boolean isExtension;

  private String exceptionMessage;
  private Exception exception;	

  public Xml(){
    super(ModelTagEnum.XML.getValue());  
    path = "";
  }

  public Xml(String name, String relation, String path){
    super(ModelTagEnum.XML.getValue());
    setName(name);
    setRelation(relation);
    if(path != null){
      this.path = path;
    }else{
      path = "";
    }

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
    super.setAttr(ModelTagAttrEnum.XML_NAME.getValue(), value);
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
    super.setAttr(ModelTagAttrEnum.XML_RELATION.getValue(), value);
    this.relation = value;
  }


  public String getRootTagName(){
    //return document.getDocumentElement().getNodeName();
    return null;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public static Document importDocument(String path) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);//命名空间支持
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setEntityResolver(new EntityResolver()
    {
      public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
      {
        return new InputSource(new ByteArrayInputStream("".getBytes())); 
      }
    });
    String uri = path;
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IResource resource = root.findMember(new Path(path));
    if(resource != null){
      uri = resource.getLocation().toOSString();
    }
//    //工作空间
//    String fullPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()+ "/" + path;
    return builder.parse(uri);
  }

  @Override
  public Document getSourceDocument()
  {
    Document document = super.getSourceDocument();
    Element element = document.createElement(nodeName);
    document.appendChild(element);

    Attr attr;  
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      element.setAttribute(attr.getKey(), attr.getValue());
    }

    try
    {
      Document xmlDocument = Xml.importDocument(path);
      Node xmlNode = xmlDocument.getDocumentElement();
      Node importNode = document.importNode(xmlNode, true);
      element.appendChild(importNode);
    }catch (Exception e){
      exception = e;
      exceptionMessage = e.getMessage();
    } 

    return document;
  }


  public Document getDocument()
  {
    Document document = super.getSourceDocument();
    Element element = document.createElement(nodeName);
    document.appendChild(element);

    Attr attr;  
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      element.setAttribute(attr.getKey(), attr.getValue());
    }

    Element pathElement = document.createElement(ModelTagEnum.XML_PATH.getValue());
    pathElement.setTextContent(path);
    element.appendChild(pathElement);

    return document;
  }

  @Override
  public Xml clone() {
    try {
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream oo = new ObjectOutputStream(bo);
      oo.writeObject(this);
      ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
      ObjectInputStream oi = new ObjectInputStream(bi);
      return (Xml) oi.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    return null;
  }

  @Override
  protected List<ProposalType> getByKey(String key)
  {
    return null;
  }

  private String getDocumentURIByPrefix(Node node, String prefix){
    String documentURI = null;
    if(node != null && prefix != null){
      String nodePrefix = node.getPrefix();
      if(nodePrefix != null && nodePrefix.equals(prefix)){
        documentURI = node.getNamespaceURI();
      }
      if(documentURI == null){
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
          Node child = nodeList.item(i);
          documentURI = getDocumentURIByPrefix(child, prefix);
          if(documentURI != null){
            break;
          }
        }
      }
    }
    return documentURI;
  }
}
