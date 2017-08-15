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
 * Define a visitor to the JET2 AST tree.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.JETASTVisitor}
 */
public interface JET2ASTVisitor
{

  /**
   * Visit a JETCompilationUnit element, prior to visiting its body elements.
   * @param compilationUnit
   */
  public abstract void visit(JET2CompilationUnit compilationUnit);

  /**
   * Visit a JETCompilationUnit element, after visiting its body elements.
   * @param compilationUnit
   */
  public abstract void endVisit(JET2CompilationUnit compilationUnit);

  /**
   * Visit a JET2Declaration.
   * @param declaration
   */
  public abstract void visit(JET2Declaration declaration);

  /**
   * Visit a JET2Directive.
   * @param directive
   */
  public abstract void visit(JET2Directive directive);

  /**
   * Visit a JET2Expression.
   * @param expression
   */
  public abstract void visit(JET2Expression expression);

  /**
   * Visit a JET2Scriptlet.
   * @param scriptlet
   */
  public abstract void visit(JET2Scriptlet scriptlet);

  /**
   * Visit a TextElement.
   * @param text
   */
  public abstract void visit(TextElement text);

  /**
   * Visit an XMLEmptyElement.
   * @param xmlEmptyElement
   */
  public abstract void visit(XMLEmptyElement xmlEmptyElement);

  /**
   * Visit an XMLBodyElement, prior to visiting its body elements.
   * @param xmlBodyElement
   */
  public abstract void visit(XMLBodyElement xmlBodyElement);

  /**
   * Visit an XMLBodyElement, after visiting its body elements.
   * @param xmlBodyElement
   */
  public abstract void endVisit(XMLBodyElement xmlBodyElement);

  /**
   * Visit the end tag of an XMLBodyElement. Happens after {@link #endVisit(XMLBodyElement)}.
   * @param xmlBodyElementEnd
   */
  public abstract void visit(XMLBodyElementEnd xmlBodyElementEnd);
  
  /**
   * Visit a Comment element.
   * @param comment
   */
  public abstract void visit(Comment comment);

}
