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
 * An abstract implementation of {@link IteratingTag}.
 *
 */
public abstract class AbstractIteratingTag extends AbstractContainerTag implements IteratingTag
{

  private String delimiter;

  protected boolean first = true;

  /**
   * 
   */
  public AbstractIteratingTag()
  {
    super();
  }

  /**
   * @return {@link CustomTagKind#ITERATING}
   * @see org.eclipse.jet.taglib.CustomTag#getKind()
   */
  public final CustomTagKind getKind()
  {
    return CustomTagKind.ITERATING;
  }

  /**
   * Default implementation of {@link ContainerTag#doBeforeBody(TagInfo, JET2Context, JET2Writer)} 
   * that writes a delimiter if set.
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    writeDelimiterIfAppropriate();
  }

  /**
   * 
   */
  private void writeDelimiterIfAppropriate()
  {
    if (first)
    {
      first = false;
    }
    else if (getDelimiter() != null)
    {
      getOut().write(getDelimiter());
    }
  }

  /**
   * Default implementation of {@link ContainerTag#doAfterBody(TagInfo, JET2Context, JET2Writer)} that does nothing.
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // does nothing by design
  }

  /**
   * Set a delimiter string that is writen between loop iterations. The default is <code>null</code>.
   * @param delimiter a delimiter string. A value of <code>null</code> means no delimiter.
   */
  protected void setDelimiter(String delimiter)
  {
    this.delimiter = delimiter;
  }

  /**
   * Return the current delimiter string.
   * @return the current delimiter string. May be <code>null</code>.
   */
  protected String getDelimiter()
  {
    return delimiter;
  }
}
