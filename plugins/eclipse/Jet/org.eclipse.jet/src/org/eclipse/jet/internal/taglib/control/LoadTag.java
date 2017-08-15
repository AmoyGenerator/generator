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


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * Implement the Standard Jet Control tag 'load'.
 */
public class LoadTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public LoadTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {

    String url = getAttribute("url"); //$NON-NLS-1$
    String var = getAttribute("var"); //$NON-NLS-1$
    String urlContext = getAttribute("urlContext"); //$NON-NLS-1$
    String type = getAttribute("type"); //$NON-NLS-1$
    String loaderId = getAttribute("loader"); //$NON-NLS-1$

    TransformContextExtender tce = new TransformContextExtender(context);
    URL baseURL = tce.getBaseURL(urlContext);
    if(url.startsWith("/")) //$NON-NLS-1$
    {
      url = url.substring(1);
    }

    URL modelURL;
    try
    {
      modelURL = new URL(baseURL, url);
    }
    catch (MalformedURLException e)
    {
      throw new JET2TagException(e);
    }

    try
    {
      Object modelRoot = TransformContextExtender.loadModel(modelURL, loaderId, type);
      context.setVariable(var, modelRoot);
    }
    catch (IOException e)
    {
      final String msg = JET2Messages.LoadTag_CouldNotLoad;
      throw new JET2TagException(MessageFormat.format(msg, new Object []{ url }), e);
    }
    catch (CoreJETException e)
    {
      final String msg = JET2Messages.LoadTag_CouldNotLoad;
      throw new JET2TagException(MessageFormat.format(msg, new Object []{ url }), e);
    }


  }

}
