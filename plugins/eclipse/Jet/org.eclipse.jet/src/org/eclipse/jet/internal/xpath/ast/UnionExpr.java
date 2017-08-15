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
 * Implement the XPath union operator ('|').
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#node-sets">http://www.w3.org/TR/xpath#node-sets</a>.
 *
 */
public class UnionExpr extends NodeSetExpr
{

  private final NodeSetExpr left;

  private final NodeSetExpr right;

  /**
   * 
   */
  public UnionExpr(NodeSetExpr left, NodeSetExpr right)
  {
    super();
    this.left = left;
    this.right = right;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.NodeSetExpr#evalAsNodeSet(org.eclipse.jet.xpath.Context)
   */
  public NodeSet evalAsNodeSet(Context context)
  {
    NodeSet leftSet = left.evalAsNodeSet(context);
    NodeSet rightSet = right.evalAsNodeSet(context);
    NodeSet unionSet = new NodeSetImpl(leftSet.size() + rightSet.size());

    // TODO This doesn't take into accound maintaining document order.
    unionSet.addAll(leftSet);
    unionSet.addAll(rightSet);

    return unionSet;
  }

  public String toString()
  {
    return left.toString() + "|" + right.toString(); //$NON-NLS-1$
  }
}
