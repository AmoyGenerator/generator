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
package org.eclipse.jet.xpath;

/**
 * Indentify a runtime exception arising from the execution of an XPath expression.
 */
public class XPathRuntimeException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = -647803127981658849L;

  /**
   * 
   */
  public XPathRuntimeException()
  {
    super();
  }

  /**
   * @param message
   */
  public XPathRuntimeException(String message)
  {
    super(message);
  }

  /**
   * @param message
   * @param throwable
   */
  public XPathRuntimeException(String message, Throwable throwable)
  {
    super(message, throwable);
  }

  /**
   * @param throwable
   */
  public XPathRuntimeException(Throwable throwable)
  {
    super(throwable);
  }

}
