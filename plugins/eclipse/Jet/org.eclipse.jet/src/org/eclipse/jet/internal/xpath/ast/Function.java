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
package org.eclipse.jet.internal.xpath.ast;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.XPathUtil;


/**
 * Implement an XPath function call
 *
 */
public class Function extends ExprNode
{

  private final XPathFunction function;

  private final List argExprs;

  private final boolean requiresContext;

  private final String functionName;

  /**
   * Create a function invocation node
   * @param functionName the function name
   * @param function the function implementation
   * @param argExprs the function arguments.
   */
  public Function(String functionName, XPathFunction function, List argExprs)
  {
    super();
    this.functionName = functionName;
    this.function = function;
    this.argExprs = argExprs;
    this.requiresContext = function instanceof XPathFunctionWithContext;
  }

  public Object evalAsObject(Context context)
  {
    if (requiresContext)
    {
      ((XPathFunctionWithContext)function).setContext(context);
    }

    List functionArgValues = new ArrayList(argExprs.size());

    for (Iterator i = argExprs.iterator(); i.hasNext();)
    {
      ExprNode expr = (ExprNode)i.next();
      functionArgValues.add(expr.evalAsObject(context));
    }
    try
    {
      final Object result = function.evaluate(functionArgValues);
      if(result instanceof Object[]) {
        return XPathUtil.asNodeSet((Object[])result);
      } else if(result instanceof Collection && !(result instanceof NodeSet)) {
        return XPathUtil.asNodeSet((Collection)result);
      } else {
        return result;
      }
    }
    finally
    {
      // ensure that cached function implementation doesn't hang on to context.
      if (requiresContext)
      {
        ((XPathFunctionWithContext)function).setContext(null);
      }
    }
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer(functionName);
    buffer.append('(');
    for (Iterator i = argExprs.iterator(); i.hasNext();)
    {
      ExprNode expr = (ExprNode)i.next();
      buffer.append(expr.toString());
      if (i.hasNext())
      {
        buffer.append(","); //$NON-NLS-1$
      }
    }
    buffer.append(')');
    return buffer.toString();
  }
}
