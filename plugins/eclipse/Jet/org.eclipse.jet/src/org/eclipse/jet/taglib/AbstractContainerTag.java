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
 * An abstract implementation of {@link ContainerTag}.
 */
public abstract class AbstractContainerTag extends AbstractCustomTag implements ContainerTag
{

  /**
   * 
   */
  public AbstractContainerTag()
  {
    super();
  }

  /**
   * @return {@link CustomTagKind#CONTAINER}
   */
  public CustomTagKind getKind()
  {
    return CustomTagKind.CONTAINER;
  }

  /**
   * Default implementation of {@link ContainerTag#setBodyContent(JET2Writer)} that writes
   * body content to the tag's output.
   */
  public void setBodyContent(JET2Writer bodyContent)
  {
    getOut().write(bodyContent);
  }

  /**
   * Default implementation of {@link EmptyTag#doAction(TagInfo, JET2Context, JET2Writer)} that simply
   * calls {@link ContainerTag#doBeforeBody(TagInfo, JET2Context, JET2Writer)} and then
   * {@link ContainerTag#doAfterBody(TagInfo, JET2Context, JET2Writer)}. 
   * This method (or subclassed versions) are only ever called if the tag is declared in the
   * extension point with the 'allowAsEmpty' set to <code>true</code>.
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    this.doBeforeBody(td, context, out);
    this.doAfterBody(td, context, out);
  }
  
  public void doEnd(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    
  }
  
}
