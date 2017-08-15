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
package org.eclipse.jet.internal.extensionpoints;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.internal.runtime.BundleManifest;
import org.eclipse.jet.taglib.TagLibrary;

/**
 * Manager for Tag Libraries defined in workspace projects
 */
public class WorkspaceTagLibraryManager implements IPluginChangeListener
{

  private PluginProjectMonitor projectMonitor;

  private final Map tagLibrariesById = new HashMap();
  private final Map libraryIdsByProjectName = new HashMap();

  private final Map projectByTagLibraryId = new HashMap();
  
  /**
   * 
   */
  public WorkspaceTagLibraryManager()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.extensionpoints.IPluginChangeListener#projectRemoved(org.eclipse.core.resources.IProject)
   */
  public void projectRemoved(IProject project)
  {
    String projectName = project.getName();
    internalRemoveProject(projectName);
  }

  /**
   * @param projectName
   */
  private void internalRemoveProject(String projectName)
  {
    String[] ids = (String[])libraryIdsByProjectName.remove(projectName);
    if(ids != null)
    {
      for (int i = 0; i < ids.length; i++)
      {
        tagLibrariesById.remove(ids[i]);
        projectByTagLibraryId.remove(ids[i]);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.extensionpoints.IPluginChangeListener#projectUpdated(org.eclipse.core.resources.IProject, org.eclipse.jet.internal.runtime.BundleManifest, java.lang.Object)
   */
  public void projectUpdated(IProject project, BundleManifest manifest, Object pluginDocumentRoot)
  {
    String projectName = project.getName();
    internalRemoveProject(projectName);
    final TagLibrary[] newLibraries = TagLibraryDataFactory.INSTANCE.createTagLibraries(manifest.getSymbolicName(), pluginDocumentRoot);
    String[] newIds = new String[newLibraries.length];
    for (int i = 0; i < newLibraries.length; i++)
    {
      newIds[i] = newLibraries[i].getLibraryId();
      tagLibrariesById.put(newIds[i], newLibraries[i]);
      projectByTagLibraryId.put(newIds[i], project);
    }
    libraryIdsByProjectName.put(projectName, newIds);
  }

  public void startup(PluginProjectMonitor projectMonitor)
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    

    if(projectMonitor == null)
    {
      throw new NullPointerException();
    }
    if(this.projectMonitor != null)
    {
      throw new IllegalStateException();
    }
    this.projectMonitor = projectMonitor;
    this.projectMonitor.addPluginProjectListener(this);
    
    timer.done();
  }
  
  public void shutdown()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "shutdown()"); //$NON-NLS-1$
    

    if (projectMonitor != null)
    {
      projectMonitor.removePluginProjectListener(this);
      projectMonitor = null;
    }
    
    timer.done();
  }
  
  public TagLibrary getTagLibrary(String id)
  {
    return (TagLibrary)tagLibrariesById.get(id);
  }
  
  public IProject getProjectDefiningTagLibrary(String id)
  {
    return (IProject)projectByTagLibraryId.get(id);
  }
}
