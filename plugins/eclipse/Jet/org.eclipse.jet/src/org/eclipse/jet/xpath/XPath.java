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
 * Interface to an XPath processor.
 * <p>Note: Once JET moves to Java 1.5 or later only, this interface will be migrated to javax.xml.xpath.XPath</p>
 *
 */
public interface XPath
{

  void setXPathFunctionResolver(XPathFunctionResolver resolver);

  XPathFunctionResolver getXPathFunctionResolver();

  void setXPathVariableResolver(XPathVariableResolver resolver);

  XPathVariableResolver getXPathVariableResolver();

  XPathExpression compile(String expression) throws XPathException;

  void setNamespaceContext(NamespaceContext nsContext);

  NamespaceContext getNamespaceContext();

}
