/**
 * <copyright>
 *
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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

package org.eclipse.jet.internal.taglib.workspace;


import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * Implement the JET2 standard workspace tag 'copyFile'.
 *
 */
public class CopyFileTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public CopyFileTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // required attributes
    String url = getAttribute("src"); //$NON-NLS-1$
    IPath path = new Path(getAttribute("target")); //$NON-NLS-1$
    // optional attributes
    String urlContext = getAttribute("srcContext"); //$NON-NLS-1$
    String binary = getAttribute("binary"); //$NON-NLS-1$
    String replace = getAttribute("replace"); //$NON-NLS-1$
    String srcEncoding = getAttribute("srcEncoding"); //$NON-NLS-1$
    String targetEncoding = getAttribute("targetEncoding"); //$NON-NLS-1$

    boolean textCopy = true;
    if (binary != null && "true".equalsIgnoreCase(binary)) { //$NON-NLS-1$
      textCopy = false;
    }
    boolean replaceExisting = true;
    if (replace != null && "false".equalsIgnoreCase(replace)) { //$NON-NLS-1$
      replaceExisting = false;
    }

    WorkspaceContextExtender wsExtender = WorkspaceContextExtender.getInstance(context);
    IFile file = getTargetFile(path, wsExtender);


    if(file.exists() && !replaceExisting) {
      // don't do anything, we're not going to write anyhow...
      return;
    }
    
    URL sourceURL = getSourceURL(url, urlContext, context);
    
    IWorkspaceAction action;
    if (textCopy)
    {
      final String sourceFileContents = ActionsUtil.readTextFile(sourceURL, srcEncoding);
      action = new WsCopyTextFileAction(context.getTemplatePath(), td, sourceFileContents, file, replaceExisting, targetEncoding);
    }
    else
    {
      final byte[] contents = ActionsUtil.readBinaryFile(sourceURL);
      action = new WsCopyBinaryFileAction(context.getTemplatePath(), td, contents, file, replaceExisting);
    }
    wsExtender.addAction(action);

  }

  /**
   * @param url
   * @param urlContext
   * @param tce
   * @return
   * @throws JET2TagException
   */
  private URL getSourceURL(String url, String urlContext, JET2Context context) throws JET2TagException
  {
    TransformContextExtender tce = TransformContextExtender.getInstance(context);

    final URL baseURL = tce.getBaseURL(urlContext);

    if (url.startsWith("/")) { //$NON-NLS-1$
      url = url.substring(1);
    }

    URL sourceURL;
    try
    {
      sourceURL = new URL(baseURL, url);
    }
    catch (MalformedURLException e)
    {
      throw new JET2TagException(e);
    }
    return sourceURL;
  }

  /**
   * @param path
   * @param wsExtender
   * @return
   * @throws JET2TagException
   */
  private IFile getTargetFile(IPath path, WorkspaceContextExtender wsExtender) throws JET2TagException
  {
    IFile file;
    if (path.isAbsolute() || !wsExtender.existsContainer())
    {
      try
      {
        file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
      }
      catch (IllegalArgumentException e)
      {
        throw new JET2TagException(e.getLocalizedMessage(), e);
      }
    }
    else
    {
      file = wsExtender.getContainer().getFile(path);
    }
    return file;
  }

}
