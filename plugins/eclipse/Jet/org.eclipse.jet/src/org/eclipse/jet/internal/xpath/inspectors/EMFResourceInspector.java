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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.INodeInspectorExtension1;


/**
 * XPath inspector for EMF Resource objects
 *
 */
public class EMFResourceInspector implements INodeInspectorExtension1, INodeInspector
{

  static final String CONTENTS_PSEUDO_ELEMENT = "contents"; //$NON-NLS-1$

  /**
   * 
   */
  public EMFResourceInspector()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspectorExtension1#getNamedChildren(java.lang.Object, org.eclipse.jet.xpath.ExpandedName)
   */
  public Object[] getNamedChildren(Object contextNode, ExpandedName nameTestExpandedName)
  {
    Resource resource = (Resource)contextNode;
    String testName = nameTestExpandedName.getLocalPart();
    if (CONTENTS_PSEUDO_ELEMENT.equals(testName))
    {
      return resource.getContents().toArray();
    }
    else
    {
      List children = new ArrayList(resource.getContents());
      for (Iterator i = children.iterator(); i.hasNext();)
      {
        EObject child = (EObject)i.next();
        if (!child.eClass().getName().equals(testName))
        {
          i.remove();
        }
      }
      return children.toArray();
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getNodeKind(java.lang.Object)
   */
  public NodeKind getNodeKind(Object obj)
  {
    return NodeKind.ROOT;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getParent(java.lang.Object)
   */
  public Object getParent(Object obj)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#stringValueOf(java.lang.Object)
   */
  public String stringValueOf(Object object)
  {
    return ""; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#expandedNameOf(java.lang.Object)
   */
  public ExpandedName expandedNameOf(Object object)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getDocumentRoot(java.lang.Object)
   */
  public Object getDocumentRoot(Object contextNode)
  {
    return contextNode;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object contextNode)
  {
    return ((Resource)contextNode).getContents().toArray();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#nameOf(java.lang.Object)
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
