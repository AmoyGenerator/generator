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
 * $Id: AbstractWorkspaceAction2.java,v 1.1 2008/04/02 15:31:12 pelder Exp $
 */
package org.eclipse.jet.taglib.workspace;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Abstract implementation of IWorkspaceAction that manages the standard
 * execution tracing information.
 */
public abstract class AbstractWorkspaceAction2 implements IWorkspaceAction2
{

  private final TagInfo tagInfo;
  private final String templatePath;

  protected AbstractWorkspaceAction2(TagInfo tagInfo, String templatePath) {
    this.tagInfo = tagInfo;
    this.templatePath = templatePath;
    
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#getTagInfo()
   */
  public final TagInfo getTagInfo()
  {
    return tagInfo;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#getTemplatePath()
   */
  public final String getTemplatePath()
  {
    return templatePath;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.workspace.IWorkspaceAction#performAction(org.eclipse.core.runtime.IProgressMonitor)
   */
  public final void performAction(IProgressMonitor monitor) throws JET2TagException
  {
    performActionIfRequired(monitor);
  }
}
