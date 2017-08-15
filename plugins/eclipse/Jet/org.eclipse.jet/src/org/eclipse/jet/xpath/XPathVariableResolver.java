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
package org.eclipse.jet.xpath;


/**
 * Defines a resolver of XPath variable names to their values.
 * <p>Note: Once JET moves to Java 1.5 or later only, this interface will be migrated to javax.xml.xpath.XPathVariableResolver</p>
 *
 */
public interface XPathVariableResolver
{

  public abstract Object resolveVariable(String variableName);

}
