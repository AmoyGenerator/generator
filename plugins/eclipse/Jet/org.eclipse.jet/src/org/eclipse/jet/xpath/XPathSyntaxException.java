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
 * Represent a syntactic error in XPath parsing.
 *
 */
public class XPathSyntaxException extends XPathException
{

  /**
   * 
   */
  private static final long serialVersionUID = -966592232843623084L;

  /**
   * 
   */
  public XPathSyntaxException()
  {
    super();
  }

  /**
   * @param arg0
   * @param arg1
   */
  public XPathSyntaxException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public XPathSyntaxException(String arg0)
  {
    super(arg0);
  }

  /**
   * @param arg0
   */
  public XPathSyntaxException(Throwable arg0)
  {
    super(arg0);
  }

}
