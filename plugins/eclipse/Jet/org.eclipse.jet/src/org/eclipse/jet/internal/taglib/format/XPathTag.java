/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: XPathTag.java,v 1.1 2008/04/28 16:45:56 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement &lt;f:xpath select="<i>expr</i>"&gt;<i>content</i>&lt;/f:xpath&gt;.
 */
public class XPathTag extends AbstractFunctionTag
{

  private static final String VAR_BODY_CONTENT = "bodyContent"; //$NON-NLS-1$

  private static final String ATTR_SELECT = "select"; //$NON-NLS-1$

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {
    final String xpath = getAttribute(ATTR_SELECT);
    final Object savedBodyContent = context.hasVariable(VAR_BODY_CONTENT) ? context.getVariable(VAR_BODY_CONTENT) : null;

    try
    {
      context.setVariable(VAR_BODY_CONTENT, bodyContent);
      final XPathContextExtender xce = XPathContextExtender.getInstance(context);
      final String result = xce.resolveAsString(xce.currentXPathContextObject(), xpath);
      return result;
    }
    finally
    {
      if (savedBodyContent != null)
      {
        context.setVariable(VAR_BODY_CONTENT, savedBodyContent);
      }
    }
  }

}
