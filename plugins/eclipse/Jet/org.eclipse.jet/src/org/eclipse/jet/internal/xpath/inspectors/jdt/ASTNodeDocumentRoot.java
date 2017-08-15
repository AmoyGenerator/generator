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

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

/**
 * Wrap an root {@link ASTNode} as XPath document root.
 *
 */
public class ASTNodeDocumentRoot {
	
	/**
	 * The root node of the AST
	 */
	private final ASTNode rootNode;
	/**
	 * The Java source
	 */
	private final String source;

	/**
	 * A weak map of document roots by AST object. 
	 */
	private static final Map documentRootsByAST = Collections.synchronizedMap(new WeakHashMap());
	
	/**
	 * Returns the {@link ASTNodeDocumentRoot} for an {@link ASTNode}, if one exists.
	 * @param astNode an ASTNode
	 * @return the document root or <code>null</code>
	 */
	static ASTNodeDocumentRoot documentRootFor(final ASTNode astNode) {
		return (ASTNodeDocumentRoot) documentRootsByAST.get(astNode.getAST());
	}
	
	/**
	 * Construct a document root from a root {@link ASTNode} and the source for the model.
	 * @param rootNode the root ASTNode (as returned by {@link ASTParser#createAST(org.eclipse.core.runtime.IProgressMonitor)}
	 * @param source the source for the AST
	 */
	public ASTNodeDocumentRoot(final ASTNode rootNode, final String source) {
		this.rootNode = rootNode;
		this.source = source;
		
		documentRootsByAST.put(rootNode.getAST(), this);
	}

	/**
	 * Return the AST root node
	 * @return the root node
	 */
	public ASTNode getRootNode() {
		return rootNode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((rootNode == null) ? 0 : rootNode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ASTNodeDocumentRoot other = (ASTNodeDocumentRoot) obj;
		if (rootNode == null) {
			if (other.rootNode != null)
				return false;
		} else if (!rootNode.equals(other.rootNode))
			return false;
		return true;
	}

	/**
	 * Return the XPath name of the root node.
	 * @return the XPath name (an empty string)
	 */
	public String getName() {
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * Return the XPath string value of the root node
	 * @return The string value of the AST (the source code)
	 */
	public String stringValue() {
		return source != null ? source : ""; //$NON-NLS-1$
	}
	
}
