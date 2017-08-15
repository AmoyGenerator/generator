/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: EvalFunction.java,v 1.1 2009/03/06 21:31:23 pelder Exp $
 */
package org.eclipse.jet.internal.xpath.functions.extras;


import java.util.List;

import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFactory;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.jet.xpath.XPathUtil;
import org.eclipse.osgi.util.NLS;


/**
 * Implement eval(String xpathExpression)
 */
public class EvalFunction implements XPathFunction, XPathFunctionWithContext
{

  private Context context;

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    // one and only argument is a string containing an unparsed XPath expression
    String unparsedXPath = XPathUtil.xpathString(args.get(0));

    // create an XPath instance, on configure it from the current XPath context.
    XPath xpath = XPathFactory.newInstance().newXPath(context.getAnnotationManager());
    xpath.setXPathFunctionResolver(context.getFunctionResolver());
    xpath.setXPathVariableResolver(context.getVariableResolver());

    try
    {
      // compile and return the result of the XPath
      XPathExpression compiledXPath = xpath.compile(unparsedXPath);
      return compiledXPath.evaluate(context.getContextNode());
    }
    catch (XPathException e)
    {
      throw new XPathRuntimeException(NLS.bind(Messages.EvalFunction_ExpressionCompileError, unparsedXPath), e);
    }
  }

  public void setContext(Context context)
  {
    // save the current XPath context - used by evaluate(List)
    this.context = context;
  }

}
