/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: TemplatePathUtil.java,v 1.2 2007/05/22 18:47:16 pelder Exp $
 */
package org.eclipse.jet.compiler;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

/**
 * Utility class for mapping between Eclipse Workspace files and template paths
 */
class TemplatePathUtil
{
  
  private TemplatePathUtil() {
    // prevent instantiation
  }

  public static URI baseLocationURI(String workspaceFullPath)
  {
    final String projectName = new Path(workspaceFullPath).segment(0);
    return projectName == null ? null : ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getLocationURI();
  }
  
  public static URI baseLocationURI(IFile templateFile)
  {
    return templateFile.getProject().getLocationURI();
  }

  public static String templatePath(String workspaceFullPath)
  {
    final Path wsPath = new Path(workspaceFullPath);
    return wsPath.segmentCount() == 0 ? "" : wsPath.removeFirstSegments(1).makeRelative().toString(); //$NON-NLS-1$
  }
  
  public static String templatePath(IFile templateFile)
  {
    return templateFile.getProjectRelativePath().toString();
  }
  
  public static IFile workspaceFile(URI baseLocation, String templatePath)
  {
    final IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocationURI(baseLocation);
    if(containers.length > 0) {
      if(containers[0].getType() == IResource.PROJECT) {
        throw new IllegalArgumentException();
      }
      return ((IProject)containers[0]).getFile(templatePath);
    } else {
      return null;
    }
  }
}
