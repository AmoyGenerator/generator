package org.eclipse.jet.model;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.jet.model.util.Index;
import org.eclipse.jet.model.util.ProposalType;
import org.w3c.dom.Document;


public abstract class Model implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** 属性列表 */
  protected List<Attr> attrs = new ArrayList<Attr>();

  /**节点名*/
  protected String nodeName;

  protected Model parent;

  public Model(String nodeName) {
    this.nodeName = nodeName;
  }

  /**
   * 得到父节点
   * @return 父节点
   */
  public Model getParent() {
    return parent;
  }

  /**
   * 设置父节点
   * @param parent
   */
  public void setParent(Model parent){
    this.parent = parent; 
  }

  public Document getSourceDocument() {		
    try {
      // 创建Document对象
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder;
      builder = factory.newDocumentBuilder();
      return builder.newDocument();
    } catch (ParserConfigurationException e) {
      return null;
    }

  }

  public Document getDocument(){
    try {
      // 创建Document对象
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder;
      builder = factory.newDocumentBuilder();
      return builder.newDocument();
    } catch (ParserConfigurationException e) {
      return null;
    }
  }
  
  public List<ProposalType> getByIndexModel(Index index)
  {
    List<ProposalType> list = new ArrayList<ProposalType>();
    if(index != null){
      String content = index.getContent();
      Index right = index.getRight();
      if(content != null){
       if(right != null){
         String rightContent = right.getContent();
        if(right.getRight() == null){
          List<ProposalType> result = getByKey(rightContent);
          if(result != null){
            list.addAll(result);
          }
        }else{
          List<Model> models = getModelByKey(rightContent);
          if(models != null){
            for (int i = 0; i < models.size(); i++)
            {
              Model model = models.get(i);
              List<ProposalType> result = model.getByIndexModel(right);
              list.addAll(result);
            }
          }
        }
       }
      }
    }
    return list;
  }
  
  protected abstract List<ProposalType> getByKey(String key);
  
  protected abstract List<Model> getModelByKey(String key);
  
  public String getNodeName() {
    return nodeName;
  }

  public void setAttr(String nodeName, String value){
    Attr attr;
    boolean hasAttr = false;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      if(attr.getKey().equals(nodeName)){
        attr.setValue(value);
        hasAttr = true;
        break;
      }
    }
    if(!hasAttr){
      attrs.add(new Attr(nodeName, value, false));
    }
  }

  /**
   * 根据属性名得到属性值
   */	
  public Attr getAttr(String key){
    Attr attr = null;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      if(attr.getKey().equals(key)){
        return attr;
      }		
    }
    return null;
  }

  /**
   * 得到属性值列表
   */
  public List<Attr> getAttrs(){
    return attrs;	
  }

  /**
   * 设置属性值列表
   * @param attrs
   */
  public void setAttrs(List<Attr> attrs) {
    this.attrs = attrs;
  }

  /**
   * 删除属性
   */
  public void removeAttr(Attr attr){
    attrs.remove(attr);
  }

  /**
   * 删除属性
   */
  public void removeAttr(String key){
    Attr attr;
    for (int i = 0; i < attrs.size(); i++) {
      attr = attrs.get(i);
      if(key.equals(attr.getKey())){
        attrs.remove(attr);
        break;
      }
    }	
  }
  
  /**
   * 把xml文本对象转换成符合jet模型的字符串
   * @throws TransformerException 
   * @throws TransformerException 
   * @throws IOException 
   */
  public String getJetXmlInput(){


    Document document = getSourceDocument();
    if (document == null) {
      return null;
    }

    String input = null;
    // DOMImplementationLS domImplLS =
    // (DOMImplementationLS)document.getImplementation();
    //
    // LSSerializer serializer = domImplLS.createLSSerializer();
    // input = serializer.writeToString(document.getDocumentElement());

    try
    {
      DOMSource source = new DOMSource(document);
      StringWriter writer = new StringWriter();
      Result result = new StreamResult(writer);
      Transformer transformer;
      TransformerFactory tf = TransformerFactory.newInstance();
      tf.setAttribute("indent-number", new Integer(2));
      transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.transform(source, result);

      input = writer.getBuffer().toString();
      
      writer.close();
    }
    catch (Exception e)
    {
    }
    return input;
  }
  
}
