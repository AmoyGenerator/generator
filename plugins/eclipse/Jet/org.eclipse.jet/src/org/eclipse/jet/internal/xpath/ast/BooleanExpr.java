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
 * Abstract AST node for boolean expressions.
 *
 */
public abstract class BooleanExpr extends ExprNode
{

  /**
   * 
   */
  public BooleanExpr()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.ExprNode#evalAsObject(org.eclipse.jet.xpath.Context, org.eclipse.jet.xpath.INodeInspector)
   */
  public Object evalAsObject(Context context)
  {
    return Boolean.valueOf(evalAsBoolean(context));
  }

  public abstract boolean evalAsBoolean(Context context);

}
