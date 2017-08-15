/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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

package org.eclipse.jet.internal.taglib.workspace;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * Implement the JET Workspace tag 'file'.
 *
 */
public class FileTag extends AbstractEmptyTag
{

  private static final String TEMPLATE__ATTR = "template"; //$NON-NLS-1$

  private static final String PATH__ATTR = "path"; //$NON-NLS-1$

  private static final String REPLACE__ATTR = "replace"; //$NON-NLS-1$

  private static final String DERIVED__ATTR = "derived"; //$NON-NLS-1$

  private static final String ENCODING__ATTR = "encoding"; //$NON-NLS-1$
  
  /**
   * 
   */
  public FileTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {

    IPath path = new Path(getAttribute(PATH__ATTR));
    String templatePath = getAttribute(TEMPLATE__ATTR);
    
    boolean replace = true;
    if (td.hasAttribute(REPLACE__ATTR))
    {
      replace = Boolean.valueOf(getAttribute(REPLACE__ATTR)).booleanValue();
    }
    boolean derived = false;
    if (td.hasAttribute(DERIVED__ATTR))
    {
      derived = Boolean.valueOf(getAttribute(DERIVED__ATTR)).booleanValue();
    }

    WorkspaceContextExtender wsExtender = WorkspaceContextExtender.getInstance(context);

    IFile file = getTargetFile(path, wsExtender);

    if(replace == false && file.exists()) {
      // don't need to run template or create action...
      return;
    }
    
    BodyContentWriter contentWriter = new BodyContentWriter();

    TransformContextExtender tce = TransformContextExtender.getInstance(context);
    tce.execute(templatePath, contentWriter);

    WsFileFromWriterAction fileAction = new WsFileFromWriterAction(
      context.getTemplatePath(),
      td,
      file,
      contentWriter,
      replace,
      derived);

    if (td.hasAttribute(ENCODING__ATTR))
    {
      fileAction.setEncoding(getAttribute(ENCODING__ATTR));
    }
    wsExtender.addAction(fileAction);
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
