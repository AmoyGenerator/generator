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
 * Abstract root type of all XPath AST nodes.
 *
 */
public abstract class ExprNode
{

  /**
   * 
   */
  public ExprNode()
  {
    super();
  }

  public abstract Object evalAsObject(Context context);

  //	public abstract boolean evalAsBoolean(Context context);

  //	public abstract String evalAsString(Context context);

  //	public abstract double evalAsDouble(Context context);

  //	public abstract Set evalAsNodeSet(Context context);
}
