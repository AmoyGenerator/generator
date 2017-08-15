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
 * An implementation of the {@link org.eclipse.jet.compiler.JET2ASTVisitor} that does nothing.
 * Override any of the visit or endVisit methods to create an operational visitor.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.JETASTVisitor}
 *
 */
public abstract class DefaultJET2ASTVisitor implements JET2ASTVisitor
{

  /**
   * 
   */
  public DefaultJET2ASTVisitor()
  {
    super();
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2CompilationUnit)
   */
  public void visit(JET2CompilationUnit compilationUnit)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#endVisit(org.eclipse.jet.compiler.JET2CompilationUnit)
   */
  public void endVisit(JET2CompilationUnit compilationUnit)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2Declaration)
   */
  public void visit(JET2Declaration declaration)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2Directive)
   */
  public void visit(JET2Directive directive)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2Expression)
   */
  public void visit(JET2Expression expression)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2Scriptlet)
   */
  public void visit(JET2Scriptlet scriptlet)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.TextElement)
   */
  public void visit(TextElement text)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.XMLEmptyElement)
   */
  public void visit(XMLEmptyElement xmlEmptyElement)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.XMLBodyElement)
   */
  public void visit(XMLBodyElement xmlBodyElement)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#endVisit(org.eclipse.jet.compiler.XMLBodyElement)
   */
  public void endVisit(XMLBodyElement xmlBodyElement)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.XMLBodyElementEnd)
   */
  public void visit(XMLBodyElementEnd xmlBodyElementEnd)
  {
    // do nothing
  }
  
  /**
   * @see org.eclipse.jet.compiler.JET2ASTVisitor#visit(org.eclipse.jet.compiler.Comment)
   */
  public void visit(Comment comment)
  {
    // do nothing
  }

}
