/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
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
 * /
 *******************************************************************************/
package org.eclipse.jet.xpath;


/**
 * Interface to a compiled XPath expression.
 * <p>Note: Once JET moves to Java 1.5 or later only, this interface will be migrated to javax.xml.xpath.XPathExpression</p>
 *
 */
public interface XPathExpression
{

  /**
   * Evaluate the expression and convert the result to a String using the XPath String function
   * @param contextObject the context object for the expression
   * @return the expression result.
   * @throws XPathRuntimeException if an evaluation error occures
   * @see XPathUtil#xpathString(Object)
   */
  String evaluateAsString(Object contextObject) throws XPathRuntimeException;

  /**
   * Evaluate the expression and convert the result to a boolean using the XPath boolean function.
   * @param contextObject the context object for the expression
   * @return the expression result.
   * @throws XPathRuntimeException if an evaluation error occures
   * @see XPathUtil#xpathBoolean(Object)
   */
  boolean evaluateAsBoolean(Object contextObject) throws XPathRuntimeException;

  /**
   * Evaluate the expression and convert the result to a number using the XPath number function.
   * @param contextObject the context object for the expression.
   * @return the expression result
   * @throws XPathRuntimeException if an evaluation error occures
   * @see XPathUtil#xpathNumber(Object)
   */
  double evaluateAsNumber(Object contextObject) throws XPathRuntimeException;

  /**
   * Evaluate the expression and return the result as a Node set. If the result cannot be converted to a
   * node set, then an exception is thrown.
   * @param contextObject the context object for the expression.
   * @return the expression result
   * @throws XPathRuntimeException if an evaluation error occures
   */
  NodeSet evaluateAsNodeSet(Object contextObject) throws XPathRuntimeException;

  /**
   * Evaluate the expression using {@link #evaluateAsNodeSet(Object)} and then return the first object in
   * the node set. An exception is thrown if the expression cannot be converted to a node set. 
   * If the resulting node set is empty, then null is returned.
   * @param contextObject the context object for the expression.
   * @return the first element in the node set or <code>null</code> if the node set is empty.
   * @throws XPathRuntimeException if an evaluation error occures
   */
  Object evaluateAsSingleNode(Object contextObject) throws XPathRuntimeException;

  /**
   * Evaluate the expression and return the 'natural' result of the expression. The result may be any of:
   * <ul>
   * <li>{@link String}</li>
   * <li>{@link Boolean}</li>
   * <li>{@link Number}</li>
   * <li>{@link NodeSet}</li>
   * </ul>
   * @param contextObject the context object for the expression.
   * @return the expression result
   * @throws XPathRuntimeException if an evaluation error occures
   */
  Object evaluate(Object contextObject) throws XPathRuntimeException;

}
