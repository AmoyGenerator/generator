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

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link XPathFunctionResolver}.
 */
public class DefaultXPathFunctionResolver implements XPathFunctionResolver
{

  private final Map functionMap = new HashMap();
  private final XPathFunctionResolver delegateResolver;

  public DefaultXPathFunctionResolver() {
    delegateResolver = null;
  }
  
  /**
   * Construct a function resolver that first attempts to the resolve a function
   * by calling the <code>delegateResolver</code> and then by calling this resolver.
   * @param delegateResolver a {@link XPathFunctionResolver}.
   */
  public DefaultXPathFunctionResolver(XPathFunctionResolver delegateResolver) {
    this.delegateResolver = delegateResolver;
    
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunctionResolver#resolveFunction(java.lang.String, int)
   */
  public XPathFunction resolveFunction(String functionName, int arity)
  {
    XPathFunction result = null;
    
    if(delegateResolver != null) {
      result = delegateResolver.resolveFunction(functionName, arity);
    }
    
    if(result == null) {
      XPathFunctionMetaData data = (XPathFunctionMetaData)functionMap.get(functionName);
      if (data != null && data.getMinArgs() <= arity && (arity <= data.getMaxArgs() || data.getMaxArgs() == -1))
      {
        result = data.getFunction();
      }
    }
    
    return result;
  }

  public void addFunction(XPathFunctionMetaData functionData)
  {
    functionMap.put(functionData.getName(), functionData);
  }
}
