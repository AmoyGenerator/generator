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


import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;


/**
 * Inspect EMFXMLNodeWrapper objects. This wraps EMF nodes that represent COMMENT or TEXT nodes.
 *
 */
public class EMFXMLNodeWrapperInspector implements INodeInspector
{

  private static final Object[] EMPTY_ARRAY = new Object []{};

  /**
   * 
   */
  public EMFXMLNodeWrapperInspector()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
   */
  public NodeKind getNodeKind(Object obj)
  {
    EMFXMLNodeWrapper wrapper = (EMFXMLNodeWrapper)obj;
    return wrapper.getNodeKind();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
   */
  public Object getParent(Object obj)
  {
    EMFXMLNodeWrapper wrapper = (EMFXMLNodeWrapper)obj;
    return wrapper.getParent();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
   */
  public String stringValueOf(Object object)
  {
    EMFXMLNodeWrapper wrapper = (EMFXMLNodeWrapper)object;
    return wrapper.getText();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#expandedNameOf(java.lang.Object)
   */
  public ExpandedName expandedNameOf(Object object)
  {
    // comment and text nodes have no expanded name
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
   */
  public Object getDocumentRoot(Object contextNode)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object contextNode)
  {
    return EMPTY_ARRAY;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
   */
  public String nameOf(Object contextNode)
  {
    return null;
  }

  public boolean testExpandedName(Object node, ExpandedName testName)
  {
    return testName.equals(expandedNameOf(node));
  }

}
