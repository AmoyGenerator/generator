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
 * Implement the XPath function 'number'.
 *
 */
public class NumberFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("number", null, new NumberFunction(), 1, 1); //$NON-NLS-1$

  /**
   * 
   */
  public NumberFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    if (args == null || args.size() != 1)
    {
      throw new IllegalArgumentException();
    }
    return new Double(evaluate(args.get(0)));
  }

  public static double evaluate(Object obj)
  {
    double result = Double.NaN;

    if (obj instanceof Double)
    {
      result = ((Double)obj).doubleValue();
    }
    else if (obj instanceof String)
    {
      String s = (String)obj;
      try
      {
        // TODO This goes beyond the spec, in that 0.1d and 0.1f as well
        // as 1.0e5 are not covered by the XPath spec, but are excepted here.
        result = Double.parseDouble(s);
      }
      catch (NumberFormatException e)
      {
        result = Double.NaN;
      }
    }
    else if (obj instanceof Boolean)
    {
      result = ((Boolean)obj).booleanValue() ? 1 : 0;
    }
    else
    {
      String s = StringFunction.evaluate(obj);
      result = evaluate(s);
    }

    return result;
  }

}
