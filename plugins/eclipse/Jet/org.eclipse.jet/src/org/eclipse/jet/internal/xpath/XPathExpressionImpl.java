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


import org.eclipse.jet.internal.xpath.ast.ExprNode;
import org.eclipse.jet.internal.xpath.functions.BooleanFunction;
import org.eclipse.jet.internal.xpath.functions.NumberFunction;
import org.eclipse.jet.internal.xpath.functions.StringFunction;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.IAnnotationManager;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFunctionResolver;
import org.eclipse.jet.xpath.XPathVariableResolver;


/**
 * Implementation of XPathExpression
 *
 */
public class XPathExpressionImpl implements XPathExpression
{

  private final ExprNode node;

  private final XPathVariableResolver variableResolver;

  private final IAnnotationManager annotationManager;

  private final XPathFunctionResolver functionResolver;

  /**
   * @param node The parsed expression
   * @param variableResolver the variable resolver
   * @param functionResolver 
   * 
   */
  public XPathExpressionImpl(ExprNode node, XPathVariableResolver variableResolver, XPathFunctionResolver functionResolver, IAnnotationManager annotationManager)
  {
    super();
    this.node = node;
    this.variableResolver = variableResolver;
    this.functionResolver = functionResolver;
    this.annotationManager = annotationManager;
  }

  public Object evaluate(Object contextObject)
  {
    //add by yuxin start
    if(node == null){
      return null;
    }
    //add by yuxin end
    return node.evalAsObject(new Context(contextObject, variableResolver, annotationManager, functionResolver));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathExpression#evaluateAsString(java.lang.Object)
   */
  public String evaluateAsString(Object contextObject)
  {

    return StringFunction.evaluate(evaluate(contextObject));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathExpression#evaluateAsBoolean(java.lang.Object)
   */
  public boolean evaluateAsBoolean(Object contextObject)
  {
    return BooleanFunction.evaluate(evaluate(contextObject));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathExpression#evaluateAsNumber(java.lang.Object)
   */
  public double evaluateAsNumber(Object contextObject)
  {
    return NumberFunction.evaluate(evaluate(contextObject));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathExpression#evaluateAsNodeSet(java.lang.Object)
   */
  public NodeSet evaluateAsNodeSet(Object contextObject)
  {
    Object result = evaluate(contextObject);
    if (result instanceof NodeSet)
    {
      return (NodeSet)result;
    }
    else
    {
      return NodeSetImpl.EMPTY_SET;
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathExpression#evaluateAsSingleNode(java.lang.Object)
   */
  public Object evaluateAsSingleNode(Object contextObject)
  {
    NodeSet nodeSet = evaluateAsNodeSet(contextObject);
    return nodeSet.size() > 0 ? nodeSet.iterator().next() : null;
  }

  public String toString()
  {
    return node.toString();
  }
}
