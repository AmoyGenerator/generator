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

import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.XPathUtil;

/**
 * Implement the XPath Function 'normalize-space'
 */
public class NormalizeSpaceFunction implements XPathFunction, XPathFunctionWithContext
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("normalize-space", null, new NormalizeSpaceFunction(), 0, 1); //$NON-NLS-1$

  private Context context;

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    String arg;
    if(args.size() == 0)
    {
      arg = XPathUtil.xpathString(context.getContextNode());
    }
    else 
    {
      arg = XPathUtil.xpathString(args.get(0));
    }
    
    return normalizeSpace(arg);
  }

  
  /**
   * Normalize a string, accoring to the XPath normalize-space function.
   * @param arg the original string.
   * @return the normalized string
   */
  public static String normalizeSpace(String input) {
    final int inputLength = input.length();
    final StringBuffer result = new StringBuffer(inputLength);
    int wsRunStart = -1; // -1 indicates not run in effect
    for(int i = 0; i < inputLength; i++) {
      final char c = input.charAt(i);
      if(Character.isWhitespace(c)) {
        if(wsRunStart == -1) {
          wsRunStart = i;
        }
      } else {
        if(wsRunStart > 0) {    // intentionally misses runs starting at 0
            result.append(' ');
        }
        wsRunStart = -1;
        result.append(c);
      }
    }
    // loop intentionally suppresses trailing
    return result.toString();
  }
  
  public void setContext(Context context)
  {
    this.context  = context;
  }

}
