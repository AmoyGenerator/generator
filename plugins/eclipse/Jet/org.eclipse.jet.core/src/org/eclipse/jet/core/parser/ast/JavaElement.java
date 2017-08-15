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
 * $Id: JavaElement.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

/**
 * Abstract representation of JET AST elements that contain Java code
 *
 * <p>
 * This class is not intended to be subclassed by clients
 * </p>
 */
public abstract class JavaElement extends BodyElement {

	private final int javaStart;

	private final int javaEnd;

	private final char[] javaContent;

	/**
	 * Create a new instance
	 * @param ast the root AST to which the element belongs
	 * @param line the line in which the element begins
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the offset at which the element starts (doc relative)
	 * @param end the offset at which the element ends (doc relative)
	 * @param javaStart the offset at which the Java code starts (doc relative)
	 * @param javaEnd the offset at which the Java code ends (doc relative)
	 * @param javaContent the java code
	 */
	JavaElement(JETAST ast, int line, int colOffset, int start, int end,
			int javaStart, int javaEnd, char[] javaContent) {
		super(ast, line, colOffset, start, end);
		this.javaStart = javaStart;
		this.javaEnd = javaEnd;
		this.javaContent = javaContent;
	}

	/**
	 * The document relative offset of the Java code within the element.
	 * @return the start offset
	 */
	public final int getJavaStart() {
		return javaStart;
	}

	/**
	 * The document relative offset of the first character after the Java code. 
	 * @return the end offset
	 */
	public final int getJavaEnd() {
		return javaEnd;
	}

	/**
	 * Return the Java content of the element
	 * @return the Java content
	 */
	public String getJavaContent() {
		return new String(javaContent);
	}
}
