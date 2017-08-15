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
 * $Id: XMLBodyElement.java,v 1.4 2007/06/01 20:26:19 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.taglib.TagDefinition;

/**
 * Define a JET XML Element that has a begin-tag and end-tag, and zero or more
 * body elements.
 * 
 * @since 0.8.0
 */
public final class XMLBodyElement extends XMLElement {

	private BodyElements bodyElements = null;

	private XMLBodyElementEnd endTag = null;

	/**
	 * Create an instance
	 * 
	 * @param ast
	 *            the owning AST
	 * @param line
	 *            the start line of the element
	 * @param colOffset
	 *            the offset within the line of the element's start.
	 * @param start
	 *            the start offset of the element (doc relative)
	 * @param end
	 *            the end offset of the element (doc relative)
	 * @param name
	 *            the QName of the element
	 * @param attributes
	 *            a Map of attribute names to their values of the element
	 * @param td
	 *            the TagDefinition
	 */
	XMLBodyElement(JETAST ast, int line, int colOffset, int start, int end,
			String name, Map attributes, TagDefinition td) {
		super(ast, line, colOffset, start, end, name, attributes, td);
	}

	/**
	 * Return a object that allows writable access to the JET2 elements
	 * contained by this element.
	 * 
	 * @return a BodyElements instance
	 */
	BodyElements getInternalBodyElements() {
		if (bodyElements == null) {
			bodyElements = new BodyElements(this);
		}
		return bodyElements;
	}

	/**
	 * Return a read-only list of JET2 elements contained by this element.
	 * 
	 * @return a List of {@link JETASTElement} instances. The empty list is
	 *         returned if there are no elements.
	 */
	public final List getBodyElements() {
		if (bodyElements == null) {
			return Collections.EMPTY_LIST;
		} else {
			return bodyElements.getBodyElements();
		}
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected final void accept0(JETASTVisitor visitor) {
		final boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			for (Iterator i = getBodyElements().iterator(); i.hasNext();) {
				JETASTElement element = (JETASTElement) i.next();
				element.accept(visitor);
			}
		}
		visitor.endVisit(this);

	}

	public void setEndTag(XMLBodyElementEnd endTag) {
		this.endTag = endTag;
	}

	/**
	 * @return Returns the endTag.
	 */
	public final XMLBodyElementEnd getEndTag() {
		return endTag;
	}

	public String toString() {
		return "Line " + getLine() + ": <" + getName() + ">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public JETASTElement getNextElement() {

		if (getBodyElements().size() > 0) {
			return (JETASTElement) getBodyElements().get(0);
		} else {
			return super.getNextElement();
		}
	}

	public void addBodyElement(BodyElement bodyElement) {
		getInternalBodyElements().addBodyElement(bodyElement);

	}

	public JETASTElement elementAfter(JETASTElement element) {
		return bodyElements.elementAfter(element);
	}

	public BodyElement elementBefore(JETASTElement element) {
		return bodyElements.elementBefore(element);
	}
}
