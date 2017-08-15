/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: IsVariableDefinedFunction.java,v 1.1 2008/04/14 20:40:55 pelder Exp $
 */
package org.eclipse.jet.internal.xpath.functions.extras;

import java.util.List;

import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.XPathUtil;

/**
 * Implement XPath function <code>boolean isVariableDefined(string name)</code>.
 */
public class IsVariableDefinedFunction implements XPathFunction, XPathFunctionWithContext
{

  private Context context;

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    final String variableName = XPathUtil.xpathString(args.get(0));
    return Boolean.valueOf(context.getVariableResolver().resolveVariable(variableName) != null);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunctionWithContext#setContext(org.eclipse.jet.xpath.Context)
   */
  public void setContext(Context context)
  {
    this.context = context;
  }

}
