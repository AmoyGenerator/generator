/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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
package org.eclipse.jet.internal.runtime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.extensionpoints.TransformData;
import org.eclipse.jet.internal.extensionpoints.TransformDataFactory;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Monitor workspace changes in JET project manifests
 */
public class ProjectJETBundleProvider implements IResourceChangeListener, IJETBundleProvider
{

  /**
   * Map&lt;String, IJETBundleDescription&gt; where String is a transformId.
   */
  private final Map descriptorsById = Collections.synchronizedMap(new HashMap());
  
  private final Map projectNameToIdMap = Collections.synchronizedMap(new HashMap());

  /**
   */
  public ProjectJETBundleProvider()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
   */
  public void resourceChanged(IResourceChangeEvent event)
  {
    switch (event.getType())
    {
      case IResourceChangeEvent.PRE_CLOSE:
      case IResourceChangeEvent.PRE_DELETE:
        handleProjectChange((IProject)event.getResource());
        break;
      case IResourceChangeEvent.POST_CHANGE:
        handleWorkspaceChange(event.getDelta());
        break;
    }
  }

  public void startup()
  {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

    for (int i = 0; i < projects.length; i++)
    {
      try
      {
        if (projects[i].exists() && projects[i].isOpen() && projects[i].hasNature(JET2Platform.JET2_NATURE_ID))
        {
          updateJETProject(projects[i]);
        }
      }
      catch (CoreException e)
      {
        // this should not happen as exists() and isOpen() tests
        // account for all possible causes if this exception
        InternalJET2Platform.logError("Exception caught in \"should never happen\" catch clause", e); //$NON-NLS-1$
      }
    }
    ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
  }

  public void shutdown()
  {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    
    descriptorsById.clear();
    projectNameToIdMap.clear();
  }

  /**
   * @param project
   */
  private void handleProjectChange(IProject project)
  {
    try
    {
      if (project.isOpen() && project.exists() && project.hasNature(JET2Platform.JET2_NATURE_ID))
      {
        removeJETProject(project.getName());
      }
    }
    catch (CoreException e)
    {
      // this should not happen as exists() and isOpen() tests
      // account for all possible causes if this exception
      InternalJET2Platform.logError("Exception caught in \"should never happen\" catch clause", e); //$NON-NLS-1$
    }
  }

  private void handleWorkspaceChange(IResourceDelta delta)
  {

    try
    {
      delta.accept(new IResourceDeltaVisitor()
        {

          public boolean visit(IResourceDelta childDelta) throws CoreException
          {
            switch (childDelta.getResource().getType())
            {
            case IResource.ROOT:
              return true;
            case IResource.PROJECT:
              IProject project = (IProject)childDelta.getResource();
              if (project.exists() && project.isOpen() && project.hasNature(JET2Platform.JET2_NATURE_ID))
              {
                IResourceDelta manifestDelta = childDelta.findMember(new Path(JarFile.MANIFEST_NAME));
                IResourceDelta pluginXML = childDelta.findMember(new Path("plugin.xml")); //$NON-NLS-1$
                if (manifestDelta != null || pluginXML != null)
                {
                  updateJETProject(project);
                }
                return true;
              }
              return false;
            case IResource.FOLDER:
              IFolder folder = (IFolder)childDelta.getResource();
              return folder.getProjectRelativePath().equals(new Path(JarFile.MANIFEST_NAME).removeLastSegments(1));
            default:
              return false;
            }
          }

        });
    }
    catch (CoreException e)
    {
      // this should not happen as exists() and isOpen() tests
      // account for all possible causes if this exception
      InternalJET2Platform.logError("Exception caught in \"should never happen\" catch clause", e); //$NON-NLS-1$
    }
  }

  public Set getAllJETBundleIds()
  {
    return descriptorsById.keySet();
  }

  public IJETBundleDescriptor getDescriptor(String id)
  {
    return (IJETBundleDescriptor)descriptorsById.get(id);
  }
 
  private void removeJETProject(final String projectName)
  {
    final String id = (String)projectNameToIdMap.get(projectName);
    if(id != null)
    {
      projectNameToIdMap.remove(projectName);
      descriptorsById.remove(id);
    }
  }

  private void updateJETProject(final IProject project)
  {
    String projectName = project.getName();
    try
    {
//      final URL projectURL = new URL("file:/" + project.getLocation() + "/"); //$NON-NLS-1$ //$NON-NLS-2$
      final URL projectURL = new URL("platform:/resource/" + project.getName() + "/"); //$NON-NLS-1$ //$NON-NLS-2$

      
      final JETBundleManifest manifest = JETBundleManager.loadManifest(projectURL);

      final String transformId = manifest.getTransformId();
      final TransformData transformData = TransformDataFactory.INSTANCE.createTransformData(transformId, projectURL);

      descriptorsById.put(transformId, new ProjectJETBundleDescriptor(manifest, transformData, projectURL, projectName));
      projectNameToIdMap.put(projectName, transformId);
    }
    catch (MalformedURLException e)
    {
      InternalJET2Platform.logError(e.getMessage(), e);
    }
    catch (IOException e)
    {
      // didn't find the manifest, not a problem...
    }
    catch (NotABundleException e)
    {
      // not an OSGi bundle, so it cannot be a JET bundle
    }
  }

  public IJETBundleDescriptor getDescriptorForProject(String name)
  {
    IJETBundleDescriptor result = null;
    String id = (String)projectNameToIdMap.get(name);
    if(id == null)
    {
      // check to see if this is a project that we haven't updated yet via
      // the workspace listener. If so, do it now.
      IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
      if(isOpenJETProject(project))
      {
        updateJETProject(project);
        id = (String)projectNameToIdMap.get(name);
      }
    }
    if(id != null)
    {
      result = (IJETBundleDescriptor)descriptorsById.get(id);
    }
    return result;
  }

  private boolean isOpenJETProject(IProject project)
  {
    try
    {
      return project != null && project.exists() && project.isOpen() && project.hasNature(JET2Platform.JET2_NATURE_ID);
    }
    catch (CoreException e)
    {
      return false;
    }
  }

  public String getProjectForId(String id)
  {
    ProjectJETBundleDescriptor descriptor = (ProjectJETBundleDescriptor)getDescriptor(id);
    return descriptor == null ? null : descriptor.getProjectName();
  }

  public Bundle load(String id, IProgressMonitor monitor) throws BundleException
  {
    Bundle bundle = null;
    ProjectJETBundleDescriptor descriptor = (ProjectJETBundleDescriptor)getDescriptor(id);
    if (descriptor != null)
    {
      if (ensureProjectIsBuilt(descriptor.getProjectName(), monitor))
      {
        final URL baseURL = descriptor.getBaseURL();
        try
        {
          final URL baseFileURL = FileLocator.toFileURL(baseURL);
          bundle = InternalJET2Platform.getDefault().getJETBundleInstaller().installBundle(baseFileURL);
        }
        catch (IOException e)
        {
          new BundleException(MessageFormat.format("Could not convert URL ''{0}'' to ''file'' protocol", new Object[] {baseURL}), e); //$NON-NLS-1$
        }
      }

    }
    return bundle;
  }

  public void unload(String id) throws BundleException
  {
    if(descriptorsById.containsKey(id))
    {
      Bundle bundle = Platform.getBundle(id);
      unload(bundle);
    }
  }

  private boolean ensureProjectIsBuilt(String name, IProgressMonitor monitor) {
    try
    {
      final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
      project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
      return true;
    }
    catch (CoreException e)
    {
      e.printStackTrace();
      return false;
    }
  }
  
  public void unload(Bundle bundle) throws BundleException
  {
    if(bundle != null)
    {
      bundle.uninstall();
    }
  }

  public Collection getAllJETBundleDescriptors()
  {
    return descriptorsById.values();
  }

}
