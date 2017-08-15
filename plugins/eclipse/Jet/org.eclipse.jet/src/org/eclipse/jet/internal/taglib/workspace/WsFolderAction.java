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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction2;


/**
 * Represent a Folder action. A folder action will:
 * <bl>
 * <li>create a folder (and any uncreated parent folders), if it does not exist</li>
 * </bl>
 *
 */
public class WsFolderAction extends AbstractWorkspaceAction2 implements IWorkspaceAction2
{

  private final IFolder folder;

  /**
   * 
   * @param context
   * @param templatePath
   * @param tagInfo
   * @param folder
   */
  public WsFolderAction(String templatePath, TagInfo tagInfo, IFolder folder)
  {
    super(tagInfo, templatePath);
    this.folder = folder;
  }

  public IResource getResource()
  {
    return folder;
  }

  public boolean requiresValidateEdit()
  {
    return false;
  }

  public boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException
  {
    return ActionsUtil.ensureFolderExists(folder, monitor);
  }

}
