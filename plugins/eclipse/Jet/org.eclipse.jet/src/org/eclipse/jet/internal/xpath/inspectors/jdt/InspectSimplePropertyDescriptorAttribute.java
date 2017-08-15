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

import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;

/**
 * Inspect {@link SimplePropertyDescriptorAttribute} for the JET XPath engine. Realizes these
 * objects as XPath attributes.
 *
 */
public class InspectSimplePropertyDescriptorAttribute implements INodeInspector {

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#expandedNameOf(java.lang.Object)
	 */
	public ExpandedName expandedNameOf(final Object node) {
		return new ExpandedName(nameOf(node));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object node) {
		return new Object[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
	 */
	public Object getDocumentRoot(final Object node) {
		final SimplePropertyDescriptorAttribute astNodeAttribute = (SimplePropertyDescriptorAttribute) node;
		return astNodeAttribute.getDocumentRoot();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
	 */
	public NodeKind getNodeKind(final Object node) {
		return NodeKind.ATTRIBUTE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
	 */
	public Object getParent(final Object node) {
		final SimplePropertyDescriptorAttribute astNodeAttribute = (SimplePropertyDescriptorAttribute) node;

		return astNodeAttribute.getParent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
	 */
	public String nameOf(final Object node) {
		final SimplePropertyDescriptorAttribute astNodeAttribute = (SimplePropertyDescriptorAttribute) node;

		return astNodeAttribute.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
	 */
	public String stringValueOf(final Object node) {
		final SimplePropertyDescriptorAttribute astNodeAttribute = (SimplePropertyDescriptorAttribute) node;

		return astNodeAttribute.stringValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public boolean testExpandedName(final Object node, final ExpandedName testName) {
		return testName.equals(expandedNameOf(node));
	}

}
