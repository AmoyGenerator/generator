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
package org.eclipse.jet.internal.taglib.control;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.MarkerHelper;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.transform.TransformContextExtender;

/**
 * Implement the standard JET2 tag &lt;c:marker&gt;.
 */
public class MarkerTag extends AbstractContainerTag
{

  int startOffset;
  /**
   * 
   */
  public MarkerTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if(out instanceof BufferedJET2Writer) {
      BufferedJET2Writer bufferedOut = (BufferedJET2Writer)out;
      startOffset = bufferedOut.getContentLength();
    } else {
      // expected out to be a BufferedJET2Writer
      throw new IllegalArgumentException();
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if (out instanceof BufferedJET2Writer)
    {
      BufferedJET2Writer bufferedOut = (BufferedJET2Writer)out;
      String description = getAttribute("description"); //$NON-NLS-1$
      int endOffset = bufferedOut.getContentLength();
      MarkerHelper.createMarkerOnWriter(
        bufferedOut,
        startOffset,
        endOffset,
        description,
        td,
        TransformContextExtender.getInstance(context).getTemplatePath());
    }
    else
    {
      // expected out to be a BufferedJET2Writer
      throw new IllegalArgumentException();
    }
  }

}
