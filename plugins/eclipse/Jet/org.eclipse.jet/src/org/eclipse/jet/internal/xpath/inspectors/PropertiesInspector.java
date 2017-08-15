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

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.CopyElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InvalidChildException;
import org.eclipse.jet.xpath.inspector.SimpleElementRequiresValueException;

/**
 * @author pelder
 */
public class PropertiesInspector implements INodeInspector, IElementInspector
{

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#expandedNameOf(java.lang.Object)
   */
  public ExpandedName expandedNameOf(Object node)
  {
    return new ExpandedName(""); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object node)
  {
    return new Object[0];
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
   */
  public Object getDocumentRoot(Object node)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
   */
  public NodeKind getNodeKind(Object obj)
  {
    return NodeKind.ELEMENT;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
   */
  public Object getParent(Object obj)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
   */
  public String nameOf(Object node)
  {
    return ""; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
   */
  public String stringValueOf(Object node)
  {
    return ""; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
   */
  public boolean testExpandedName(Object node, ExpandedName testName)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#addElement(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName, java.lang.Object)
   */
  public Object addElement(Object node, ExpandedName elementName, Object addBeforeThisSibling) throws SimpleElementRequiresValueException,
    InvalidChildException
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#addTextElement(java.lang.Object, java.lang.String, java.lang.String, boolean)
   */
  public Object addTextElement(Object parentElement, String name, String bodyContent, boolean asCData) throws AddElementException
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#copyElement(java.lang.Object, java.lang.Object, java.lang.String, boolean)
   */
  public Object copyElement(Object tgtParent, Object srcElement, String name, boolean recursive) throws CopyElementException
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#createAttribute(java.lang.Object, java.lang.String, java.lang.String)
   */
  public boolean createAttribute(Object node, String attributeName, String value)
  {
    Properties props = (Properties)node;
    props.setProperty(attributeName, value);
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#getAttributes(java.lang.Object)
   */
  public Object[] getAttributes(Object node)
  {
    Properties props = (Properties)node;
    Set keySet = props.keySet();
    
    Object[] result = new Object[keySet.size()];
    int index = 0;
    for (Iterator i = keySet.iterator(); i.hasNext(); index++)
    {
      String key = (String)i.next();
      
      result[index] = new AbstractWrappedAttribute(props, key) {

        public String getStringValue()
        {
          return ((Properties)getParent()).getProperty(getName());
        }};
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#getNamedAttribute(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
   */
  public Object getNamedAttribute(Object node, ExpandedName nameTestExpandedName)
  {
    Properties props = (Properties)node;
    String name = nameTestExpandedName.getLocalPart();
    if(props.containsKey(name)) {
      return new AbstractWrappedAttribute(props, name) {

        public String getStringValue()
        {
          return ((Properties)getParent()).getProperty(getName());
        }};
    }
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#removeAttribute(java.lang.Object, java.lang.String)
   */
  public void removeAttribute(Object node, String name)
  {
    Properties props = (Properties)node;
    props.remove(name);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.IElementInspector#removeElement(java.lang.Object)
   */
  public void removeElement(Object node)
  {
    throw new UnsupportedOperationException();
  }

}
