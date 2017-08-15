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
 * Implement the XPath functions 'true' and 'false'.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-true">http://www.w3.org/TR/xpath#function-true</a>
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-false">http://www.w3.org/TR/xpath#function-false</a>
 *
 */
public class TrueFalseFunction implements XPathFunction
{

  public static final XPathFunctionMetaData TRUE_FUNCTION_META_DATA = new XPathFunctionMetaData("true", null, new TrueFalseFunction(true), 0, 0); //$NON-NLS-1$
  public static final XPathFunctionMetaData FALSE_FUNCTION_META_DATA = new XPathFunctionMetaData("false", null, new TrueFalseFunction(false), 0, 0); //$NON-NLS-1$
  private final Boolean functionReturnValue;

  public TrueFalseFunction(boolean functionReturnValue)
  {
    this.functionReturnValue = Boolean.valueOf(functionReturnValue);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    return functionReturnValue;
  }

}
