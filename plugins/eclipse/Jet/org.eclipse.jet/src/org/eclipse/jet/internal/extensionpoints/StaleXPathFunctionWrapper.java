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
 * $Id: StaleXPathFunctionWrapper.java,v 1.2 2008/09/12 12:56:28 pelder Exp $
 */
package org.eclipse.jet.internal.extensionpoints;

import java.util.List;

import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.osgi.util.NLS;

/**
 * Wrapper fro XPath functions whose plug-in information is stale and cannot be loaded
 */
public class StaleXPathFunctionWrapper implements XPathFunction
{

  private final String name;

  public StaleXPathFunctionWrapper(String name)
  {
    this.name = name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    throw new XPathRuntimeException(NLS.bind(
      Messages.StaleXPathFunctionWrapper_DefiningPluginNotAvailable,
      name));
  }

}
