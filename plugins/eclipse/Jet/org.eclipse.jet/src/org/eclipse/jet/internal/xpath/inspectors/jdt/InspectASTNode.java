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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.CopyElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.INodeInspectorExtension1;
import org.eclipse.jet.xpath.inspector.InvalidChildException;
import org.eclipse.jet.xpath.inspector.SimpleElementRequiresValueException;

/**
 * Inspect ASTNode objects for the JET XPath engine. Realizes ASTNodes as XPath elements.
 *
 */
public class InspectASTNode implements INodeInspector, IElementInspector, INodeInspectorExtension1 {

	private static final Map propertyDescriptorByType = new HashMap(50);
	private static final Map attributePropertyDescriptorByType = new HashMap(50);
	private static final Map childPropertyDescriptorByType = new HashMap(50);
	
	/**
	 * Return the named property descriptor for the given node.
	 * @param node
	 * @param id
	 * @return the property descriptor or null, if id is not know property
	 */
	private StructuralPropertyDescriptor getPropertyDescriptor(final ASTNode node, final String id) {
		final List propsList = getPropertyDescriptors(node);
		
		for (final Iterator i = propsList.iterator(); i.hasNext();) {
			final StructuralPropertyDescriptor spd = (StructuralPropertyDescriptor) i.next();
			if(spd.getId().equals(id)) {
				return spd;
			}
		}
		
		
		return null;
	}

	/**
	 * Return the property descriptors for the given type.
	 * @param node
	 * @return
	 */
	private List getPropertyDescriptors(final ASTNode node) {
		final Integer nodeType = new Integer(node.getNodeType());
		
		List propsList = (List)propertyDescriptorByType.get(nodeType);
		
		if(propsList == null) {
			synchronized (propertyDescriptorByType) {
				propsList = (List)propertyDescriptorByType.get(nodeType);
				if(propsList == null) {
					propsList = node.structuralPropertiesForType();
					propertyDescriptorByType.put(nodeType, propsList);
				}
				
			}
		}
		return propsList;
	}
	
	/**
	 * @param node
	 * @return
	 */
	private List getAttributePropertyDescriptors(final ASTNode node) {
		final Integer nodeType = new Integer(node.getNodeType());
		
		List propsList = (List)attributePropertyDescriptorByType.get(nodeType);
		if(propsList == null) {
			synchronized (attributePropertyDescriptorByType) {
				propsList = (List)attributePropertyDescriptorByType.get(nodeType);
				if(propsList == null) {
					final List allPropsList = node.structuralPropertiesForType();
					propsList = new ArrayList(allPropsList.size());
					for (final Iterator i = allPropsList.iterator(); i
							.hasNext();) {
						final StructuralPropertyDescriptor spd = (StructuralPropertyDescriptor) i.next();
						if(spd.isSimpleProperty()) {
							propsList.add(spd);
						}
						
					}
					attributePropertyDescriptorByType.put(nodeType, propsList);
				}
				
			}
		}
		return propsList;
	}
	
	/**
	 * @param node
	 * @return
	 */
	private List getElementPropertyDescriptors(final ASTNode node) {
		final Integer nodeType = new Integer(node.getNodeType());
		
		List propsList = (List)childPropertyDescriptorByType.get(nodeType);
		if(propsList == null) {
			synchronized (childPropertyDescriptorByType) {
				propsList = (List)childPropertyDescriptorByType.get(nodeType);
				if(propsList == null) {
					final List allPropsList = node.structuralPropertiesForType();
					propsList = new ArrayList(allPropsList.size());
					for (final Iterator i = allPropsList.iterator(); i
							.hasNext();) {
						final StructuralPropertyDescriptor spd = (StructuralPropertyDescriptor) i.next();
						if(!spd.isSimpleProperty()) {
							propsList.add(spd);
						}
						
					}
					childPropertyDescriptorByType.put(nodeType, propsList);
				}
				
			}
		}
		return propsList;
	}
	
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
		final ASTNode astNode = (ASTNode) node;
		final List spds = getElementPropertyDescriptors(astNode);
		final List children = new ArrayList(spds.size());
		for (final Iterator i = spds.iterator(); i.hasNext();) {
			final StructuralPropertyDescriptor spd = (StructuralPropertyDescriptor) i.next();
			final Object structuralProperty = astNode.getStructuralProperty(spd);
			if(spd.isChildProperty() && structuralProperty != null) {
				children.add(structuralProperty);
			} else if(spd.isChildListProperty() && structuralProperty != null) {
				final List spList = (List)structuralProperty;
				children.addAll(spList);
			}
		}
		return children.toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
	 */
	public Object getDocumentRoot(final Object node) {
		final ASTNode astNode = (ASTNode) node;
		return ASTNodeDocumentRoot.documentRootFor(astNode);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
	 */
	public NodeKind getNodeKind(final Object node) {
		return NodeKind.ELEMENT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
	 */
	public Object getParent(final Object node) {
		final ASTNode astNode = (ASTNode) node;
		final ASTNode parent = astNode.getParent();
		return parent == null ? (Object)ASTNodeDocumentRoot.documentRootFor(astNode) : (Object)parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
	 */
	public String nameOf(final Object node) {
		final ASTNode astNode = (ASTNode) node;
		final StructuralPropertyDescriptor locationInParent = astNode.getLocationInParent();
		if(locationInParent != null) {
			return locationInParent.getId();
		} else {
			final String fullyQualifiedName = ASTNode.nodeClassForType(astNode.getNodeType()).getName();
			final int lastDot = fullyQualifiedName.lastIndexOf('.');
			final StringBuffer simpleName = new StringBuffer(lastDot >= 0 ? fullyQualifiedName.substring(lastDot + 1) : fullyQualifiedName);
			simpleName.setCharAt(0, Character.toLowerCase(simpleName.charAt(0)));
			return simpleName.toString();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
	 */
	public String stringValueOf(final Object node) {
		final ASTNode astNode = (ASTNode) node;
		return getSource(astNode);
	}

	/**
	 * @param astNode
	 * @return
	 */
	private String getSource(final ASTNode astNode) {
		final ASTNodeDocumentRoot documentRoot = ASTNodeDocumentRoot.documentRootFor(astNode);
		
		final int start = astNode.getStartPosition();
		final int end = start + astNode.getLength();
		return documentRoot.stringValue().substring(start, end);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public boolean testExpandedName(final Object node, final ExpandedName testName) {
		return testName.equals(expandedNameOf(node));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#addElement(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName, java.lang.Object)
	 */
	public Object addElement(final Object node, final ExpandedName elementName,
			final Object addBeforeThisSibling)
			throws SimpleElementRequiresValueException, InvalidChildException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#addTextElement(java.lang.Object, java.lang.String, java.lang.String, boolean)
	 */
	public Object addTextElement(final Object parentElement, final String name,
			final String bodyContent, final boolean asCData) throws AddElementException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#copyElement(java.lang.Object, java.lang.Object, java.lang.String, boolean)
	 */
	public Object copyElement(final Object tgtParent, final Object srcElement, final String name,
			final boolean recursive) throws CopyElementException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#createAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean createAttribute(final Object node, final String attributeName,
			final String value) {
		return false;
	}

	public Object[] getAttributes(final Object node) {
		final ASTNode astNode = (ASTNode) node;
		final List spds = getAttributePropertyDescriptors(astNode);
		final List nonspds = NonDescriptorAttribute.getAttributes(astNode);
		final List result = new ArrayList(spds.size() + nonspds.size());
		result.addAll(nonspds);
		for (final Iterator i = spds.iterator(); i.hasNext();) {
			final SimplePropertyDescriptor spd = (SimplePropertyDescriptor) i.next();
			if(astNode.getStructuralProperty(spd) != null) {
			  result.add(new SimplePropertyDescriptorAttribute(astNode,spd));
			}
		}
		return result.toArray();
	}

	public Object getNamedAttribute(final Object node,
			final ExpandedName nameTestExpandedName) {
		final ASTNode astNode = (ASTNode) node;
		
		final String propertyName = nameTestExpandedName.getLocalPart();

		final NonDescriptorAttribute attribute = NonDescriptorAttribute.getAttribute(astNode, propertyName);
		if(attribute != null) {
			return attribute;
		}
		
		final StructuralPropertyDescriptor spd = getPropertyDescriptor(astNode, propertyName);
		
		if(spd != null && spd.isSimpleProperty() && astNode.getStructuralProperty(spd) != null) {
			return new SimplePropertyDescriptorAttribute(astNode, (SimplePropertyDescriptor)spd);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#removeAttribute(java.lang.Object, java.lang.String)
	 */
	public void removeAttribute(final Object node, final String name) {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#removeElement(java.lang.Object)
	 */
	public void removeElement(final Object node) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspectorExtension1#getNamedChildren(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public Object[] getNamedChildren(final Object node, final ExpandedName nameTestExpandedName) {
		final ASTNode astNode = (ASTNode) node;
		final String propertyName = nameTestExpandedName.getLocalPart();
		final StructuralPropertyDescriptor spd = getPropertyDescriptor(astNode, propertyName);
		
		if(spd == null) {
			return new Object[0];
		}
        final Object structuralProperty = astNode.getStructuralProperty(spd);
        if(spd.isChildProperty()) {
			return structuralProperty != null ? new Object[] {structuralProperty} : null;
		} else if(spd.isChildListProperty()) {
			return structuralProperty != null ? ((List)structuralProperty).toArray() : null;
		}
		
		
		return new Object[0];
	}

}
