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
package org.eclipse.jet.internal.xpath.ast;


import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.inspector.INodeInspector;


/**
 * Represent the document root ('/') in an XPath expression.
 *
 */
public class Root extends NodeSetExpr
{

  /**
   * 
   */
  public Root()
  {
    super();
  }

  public NodeSet evalAsNodeSet(Context context)
  {
    Object contextNode = context.getContextNode();
    if (contextNode == null)
    {
      return NodeSetImpl.EMPTY_SET;
    }
    
    INodeInspector inspector = context.getContextNodeInspector();
    if(inspector == null)
    {
      return NodeSetImpl.EMPTY_SET;
    }
    Object root = inspector.getDocumentRoot(contextNode);

    if (root != null)
    {
      NodeSet result = new NodeSetImpl(1);
      result.add(root);
      return result;
    }
    else
    {
      return NodeSetImpl.EMPTY_SET;
    }
  }

  public String toString()
  {
    return "<root>"; //$NON-NLS-1$
  }
}
