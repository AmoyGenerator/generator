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
 * Implement the XPath function 'starts-with'.
 * <p>
 * <a href="http://www.w3.org/TR/xpath#function-starts-with">http://www.w3.org/TR/xpath#function-starts-with</a>.
 *
 */
public class StartsWithFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("starts-with", null, new StartsWithFunction(), 2, 2); //$NON-NLS-1$

  /**
   * 
   */
  public StartsWithFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    String s1 = StringFunction.evaluate(args.get(0));
    String s2 = StringFunction.evaluate(args.get(1));

    final Boolean valueOf = Boolean.valueOf(s1.startsWith(s2));
    return valueOf;
  }

}
