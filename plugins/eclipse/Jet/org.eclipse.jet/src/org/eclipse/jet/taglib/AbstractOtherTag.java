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
 * An abstract implementation of {@link OtherTag}.
 *
 */
public abstract class AbstractOtherTag extends AbstractCustomTag implements OtherTag
{

  /**
   * Create an instance
   */
  public AbstractOtherTag()
  {
    super();
  }

  /**
   * @return {@link CustomTagKind#OTHER}
   * @see org.eclipse.jet.taglib.CustomTag#getKind()
   */
  public final CustomTagKind getKind()
  {
    return CustomTagKind.OTHER;
  }
}
