/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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

import java.util.Iterator;

import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.internal.xpath.functions.BooleanFunction;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;

/**
 * Represent a filtering predicate [...].
 */
public final class Predicate
{
  private final ExprNode expr;

  public Predicate(ExprNode expr) {
    this.expr = expr;
  }

  /**
   * Apply the predicate to the node set represented by nodeSet
   * @param context
   * @param nodeSet
   * @return
   */
  public NodeSet filter(Context context, NodeSet nodeSet) {
    int contextPosition = 1;
    final int contextSize = nodeSet.size();
    NodeSet result = new NodeSetImpl(contextSize);
    for (Iterator i = nodeSet.iterator(); i.hasNext(); contextPosition++)
    {
      Object node = (Object)i.next();
      Context subContext = context.newSubContext(node, contextPosition, contextSize);
      Object exprVal = expr.evalAsObject(subContext);
      if (exprVal instanceof Number)
      {
        if (((Number)exprVal).doubleValue() == contextPosition)
        {
          result.add(node);
        }
      }
      else if (BooleanFunction.evaluate(exprVal))
      {
        result.add(node);
      }

    }

    return result;
    
  }
  
  public String toString()
  {
    return "[" + expr.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
