/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
 * $Id$
 */

package org.eclipse.jet.internal.taglib.control;


import java.text.MessageFormat;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the standard JET2 control tag 'addTextElement'.
 *
 */
public class AddTextElementTag extends AbstractFunctionTag
{

  /**
   * 
   */
  public AddTextElementTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {
    String select = getAttribute("select"); //$NON-NLS-1$
    String name = getAttribute("name"); //$NON-NLS-1$

    String var = getAttribute("var"); //$NON-NLS-1$
    boolean asCData = Boolean.valueOf(getAttribute("cdata")).booleanValue(); //$NON-NLS-1$

    XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);

    final Object parentElement = xpathExtender.resolveSingle(xpathExtender.currentXPathContextObject(), select);
    if (parentElement == null)
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.XPath_NoElementSelected, new Object []{ select }));
    }

    final Object newElement = xpathExtender.addTextElement(parentElement, name, bodyContent, asCData);
    if (var != null)
    {
      context.setVariable(var, newElement);
    }
    return ""; //$NON-NLS-1$
  }

}
