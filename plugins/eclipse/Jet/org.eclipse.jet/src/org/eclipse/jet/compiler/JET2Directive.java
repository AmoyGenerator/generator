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


import java.util.Map;

import org.eclipse.jet.core.parser.ast.JETDirective;


/**
 * Define a Directive Element in the JET2 AST
 * @deprecated Since 0.8.0, use {@link JETDirective}
 */
public final class JET2Directive extends JET2ASTElement
{

  private final JETDirective delegate;

  public JET2Directive(JET2AST jet2ast, JETDirective directive)
  {
    super(jet2ast, directive);
    this.delegate = directive;
  }

  /**
   * Return the delegate name
   * @return the delegate name
   */
  public final String getName()
  {
    return delegate.getName();
  }

  /**
   * Return a Map the delegate attribute names to values
   * @return a Map of the delegate attributes (unmodifiable)
   */
  public final Map getAttributes()
  {
    return delegate.getAttributes();
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
  }
}
