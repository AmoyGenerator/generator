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


import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;


/**
 * Implement the XPath 'concat' function.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-concat">http://www.w3.org/TR/xpath#function-concat</a>.
 *
 */
public class ConcatFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("concat", null, new ConcatFunction(), 2, -1); //$NON-NLS-1$

  /**
   * 
   */
  public ConcatFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    StringBuffer result = new StringBuffer();

    for (Iterator iter = args.iterator(); iter.hasNext();)
    {
      Object value = (Object)iter.next();
      result.append(StringFunction.evaluate(value));
    }
    return result.toString();
  }

}
