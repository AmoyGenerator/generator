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
 * Implement a unary minus operation
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#NT-UnaryExpr">http://www.w3.org/TR/xpath#NT-UnaryExpr</a>.
 *
 */
public class UnaryMinus extends NumberExpr
{

  private final ExprNode right;

  /**
   * 
   */
  public UnaryMinus(ExprNode right)
  {
    super();
    this.right = right;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.NumberExpr#evalAsDouble(org.eclipse.jet.xpath.Context)
   */
  public double evalAsDouble(Context context)
  {
    double rightValue = NumberFunction.evaluate(right.evalAsObject(context));
    return -rightValue;
  }

  public String toString()
  {
    return "-(" + right + ")"; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
