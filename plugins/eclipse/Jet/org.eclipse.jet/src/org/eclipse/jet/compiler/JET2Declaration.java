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

import org.eclipse.jet.core.parser.ast.JavaDeclaration;


/**
 * Define a Java Declaration Element in the JET2 AST
 * @deprecated Since 0.8.0, use {@link JavaDeclaration}
 *
 */
public final class JET2Declaration extends JavaElement
{

  public JET2Declaration(JET2AST jet2ast, JavaDeclaration declaration)
  {
    super(jet2ast, declaration);
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
  }

}
