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
 * $Id: BodyElement.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

/**
 * Abstract element representing elements in the body of a compilation unit or another tag.
 * 
 * <p>
 * This class is not intended to be subclassed by clients
 * </p>
 *
 */
public abstract class BodyElement extends JETASTElement {

	
	/**
	 * initialize a BodyElement
	 * @param ast the parent AST
	 * @param line the start line of the element
	 * @param column the start column
	 * @param start the zero-based offset of the first char of the element from the start of the document
	 * @param end the zero-based offset of the next char after the element from the start of the document 
	 */
	protected BodyElement(JETAST ast, int line, int column, int start, int end) {
		super(ast, line, column, start, end);
	}

}
