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
import org.eclipse.jet.xpath.XPathUtil;

/**
 * Implement the XPath 1.0 function 'translate'
 */
public class TranslateFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("translate", null, new TranslateFunction(), 3, 3); //$NON-NLS-1$

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    String arg = XPathUtil.xpathString(args.get(0));
    String mapString = XPathUtil.xpathString(args.get(1));
    String transString = XPathUtil.xpathString(args.get(2));
    
    for (int i = 0; i < mapString.length() && arg.length() > 0; i++)
    {
      char c = mapString.charAt(i);
      char replacement = i < transString.length() ? transString.charAt(i) : 0;
      if(replacement != 0)
      {
        arg = arg.replace(c, replacement);
      }
      else
      {
        arg = arg.replaceAll("\\Q" + c + "\\E", "");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
      }
    }
    return arg;
  }

}
