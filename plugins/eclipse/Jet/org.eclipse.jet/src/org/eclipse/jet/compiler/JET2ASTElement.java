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
 * An abstract class representing common aspects of all JET2 AST elements.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.JETASTElement}
 */
public abstract class JET2ASTElement
{

  private JET2AST ast;

  private org.eclipse.jet.core.parser.ast.JETASTElement delegate;

  /**
   * @return Returns the parent.
   */
  public final JET2ASTElement getParent()
  {
    return ast.wrap(delegate.getParent());
  }

  public JET2ASTElement(JET2AST ast, org.eclipse.jet.core.parser.ast.JETASTElement delegate)
  {
    this.ast = ast;
    this.delegate = delegate;
  }

  /**
   * The document relative offset of the start of the element. 
   * @return the start offset
   */
  public final int getStart()
  {
    return delegate.getStart();
  }

  /**
   * The document relative offset of the first character after the element.
   * @return the end offset
   */
  public final int getEnd()
  {
    return delegate.getEnd();
  }

  /**
   * Visit the AST and its contained elements.
   * @param visitor
   */
  public abstract void accept(JET2ASTVisitor visitor);

  /**
   * Return the AST root object
   * @return the AST root object
   * @since 0.8.0
   */
  public JET2AST getAst()
  {
    return ast;
  }

  /**
   * Return the line (one-based) on which the element starts.
   * @return the line number.
   */
  public final int getLine()
  {
    return delegate.getLine();
  }

  public JET2ASTElement getNextElement()
  {
    return ast.wrap(delegate.getNextElement());
  }
  
  public JET2ASTElement getPrevElement()
  {
    return ast.wrap(delegate.getPrevElement());
  }
  /**
   * Return the column number (one-based) at which the element starts.
   * @return the column number.
   */
  public final int getColumn()
  {
    return delegate.getColumn();
  }

  /**
   * Indicate whether the the surrounding whitespace, including the trailing new line
   * should be removed from the template output. In general, elements that create
   * output should return <code>false</code>, 
   * while element that do should should return <code>true</code>.
   * @return <code>true</code> if the containing line should be removed if otherwise empty.
   */
  public final boolean removeLineWhenOtherwiseEmpty()
  {
    return delegate.removeLineWhenOtherwiseEmpty();
  }

  protected void setDelegate(org.eclipse.jet.core.parser.ast.JETCompilationUnit delegate)
  {
    this.delegate = delegate;
    this.ast = new JET2AST(delegate.getAst());
  }
}
