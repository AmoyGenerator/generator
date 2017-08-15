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
 * $Id: XMLEmptyElement.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

import java.util.Map;

import org.eclipse.jet.taglib.TagDefinition;

/**
 * Define an empty XML element in the JET AST.
 *
 * @since 0.8.0
 */
public final class XMLEmptyElement extends XMLElement {

	/**
	 * Create an instance
	 * @param ast the owning AST
	 * @param line the start line of the element
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param name the QName of the element
	 * @param attributes a Map of attribute names to values for the element.
	 * @param td the TagDefinition
	 */
	XMLEmptyElement(JETAST ast, int line, int colOffset, int start, int end,
			String name, Map attributes, TagDefinition td) {
		super(ast, line, colOffset, start, end, name, attributes, td);
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected void accept0(JETASTVisitor visitor) {
	    visitor.visit(this);
	    visitor.endVisit(this);
	}

	public String toString() {
		return "Line " + getLine() + ": <" + getName() + "/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
