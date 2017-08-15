/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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

package org.eclipse.jet.compiler;



/**
 * Define an empty XML element in the JET2 AST.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.XMLEmptyElement}
 */
public final class XMLEmptyElement extends XMLElement
{


  private final org.eclipse.jet.core.parser.ast.XMLEmptyElement delegate;

  public XMLEmptyElement(JET2AST jet2ast, org.eclipse.jet.core.parser.ast.XMLEmptyElement element)
  {
    super(jet2ast, element);
    this.delegate = element;
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
  }

  public String toString()
  {
    return delegate.toString();
  }

}
