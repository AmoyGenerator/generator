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
package org.eclipse.jet.internal.xpath.functions;


import java.util.List;

import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;


/**
 * Implement the XPath 'round' function.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-round">http://www.w3.org/TR/xpath#function-round</a>.
 *
 */
public class RoundFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("round", null, new RoundFunction(), 1, 1); //$NON-NLS-1$

  /**
   * 
   */
  public RoundFunction()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public static double evaluate(Object object)
  {
    double d = NumberFunction.evaluate(object);
    if (-0.5 <= d && d <= -0.0d)
    {
      return -0.0d;
    }
    else
    {
      return Math.floor(d + 0.5);
    }
  }
}
