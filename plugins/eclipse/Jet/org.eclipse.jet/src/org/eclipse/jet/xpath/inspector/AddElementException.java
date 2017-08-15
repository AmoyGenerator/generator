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

package org.eclipse.jet.xpath.inspector;


/**
 * Exception thrown in case of errors by {@link org.eclipse.jet.xpath.inspector.IElementInspector#addTextElement(Object, String, String, boolean)}.
 *
 */
public class AddElementException extends Exception
{

  /**
   * 
   */
  private static final long serialVersionUID = -8009815417886075870L;

  /**
   * 
   */
  public AddElementException()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   */
  public AddElementException(String arg0)
  {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   * @param arg1
   */
  public AddElementException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   */
  public AddElementException(Throwable arg0)
  {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

}
