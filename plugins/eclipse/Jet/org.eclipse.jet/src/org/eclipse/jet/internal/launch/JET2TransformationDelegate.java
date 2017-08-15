/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

package org.eclipse.jet.internal.launch;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.RuntimeLoggerContextExtender;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.transform.JETLaunchConstants;
import org.eclipse.osgi.util.NLS;


/**
 * Create an Eclipse Launch delegate for JET2 Transformations
 *
 */
public class JET2TransformationDelegate extends LaunchConfigurationDelegate implements ILaunchConfigurationDelegate, ILaunchConfigurationDelegate2
{

  /**
   * 
   */
  public JET2TransformationDelegate()
  {
    super();
  }

  /**
   * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException
  {

    String id = getId(configuration);
    IResource source = getSource(configuration);
    int logFilterLevel = getLogFilterLevel(configuration);
    final JETProcess process = new JETProcess(launch, id, source, logFilterLevel, monitor);
    launch.addProcess(process);
    
    process.run();
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.LaunchConfigurationDelegate#getBuildOrder(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String)
   */
  protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException
  {
    // Identify projects for which save and build should be done
    return getReferencedWorkspaceProjects(configuration);
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.LaunchConfigurationDelegate#getProjectsForProblemSearch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String)
   */
  protected IProject[] getProjectsForProblemSearch(ILaunchConfiguration configuration, String mode) throws CoreException
  {
    // Identify projects for which save and build should be done
    return getReferencedWorkspaceProjects(configuration);
  }

  /**
   * @param configuration
   * @return
   * @throws CoreException
   */
  private IProject[] getReferencedWorkspaceProjects(ILaunchConfiguration configuration) throws CoreException
  {
    List projects = new LinkedList();
    internalGetReferencedProjects(getId(configuration), projects, new HashSet());
    return (IProject[])projects.toArray(new IProject[projects.size()]);
  }

  /**
   * @param id
   * @return
   */
  private IProject getJETProject(final String id)
  {
    final String projectName = JET2Platform.getJETBundleManager().getProjectForId(id);
    final IProject project = projectName != null ? ResourcesPlugin.getWorkspace().getRoot().getProject(projectName) : null;
    return project;
  }
  
  /**
   * Compute the workspace projects refered to the the given JET transformation.
   * @param id the JET transformation ID
   * @param projects the list of IProjects to populate
   * @param knownIds
   */
  private void internalGetReferencedProjects(String id, List projects, Set knownIds ) {
    // Do a bit of recusive loop prevention...
    if(knownIds.contains(id)) {
      return; // already seen this ID...
    } else {
      knownIds.add(id);
    }
    final IJETBundleDescriptor descriptor = JET2Platform.getJETBundleManager().getDescriptor(id);
    final IProject jetProject = getJETProject(id);
    if(jetProject != null) {
      projects.add(jetProject);
    }
    
    final String overridesId = descriptor.getOverridesId();
    if(overridesId != null) {
      internalGetReferencedProjects(overridesId, projects, knownIds);
    }
    
    
  }
  
  private IResource getSource(ILaunchConfiguration configuration) throws CoreException
  {
    final String sourcePath = configuration.getAttribute(JETLaunchConstants.SOURCE, (String)null);
    if (sourcePath == null)
    {
      IStatus status = new Status(IStatus.ERROR,JET2Platform.PLUGIN_ID,
        NLS.bind(JET2Messages.JET2TransformationDelegate_MissingAttribute,
          JETLaunchConstants.SOURCE,
          configuration.getName()));
      throw new CoreException(status);
    }
    final IPath path = new Path(sourcePath);
    final IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
    IResource resource = wsRoot.findMember(path);
    if(resource == null) {
      // assume its a file, at least that way, JET will issue an error message
      resource = path.segmentCount() == 1 ? (IResource)wsRoot.getProject(path.segment(0)) : (IResource)wsRoot.getFile(path);
    }
    return resource;
  }

  private String getId(ILaunchConfiguration configuration) throws CoreException
  {
    String id = configuration.getAttribute(JETLaunchConstants.ID, (String)null);
    if (id == null)
    {
      IStatus status = new Status(IStatus.ERROR,JET2Platform.PLUGIN_ID,
        NLS.bind(JET2Messages.JET2TransformationDelegate_MissingAttribute,
          JETLaunchConstants.ID,
          configuration.getName()));
      throw new CoreException(status);
    }
    return id;
  }

  private int getLogFilterLevel(ILaunchConfiguration configuration) throws CoreException
  {
    return configuration.getAttribute(JETLaunchConstants.LOG_FILTER_LEVEL, RuntimeLoggerContextExtender.INFO_LEVEL);
  }
}
