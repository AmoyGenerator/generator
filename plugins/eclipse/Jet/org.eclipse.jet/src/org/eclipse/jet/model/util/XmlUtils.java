/**
 * <copyright>
 *
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.jet.model.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlUtils
{

  public static List<ProposalType> getStringsByDocXPath(Document doc, String query) throws XPathExpressionException
  {
    List<ProposalType> proposalTypes = new ArrayList<ProposalType>();
    Object object = getObjectByDocXPath(doc, query);
    if(object instanceof NodeList){
      NodeList nodes = (NodeList) object;
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); j++)
        {
          Node attribute = attributes.item(j);
          if(!(attribute instanceof Text)){
            String nodeName = '@' + attribute.getNodeName();
            ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_XML, nodeName);
            proposalTypes.add(proposalType);
            String content = getXmlContent(node);
            proposalType.setInfo(content);
          }
        }
        NodeList children = node.getChildNodes();
        for (int j = 0; j < children.getLength(); j++)
        {
          Node child = children.item(j);
          if(!(child instanceof Text)){
            ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_XML, child.getNodeName());
            proposalTypes.add(proposalType);
            String content = getXmlContent(child);
            proposalType.setInfo(content);
          }
        }
      }
    }  
    return proposalTypes;
  }

  public static Object getObjectByDocXPath(final Document doc, String query) throws XPathExpressionException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
    factory.setNamespaceAware(true);
    String xmlNodeName = doc.getDocumentElement().getNodeName();
    XPathFactory pathFactory = XPathFactory.newInstance();   
    XPath xpath = pathFactory.newXPath();

    String queryXpath;
    if(!query.trim().isEmpty()){
      queryXpath = xmlNodeName + '/' + query;
    }else{
      queryXpath = xmlNodeName;
    }

    xpath.setNamespaceContext(new NamespaceContext()
    {

      public Iterator getPrefixes(String namespaceURI)
      {
        return null;
      }

      public String getPrefix(String namespaceURI)
      {
        return null;
      }

      public String getNamespaceURI(String prefix)
      {
        if(prefix != null){
          return getDocumentURIByPrefix(doc, prefix);
        }
        return null;
      }
    });

    XPathExpression pathExpression = xpath.compile(queryXpath);
    Object object = pathExpression.evaluate(doc, XPathConstants.NODESET);
    return object;
  }

  private static String getDocumentURIByPrefix(Node node, String prefix){
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

  public static String getXmlContent(Node node){

    if (node == null) {
      return null;
    }
    
    String input = null;
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      document.appendChild(document.importNode(node, true));
      
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
      e.printStackTrace();
    }
    return input;
  }
  
}
