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


/**
 * A Number literal in an XPath expression
 *
 */
public class NumberLiteral extends NumberExpr
{

  private final double literal;

  /**
   * 
   */
  public NumberLiteral(double literal)
  {
    super();
    this.literal = literal;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.NumberNode#evalAsDouble(org.eclipse.jet.xpath.Context, org.eclipse.jet.xpath.INodeInspector)
   */
  public double evalAsDouble(Context context)
  {
    return literal;
  }

  public double getDoubleValue()
  {
    return literal;
  }

  public String toString()
  {
    return String.valueOf(literal);
  }
}
