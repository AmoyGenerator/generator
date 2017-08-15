/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * Implement the standard JET2 Java tag 'package.
 */
public class PackageTag_bk extends AbstractContainerTag
{

  private String packageName;
  private IPath srcFolderPath;

  /**
   * 
   */
  public PackageTag_bk()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    packageName = getAttribute("name"); //$NON-NLS-1$
    String srcFolder = getAttribute("srcFolder"); //$NON-NLS-1$
    
    WorkspaceContextExtender wce = WorkspaceContextExtender.getInstance(context);

    
    if(srcFolder != null)
    {
      srcFolderPath = new Path(srcFolder);
    }
    else
    {
      srcFolderPath = wce.getContainer().getFullPath().makeRelative();
    }

    wce.addAction(new JavaPackageAction(srcFolderPath, packageName, 
      td, TransformContextExtender.getInstance(context).getTemplatePath()));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // nothing to do
  }


  public String getPackageName()
  {
    return packageName;
  }

  public IPath getSourceFolderPath()
  {
    return srcFolderPath;
  }
}
