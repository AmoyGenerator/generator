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


import org.eclipse.jet.internal.xpath.functions.NumberFunction;
import org.eclipse.jet.xpath.Context;


/**
 * Abstract class defining Binary numeric operations.
 *
 */
public abstract class BinaryOp extends NumberExpr
{

  public static final class Add extends BinaryOp
  {

    public Add(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    protected double doEval(double left, double right)
    {
      return left + right;
    }

    protected String opName()
    {
      return "+"; //$NON-NLS-1$
    }

  }

  public static final class Subtract extends BinaryOp
  {

    public Subtract(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    protected double doEval(double left, double right)
    {
      return left - right;
    }

    protected String opName()
    {
      return " - "; //$NON-NLS-1$
    }

  }

  public static final class Multiply extends BinaryOp
  {

    public Multiply(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    protected double doEval(double left, double right)
    {
      return left * right;
    }

    protected String opName()
    {
      return "*"; //$NON-NLS-1$
    }
  }

  public static final class Div extends BinaryOp
  {

    public Div(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    protected double doEval(double left, double right)
    {
      return left / right;
    }

    protected String opName()
    {
      return " div "; //$NON-NLS-1$
    }

  }

  public static final class Mod extends BinaryOp
  {

    public Mod(ExprNode left, ExprNode right)
    {
      super(left, right);
    }

    protected double doEval(double left, double right)
    {
      return left % right;
    }

    protected String opName()
    {
      return " mod "; //$NON-NLS-1$
    }

  }

  private final ExprNode left;

  private final ExprNode right;

  /**
   * 
   */
  public BinaryOp(ExprNode left, ExprNode right)
  {
    super();
    this.left = left;
    this.right = right;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.NumberNode#evalAsDouble(org.eclipse.jet.xpath.Context)
   */
  public final double evalAsDouble(Context context)
  {
    double dLeft = evalAsNumber(context, left);
    double dRight = evalAsNumber(context, right);
    return doEval(dLeft, dRight);
  }

  private final double evalAsNumber(Context context, ExprNode node)
  {
    double result = Double.NaN;
    if (node instanceof NumberExpr)
    {
      result = ((NumberExpr)node).evalAsDouble(context);
    }
    else
    {
      Object obj = node.evalAsObject(context);
      result = NumberFunction.evaluate(obj);
    }
    return result;
  }

  protected abstract double doEval(double left, double right);

  protected abstract String opName();

  public String toString()
  {
    return "(" + left.toString() + opName() + right.toString() + ")"; //$NON-NLS-1$//$NON-NLS-2$
  }

}
