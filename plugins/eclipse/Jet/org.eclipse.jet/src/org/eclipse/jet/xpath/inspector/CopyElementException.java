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
 * Thrown during if an error occurs during the execution of 
 * {@link org.eclipse.jet.xpath.inspector.IElementInspector#copyElement(Object, Object, String, boolean)}.
 *
 */
public class CopyElementException extends Exception
{

  /**
   * 
   */
  private static final long serialVersionUID = -7536131797925451416L;

  /**
   * 
   */
  public CopyElementException()
  {
    super();
  }

  /**
   * @param arg0
   */
  public CopyElementException(String arg0)
  {
    super(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public CopyElementException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  public CopyElementException(Throwable arg0)
  {
    super(arg0);
  }

}
