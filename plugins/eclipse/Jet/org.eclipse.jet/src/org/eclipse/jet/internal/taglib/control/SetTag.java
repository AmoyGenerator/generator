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
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the standard JET2 Control tag 'set'.
 */
public class SetTag extends AbstractContainerTag
{

  private String tagContent = ""; //$NON-NLS-1$
  
  /**
   * 
   */
  public SetTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String selectXPath = getAttribute("select"); //$NON-NLS-1$
    String name = getAttribute("name"); //$NON-NLS-1$

    XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);
    Object element = xpathExtender.resolveSingle(xpathExtender.currentXPathContextObject(), selectXPath);
    if (element == null)
    {
      throw new JET2TagException(JET2Messages.XPath_NoElementSelected);
    }

    final boolean isSet = xpathExtender.setAttribute(element, name, tagContent);
    if (!isSet)
    {
      String msg = JET2Messages.SetTag_CoundNotSet;
      throw new JET2TagException(MessageFormat.format(msg, new Object []{ name }));
    }
  }

  public void setBodyContent(JET2Writer bodyContent)
  {
    tagContent = bodyContent.toString();
  }
}
