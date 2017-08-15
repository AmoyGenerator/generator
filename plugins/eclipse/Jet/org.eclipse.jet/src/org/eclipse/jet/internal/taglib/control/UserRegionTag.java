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
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.UserRegionHelper;


/**
 * Implements the JET2 Standard tag 'userRegion'.
 *
 */
public class UserRegionTag extends AbstractContainerTag
{

  private int userRegionOffset;
  private int initialCodeStart = -1;
  private int initialCodeEnd = -1;
  private String unmodifiedMarker = null;


  /**
   * 
   */
  public UserRegionTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doEnd(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if(out instanceof BufferedJET2Writer) {
      UserRegionHelper.markUserRegion(out, userRegionOffset, ((BufferedJET2Writer)out).getContentLength());
    } else {
      throw new IllegalArgumentException();
    }
    if (initialCodeStart >= 0)
    {
      UserRegionHelper.markInitialCode(out, initialCodeStart, initialCodeEnd, unmodifiedMarker);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doStart(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if(out instanceof BufferedJET2Writer) {
      userRegionOffset = ((BufferedJET2Writer)out).getContentLength();
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  /**
   * Allow InitialCodeTag to register its region.
   * @param start start of the initial code region (includsive of start)
   * @param end end of the inital code region (exclusive of end)
   * @param unmodifiedMarker an optional marker that indicates unmodified text.
   */
  void setInitiaCodeRegion(int start, int end, String unmodifiedMarker) {
    this.initialCodeStart = start;
    this.initialCodeEnd = end;
    this.unmodifiedMarker = unmodifiedMarker;
  }

}
