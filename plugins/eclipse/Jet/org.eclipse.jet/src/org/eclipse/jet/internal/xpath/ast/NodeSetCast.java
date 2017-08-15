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


import java.util.Collection;

import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathUtil;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Internally generated operation that casts is ExprNode argument to a NodeSet.
 *
 */
public class NodeSetCast extends NodeSetExpr
{

  private final ExprNode left;

  /**
   * 
   */
  public NodeSetCast(ExprNode left)
  {
    super();
    this.left = left;
  }

  public NodeSet evalAsNodeSet(Context context)
  {
    Object leftVal = left.evalAsObject(context);
    NodeSet result;
    if (leftVal instanceof String || leftVal instanceof Boolean || leftVal instanceof Number)
    {
      // These all convert to an empty NodeSet
      result = NodeSetImpl.EMPTY_SET;
    }
    else if (leftVal instanceof NodeSet)
    {
      return (NodeSet)leftVal;
    }
    else if(leftVal != null && leftVal.getClass().isArray()) {
      return XPathUtil.asNodeSet((Object[])leftVal);
    }
    else if(leftVal instanceof Collection) {
      return XPathUtil.asNodeSet((Collection)leftVal);
    }
    else
    {
      INodeInspector inspector = InspectorManager.getInstance().getInspector(leftVal);
      if (inspector != null && inspector.getNodeKind(leftVal) != null)
      {
        // an inspector recognized the object, so put it into a NodeSet
        NodeSet nodeSet = new NodeSetImpl(1);
        nodeSet.add(leftVal);
        result = nodeSet;
      }
      else
      {
        // we don't know what we have, map it to an empty node set.
        result = new NodeSetImpl();
      }
    }
    return result;
  }

  public String toString()
  {
    return left.toString();
  }
}
