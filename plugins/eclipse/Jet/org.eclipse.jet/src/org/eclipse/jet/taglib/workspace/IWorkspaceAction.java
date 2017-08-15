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

package org.eclipse.jet.taglib.workspace;


import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Represents a workspace action managed by {@link WorkspaceContextExtender}.
  * <p>
 * Since 0.9 clients that perform expensive actions that can be avoided should implement 
 * {@link IWorkspaceAction2} in preference to {@link IWorkspaceAction} and should derive
 * their implementations for {@link AbstractWorkspaceAction2} instead of {@link AbstractWorkspaceAction}.
 * </p>
 * <p>Since 0.9, the following extension interfaces may be implemented:</p>
 * <ul>
 * <li> {@link IWorkspaceActionExtension} to enable avoidance of unnecessary file writes on {@link JET2Writer} based actions</li>
 * </ul>
 *
 */
public interface IWorkspaceAction
{

  /**
   * Returns the workspace resource with which the action is associated. If the associated
   * workspace resource is unknown at the time of the call, <code>null</code> may be returned.
   * @return an Eclipse resource handle or <code>null</code>
   * @throws JET2TagException if the resource cannot be created
   */
  public abstract IResource getResource() throws JET2TagException;

  /**
   * Returns the Tag information of the tag that originated this action.
   * @return a non-null TagInfo object.
   */
  public abstract TagInfo getTagInfo();

  /**
   * Returns the path of the template that originated this action.
   * @return a non-null String
   */
  public abstract String getTemplatePath();

  /**
   * Tests whether the action resource must participate in an Eclipse Team
   * validateEdit operation prior to the action being performed.
   * If <code>true</code> is returned, then {@link #getResource()} must return an instance
   * of {@link org.eclipse.core.resources.IFile}.
   * @return <code>true</code> if the resource must participate in validateEdit.
   * @throws JET2TagException if the result cannot calculated because of some error
   * @see org.eclipse.core.resources.IWorkspace#validateEdit(org.eclipse.core.resources.IFile[], java.lang.Object)
   */
  public abstract boolean requiresValidateEdit() throws JET2TagException;

  /**
   * Perform the workspace action
   * @param monitor the progress monitor to which progress information will be written.
   * @throws JET2TagException if the action cannot be successfully performed.
   */
  public abstract void performAction(IProgressMonitor monitor) throws JET2TagException;

}
