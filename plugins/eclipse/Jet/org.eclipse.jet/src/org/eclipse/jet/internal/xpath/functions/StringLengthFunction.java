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


/**
 * Implement the XPath function 'string-length'.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-string-length">http://www.w3.org/TR/xpath#function-string-length</a>.
 *
 */
public class StringLengthFunction implements XPathFunction, XPathFunctionWithContext
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("string-length", null, new StringLengthFunction(), 0, 1); //$NON-NLS-1$
  private Context context;

  /**
   * 
   */
  public StringLengthFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {

    String s;
    if (args.size() == 0)
    {
      s = context.getContextNodeInspector().stringValueOf(context.getContextNode());
    }
    else
    {
      s = StringFunction.evaluate(args.get(0));
    }

    return new Integer(s.length());
  }

  public void setContext(Context context)
  {
    this.context = context;
  }

}
