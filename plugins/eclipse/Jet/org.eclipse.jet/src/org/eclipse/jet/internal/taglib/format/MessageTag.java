/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: MessageTag.java,v 1.1 2009/03/06 20:53:09 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;


import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.osgi.util.NLS;


/**
 * @author pelder
 */
public class MessageTag extends AbstractContainerTag
{

  private String key = null;

  private String var = null;

  private ResourceBundle bundle = null;

  private List parameters = new ArrayList(5);

  public void addParam(String value)
  {
    parameters.add(value);
  }

  private Object[] getBindings()
  {
    return parameters.toArray();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    key = getAttribute("key"); //$NON-NLS-1$
    var = getAttribute("var"); //$NON-NLS-1$
    bundle = ResourceBundleUtil.findBundle(context, this);
  }

  public void setBodyContent(JET2Writer bodyContent)
  {
    // do not write body content
    // if no key was specified in an attribute, get if from the content...
    if (key == null && bodyContent instanceof BufferedJET2Writer)
    {
      key = ((BufferedJET2Writer)bodyContent).getContent().trim();
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    final String formattedMessage;
    if (key == null)
    {
      throw new JET2TagException(Messages.MessageTag_NoKey);
    }
    String message;
    try
    {
      message = bundle.getString(key);
    }
    catch (MissingResourceException e)
    {
      // including the MissingResourceException adds no useful information - don't add it
      throw new JET2TagException(NLS.bind(Messages.MessageTag_NoMessageForKey, key));
    }
    formattedMessage = NLS.bind(message, getBindings());

    if (var != null)
    {
      context.setVariable(var, formattedMessage);
    }
    else
    {
      out.write(formattedMessage);
    }
  }

}
