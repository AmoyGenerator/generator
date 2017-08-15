/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;

/**
 * @author pelder
 *
 */
public class InspectNonDescriptorAttribute implements INodeInspector {

	private static final Object[] NULL_OBJECT_ARRAY = new Object[0];

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#expandedNameOf(java.lang.Object)
	 */
	public ExpandedName expandedNameOf(Object node) {
		return new ExpandedName(nameOf(node));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object node) {
		return NULL_OBJECT_ARRAY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
	 */
	public Object getDocumentRoot(Object node) {
		return ASTNodeDocumentRoot.documentRootFor((ASTNode) getParent(node));
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
	public Object getParent(Object obj) {
		return ((NonDescriptorAttribute)obj).getParent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
	 */
	public String nameOf(Object node) {
		return ((NonDescriptorAttribute)node).getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
	 */
	public String stringValueOf(Object node) {
		return ((NonDescriptorAttribute)node).getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public boolean testExpandedName(Object node, ExpandedName testName) {
		return expandedNameOf(node).equals(testName);
	}

}
