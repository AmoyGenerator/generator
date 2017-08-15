/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.taglib;


/**
 * Abstract implementation a tag element that treats is body content as if it where
 * an argument to a function, and writes the resulting calculation to the output.
 *
 */
public abstract class AbstractFunctionTag extends AbstractCustomTag implements FunctionTag
{

  /**
   * 
   */
  public AbstractFunctionTag()
  {
    super();
  }

  /**
   * @return {@link CustomTagKind#FUNCTION}
   * @see org.eclipse.jet.taglib.CustomTag#getKind()
   */
  public final CustomTagKind getKind()
  {
    return CustomTagKind.FUNCTION;
  }

}
