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
 * A String literal in an expression.
 *
 */
public class StringLiteral extends StringNode
{

  private final String literal;

  /**
   * 
   */
  public StringLiteral(String literal)
  {
    super();
    this.literal = literal;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.StringNode#evalAsString(org.eclipse.jet.xpath.Context, org.eclipse.jet.xpath.INodeInspector)
   */
  public String evalAsString(Context context)
  {
    return literal;
  }

  public String getLiteral()
  {
    return literal;
  }

  public String toString()
  {
    String quote = literal.indexOf('\'') >= 0 ? "\"" : "'"; //$NON-NLS-1$//$NON-NLS-2$
    return quote + literal + quote;
  }
}
