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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jet.taglib.workspace.IWorkspaceAction2;
import org.eclipse.jet.taglib.workspace.IWorkspaceActionExtension;


/**
 * Create or update a text file with the contents of a writer..
 *
 */
public class WsFileFromWriterAction extends AbstractWorkspaceAction2 implements IWorkspaceAction2, IWorkspaceActionExtension
{

  private final IFile file;

  private final BufferedJET2Writer writer;

  private String encoding;

  private final boolean replace;

  private final boolean derived;

  /**
   * 
   * @param context
   * @param templatePath
   * @param tagInfo
   * @param file
   * @param writer
   * @param replace
   * @param derived
   */
  public WsFileFromWriterAction(
    String templatePath,
    TagInfo tagInfo,
    IFile file,
    BufferedJET2Writer writer,
    boolean replace,
    boolean derived)
  {
    super(tagInfo, templatePath);
    this.file = file;
    this.writer = writer;
    this.replace = replace;
    this.derived = derived;
  }

  public IResource getResource()
  {
    return file;
  }

  public boolean requiresValidateEdit()
  {
    return replace && file.exists() && file.isReadOnly() /* && content has changed */;
  }

  public boolean performActionIfRequired(IProgressMonitor monitor) throws JET2TagException
  {
    return ActionsUtil.writeTextFile(file, replace, encoding, derived, writer.getContent(), monitor);
  }

  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }

  public BufferedJET2Writer getContentWriter()
  {
    return writer;
  }

}
