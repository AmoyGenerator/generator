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
 * Implement the XPath function 'ceiling'.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-ceiling">http://www.w3.org/TR/xpath#function-ceiling</a>.
 *
 */
public class CeilingFunction implements XPathFunction
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("ceiling", null, new CeilingFunction(), 1, 1); //$NON-NLS-1$

  /**
   * 
   */
  public CeilingFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    double d = NumberFunction.evaluate(args.get(0));
    return new Double(Math.ceil(d));
  }

}
