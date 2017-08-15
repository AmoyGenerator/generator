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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * @author pelder
 *
 */
public abstract class NonDescriptorAttribute {
	
	private static final String NODE_TYPE_ATTR = "nodeType"; //$NON-NLS-1$
	private static final String SOURCE_ATTR = "source"; //$NON-NLS-1$
	private static final String START_POSITION_ATTR = "startPosition"; //$NON-NLS-1$
	private static final String LENGTH_ATTR = "length"; //$NON-NLS-1$

	public static final class StartPosition extends NonDescriptorAttribute {

		public StartPosition(ASTNode node) {
			super(node);
		}

		public String getName() {
			return START_POSITION_ATTR;
		}

		public String getValue() {
			// TODO Auto-generated method stub
			return Integer.toString(getParent().getStartPosition());
		}
		
	}
	
	public static final class Length extends NonDescriptorAttribute {

		public Length(ASTNode node) {
			super(node);
		}

		public String getName() {
			return LENGTH_ATTR;
		}

		public String getValue() {
			return Integer.toString(getParent().getLength());
		}
	}
	
	public static final class Source extends NonDescriptorAttribute {

		public Source(ASTNode node) {
			super(node);
		}

		public String getName() {
			return SOURCE_ATTR;
		}

		public String getValue() {
			final ASTNode astNode = getParent();
			final ASTNodeDocumentRoot documentRoot = ASTNodeDocumentRoot.documentRootFor(astNode);
			
			final int start = astNode.getStartPosition();
			final int end = start + astNode.getLength();
			return documentRoot.stringValue().substring(start, end);
		}
		
	}
	
	public static final class NodeType extends NonDescriptorAttribute {

		public NodeType(ASTNode node) {
			super(node);
		}

		public String getName() {
			return NODE_TYPE_ATTR;
		}

		public String getValue() {
			final String name = ASTNode.nodeClassForType(getParent().getNodeType()).getName();
			final int lastDot = name.lastIndexOf('.');
			
			return name.substring(lastDot + 1);
		}
		
	}
	
	public static NonDescriptorAttribute getAttribute(ASTNode astNode, String propertyName) {
		if(LENGTH_ATTR.equals(propertyName)) {
			return new NonDescriptorAttribute.Length(astNode);
		} else if(START_POSITION_ATTR.equals(propertyName)) {
			return new NonDescriptorAttribute.StartPosition(astNode);
		} else if(SOURCE_ATTR.equals(propertyName)) {
			return new NonDescriptorAttribute.Source(astNode);
		} else if(NODE_TYPE_ATTR.equals(propertyName)) {
			return new NonDescriptorAttribute.NodeType(astNode);
		}
		return null;
	}
	
	public static List getAttributes(ASTNode astNode) {
		final List result = new ArrayList(4);
		result.add(getAttribute(astNode, START_POSITION_ATTR));
		result.add(getAttribute(astNode, LENGTH_ATTR));
		result.add(getAttribute(astNode, SOURCE_ATTR));
		result.add(getAttribute(astNode, NODE_TYPE_ATTR));
		return result;
	}
	private final ASTNode node;

	public NonDescriptorAttribute(ASTNode node) {
		if(node == null) {
			throw new NullPointerException();
		}
		this.node = node;
		
	}
	
	public ASTNode getParent() {
		return node;
	}
	
	public abstract String getName();
	
	public abstract String getValue();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NonDescriptorAttribute)) {
			return false;
		}
		NonDescriptorAttribute other = (NonDescriptorAttribute) obj;
		if (!getName().equals(other.getName())) {
			return false;
		}
		if (!node.equals(other.node)) {
			return false;
		}
		return true;
	}

}
