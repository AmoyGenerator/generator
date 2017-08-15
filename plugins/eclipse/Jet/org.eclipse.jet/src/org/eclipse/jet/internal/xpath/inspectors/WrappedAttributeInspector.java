/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.jet.internal.xpath.inspectors;

import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;

/**
 * XPath inspector for simple object attributes wrapped {@link IWrappedAttribute}.
 * @see ResourceInspector
 * @see IWrappedAttribute
 *
 */
public class WrappedAttributeInspector implements INodeInspector {

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#expandedNameOf(java.lang.Object)
	 */
	public ExpandedName expandedNameOf(Object node) {
		IWrappedAttribute attr = (IWrappedAttribute) node;
		return new ExpandedName(attr.getName());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object node) {
		return new Object[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
	 */
	public Object getDocumentRoot(Object node) {
		IWrappedAttribute attr = (IWrappedAttribute) node;
		return attr.getDocumentRoot();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
	 */
	public NodeKind getNodeKind(Object obj) {
		return NodeKind.ATTRIBUTE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
	 */
	public Object getParent(Object node) {
		IWrappedAttribute attr = (IWrappedAttribute) node;
		return attr.getParent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
	 */
	public String nameOf(Object node) {
		IWrappedAttribute attr = (IWrappedAttribute) node;
		return attr.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
	 */
	public String stringValueOf(Object node) {
		IWrappedAttribute attr = (IWrappedAttribute) node;
		String stringValue = attr.getStringValue();
		return stringValue == null ? "" : stringValue; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public boolean testExpandedName(Object node, ExpandedName testName) {
		return expandedNameOf(node).equals(testName);
	}

}
