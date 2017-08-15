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
 * $Id: JETDirective.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Define a Directive Element in the JET AST
 * 
 */
public final class JETDirective extends BodyElement {

	private final String name;

	private final Map attributes;

	/**
	 * Create an instance
	 * 
	 * @param ast
	 *            the owning JET2 AST
	 * @param line
	 *            the start line of the directive
	 * @param colOffset
	 *            the offset within the line of the element's start.
	 * @param start
	 *            the start offset of the directive (doc relative)
	 * @param end
	 *            the end offset of the directive (doc relative)
	 * @param name
	 *            the directive name
	 * @param attributes
	 *            a Map of attribute names to attribute values for the directive
	 */
	JETDirective(JETAST ast, int line, int colOffset, int start, int end,
			String name, Map attributes) {
		super(ast, line, colOffset, start, end);
		this.name = name;
		this.attributes = Collections.unmodifiableMap(new LinkedHashMap(
				attributes));
	}

	/**
	 * Return the directive name
	 * 
	 * @return the directive name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Return a Map the directive attribute names to values
	 * 
	 * @return a Map of the directive attributes (unmodifiable)
	 */
	public final Map getAttributes() {
		return attributes;
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected void accept0(JETASTVisitor visitor) {
	    visitor.visit(this);
	    visitor.endVisit(this);
	}

	public boolean removeLineWhenOtherwiseEmpty() {
		return true;
	}

}
