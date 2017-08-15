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


import org.eclipse.jet.internal.xpath.functions.BooleanFunction;
import org.eclipse.jet.xpath.Context;


/**
 * Abstract implementation of logical operations 'or' and 'and'
 *
 */
public abstract class LogicalOp extends BooleanExpr
{

  protected final ExprNode left;

  protected final ExprNode right;

  public static final class Or extends LogicalOp
  {

    public Or(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    public boolean evalAsBoolean(Context context)
    {
      return eval(context, left) || eval(context, right);
    }

    public String opName()
    {
      return " or "; //$NON-NLS-1$
    }

  }

  public static final class And extends LogicalOp
  {

    public And(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    public boolean evalAsBoolean(Context context)
    {
      return eval(context, left) && eval(context, right);
    }

    public String opName()
    {
      return " and "; //$NON-NLS-1$
    }

  }

  /**
   * 
   */
  public LogicalOp(ExprNode left, ExprNode right)
  {
    super();
    this.left = left;
    this.right = right;
  }

  protected final boolean eval(Context context, ExprNode node)
  {
    if (node instanceof BooleanExpr)
    {
      return ((BooleanExpr)node).evalAsBoolean(context);
    }
    else
    {
      Object result = node.evalAsObject(context);
      return BooleanFunction.evaluate(result);
    }
  }

  public abstract String opName();

  public String toString()
  {
    return "(" + left.toString() + opName() + right.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
  }

}
