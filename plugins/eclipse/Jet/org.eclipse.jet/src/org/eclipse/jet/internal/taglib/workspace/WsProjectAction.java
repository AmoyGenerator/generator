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


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction2;


/**
 * Represent a Project action. A project action will:
 * <bl>
 * <li>create a project, if it does not exist</li>
 * <li>open a project, if it is not open</li>
 * </bl>
 *
 */
public class WsProjectAction extends AbstractWorkspaceAction2 implements IWorkspaceAction2
{

  private final IProject project;

  private final IProjectDescription description;

  /**
   * 
   * @param context
   * @param templatePath
   * @param tagInfo
   * @param project
   * @param description
   */
  public WsProjectAction(String templatePath, TagInfo tagInfo, IProject project, IProjectDescription description)
  {
    super(tagInfo, templatePath);
    this.project = project;
    this.description = description;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.workspace.IWorkspaceAction#getResource()
   */
  public IResource getResource()
  {
    return project;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.workspace.IWorkspaceAction#requiresValidateEdit()
   */
  public boolean requiresValidateEdit()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.workspace.IWorkspaceAction#performAction(org.eclipse.core.runtime.IProgressMonitor)
   */
  public boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException
  {
    return ActionsUtil.createProject(project, description, monitor);

//    final String createMessage = MessageFormat.format(JET2Messages.WsProjectAction_CreatingProject, new Object []{ project.getName() });
//    RuntimeLoggerContextExtender.log(context, createMessage, getTagInfo(), getTemplatePath(), RuntimeLoggerContextExtender.INFO_LEVEL);

  }

}
