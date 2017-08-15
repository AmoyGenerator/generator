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
 * $Id: JET2ProjectNature.java,v 1.1 2007/05/10 16:22:36 pelder Exp $
 */
package org.eclipse.jet.internal.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jet.JET2Platform;

/**
 * Project Nature class for JET
 */
public class JET2ProjectNature implements IProjectNature
{

  private static final String JAVA_BUILDER_ID = JavaCore.BUILDER_ID;
  private static final String JET_BUILDER_ID = JET2Platform.PLUGIN_ID + ".builder"; //$NON-NLS-1$
  private static final String EMF_JET_BUILDER_ID = "org.eclipse.emf.codegen.JETBuilder"; //$NON-NLS-1$
  
  private IProject project;

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#configure()
   */
  public void configure() throws CoreException
  {
    IProjectDescription description = project.getDescription();
    ICommand[] buildSpec = description.getBuildSpec();
    List buildSpecList = new ArrayList(Arrays.asList(buildSpec));
    
    
    ICommand jetCommand = description.newCommand();
    jetCommand.setBuilderName(JET_BUILDER_ID);
    
    boolean jetCommandAdded = false;
    for (ListIterator i = buildSpecList.listIterator(); i.hasNext();)
    {
      ICommand command = (ICommand)i.next();
      if(EMF_JET_BUILDER_ID.equals(command.getBuilderName()))
      {
        i.remove();
      } else if(JAVA_BUILDER_ID.equals(command.getBuilderName()))
      {
        // back up, insert before this builder...
        i.previous();
        i.add(jetCommand);
        // skip both the newly inserted element, and the Java builder...
        i.next();
        jetCommandAdded = true;
      }
    }
    
    if(!jetCommandAdded)
    {
      buildSpecList.add(jetCommand);
    }
    
    
    description.setBuildSpec((ICommand[])buildSpecList.toArray(new ICommand[buildSpecList.size()]));
    
    project.setDescription(description, null);
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#deconfigure()
   */
  public void deconfigure() throws CoreException
  {
    IProjectDescription description = project.getDescription();
    
    ICommand[] buildSpec = description.getBuildSpec();
    ICommand[] newBuildSpec = new ICommand[Math.max(0, buildSpec.length)];
    
    int j = 0;
    for (int i = 0; i < buildSpec.length; i++)
    {
      if(!JET_BUILDER_ID.equals(buildSpec[i])) 
      {
        newBuildSpec[j++] = buildSpec[i];
      }
    }
    
    description.setBuildSpec(newBuildSpec);
    project.setDescription(description, null);
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#getProject()
   */
  public IProject getProject()
  {
    return project;
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
   */
  public void setProject(IProject project)
  {
    this.project = project;
  }

}
