/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: IWorkspaceAction2.java,v 1.1 2008/04/02 15:31:12 pelder Exp $
 */
package org.eclipse.jet.taglib.workspace;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.JET2TagException;

/**
 * Enhanced workspace action protocol based on {@link IWorkspaceAction} that provides feedback on whether
 * an action IO action was performed.
 * <p>
 * Clients that perform expensive actions that can be avoided should implement 
 * {@link IWorkspaceAction2} in preference to {@link IWorkspaceAction} and should derive
 * their implementations for {@link AbstractWorkspaceAction2} instead of {@link AbstractWorkspaceAction}.
 * </p>
 * <p>Since 0.9, the following extension interfaces may be implemented:</p>
 * <ul>
 * <li> {@link IWorkspaceActionExtension} to enable avoidance of unnecessary file writes on {@link JET2Writer} based actions</li>
 * </ul>
 */
public interface IWorkspaceAction2 extends IWorkspaceAction
{
  /**
   * Perform the workspace action if required. The method returns <code>true</code> if
   * the action was performed and <code>false</code> otherwise. Note that implementations should
   * throw a {@link JET2TagException} in the case of an error rather than returning <code>false</code>.
   * The framework will call this method instead of {@link IWorkspaceAction#performAction(IProgressMonitor)}.
   * @param monitor the progress monitor to which progress information will be written.
   * @return <code>true</code> if files/folders were actually created/updated and <code>false</code> otherwise
   * @throws JET2TagException if the action cannot be successfully performed.
   */
  public abstract boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException;

}
