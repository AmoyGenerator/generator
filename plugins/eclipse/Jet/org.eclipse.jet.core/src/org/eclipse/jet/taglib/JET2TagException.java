/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
 * $Id: JET2TagException.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;


/**
 * Encapsulate an execution error during tag execution. 
 *
 */
public class JET2TagException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = -2909288156948233977L;

  /**
   * 
   */
  public JET2TagException()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   */
  public JET2TagException(String message)
  {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   */
  public JET2TagException(String message, Throwable cause)
  {
	  
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param cause
   */
  public JET2TagException(Throwable cause)
  {
    super(cause);
    // TODO Auto-generated constructor stub
  }

}
