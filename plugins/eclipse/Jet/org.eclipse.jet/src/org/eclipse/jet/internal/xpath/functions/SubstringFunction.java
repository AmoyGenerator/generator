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
 * Implement the XPath 'substring' function.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-substring">http://www.w3.org/TR/xpath#function-substring</a>.
 *
 */
public class SubstringFunction implements XPathFunction
{

  private static final String EMPTY_STRING = ""; //$NON-NLS-1$
  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("substring", null, new SubstringFunction(), 2, 3); //$NON-NLS-1$

  /**
   * 
   */
  public SubstringFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    String s1 = StringFunction.evaluate(args.get(0));
    double d1 = RoundFunction.evaluate(args.get(1));

    if (Double.isNaN(d1) || d1 == Double.POSITIVE_INFINITY)
    {
      // cannot do any comparisons against Nan, no string is long as positive infinity.
      return EMPTY_STRING;
    }

    int start = d1 > 0 ? (int)d1 - 1 : 0;

    if (args.size() == 2)
    {
      if (start < s1.length())
      {
        return s1.substring(start);
      }
      else
      {
        return EMPTY_STRING;
      }
    }
    else
    {
      double d2 = RoundFunction.evaluate(args.get(2));
      if (Double.isNaN(d2) || d2 <= 0)
      {
        // end undefined or it is at or before start
        return EMPTY_STRING;
      }
      double endPos = d1 + d2;
      if (Double.isNaN(endPos))
      {
        return EMPTY_STRING;
      }
      else
      {
        int end = endPos > s1.length() ? s1.length() : ((int)(endPos) - 1);
        return s1.substring(start, end);
      }
    }
  }

}
