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
package org.eclipse.jet;

/**
 * Represents an exception in the core JET engine
 */
public class CoreJETException extends Exception
{

  /**
   * 
   */
  public CoreJETException()
  {
    super();
  }

  /**
   * Create an exception wrapping another exception
   * @param cause the root cause of the exception
   */
  public CoreJETException(Throwable cause)
  {
    super(cause);
  }

  /**
   * 
   */
  private static final long serialVersionUID = -3547604779390116089L;

  /**
   * Create a CoreJETException
   * @param msg the exception message
   */
  public CoreJETException(String msg)
  {
    super(msg);
  }

  /**
   * Create a CoreJETException
   * @param message the execution message
   * @param cause the root cause of the exception
   */
  public CoreJETException(String message, Throwable cause)
  {
    super(message, cause);
  }


}
