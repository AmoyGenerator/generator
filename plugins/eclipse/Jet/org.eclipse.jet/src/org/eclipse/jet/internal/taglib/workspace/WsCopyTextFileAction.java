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


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction2;


/**
 * Copy a binary file from a source URL to a target workspace file.
 *
 */
public class WsCopyTextFileAction extends AbstractWorkspaceAction2 implements IWorkspaceAction2
{

  private final IFile file;

  private final boolean replace;

  private final String targetEncoding;

  private final String sourceFileContents;

  public WsCopyTextFileAction(
    String templatePath,
    TagInfo td,
    String sourceFileContents,
    IFile file,
    boolean replaceExisting,
    String targetEncoding)
  {
    super(td, templatePath);
    this.sourceFileContents = sourceFileContents;
    this.file = file;
    this.replace = replaceExisting;
    this.targetEncoding = targetEncoding;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.workspace.IWorkspaceAction#getResource()
   */
  public IResource getResource()
  {
    return file;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.workspace.IWorkspaceAction#requiresValidateEdit()
   */
  public boolean requiresValidateEdit()
  {
    return replace && file.exists() && file.isReadOnly() /* && content has changed */;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.workspace.IWorkspaceAction#performAction(org.eclipse.core.runtime.IProgressMonitor)
   */
  public boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException
  {
    return ActionsUtil.writeTextFile(file, replace, targetEncoding, false, sourceFileContents, monitor);
  }

}
