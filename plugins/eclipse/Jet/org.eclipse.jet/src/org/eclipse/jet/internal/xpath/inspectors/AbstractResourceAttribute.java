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

import org.eclipse.core.resources.IResource;

/**
 * Abstract implementation of {@link IWrappedAttribute} for resource Attributes.
 *
 */
public abstract class AbstractResourceAttribute extends AbstractWrappedAttribute implements IWrappedAttribute {

	public AbstractResourceAttribute(IResource parent, String name) {
		super(parent, name);
		
	}
	public final IResource getResource() {
		return (IResource)getParent();
	}
    
   /* (non-Javadoc)
     * @see org.eclipse.jet.experiments.IResourceAttribute#getDocumentRoot()
     */
	public final Object getDocumentRoot() {
        return getResource().getWorkspace().getRoot();
    }

}
