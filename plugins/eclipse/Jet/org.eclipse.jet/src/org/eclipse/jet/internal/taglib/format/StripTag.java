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

package org.eclipse.jet.internal.taglib.format;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the Standard JET Format Tag 'strip' (strip whitespace).
 *
 */
public class StripTag extends AbstractFunctionTag
{

  /**
   * 
   */
  public StripTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {

    StringBuffer buffer = new StringBuffer(bodyContent);
    for (int i = 0; i < buffer.length(); i++)
    {
      if (Character.isSpaceChar(buffer.charAt(i)))
      {
        buffer.deleteCharAt(i);
      }
    }
    return buffer.toString();
  }

}
