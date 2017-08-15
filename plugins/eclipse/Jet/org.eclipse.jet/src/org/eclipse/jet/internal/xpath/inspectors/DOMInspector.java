/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.jet.internal.xpath.inspectors;


import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.CopyElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * Implement a INodeInspector on org.w3c.dom.Node.
 *
 */
public class DOMInspector implements INodeInspector, IElementInspector
{

  private static final Object[] EMPTY_OBJECT_ARRAY = new Object []{};

  /**
   * 
   */
  public DOMInspector()
  {
    super();
  }

  public NodeKind getNodeKind(Object obj)
  {
    NodeKind result = null;
    if (obj instanceof Node)
    {
      Node node = (Node)obj;
      switch (node.getNodeType())
      {
        case Node.ATTRIBUTE_NODE:
        result = NodeKind.ATTRIBUTE;
          break;
        case Node.COMMENT_NODE:
        result = NodeKind.COMMENT;
          break;
        case Node.DOCUMENT_NODE:
        result = NodeKind.ROOT;
          break;
        case Node.ELEMENT_NODE:
        result = NodeKind.ELEMENT;
          break;
        case Node.PROCESSING_INSTRUCTION_NODE:
        result = NodeKind.PROCESSING_INSTRUCTION;
          break;
        case Node.CDATA_SECTION_NODE:
        case Node.TEXT_NODE:
        result = NodeKind.TEXT;
          break;
        // TODO How to we get Namespaces?
      }
    }
    return result;
  }

  public Object getParent(Object obj)
  {
    Object result = null;
    if (obj instanceof Node)
    {
      Node node = (Node)obj;
      switch (node.getNodeType())
      {
        case Node.ATTRIBUTE_NODE:
        result = ((Attr)node).getOwnerElement();
          break;
        default:
        result = node.getParentNode();
          break;
      }
    }
    return result;
  }

  public String stringValueOf(Object obj)
  {
    String result = null;
    if (obj instanceof Node)
    {
      Node node = (Node)obj;
      switch (node.getNodeType())
      {
        case Node.ATTRIBUTE_NODE:
        case Node.COMMENT_NODE:
        case Node.PROCESSING_INSTRUCTION_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.TEXT_NODE:
        result = node.getNodeValue();
          break;
        case Node.DOCUMENT_NODE:
        case Node.ELEMENT_NODE:
        StringBuffer buffer = new StringBuffer();
        textForNode(buffer, node);
        result = buffer.toString();
          break;
        // TODO How to we get Namespaces?
      }
    }
    return result;
  }

  private void textForNode(StringBuffer buffer, Node node)
  {
    NodeList childNodes = node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++)
    {
      Node childNode = childNodes.item(i);
      switch (childNode.getNodeType())
      {
        case Node.CDATA_SECTION_NODE:
        case Node.TEXT_NODE:
        buffer.append(childNode.getNodeValue());
          break;
        case Node.ELEMENT_NODE:
        textForNode(buffer, childNode);
      }
    }
  }

  public ExpandedName expandedNameOf(Object obj)
  {
    ExpandedName result = null;
    if (obj instanceof Node)
    {
      Node node = (Node)obj;
      switch (node.getNodeType())
      {
        case Node.ATTRIBUTE_NODE:
        case Node.ELEMENT_NODE:
        String localName = node.getLocalName();
        if (localName != null)
        {
          result = new ExpandedName(node.getNamespaceURI(), localName);
        }
        else
        {
          result = new ExpandedName(node.getNodeName());
        }
          break;
        case Node.PROCESSING_INSTRUCTION_NODE:
        result = new ExpandedName(node.getNodeName());
          break;
        // TODO How to we get Namespaces?
      }
    }
    return result;
  }

  public Object[] getAttributes(Object contextNode)
  {
    Object[] result = EMPTY_OBJECT_ARRAY;
    if (contextNode instanceof Node)
    {
      Node node = (Node)contextNode;
      // only elements will return a non-null result, don't bother checking
      // for elements explicitly...
      NamedNodeMap attributes = node.getAttributes();
      if (attributes != null)
      {
        result = new Object [attributes.getLength()];
        for (int i = 0; i < attributes.getLength(); i++)
        {
          result[i] = attributes.item(i);
        }
      }

    }
    return result;
  }

  public Object getDocumentRoot(Object contextNode)
  {
    Object result = null;
    if (contextNode instanceof Document)
    {
      result = contextNode;
    }
    else if (contextNode instanceof Node)
    {
      result = ((Node)contextNode).getOwnerDocument();
    }
    return result;
  }

  public Object[] getChildren(Object contextNode)
  {
    Object[] result = EMPTY_OBJECT_ARRAY;
    if (contextNode instanceof Node)
    {
      Node node = (Node)contextNode;
      switch (node.getNodeType())
      {
        case Node.DOCUMENT_NODE:
        case Node.ELEMENT_NODE:
        NodeList childNodes = node.getChildNodes();
        int nonAttributeChildCount = 0;
        for (int i = 0; i < childNodes.getLength(); i++)
        {
          if (childNodes.item(i).getNodeType() != Node.ATTRIBUTE_NODE)
          {
            ++nonAttributeChildCount;
          }
        }
        result = new Object [nonAttributeChildCount];
        int resultIndex = 0;
        for (int i = 0; i < childNodes.getLength(); i++)
        {
          if (childNodes.item(i).getNodeType() != Node.ATTRIBUTE_NODE)
          {
            result[resultIndex] = childNodes.item(i);
            ++resultIndex;
          }
        }
          break;
      }

    }
    return result;
  }

  public String nameOf(Object contextNode)
  {
    String result = null;
    if (contextNode instanceof Node)
    {
      Node node = (Node)contextNode;
      switch (node.getNodeType())
      {
        case Node.ATTRIBUTE_NODE:
        case Node.ELEMENT_NODE:
        case Node.PROCESSING_INSTRUCTION_NODE:
        result = node.getNodeName();
          break;
        // TODO How do we get Namespaces?
      }
    }
    return result;

  }

  public Object getNamedAttribute(Object contextNode, ExpandedName nameTestExpandedName)
  {
    Node node = (Node)contextNode;
    String nsURI = nameTestExpandedName.getNamespaceURI();
    if (nsURI == null)
    {
      Node namedItem = node.getAttributes().getNamedItem(nameTestExpandedName.getLocalPart());
      if(namedItem == null) {
        namedItem = node.getAttributes().getNamedItem(nameTestExpandedName.getLocalPart());
      }
      return namedItem;
    }
    else
    {
      return node.getAttributes().getNamedItemNS(nsURI, nameTestExpandedName.getLocalPart());
    }
  }

  public boolean createAttribute(Object contextNode, String attributeName, String value)
  {
    Element element = (Element)contextNode;
    element.setAttribute(attributeName, value);
    return true;
    //		return element.getAttribute(attributeName);
  }

  public Object addElement(Object contextNode, ExpandedName elementName, Object addBeforeThisSibling)
  {
    Element element = (Element)contextNode;
    if (addBeforeThisSibling != null && !(addBeforeThisSibling instanceof Node))
    {
      throw new IllegalStateException();
    }
    Node addBeforeThisNode = (Node)addBeforeThisSibling;
    String nsURI = elementName.getNamespaceURI();
    String localName = elementName.getLocalPart();

    Element newChild = null;
    if (nsURI == null)
    {
      newChild = element.getOwnerDocument().createElement(localName);
    }
    else
    {
      newChild = element.getOwnerDocument().createElementNS(nsURI, localName);
    }
    if (addBeforeThisSibling == null)
    {
      element.appendChild(newChild);
    }
    else
    {
      element.insertBefore(newChild, addBeforeThisNode);
    }
    return newChild;
  }

  public void removeElement(Object contextNode)
  {
    Element element = (Element)contextNode;

    Node parent = element.getParentNode();
    if (parent != null)
    {
      parent.removeChild(element);
    }
  }

  public Object copyElement(Object tgtParent, Object srcElement, String name, boolean recursive) throws CopyElementException
  {
    Element parentElement = (Element)tgtParent;

    Element newElement = null;
    final Document ownerDocument = parentElement.getOwnerDocument();
    if (srcElement instanceof Element)
    {
      Element source = (Element)srcElement;
      if (!name.equals(source.getLocalName()))
      {
        newElement = ownerDocument.createElement(name);
        NodeList childNodes = source.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
          newElement.appendChild(ownerDocument.importNode(childNodes.item(i), true));
        }
        NamedNodeMap attributes = source.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++) {
          newElement.setAttributeNodeNS((Attr)ownerDocument.importNode(attributes.item(i), false));
        }
        parentElement.appendChild(newElement);
        
//        throw new CopyElementException(JET2Messages.DOMInspector_InconsistentElementName);
      } else {
        newElement = (Element)ownerDocument.importNode(source, recursive);
        parentElement.appendChild(newElement);
      }


    }
    else if(srcElement instanceof Document) {
      newElement = ownerDocument.createElement(name);
      final NodeList childNodes = ((Document)srcElement).getChildNodes();
      for(int i = 0; i < childNodes.getLength(); i++) {
        newElement.appendChild(ownerDocument.importNode(childNodes.item(i), true));
      }
      parentElement.appendChild(newElement);
    }
    else
    {
      throw new CopyElementException(JET2Messages.DOMInspector_NotDOMObject);
    }
    return newElement;
  }

  public Object addTextElement(Object parentElementObject, String name, String bodyContent, boolean asCData) throws AddElementException
  {
    Element parentElement = (Element)parentElementObject;
    final Element newElement = parentElement.getOwnerDocument().createElement(name);
    Text text;
    if (asCData)
    {
      text = parentElement.getOwnerDocument().createTextNode(bodyContent);
    }
    else
    {
      text = parentElement.getOwnerDocument().createCDATASection(bodyContent);
    }
    newElement.appendChild(text);
    parentElement.appendChild(newElement);
    return newElement;
  }

  public void removeAttribute(Object object, String name)
  {
    Element element = (Element)object;
    element.removeAttribute(name);
  }

  public boolean testExpandedName(Object node, ExpandedName testName)
  {
    boolean match = testName.equals(expandedNameOf(node));
    if(!match) {
      String localName = ((Node)node).getLocalName();
      if(localName != null) {
        match = testName.equals(new ExpandedName(localName));
      }
    }
    return match;
  }

}
