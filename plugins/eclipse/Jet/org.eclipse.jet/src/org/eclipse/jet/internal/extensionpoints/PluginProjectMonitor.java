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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.internal.runtime.BundleManifest;
import org.eclipse.jet.internal.runtime.JETBundleManager;
import org.eclipse.jet.internal.runtime.NotABundleException;
import org.eclipse.jet.internal.runtime.model.XMLDOMLoader;

/**
 * Monitor plugin projects in the workspace for changes in extension point definitions
 * that the JET compiler cares about.
 */
public class PluginProjectMonitor implements IResourceChangeListener
{

  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/pluginProjectMonitor")).booleanValue(); //$NON-NLS-1$

  private final Set listeners = Collections.synchronizedSet(new HashSet());
  
  public static boolean isPluginProject(IProject project)
  {
    if (project.isOpen())
      return hasBundleManifest(project) || hasPluginManifest(project);
    return false;
  }

  public static boolean hasBundleManifest(IProject project)
  {
    return project.exists(new Path(JarFile.MANIFEST_NAME));
  }

  public static boolean hasPluginManifest(IProject project)
  {
    return project.exists(new Path("plugin.xml")); //$NON-NLS-1$
  }

  public static boolean isJETPluginProject(IProject project)
  {
    boolean result = false;
    try
    {
      result = project.isOpen() && project.hasNature(JET2Platform.JET2_NATURE_ID);
    }
    catch (CoreException e)
    {
      // nothing to do, its not a JET project.
    }
    return result;
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
              if (isPluginProject(project))
              {
                IResourceDelta manifestDelta = childDelta.findMember(new Path(JarFile.MANIFEST_NAME));
                IResourceDelta pluginXML = childDelta.findMember(new Path("plugin.xml")); //$NON-NLS-1$
                if (manifestDelta != null || pluginXML != null)
                {
                  updateProject(project);
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

  private void handleProjectChange(IProject project)
  {
    try
    {
      if (project.isOpen() && project.exists() && project.hasNature(JET2Platform.JET2_NATURE_ID))
      {
        removeJETProject(project);
      }
    }
    catch (CoreException e)
    {
      // this should not happen as exists() and isOpen() tests
      // account for all possible causes if this exception
      InternalJET2Platform.logError("Exception caught in \"should never happen\" catch clause", e); //$NON-NLS-1$
    }
  }

  private void removeJETProject(IProject project)
  {
    for (Iterator i = listeners.iterator(); i.hasNext();)
    {
      IPluginChangeListener listener = (IPluginChangeListener)i.next();
      listener.projectRemoved(project);
    }
  }

  public void startup()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    
    if(DEBUG) System.out.println("PluginProjectMonitor.startup()"); //$NON-NLS-1$
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

    for (int i = 0; i < projects.length; i++)
    {
      final IProject project = projects[i];
      if (isPluginProject(project))
      {
        updateProject(project);
      }
    }
    ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    
    timer.done();
  }


  
  private void updateProject(IProject project)
  {
    if(DEBUG) System.out.println("PluginProjectMonitor.updateProject(" + project + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    try
    {
      
      final URL projectURL = new URL("platform:/resource/" + project.getName() + "/"); //$NON-NLS-1$ //$NON-NLS-2$
      final BundleManifest manifest = JETBundleManager.loadManifest(projectURL);
      Object pluginDocumentRoot = null;
      if(hasPluginManifest(project)) {
        if(DEBUG)System.out.println("  has plugin.xml"); //$NON-NLS-1$
        URL extensionsURL = new URL(projectURL, "plugin.xml"); //$NON-NLS-1$
        pluginDocumentRoot = new XMLDOMLoader().load(extensionsURL) ;
      }
      
      for (Iterator i = listeners.iterator(); i.hasNext();)
      {
        IPluginChangeListener listener = (IPluginChangeListener)i.next();
        
        listener.projectUpdated(project, manifest, pluginDocumentRoot);
      }
      
    }
    catch (MalformedURLException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    catch (FileNotFoundException e)
    {
      // arises when we cannot find MANIFEST.MF. In this case, we'll
      // just assume the plug-in does not contain JET tags
    }
    catch (IOException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    catch (NotABundleException e)
    {
      // arises when we find MANIFEST.MF that is not a OSGi bundle. In this case, we'll
      // just assume the plug-in does not contain JET tags
    }
  }

  public void shutdown()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "shutdown()"); //$NON-NLS-1$
    
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
   
    listeners.clear();
    
    timer.done();
  }
  
  public void addPluginProjectListener(IPluginChangeListener listener)
  {
    if(listener == null)
    {
      throw new NullPointerException();
    }
    listeners.add(listener);
  }
  
  public void removePluginProjectListener(IPluginChangeListener listener)
  {
    listeners.remove(listener);
  }

}
