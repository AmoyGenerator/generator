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
 * An JET2 AST element representing a comment
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.Comment}
 */
public class Comment extends JET2ASTElement
{

  private final org.eclipse.jet.core.parser.ast.Comment delegate;
 
  public Comment(JET2AST ast, org.eclipse.jet.core.parser.ast.Comment comment)
  {
    super(ast, comment);
    delegate = comment;
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);

  }

  /**
   * @return Returns the commentEnd.
   */
  public int getCommentEnd()
  {
    return delegate.getCommentEnd();
  }

  /**
   * @return Returns the commentStart.
   */
  public int getCommentStart()
  {
    return delegate.getCommentStart();
  }

  /**
   * Return the comment text
   * @return the comment text
   */
  public String getCommentText()
  {
    return delegate.getCommentText();
  }

}
