/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * $Id: JETASTVisitor.java,v 1.3 2009/04/06 17:55:06 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

/**
 * A visitor for a JETAST tree. This implementation provides 'do nothing' actions for each visit method.
 * Subclasses need override only the visit methods required.
 * 
 * @see JETASTVisitor
 * @since 0.8.0
 *
 */
public abstract class JETASTVisitor {

	/**
	 * 
	 */
	public JETASTVisitor() {
		super();
	}

	/**
	 * Visit a JETCompilationUnit element, prior to visiting its body elements.
	 * @param compilationUnit
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(JETCompilationUnit compilationUnit) {
		return true;
	}

	/**
	 * Visit a JETCompilationUnit element, after visiting its body elements.
	 * @param compilationUnit
	 */
	public void endVisit(JETCompilationUnit compilationUnit) {
		// do nothing
	}

	/**
	 * Visit a JavaDeclaration.
	 * @param declaration the JavaDeclaration element
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(JavaDeclaration declaration) {
		return true;
	}

	/**
	 * Visit a JETDirective.
	 * @param directive the JETDirective element
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(JETDirective directive) {
		return true;
	}

	/**
	 * Visit a JavaExpression.
	 * @param expression
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(JavaExpression expression) {
		return true;
	}

	/**
	 * Visit a JavaScriptlet.
	 * @param scriptlet
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(JavaScriptlet scriptlet) {
		return true;
	}

	/**
	 * Visit a TextElement.
	 * @param text
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(TextElement text) {
		return true;
	}

	/**
	 * Visit an XMLEmptyElement.
	 * @param xmlEmptyElement
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(XMLEmptyElement xmlEmptyElement) {
		return true;
	}

	/**
	 * Visit an XMLBodyElement, prior to visiting its body elements.
	 * @param xmlBodyElement
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(XMLBodyElement xmlBodyElement) {
		return true;
	}

	/**
	 * Visit an XMLBodyElement, after visiting its body elements.
	 * @param xmlBodyElement
	 */
	public void endVisit(XMLBodyElement xmlBodyElement) {
		// do nothing
	}

	/**
	 * Visit the end tag of an XMLBodyElement. Happens after {@link #endVisit(XMLBodyElement)}.
	 * @param xmlBodyElementEnd
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(XMLBodyElementEnd xmlBodyElementEnd) {
		return true;
	}

	/**
	 * Visit a Comment element.
	 * @param comment
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(Comment comment) {
		return true;
	}

	/**
	 * Visit a section of included content (JET1 only)
	 * @param content the included content element
	 * @return <code>true</code> if the children of this element should be
	 * visited, and <code>false</code> if the children of this element should
	 * be skipped
	 */
	public boolean visit(IncludedContent content) {
		return true;
	}

	/**
	 * Post visit a section of included content (JET1 only)
	 * @param content the included content element
	 */
	public void endVisit(IncludedContent content) {
		// Do nothing
	}
	

	/**
	 * Visit a JavaDeclaration element, after visiting its children.
	 * @param declaration the JavaDeclaration
	 */
	public void endVisit(JavaDeclaration declaration) {
		// Do nothing
	}
	

	/**
	 * Visit a JETDirective element, after visiting its children.
	 * @param directive the JETDirective element
	 */
	public void endVisit(JETDirective directive) {
		// Do nothing
	}
	

	/**
	 * Visit a JavaExpression element, after visiting its children.
	 * @param expression the JavaExpression element
	 */
	public void endVisit(JavaExpression expression) {
		// Do nothing
	}
	

	/**
	 * Visit a JavaScriptlet element, after visiting its children.
	 * @param scriptlet the JavaScriptlet element
	 */
	public void endVisit(JavaScriptlet scriptlet) {
		// Do nothing
	}
	

	/**
	 * Visit a TextElement element, after visiting its children.
	 * @param text the TextElement element
	 */
	public void endVisit(TextElement text) {
		// Do nothing
	}
	

	/**
	 * Visit a XMLEmptyElement element, after visiting its children.
	 * @param xmlEmptyElement the XMLEmptyElement element
	 */
	public void endVisit(XMLEmptyElement xmlEmptyElement) {
		// Do nothing
	}
	

	/**
	 * Visit a XMLBodyElementEnd element, after visiting its children.
	 * @param xmlBodyElementEnd the XMLBodyElementEnd element
	 */
	public void endVisit(XMLBodyElementEnd xmlBodyElementEnd) {
		// Do nothing
	}
	

	/**
	 * Visit a Comment element, after visiting its children.
	 * @param comment the Comment element
	 */
	public void endVisit(Comment comment) {
		// Do nothing
	}
	

	/**
	 * Visits the AST Node after all type specific visit/end visits
	 * @param element the AST element
	 */
	public void postVisit(JETASTElement element) {
		// Do nothing
	}
	

	/**
	 * Visits an AST Node prior to a type specific visit
	 * @param element the AST element
	 */
	public void preVisit(JETASTElement element) {
		// Do nothing
	}

	/**
	 * @param embeddedExpression
	 * @since 0.10.0
	 */
	public boolean visit(EmbeddedExpression embeddedExpression) {
		return true;
	}

	/**
	 * @param embeddedExpression
	 * @since 0.10.0
	 */
	public void endVisit(EmbeddedExpression embeddedExpression) {
		// Do nothing
	}

}
