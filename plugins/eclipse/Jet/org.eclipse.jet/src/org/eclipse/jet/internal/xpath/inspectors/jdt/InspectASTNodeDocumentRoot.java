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
 * Inspect {@link ASTNodeDocumentRoot} objects for the JET XPath engine
 *
 */
public class InspectASTNodeDocumentRoot implements INodeInspector {

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
		final ASTNodeDocumentRoot astDocumentRoot = (ASTNodeDocumentRoot) node;

		return new Object[] {astDocumentRoot.getRootNode()};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
	 */
	public Object getDocumentRoot(final Object node) {
		return node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
	 */
	public NodeKind getNodeKind(final Object obj) {
		return NodeKind.ROOT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
	 */
	public Object getParent(final Object obj) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
	 */
	public String nameOf(final Object node) {
		final ASTNodeDocumentRoot astDocumentRoot = (ASTNodeDocumentRoot) node;
		
		return astDocumentRoot.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
	 */
	public String stringValueOf(final Object node) {
		final ASTNodeDocumentRoot astDocumentRoot = (ASTNodeDocumentRoot) node;
		return astDocumentRoot.stringValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public boolean testExpandedName(final Object node, final ExpandedName testName) {
		return testName.equals(expandedNameOf(node));
	}

}
