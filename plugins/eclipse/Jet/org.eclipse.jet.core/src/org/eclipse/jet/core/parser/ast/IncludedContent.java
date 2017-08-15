/*
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.core.parser.ast;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Container for content included as a result of a JET V1 &lt;%&#064;include %&gt; directive.
 *
 */
public final class IncludedContent extends BodyElement {

	private final URI baseLocationURI;
	private BodyElements bodyElements = null;

	private final String templatePath;

	/**
	 * @param ast
	 * @param line
	 * @param column
	 * @param start
	 * @param end
	 */
	IncludedContent(JETAST ast, String templatePath, URI baseLocationURI, int line, int column, int start, int end) {
		super(ast, line, column, start, end);
		this.templatePath = templatePath;
		this.baseLocationURI = baseLocationURI;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.ast.JETASTElement#accept(org.eclipse.jet.tools.parser.ast.IJETASTVisitor)
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

	/**
	 * Return the base location URI of the template
	 * @return a location URI
	 */
	public URI getBaseLocationURI() {
		return baseLocationURI;
	}

	/**
	 * Return a read-only list of JET elements contained by this element.
	 * @return a List of {@link JETASTElement} instances. The empty list is returned if there are no elements.
	 */
	public final List getBodyElements() {
		if (bodyElements == null) {
			return Collections.EMPTY_LIST;
		} else {
			return bodyElements.getBodyElements();
		}
	}

	/**
	 * Return the template Path of the include content
	 * @return the template Path
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.ast.JETASTElement#removeLineWhenOtherwiseEmpty()
	 */
	public boolean removeLineWhenOtherwiseEmpty() {
		return false;
	}

	/**
	 * Add an JET AST element to the body of the include
	 * @param bodyElement
	 */
	public void addBodyElement(BodyElement bodyElement) {
		getInternalBodyElements().addBodyElement(bodyElement);
	
	}


	/**
	 * Find the element after the given element in the directly contained elements
	 * @param element a JET AST element
	 * @return a {@link BodyElement} or <code>null</code>
	 */
	public BodyElement elementAfter(JETASTElement element) {
		return bodyElements.elementAfter(element);
	}

	/**
	 * Find the element before the given element in the directly contained elements
	 * @param element a JET AST element
	 * @return a {@link BodyElement} or <code>null</code>
	 */
	public BodyElement elementBefore(JETASTElement element) {
		return bodyElements.elementBefore(element);
	}

	/**
	 * Return a object that allows writable access to the JET2 elements contained by this element.
	 * @return a BodyElements instance
	 */
	BodyElements getInternalBodyElements() {
		if (bodyElements == null) {
			bodyElements = new BodyElements(this);
		}
		return bodyElements;
	}

	public JETASTElement getNextElement() {
	
		if (getBodyElements().size() > 0) {
			return (JETASTElement) getBodyElements().get(0);
		} else {
			return super.getNextElement();
		}
	}

	
}
