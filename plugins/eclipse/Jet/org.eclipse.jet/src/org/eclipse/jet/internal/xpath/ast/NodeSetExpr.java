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


import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;


/**
 * Abstract class representing a subexpression returning a Node-set.
 *
 */
public abstract class NodeSetExpr extends ExprNode
{

  /**
   * 
   */
  public NodeSetExpr()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.ExprNode#evalAsObject(org.eclipse.jet.xpath.Context, org.eclipse.jet.xpath.INodeInspector)
   */
  public final Object evalAsObject(Context context)
  {
    return evalAsNodeSet(context);
  }

  public abstract NodeSet evalAsNodeSet(Context context);

}
