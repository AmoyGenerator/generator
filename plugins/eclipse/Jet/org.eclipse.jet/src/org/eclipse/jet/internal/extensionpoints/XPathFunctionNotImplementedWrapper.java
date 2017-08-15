/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: XPathFunctionNotImplementedWrapper.java,v 1.2 2008/09/12 12:56:28 pelder Exp $
 */
package org.eclipse.jet.internal.extensionpoints;

import java.util.List;

import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.osgi.util.NLS;

/**
 * Wrapper for XPath function definition that does not implement org.eclipse.jet.xpath.XPathFunction
 */
public class XPathFunctionNotImplementedWrapper implements XPathFunction
{

  private final String functionClassName;
  private String functionName;
  private String pluginID;

  public XPathFunctionNotImplementedWrapper(String functionName, String pluginID, String functionClassName)
  {
    this.functionClassName = functionClassName;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    throw new XPathRuntimeException(NLS.bind(
      Messages.XPathFunctionNotImplementedWrapper_XPathFunctionNotImplemented,
      new Object []{ functionName, pluginID, functionClassName }));
  }

}
