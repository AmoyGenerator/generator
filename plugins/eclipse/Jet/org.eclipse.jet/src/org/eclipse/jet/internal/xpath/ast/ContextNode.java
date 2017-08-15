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


/**
 * Represent value of the passed context as a node set..
 *
 */
public class ContextNode extends NodeSetExpr
{

  /**
   * 
   */
  public ContextNode()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.NodeSetNode#evalAsNodeSet(org.eclipse.jet.xpath.Context, org.eclipse.jet.xpath.INodeInspector)
   */
  public NodeSet evalAsNodeSet(Context context)
  {
    NodeSet result = new NodeSetImpl(1);
    result.add(context.getContextNode());
    return result;
  }

  public String toString()
  {
    return "<ctx>"; //$NON-NLS-1$
  }
}
