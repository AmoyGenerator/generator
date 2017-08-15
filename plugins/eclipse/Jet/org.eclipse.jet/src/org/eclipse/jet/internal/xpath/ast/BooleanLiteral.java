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
 * Define boolean literals
 *
 */
public class BooleanLiteral extends BooleanExpr
{

  private final boolean value;

  public static final BooleanLiteral TRUE = new BooleanLiteral(true);

  public static final BooleanLiteral FALSE = new BooleanLiteral(false);

  /**
   * 
   */
  private BooleanLiteral(boolean value)
  {
    super();
    this.value = value;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.BooleanExpr#evalAsBoolean(org.eclipse.jet.xpath.Context)
   */
  public boolean evalAsBoolean(Context context)
  {
    return value;
  }

  /**
   * Return the boolean literal corresponding to the passed boolean value.
   * @param value <code>true</code> or <code>false</code>
   * @return {@link #TRUE} or {@link #FALSE}
   */
  public static BooleanLiteral literalFor(boolean value)
  {
    return value ? TRUE : FALSE;
  }
}
