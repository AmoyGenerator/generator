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
 * $Id: JETASTElement.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

/**
 * An abstract class representing common aspects of all JET AST elements.
 * 
 * <p>
 * This class is not intended to be subclassed by clients
 * </p>
 */
public abstract class JETASTElement {

	private final int start;

	private final int end;

	private final JETAST ast;

	private final int line;

	private JETASTElement parent = null;

	private final int column;

	/**
	 * @return Returns the parent.
	 */
	public final JETASTElement getParent() {
		return parent;
	}

	/**
	 * Set the parent element
	 * 
	 * @param parent
	 *            The parent to set.
	 */
	final void setParent(JETASTElement parent) {
		this.parent = parent;
	}

	/**
	 * Construct a new AST element
	 * 
	 * @param ast
	 *            the AST root
	 * @param line
	 *            the line of the element
	 * @param column
	 *            the one-based offset within the line of the element's start.
	 * @param start
	 *            the start offset of the element
	 * @param end
	 *            the end offset of the element
	 * 
	 */
	JETASTElement(JETAST ast, int line, int column, int start, int end) {
		super();
		this.ast = ast;
		this.column = column;
		this.line = line;
		this.start = start;
		this.end = end;
	}

	/**
	 * The document relative offset of the start of the element.
	 * 
	 * @return the start offset
	 */
	public final int getStart() {
		return start;
	}

	/**
	 * The document relative offset of the first character after the element.
	 * 
	 * @return the end offset
	 */
	public final int getEnd() {
		return end;
	}

	/**
	 * Visit the AST and its contained elements.
	 * 
	 * @param visitor
	 */
	public final void accept(JETASTVisitor visitor) {
		visitor.preVisit(this);
		accept0(visitor);
		visitor.postVisit(this);
	}

	/**
	 * Visit the AST and its contained elements.
	 * 
	 * @param visitor
	 */
	protected abstract void accept0(JETASTVisitor visitor);

	/**
	 * Return the AST root object
	 * 
	 * @return the AST root object
	 * @since 0.8.0
	 */
	public JETAST getAst() {
		return ast;
	}

	/**
	 * Return the line (one-based) on which the element starts.
	 * 
	 * @return the line number.
	 */
	public final int getLine() {
		return line;
	}

	/**
	 * @param element
	 *            The element for which body elements are sought
	 * @return the body elements object
	 */
	private BodyElements getBodyElements(JETASTElement element) {
		BodyElements bodyElements = null;
		if (parent instanceof JETCompilationUnit) {
			bodyElements = ((JETCompilationUnit) parent)
					.getInternalBodyElements();
		} else if (parent instanceof XMLBodyElement) {
			bodyElements = ((XMLBodyElement) parent).getInternalBodyElements();
		}
		return bodyElements;
	}

	public JETASTElement getNextElement() {
		JETASTElement next = null;
		if (parent != null) {
			BodyElements bodyElements = getBodyElements(parent);
			if (bodyElements != null) {
				next = bodyElements.elementAfter(this);
			} else {
				next = parent.getNextElement();
			}

		}
		return next;
	}

	public JETASTElement getPrevElement() {
		JETASTElement prev = null;
		if (parent != null) {
			BodyElements bodyElements = getBodyElements(parent);
			if (bodyElements != null) {
				prev = bodyElements.elementBefore(this);
			} else {
				prev = parent.getPrevElement();
			}

		}
		return prev;
	}

	/**
	 * Return the column number (one-based) at which the element starts.
	 * 
	 * @return the column number.
	 */
	public final int getColumn() {
		return column;
	}

	/**
	 * Indicate whether the the surrounding whitespace, including the trailing
	 * new line should be removed from the template output. In general, elements
	 * that create output should return <code>false</code>, while element
	 * that do should should return <code>true</code>.
	 * 
	 * @return <code>true</code> if the containing line should be removed if
	 *         otherwise empty.
	 */
	public abstract boolean removeLineWhenOtherwiseEmpty();
}
