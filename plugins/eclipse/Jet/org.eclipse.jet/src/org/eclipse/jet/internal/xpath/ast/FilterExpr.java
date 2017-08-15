/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: FilterExpr.java,v 1.2 2007/05/16 19:35:18 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.xpath.ast;


import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;


/**
 * Represent an XPath predicate expression
 *
 */
public class FilterExpr extends NodeSetExpr
{

  private final Predicate predicate;

  private final NodeSetExpr leftNodeSet;

  /**
   * 
   */
  public FilterExpr(NodeSetExpr leftNodeSet, Predicate predicate)
  {
    super();
    this.leftNodeSet = leftNodeSet;
    this.predicate = predicate;
  }

  public NodeSet evalAsNodeSet(Context context)
  {
    NodeSet nodeSet = leftNodeSet.evalAsNodeSet(context);
    return predicate.filter(context, nodeSet);
  }

  public String toString()
  {
    return leftNodeSet.toString() + predicate;
  }

}
