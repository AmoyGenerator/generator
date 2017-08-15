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
 * $Id$
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.xpath.ast;


import java.util.Iterator;

import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;


/**
 * Represent an XPath Step
 */
public class Step extends NodeSetExpr
{

  private static final Predicate[] EMPTY_PREDICATE_EXPRS = new Predicate[0];

  private final Axis axis;

  private final NodeTest nodeTest;

  private final NodeSetExpr leftLocationPath;

  private final Predicate[] predicates;

  /**
   * 
   */
  public Step(NodeSetExpr leftLocationPath, Axis axis, NodeTest nodeTest)
  {
    this(leftLocationPath, axis, nodeTest, EMPTY_PREDICATE_EXPRS);
  }

  /**
   * @param predicates TODO
   * 
   */
  public Step(NodeSetExpr leftLocationPath, Axis axis, NodeTest nodeTest, Predicate[] predicates)
  {
    super();
    this.leftLocationPath = leftLocationPath;
    this.axis = axis;
    this.nodeTest = nodeTest;
    this.predicates = predicates;
  }

  public NodeSet evalAsNodeSet(Context context)
  {

    NodeSet leftNodeSet = leftLocationPath.evalAsNodeSet(context);

    NodeSet result = NodeSetImpl.EMPTY_SET;

    int contextSize = leftNodeSet.size();
    int contextPosition = 1;
    for (Iterator i = leftNodeSet.iterator(); i.hasNext(); contextPosition++)
    {
      Object contextNode = (Object)i.next();
      Context subContext = context.newSubContext(contextNode, contextPosition, contextSize);

      NodeSet subResult = axis.evaluate(nodeTest, subContext);

      for (int j = 0; j < predicates.length; j++)
      {
        Predicate predicate = predicates[j];
        subResult = predicate.filter(context, subResult);
      }
      
      if (result.size() == 0 && subResult.size() > 0)
      {
        result = subResult; // save copying the set...
      }
      else if (subResult.size() > 0)
      {
        result.addAll(subResult);
      }
    }

    return result;
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(leftLocationPath).append("/").append(axis.toString()).append(nodeTest.toString()); //$NON-NLS-1$
    for (int i = 0; i < predicates.length; i++)
    {
      buffer.append(predicates[i].toString());
    }
    return buffer.toString();
  }
}
