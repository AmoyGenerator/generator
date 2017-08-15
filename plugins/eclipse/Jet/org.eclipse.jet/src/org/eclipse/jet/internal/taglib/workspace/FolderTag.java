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


import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;


/**
 * Implement the JET2 Standard Workspace tag 'folder'.
 *
 */
public class FolderTag extends AbstractContainerTag
{

  private static final String PATH__ATTR = "path"; //$NON-NLS-1$

  private boolean containerPushed = false;

  /**
   * 
   */
  public FolderTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doStart(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {

    IPath path = new Path(getAttribute(PATH__ATTR));

    WorkspaceContextExtender wsExtender = WorkspaceContextExtender.getInstance(context);

    IFolder folder;
    if (path.isAbsolute() || !wsExtender.existsContainer())
    {
      try
      {
        folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
      }
      catch (IllegalArgumentException e)
      {
        throw new JET2TagException(e.getLocalizedMessage(), e);
      }
    }
    else
    {
      folder = wsExtender.getContainer().getFolder(path);
    }
    wsExtender.addAction(new WsFolderAction(context.getTemplatePath(), td, folder));

    wsExtender.pushContainer(folder);
    containerPushed = true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doEnd(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    WorkspaceContextExtender wsExtender = WorkspaceContextExtender.getInstance(context);

    if (containerPushed)
    {
      wsExtender.popContainer();
    }
  }

}
