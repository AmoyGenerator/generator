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


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;


/**
 * Partial implementation of {@link org.eclipse.jet.taglib.ConditionalTag}
 *
 */
public abstract class AbstractConditionalTag extends AbstractContainerTag implements ConditionalTag
{

  /**
   * Construct an instance
   */
  public AbstractConditionalTag()
  {
    super();
  }

  /**
   * @return the value {@link CustomTagKind#CONDITIONAL}.
   * @see org.eclipse.jet.taglib.CustomTag#getKind()
   */
  public final CustomTagKind getKind()
  {
    return CustomTagKind.CONDITIONAL;
  }

  /**
   * Default version of {@link ContainerTag#doBeforeBody(TagInfo, JET2Context, JET2Writer)} that does nothing.
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // does nothing by design.
  }

  /**
   * Default version of {#link {@link ContainerTag#doAfterBody(TagInfo, JET2Context, JET2Writer) } that does nothing.
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // does nothing by design.
  }
}
