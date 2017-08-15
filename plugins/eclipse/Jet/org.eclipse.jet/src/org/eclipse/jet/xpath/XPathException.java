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
 * Root for all XPath related Exceptions
 *
 */
public class XPathException extends Exception
{

  /**
   * 
   */
  private static final long serialVersionUID = -5729563043717320280L;

  public XPathException()
  {
    super();
  }

  public XPathException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
  }

  public XPathException(String arg0)
  {
    super(arg0);
  }

  public XPathException(Throwable arg0)
  {
    super(arg0);
  }

}
