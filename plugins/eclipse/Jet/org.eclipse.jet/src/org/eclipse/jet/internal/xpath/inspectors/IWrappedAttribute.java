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

/**
 * Interface defining common behavior of object attributes exposed to an XPath inspector.
 * @see WrappedAttributeInspector
 * @see ResourceAttributeFactory
 * @see AbstractResourceAttribute
 *
 */
public interface IWrappedAttribute {
	
	public abstract String getStringValue();
	
    public abstract Object getDocumentRoot();

    public abstract String getName();

    public abstract Object getParent();

}
