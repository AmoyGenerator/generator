/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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
 * $Id: XMLBodyElementEnd.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

import java.util.List;

/**
 * Represent the closing tag of an XML Tag with a body.
 * 
 * @since 0.8.0
 */
public class XMLBodyElementEnd extends BodyElement {

	private XMLBodyElement startTag;

	/**
	 * 
	 * @param ast
	 * @param line
	 * @param colOffset
	 * @param start
	 * @param end
	 * @param startTag
	 */
	XMLBodyElementEnd(JETAST ast, int line, int colOffset, int start, int end) {
		super(ast, line, colOffset, start, end);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
	 */
	protected void accept0(JETASTVisitor visitor) {
	    visitor.visit(this);
	    visitor.endVisit(this);
	}

	/**
	 * @return Returns the startTag.
	 */
	public final XMLBodyElement getStartTag() {
		return startTag;
	}

	public boolean removeLineWhenOtherwiseEmpty() {
		return startTag == null ? false : startTag
				.removeLineWhenOtherwiseEmpty();
	}

	/**
	 * @param startTag The startTag to set.
	 */
	public final void setStartTag(XMLBodyElement startTag) {
		this.startTag = startTag;
	}

	public JETASTElement getPrevElement() {
		final List bodyElements = startTag.getBodyElements();
		if (bodyElements.size() > 0) {
			return (JETASTElement) bodyElements.get(bodyElements.size() - 1);
		} else {
			return startTag;
		}
	}

	public String toString() {
		return "Line " + getLine() + ": </" + getStartTag().getName() + ">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
