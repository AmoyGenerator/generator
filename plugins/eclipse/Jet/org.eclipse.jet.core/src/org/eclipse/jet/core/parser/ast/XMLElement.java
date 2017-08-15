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
 * $Id: XMLElement.java,v 1.3 2007/05/17 14:18:10 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jet.taglib.TagDefinition;

/**
 * An abstract implementation representing all XML-based elements in the JET AST
 * 
 * <p>
 * This class is not intended to be subclassed by clients
 * </p>
 * 
 * @since 0.8.0
 */
public abstract class XMLElement extends BodyElement {

	private final String name;

	private final Map attributes;

	private final TagDefinition tagDefinition;

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
	 *            a Map of attribute names to values for the element.
	 * @param td
	 *            the TagDefinition
	 */
	XMLElement(JETAST ast, int line, int colOffset, int start, int end,
			String name, Map attributes, TagDefinition td) {
		super(ast, line, colOffset, start, end);
		this.name = name;
		this.tagDefinition = td;
		this.attributes = Collections.unmodifiableMap(new LinkedHashMap(attributes)); // defensive
																				// copy

	}

	/**
	 * Return the QName of the element
	 * 
	 * @return a string
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Return a read-only map of the attributes (name to value map)
	 * 
	 * @return a Map with String keys (attribute name) and String values
	 */
	public final Map getAttributes() {
		return attributes;
	}

	/**
	 * Return the NCName (unqualified name) of the element.
	 * 
	 * @return the name with any XML namespace prefix (<i>prefix</i>:) removed
	 */
	public String getTagNCName() {
		String qName = getName();
		int sepIndex = qName.indexOf(':');
		return sepIndex >= 0 ? qName.substring(sepIndex + 1) : qName;
	}

	/**
	 * Return the XML Namespace prefixe of the element
	 * 
	 * @return the namespace prefix or the empty string if there is no namespace
	 *         prefix.
	 */
	public String getNSPrefix() {
		String qName = getName();
		int sepIndex = qName.indexOf(':');
		return sepIndex >= 0 ? qName.substring(0, sepIndex) : ""; //$NON-NLS-1$
	}

	/**
	 * @return Returns the td.
	 */
	public final TagDefinition getTagDefinition() {
		return tagDefinition;
	}

	public final boolean removeLineWhenOtherwiseEmpty() {
		return tagDefinition.removeWhenContainingLineIsEmpty();
	}
}
