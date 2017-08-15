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

import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;


/**
 * Implement the XPath 'boolean' function.
 *
 */
public class BooleanFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("boolean", null, new BooleanFunction(), 1, 1); //$NON-NLS-1$

  /**
   * 
   */
  public BooleanFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    if (args.size() != 1)
    {
      throw new IllegalArgumentException();
    }
    return Boolean.valueOf(evaluate(args.get(0)));
  }

  public static boolean evaluate(Object value)
  {
    if (value instanceof Boolean)
    {
      return ((Boolean)value).booleanValue();
    }
    else if (value instanceof String)
    {
      return ((String)value).length() > 0;
    }
    else if (value instanceof Double)
    {
      double d = ((Double)value).doubleValue();
      if (Double.isNaN(d))
      {
        return false;
      }
      else
      {
        return d != 0 && d != -0;
      }
    }
    else if (value instanceof NodeSet)
    {
      return ((NodeSet)value).size() > 0;
    }
    return false;
  }

}
