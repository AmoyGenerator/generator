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

package org.eclipse.jet.internal.taglib.workspace;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;


/**
 * Implement the JET2 Standard Workspace tag 'project'.
 *
 */
public class ProjectTag extends AbstractContainerTag
{

  private static final String NAME__ATTR = "name"; //$NON-NLS-1$

  private boolean containerPushed = false;

  /**
   * 
   */
  public ProjectTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doStart(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String name = getAttribute(NAME__ATTR);
    String location = getAttribute("location"); //$NON-NLS-1$

    WorkspaceContextExtender wsExtender = WorkspaceContextExtender.getInstance(context);

    IProject project;
    try
    {
      project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    }
    catch (IllegalArgumentException e)
    {
      throw new JET2TagException(e.getMessage(), e);
    }

    final IProjectDescription projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(name);
    if(location != null) {
      
      projectDescription.setLocation(new Path(location));
      
    }
    wsExtender.addAction(new WsProjectAction(context.getTemplatePath(), td, project, projectDescription));

    wsExtender.pushContainer(project);
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
