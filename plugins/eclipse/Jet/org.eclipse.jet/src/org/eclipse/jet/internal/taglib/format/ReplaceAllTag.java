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


import java.util.regex.PatternSyntaxException;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the Standard JET2 Format Tag 'replaceAll'
 *
 */
public class ReplaceAllTag extends AbstractFunctionTag
{

  /**
   * 
   */
  public ReplaceAllTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {
    String value = getAttribute("value"); //$NON-NLS-1$
    String replacement = getAttribute("replacement"); //$NON-NLS-1$
    boolean isRegex = Boolean.valueOf(getAttribute("regex")).booleanValue(); //$NON-NLS-1$

    try
    {
      if (isRegex)
      {
        return bodyContent.replaceAll(value, replacement);
      }
      else
      {
        StringBuffer result = new StringBuffer(bodyContent);
        int i = 0;
        while ((i = result.indexOf(value, i)) >= 0)
        {
          result.replace(i, i + value.length(), replacement);
          i = i + replacement.length() + 1;
        }
        return result.toString();
      }
    }
    catch (PatternSyntaxException e)
    {
      throw new JET2TagException(e);
    }
  }

}
