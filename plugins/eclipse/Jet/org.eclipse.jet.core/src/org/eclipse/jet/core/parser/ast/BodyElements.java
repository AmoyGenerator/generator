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
 * $Id: BodyElements.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container class core JET AST elements contained in another AST elements in
 * their body.
 * 
 */
class BodyElements {

	private List bodyElements = new ArrayList();

	private final JETASTElement owner;

	/**
	 * Create a new instance
	 * 
	 * @param owner
	 *            The owner of the body elements
	 */
	public BodyElements(JETASTElement owner) {
		super();
		this.owner = owner;
	}

	/**
	 * Return a list of elements in the body
	 * 
	 * @return a {@link List} of elements, the empty list if there are no
	 *         elements.
	 */
	public List getBodyElements() {
		return Collections.unmodifiableList(bodyElements);
	}

	/**
	 * Add a body element to the body
	 * 
	 * @param bodyElement
	 *            the text to add
	 */
	public void addBodyElement(BodyElement bodyElement) {
		bodyElements.add(bodyElement);
		bodyElement.setParent(owner);
	}

	/**
	 * @return Returns the owner.
	 */
	public final JETASTElement getOwner() {
		return owner;
	}

	/**
	 * Return the element after the passed element
	 * 
	 * @param element
	 *            an element in the list
	 * @return the next element or <code>null</code>
	 * @throws IllegalArgumentException
	 *             if element is not in the body
	 */
	public BodyElement elementAfter(JETASTElement element) {
		BodyElement nextElement = null;
		if (bodyElements == null) {
			throw new IllegalArgumentException();
		}
		int index = bodyElements.indexOf(element);
		if (index == -1) {
			throw new IllegalArgumentException();
		}

		if (index + 1 < bodyElements.size()) {
			nextElement = (BodyElement) bodyElements.get(index + 1);
		}
		return nextElement;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public BodyElement elementBefore(JETASTElement element) {
		BodyElement prevElement = null;
		if (bodyElements == null) {
			throw new IllegalArgumentException();
		}
		int index = bodyElements.indexOf(element);
		if (index == -1) {
			throw new IllegalArgumentException();
		}

		if (index > 0) {
			prevElement = (BodyElement) bodyElements.get(index - 1);
		}
		return prevElement;
	}

}
