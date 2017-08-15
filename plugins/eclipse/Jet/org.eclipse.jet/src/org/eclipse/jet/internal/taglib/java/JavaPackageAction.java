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
package org.eclipse.jet.internal.taglib.java;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.java.JavaActionsUtil;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction2;

/**
 * Create a Java package
 */
public class JavaPackageAction extends AbstractWorkspaceAction2 implements IWorkspaceAction2
{

  private final IPath srcFolderPath;
  private final String packageName;
  private IFolder underlyingFolder;
  
  public JavaPackageAction(IPath srcFolderPath, String packageName, TagInfo td, String templatePath)
  {
    super(td, templatePath);
    this.srcFolderPath = srcFolderPath;
    this.packageName = packageName;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#getResource()
   */
  public IResource getResource() throws JET2TagException
  {
    return underlyingFolder;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#performAction(org.eclipse.core.runtime.IProgressMonitor)
   */
  public boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException
  {
    IContainer container = JavaActionsUtil.getContainerForPackage(srcFolderPath, packageName);
    
    if(container instanceof IFolder) 
    {
      underlyingFolder = (IFolder)container;
      return ActionsUtil.ensureFolderExists(underlyingFolder, monitor);
    } else {
      return false; // container was a project - did not create it...
    }
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#requiresValidateEdit()
   */
  public boolean requiresValidateEdit() throws JET2TagException
  {
    return false; // validateEdit only works on files
  }

}
