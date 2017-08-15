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
 * $Id: WorkspaceTemplateResolverHelperFactory.java,v 1.2 2007/05/01 19:49:11 pelder Exp $
 */
package org.eclipse.jet.internal.parser.resources;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.core.parser.ITemplateResolverHelper;
import org.eclipse.jet.core.parser.ITemplateResolverHelperFactory;

/**
 * Implement factory for resolving URIs that point into the workspace
 */
public class WorkspaceTemplateResolverHelperFactory implements ITemplateResolverHelperFactory
{

  private static final WorkspaceTemplateResolverHelperFactory instance = new WorkspaceTemplateResolverHelperFactory();
  
  /**
   * Return the singleton instance of this class
   * @return the instance
   */
  public static ITemplateResolverHelperFactory getInstance() {
    return instance;
  }
  
  private WorkspaceTemplateResolverHelperFactory()
  {
  }
  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.tools.parser.ITemplateResolverHelperFactory#getTemplateResolverHelper(java.net.URI)
   */
  public ITemplateResolverHelper getTemplateResolverHelper(URI baseLocation)
  {
    IContainer container = getWorkspaceContainer(baseLocation);
    ITemplateResolverHelper helper = null;
    if(container != null) {
      helper =  new ResourceTemplateResolverHelper(container);
    }
    return helper;
  }

  private IContainer getWorkspaceContainer(URI baseLocation)
  {
    if ("platform".equals(baseLocation.getScheme()))
    {
      IPath p = new Path(baseLocation.getSchemeSpecificPart());
      if("resource".equals(p.segment(0))) {
        p = p.removeFirstSegments(1);
        IResource member = ResourcesPlugin.getWorkspace().getRoot().findMember(p);
        return member instanceof IContainer ? (IContainer)member : null;
      } else {
        return null;
      }
    }
    else
    {
      final IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocationURI(baseLocation);
      return containers.length > 0 ? containers[0] : null;
    }
  }

}
