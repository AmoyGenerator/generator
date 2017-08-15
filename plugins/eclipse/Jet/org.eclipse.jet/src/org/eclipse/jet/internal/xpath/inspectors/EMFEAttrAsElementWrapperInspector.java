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


import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.InvalidChildException;
import org.eclipse.jet.xpath.inspector.SimpleElementRequiresValueException;


/**
 * XPath Inspector for {@link org.eclipse.jet.internal.xpath.inspectors.EMFEAttrAsElementWrapper EMFEAttrAsElementWrapper}.
 *
 */
public class EMFEAttrAsElementWrapperInspector extends EObjectInspector implements IElementInspector
{

  /**
   * 
   */
  public EMFEAttrAsElementWrapperInspector()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getNodeKind(java.lang.Object)
   */
  public NodeKind getNodeKind(Object obj)
  {
    return NodeKind.ELEMENT;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getParent(java.lang.Object)
   */
  public Object getParent(Object obj)
  {
    EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)obj;
    return wrapper.getEObject();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#stringValueOf(java.lang.Object)
   */
  public String stringValueOf(Object object)
  {
    EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)object;
    return stringValueOfFeature(wrapper.getFeature(), wrapper.getValue());
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#expandedNameOf(java.lang.Object)
   */
  public ExpandedName expandedNameOf(Object object)
  {
    EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)object;
    return expandedNameOfFeature(wrapper.getEObject(), wrapper.getFeature());
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getDocumentRoot(java.lang.Object)
   */
  public Object getDocumentRoot(Object contextNode)
  {
    EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)contextNode;
    return super.getDocumentRoot(wrapper.getEObject());
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object contextNode)
  {
    // this is a simple element, which has a single text node as a child, containing its value...
    return new Object[] { new EMFXMLNodeWrapper(contextNode, stringValueOf(contextNode), NodeKind.TEXT)};
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#nameOf(java.lang.Object)
   */
  public String nameOf(Object contextNode)
  {
    EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)contextNode;
    return nameOfFeature(wrapper.getEObject(), wrapper.getFeature());
  }

  public Object addElement(Object contextNode, ExpandedName elementName, Object addBeforeThisSibling) throws SimpleElementRequiresValueException,
    InvalidChildException
  {
    return null;
  }

  public void removeElement(Object contextNode)
  {
    EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)contextNode;
    wrapper.remove();
  }
  
  public Object[] getAttributes(Object contextNode)
  {
    return new Object[0];
  }
  
  public Object[] getNamedChildren(Object contextNode, ExpandedName nameTestExpandedName)
  {
    return new Object[0];
  }
  
  public void removeAttribute(Object element, String name)
  {
    // do nothing
  }
  
  public Object addTextElement(Object parentElement, String name, String bodyContent, boolean asCData) throws AddElementException
  {
    // FIXME Externalize
    throw new AddElementException("Cannot add child element to simple types");
  }
  
  public boolean createAttribute(Object contextNode, String attributeName, String value)
  {
    return false;
  }
  
  public Object getNamedAttribute(Object contextNode, ExpandedName nameTestExpandedName)
  {
    return null;
  }
  
  
}
