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
package org.eclipse.jet.taglib.workspace;

import org.eclipse.jet.taglib.TagInfo;

/**
 * Abstract implementation of IWorkspaceAction that manages the standard
 * execution tracing information.
 */
public abstract class AbstractWorkspaceAction implements IWorkspaceAction
{

  private final TagInfo tagInfo;
  private final String templatePath;

  protected AbstractWorkspaceAction(TagInfo tagInfo, String templatePath) {
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


}
