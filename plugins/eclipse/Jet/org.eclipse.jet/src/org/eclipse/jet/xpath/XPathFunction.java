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


import java.util.List;



/**
 * Interface to an XPath function implementation. Functions that require the XPath context should also
 * implement {@link XPathFunctionWithContext}.
 * <p>Note: Once JET moves to Java 1.5 or later only, this interface will be migrated to javax.xml.xpath.XPathFunction</p>
 *
 */
public interface XPathFunction
{

  /**
   * Evaluate the function with given the arguments.
   * The types of arguments should be one of
   * <bl>
   * <li>{@link NodeSet} representing a result of an XPath path expression</li>
   * <li>{@link Double} representing a numeric value</li>
   * <li>{@link String} representing a string value</li>
   * <li>{@link Boolean} representing a logical value</li>
   * </bl>
   * @param args a non-null List of arguments.
   * @return the function result
   * @throws XPathRuntimeException if the function cannot be evaluated successfully.
   */
  Object evaluate(List args);

}
