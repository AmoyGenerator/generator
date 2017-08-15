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
 * Marker interface for XPathFunctions that require the XPath Context passed as an argument.
 * If this marker is implemented, then {@link #setContext(Context)} is called immediately before
 * {@link XPathFunction#evaluate(java.util.List)}.
 *
 */
public interface XPathFunctionWithContext
{
  /**
   * Set the XPath context to use during evaluation of the function.
   * @param context the XPath context object. Will not be <code>null</code>.
   */
  public abstract void setContext(Context context);

}
