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
package org.eclipse.jet.internal.xpath;


import java.text.MessageFormat;

import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.xpath.ast.ExprNode;
import org.eclipse.jet.internal.xpath.functions.BuiltinXPathFunctionResolver;
import org.eclipse.jet.internal.xpath.parser.Token;
import org.eclipse.jet.internal.xpath.parser.XPathParser;
import org.eclipse.jet.xpath.IAnnotationManager;
import org.eclipse.jet.xpath.NamespaceContext;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFunctionResolver;
import org.eclipse.jet.xpath.XPathSyntaxException;
import org.eclipse.jet.xpath.XPathVariableResolver;


/**
 * XPath implementation
 *
 */
public class XPathImpl implements XPath
{

  private XPathFunctionResolver functionResolver = BuiltinXPathFunctionResolver.getInstance();

  private XPathVariableResolver variableResolver;

  private final IAnnotationManager annotationManager;

  private NamespaceContext nsContext;

  /**
   * 
   */
  public XPathImpl(IAnnotationManager annotationManager)
  {
    super();
    this.annotationManager = annotationManager;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPath#setXPathFunctionResolver(org.eclipse.jet.xpath.XPathFunctionResolver)
   */
  public void setXPathFunctionResolver(XPathFunctionResolver resolver)
  {
    this.functionResolver = resolver;

  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPath#getXPathFunctionResolver()
   */
  public XPathFunctionResolver getXPathFunctionResolver()
  {
    return functionResolver;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPath#setXPathVariableResolver(org.eclipse.jet.xpath.XPathVariableResolver)
   */
  public void setXPathVariableResolver(XPathVariableResolver resolver)
  {
    this.variableResolver = resolver;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPath#getXPathVariableResolver()
   */
  public XPathVariableResolver getXPathVariableResolver()
  {
    return variableResolver;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPath#compile(java.lang.String)
   */
  public XPathExpression compile(String expression) throws XPathException
  {
    XPathParser parser = new XPathParser(expression, functionResolver, nsContext);

    ExprNode expr = parser.expr();
    if(parser.peekNext() != Token.EOF_TOKEN) {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_UnrecognizedToken, new Object[] {parser.peekNext()}));
    }
    // FIXME Do a semantic analysis of expr to detect semantic errors not found in parsing.

    XPathExpressionImpl pathExpr = new XPathExpressionImpl(expr, variableResolver, functionResolver, annotationManager);
    return pathExpr;
  }

  public void setNamespaceContext(NamespaceContext nsContext)
  {
    if (nsContext == null)
    {
      throw new NullPointerException();
    }
    this.nsContext = nsContext;
  }

  public NamespaceContext getNamespaceContext()
  {
    return nsContext;
  }

}
