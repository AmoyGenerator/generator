/*
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.core.parser.ast;

/**
 * Represent an embedded expression in the template body
 *
 */
public class EmbeddedExpression extends BodyElement {

	private final String language;
	private final String expression;

	protected EmbeddedExpression(JETAST ast, int line, int column, int start,
			int end, String language, char[] content) {
		super(ast, line, column, start, end);
		this.language = language;
		this.expression = new String(content);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(org.eclipse.jet.core.parser.ast.JETASTVisitor)
	 */
	protected void accept0(JETASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#removeLineWhenOtherwiseEmpty()
	 */
	public boolean removeLineWhenOtherwiseEmpty() {
		return false;
	}

	/**
	 * Return the language of the embedded expression
	 * @return the language a string
	 */
	public final String getLanguage() {
		return language;
	}

	/**
	 * Return the embedded Expression
	 * @return the expression
	 */
	public final String getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return expression;
	}
}
