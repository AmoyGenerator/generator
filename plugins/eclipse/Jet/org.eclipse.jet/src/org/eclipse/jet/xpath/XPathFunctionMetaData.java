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

import org.eclipse.jet.internal.extensionpoints.XPathFunctionLazyLoadWrapper;


/**
 * Invariant class that defines implementation details of XPath functions.
 */
public final class XPathFunctionMetaData
{

  private XPathFunction function;

  private final int minArgs;

  private final int maxArgs;

  private final String name;

  private final String namespace;

  /**
   * Return the namespace of the function.
   * @return Returns the namespace.
   */
  public final String getNamespace()
  {
    return namespace;
  }

  /**
   * Create function metadata.
   * @param name the name of the function, as it occurs in XPath expressions.
   * @param namespace the namespace of the functions.
   * @param function the function implementation
   * @param minArgs the minimum number of arguments. Must be non-negative.
   * @param maxArgs the maximum number of arguments. Must be non-negative or -1 indicating an unlimited number of arguments.
   * @throws NullPointerException if name or function name are <code>null</code>.
   * @throws IllegalArgumentException if minArgs or maxArgs do not conform to their expected values.
   */
  public XPathFunctionMetaData(String name, String namespace, XPathFunction function, int minArgs, int maxArgs)
  {
    if(name == null) {
      throw new NullPointerException("name"); //$NON-NLS-1$
    }
    if(function == null) {
      throw new NullPointerException("function"); //$NON-NLS-1$
    }
    if(minArgs < 0) {
      throw new IllegalArgumentException("minArgs < 0"); //$NON-NLS-1$
    }
    if(maxArgs < 0 && maxArgs != -1) {
      throw new IllegalArgumentException("maxArgs < 0 && maxArgs != -1"); //$NON-NLS-1$
    }
    if(maxArgs < minArgs && maxArgs != -1) {
      throw new IllegalArgumentException("maxArgs < minArgs"); //$NON-NLS-1$
    }
    
    this.name = name;
    this.namespace = namespace;
    this.function = function;
    this.minArgs = minArgs;
    this.maxArgs = maxArgs;

  }

  /**
   * Return the function implementation
   * @return Returns the function.
   */
  public synchronized XPathFunction getFunction()
  {
    if(function instanceof XPathFunctionLazyLoadWrapper) {
      XPathFunctionLazyLoadWrapper wrapper = (XPathFunctionLazyLoadWrapper)function;
      function = wrapper.resolveFunction();
    }
    return function;
  }

  /**
   * Return the mininum number of arguments.
   * @return Returns the minArgs.
   */
  public int getMinArgs()
  {
    return minArgs;
  }

  /**
   * Return the maximum number of arguments or -1 if the function allows unlimited arguments.
   * @return Returns the maxArgs.
   */
  public int getMaxArgs()
  {
    return maxArgs;
  }

  /**
   * Return the function name, as used in XPath expressions.
   * @return Returns the name.
   */
  public String getName()
  {
    return name;
  }
}