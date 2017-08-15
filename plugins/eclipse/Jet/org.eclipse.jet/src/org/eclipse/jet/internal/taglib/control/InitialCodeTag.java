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


import java.text.MessageFormat;

import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the 'initialCode' subtag of the JET2 Standard Tag 'userRegion'.
 *
 */
public class InitialCodeTag extends AbstractContainerTag
{

  private int initialCodeOffset;

  /**
   * 
   */
  public InitialCodeTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doStart(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if(out instanceof BufferedJET2Writer) {
      initialCodeOffset = ((BodyContentWriter)out).getContentLength();
    } else {
      throw new IllegalArgumentException();
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doEnd(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String unmodifiedMarker = getAttribute("unmodifiedMarker"); //$NON-NLS-1$

    if (!(getParent() instanceof UserRegionTag))
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_IllegalParent, new Object []{ "userRegion" })); //$NON-NLS-1$
    }

    if (out instanceof BodyContentWriter){
      ((UserRegionTag)getParent()).setInitiaCodeRegion(initialCodeOffset, ((BufferedJET2Writer)out).getContentLength(), unmodifiedMarker);
    } else {
      throw new IllegalArgumentException(); 
    }
  }

}
