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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.java.JavaActionsUtil;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.IWorkspaceActionExtension;

/**
 * Action for creating files in a Java context
 */
public final class JavaFileAction extends AbstractWorkspaceAction2 implements IWorkspaceAction2, IWorkspaceActionExtension
{

  protected final IPath srcFolderPath;
  protected final String pkgName;
  protected final String fileName;
  private final BodyContentWriter content;
  private final boolean replace;
  private final String encoding;
  private final boolean derived;
  private IFile file;

  public JavaFileAction(IPath srcFolderPath, String pkgName, String fileName, BodyContentWriter content, boolean replace, String encoding, boolean derived, TagInfo td, String templatePath)
  {
    super(td, templatePath);
    this.srcFolderPath = srcFolderPath;
    this.pkgName = pkgName;
    this.fileName = fileName;
    this.content = content;
    this.replace = replace;
    this.encoding = encoding;
    this.derived = derived;
  }

  /**
   * @return
   * @throws JET2TagException
   */
  private final IFile getUnderlyingFile() throws JET2TagException
  {
    if(file == null) {
      file = JavaActionsUtil.getResourceForJavaResource(srcFolderPath, pkgName, fileName);
    }
    return file;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#getResource()
   */
  public final IResource getResource() throws JET2TagException
  {
    return getUnderlyingFile();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#performAction(org.eclipse.core.runtime.IProgressMonitor)
   */
  public final boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException
  {
    IFile targetFile = getUnderlyingFile();
    return ActionsUtil.writeTextFile(targetFile, replace, encoding, derived, content.getContent(), monitor);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#requiresValidateEdit()
   */
  public final boolean requiresValidateEdit() throws JET2TagException
  {
    if(!replace)
    {
      return false;
    }
    try
    {
      IFile res = getUnderlyingFile();
      return res.exists() &&  res.getResourceAttributes().isReadOnly();
    }
    catch (JET2TagException e)
    {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceActionExtension#getFileContent()
   */
  public BufferedJET2Writer getContentWriter()
  {
    return content;
  }


  
}
