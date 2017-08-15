/**
 * <copyright>
 *
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 */
package org.eclipse.jet.internal.xpath.inspectors.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

/**
 * Wrap ASTNode simple properties as XPath attribute objects
 *
 */
public class SimplePropertyDescriptorAttribute {

	/**
	 * The AST Node containing the property
	 */
	private final ASTNode astNode;
	
	/**
	 * The AST property descriptor
	 */
	private final SimplePropertyDescriptor simplePropertyDescriptor;

	/**
	 * Create an AST Node Attribute from an AST Node and property descriptor
	 * @param astNode the AST Node
	 * @param spd the property descriptor
	 */
	public SimplePropertyDescriptorAttribute(final ASTNode astNode, final SimplePropertyDescriptor spd) {
		this.astNode = astNode;
		this.simplePropertyDescriptor = spd;
	}
	
	/**
	 * Return the XPath name of the AST Node
	 * @return the XPath name
	 */
	public String getName() {
		return simplePropertyDescriptor.getId();
	}

	/**
	 * Return the XPath string value of the property
	 * @return the XPath string value
	 */
	public String stringValue() {
		final Object value = astNode.getStructuralProperty(simplePropertyDescriptor);
		return value != null ? value.toString() : ""; //$NON-NLS-1$
	}
	
	/**
	 * Return the XPath parent of the property
	 * @return the XPath parent (the ASTNode)
	 */
	public Object getParent() {
		return astNode;
	}
	
	/**
	 * Return the XPath document root of the property
	 * @return the document root object.
	 */
	public Object getDocumentRoot() {
		return ASTNodeDocumentRoot.documentRootFor(astNode);
	}
}
