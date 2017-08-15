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


import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;
import org.eclipse.jet.xpath.XPathRuntimeException;


/**
 * Implement the XPath function 'sum'.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-sum">http://www.w3.org/TR/xpath#function-sum</a>.
 *
 */
public class SumFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("sum", null, new SumFunction(), 1, 1); //$NON-NLS-1$

  /**
   * 
   */
  public SumFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    Object arg = args.get(0);
    if (!(arg instanceof NodeSet))
    {
      String msg = JET2Messages.XPath_MustBeNodeNodeSet;
      throw new XPathRuntimeException(MessageFormat.format(msg, new Object []{ "sum" })); //$NON-NLS-1$
    }

    double result = 0;
    NodeSet nodeSet = (NodeSet)arg;
    for (Iterator i = nodeSet.iterator(); i.hasNext();)
    {
      Object obj = i.next();
      result = result + NumberFunction.evaluate(obj);
    }
    return new Double(result);
  }

}
