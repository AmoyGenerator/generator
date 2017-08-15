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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet;


/**
 * Runtime exception to wrap the rather inconvenient checked exception thrown by
 * the org.eclipse.jface.text classes and methods.
 * 
 * @deprecated No longer used
 *
 */
public class WriterPositionException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = -4660059269261007284L;

  /**
   * Create a WriterPostionException that wraps another exception
   * @param e
   */
  public WriterPositionException(Throwable e)
  {
    super(e);
  }

}
