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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.CopyElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InvalidChildException;
import org.eclipse.jet.xpath.inspector.SimpleElementRequiresValueException;

/**
 * Inspector for IResource objects.
 *
 */
public class ResourceInspector implements INodeInspector, IElementInspector {

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#expandedNameOf(java.lang.Object)
	 */
	public ExpandedName expandedNameOf(Object node) {
		IResource resource = (IResource) node;
		ExpandedName result = null;
		switch(resource.getType()) {
		case IResource.FILE:
			result = new ExpandedName("file"); //$NON-NLS-1$
			break;
		case IResource.FOLDER:
			result = new ExpandedName("folder"); //$NON-NLS-1$
			break;
		case IResource.PROJECT:
			result = new ExpandedName("project"); //$NON-NLS-1$
			break;
		case IResource.ROOT:
			result = new ExpandedName("root"); //$NON-NLS-1$
			break;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object node) {
		IResource resource = (IResource) node;
		
		if(resource instanceof IContainer) {
			IContainer container = (IContainer) resource;
			try {
				return container.members();
			} catch (CoreException e) {
				return new Object[0];
			}
		} else {
			return new Object[0];
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getDocumentRoot(java.lang.Object)
	 */
	public Object getDocumentRoot(Object node) {
		IResource resource = (IResource) node;
		return resource.getWorkspace().getRoot();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(java.lang.Object)
	 */
	public NodeKind getNodeKind(Object obj) {
		return NodeKind.ELEMENT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#getParent(java.lang.Object)
	 */
	public Object getParent(Object node) {
		IResource resource = (IResource) node;
		return resource.getParent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#nameOf(java.lang.Object)
	 */
	public String nameOf(Object node) {
		ExpandedName expandedName = expandedNameOf(node);
		return expandedName.getLocalPart();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#stringValueOf(java.lang.Object)
	 */
	public String stringValueOf(Object node) {
		return ""; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.INodeInspector#testExpandedName(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public boolean testExpandedName(Object node, ExpandedName testName) {
		return expandedNameOf(node).equals(testName);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#addElement(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName, java.lang.Object)
	 */
	public Object addElement(Object node, ExpandedName elementName,
			Object addBeforeThisSibling)
			throws SimpleElementRequiresValueException, InvalidChildException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#addTextElement(java.lang.Object, java.lang.String, java.lang.String, boolean)
	 */
	public Object addTextElement(Object parentElement, String name,
			String bodyContent, boolean asCData) throws AddElementException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#copyElement(java.lang.Object, java.lang.Object, java.lang.String, boolean)
	 */
	public Object copyElement(Object tgtParent, Object srcElement, String name,
			boolean recursive) throws CopyElementException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#createAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean createAttribute(Object node, String attributeName,
			String value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#getAttributes(java.lang.Object)
	 */
	public Object[] getAttributes(Object node) {
		IResource resource = (IResource) node;
		ResourceAttributeFactory factory = new ResourceAttributeFactory();

		return new Object[] {
				factory.create(ResourceAttributeFactory.FILE_EXTENSION,
						resource),
				factory.create(ResourceAttributeFactory.FULL_PATH, resource),
				factory.create(ResourceAttributeFactory.LOCAL_TIMESTAMP,
						resource),
				factory.create(ResourceAttributeFactory.LOCATION, resource),
				factory.create(ResourceAttributeFactory.LOCATION_URI, resource),
				factory.create(ResourceAttributeFactory.MODIFICATION_STAMP,
						resource),
				factory.create(ResourceAttributeFactory.NAME, resource),
				factory.create(ResourceAttributeFactory.PROJECT, resource),
				factory.create(ResourceAttributeFactory.PROJECT_RELATIVE_PATH,
						resource),
				factory.create(ResourceAttributeFactory.RAW_LOCATION, resource),
				factory.create(ResourceAttributeFactory.RAW_LOCATION_URI,
						resource),
				factory.create(ResourceAttributeFactory.DERIVED, resource),
				factory.create(ResourceAttributeFactory.LINKED, resource),
				factory.create(ResourceAttributeFactory.PHANTOM, resource),
				factory.create(ResourceAttributeFactory.READ_ONLY, resource),
				factory.create(ResourceAttributeFactory.TEAM_PRIVATE_MEMBER,
						resource), };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#getNamedAttribute(java.lang.Object, org.eclipse.jet.xpath.inspector.ExpandedName)
	 */
	public Object getNamedAttribute(Object node,
			ExpandedName nameTestExpandedName) {
		IResource resource = (IResource) node;
		return new ResourceAttributeFactory().create(nameTestExpandedName.getLocalPart(), resource);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#removeAttribute(java.lang.Object, java.lang.String)
	 */
	public void removeAttribute(Object node, String name) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.inspector.IElementInspector#removeElement(java.lang.Object)
	 */
	public void removeElement(Object node) {
		throw new UnsupportedOperationException();
	}

}
