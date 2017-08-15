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

package org.eclipse.jet.internal.builder;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.SavedStateManager;
import org.eclipse.jet.internal.compiler.SimplifiedCompiler;
import org.eclipse.jet.internal.core.compiler.IJETCompiler;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.osgi.util.NLS;


/**
 * Implement the builder that compiles JET2 files into Java
 *
 */
public class JET2Builder extends IncrementalProjectBuilder implements SavedStateManager.IStateSaver
{
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/builder")).booleanValue(); //$NON-NLS-1$

  static final String COMPILESTATE_PROJECT_DIR = "projects"; //$NON-NLS-1$

  private Serializable savedState;
  
  private final SavedStateManager savedStateManager;

  /**
   * 
   */
  public JET2Builder()
  {
    super();
    savedStateManager = InternalJET2Platform.getDefault().getSavedStateManager();
  }

  protected void clean(IProgressMonitor monitor) throws CoreException
  {
    if(DEBUG) {
      System.out.println("JET2Builder: cleaning " + getProject());     //$NON-NLS-1$
    }

    getCompiler(new SubProgressMonitor(monitor,1)).clean();
    
    savedState = null;
    
    if(DEBUG) {
      System.out.println("*JET2Builder: done cleaning " + getProject());     //$NON-NLS-1$
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
   */
  protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException
  {
    IProject[] projectDependencies = null;
    if (DEBUG)
      System.out.println("JET2Builder: build(" + getProject() + ", " + kind + ")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    try
    {
      IResourceDelta delta = getDelta(getProject());
      if (kind == IncrementalProjectBuilder.FULL_BUILD || isReferencedProjectChanged() || delta == null )
      {
        monitor.beginTask(NLS.bind(JET2Messages.JET2Builder_CompilingAll, getProject().getName()), 2);
        clean(new SubProgressMonitor(monitor, 1));
        projectDependencies = fullBuild(new SubProgressMonitor(monitor, 1));
        monitor.done();
      }
      else // INCREMENTAL BUILD or AUTO BUILD -- treat as the same thing
      {
        projectDependencies = incrementalBuild(delta, monitor);
      }
      return projectDependencies;
    }
    finally
    {
      if (DEBUG)
        System.out.println("JET2Builder: done build(" + getProject() + ", " + kind + ")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    }
  }

  private boolean isReferencedProjectChanged()
  {
    final IProject[] projects = computeReferencedProjects(getCompiler(null).getTagLibaryDependencies());
    for (int i = 0; i < projects.length; i++)
    {
      final IResourceDelta delta = getDelta(projects[i]);
      if(delta != null)
      {
        if(delta.findMember(new Path("plugin.xml")) != null  //$NON-NLS-1$
            || delta.findMember(new Path(JarFile.MANIFEST_NAME)) != null)
        {
          if(DEBUG) System.out.println("   forcing full build. ref updated:" + projects[i]); //$NON-NLS-1$
          return true;
        }
      }
    }
    
    return false;
  }

  /**
   * Perform an incremental build
   * @param mainDelta
   * @param monitor
   * @throws CoreException
   */
  private IProject[] incrementalBuild(IResourceDelta mainDelta, final IProgressMonitor monitor) throws CoreException
  {
    if(DEBUG) {
      System.out.println("JET2Builder: incrementalBuild " + getProject());     //$NON-NLS-1$
    }
    
    final IJETCompiler compiler = getCompiler(monitor);
    
    final Set templatesToRemove = new HashSet();
    final Set templatesToCompile = new HashSet();
    
    // figure out how much work we must do (for the progress monitor).
    mainDelta.accept(new IResourceDeltaVisitor()
      {
        public boolean visit(IResourceDelta delta) throws CoreException
        {
          final String templatePath = delta.getResource().getProjectRelativePath().toString();
          switch (delta.getKind())
          {
            case IResourceDelta.ADDED:
            case IResourceDelta.CHANGED:
              if (compiler.isTemplate(templatePath))
              {
                templatesToCompile.add(templatePath);
              }
              templatesToCompile.addAll(Arrays.asList(compiler.getAffectedTemplatePaths(templatePath)));
              break;
            case IResourceDelta.REMOVED:
              if (compiler.isTemplate(templatePath))
              {
                templatesToRemove.add(templatePath);
              }
              templatesToCompile.addAll(Arrays.asList(compiler.getAffectedTemplatePaths(templatePath)));
              break;
          }
          return true;
        }
      });
    monitor.beginTask(NLS.bind(JET2Messages.JET2Builder_CompilingChanged, getProject().getName()), 
      templatesToCompile.size() + templatesToRemove.size());
    try
    {
      for (Iterator i = templatesToRemove.iterator(); i.hasNext();)
      {
        String templatePath = (String)i.next();
        monitor.setTaskName(NLS.bind(JET2Messages.JET2Builder_Cleaning,templatePath));
        compiler.removeTemplate(templatePath);
        monitor.worked(1);
      }
      for (Iterator i = templatesToCompile.iterator(); i.hasNext();)
      {
        String templatePath = (String)i.next();
        monitor.setTaskName(NLS.bind(JET2Messages.JET2Builder_Compiling,templatePath));
        compiler.compile(templatePath);
        monitor.worked(1);
      }
      if(templatesToCompile.size() > 0 || templatesToRemove.size() > 0)
      {
        compiler.finish();
      }
      savedState = compiler.getMemento();
      return computeReferencedProjects(compiler.getTagLibaryDependencies());
    }
    finally
    {
      monitor.done();
      if(DEBUG) {
        System.out.println("JET2Builder: done incrementalBuild " + getProject());     //$NON-NLS-1$
      }
    }

  }

  /**
   * Perform a Full build
   * @param monitor
   * @throws CoreException
   */
  private IProject[] fullBuild(final IProgressMonitor monitor) throws CoreException
  {
    if(DEBUG) {
      System.out.println("JET2Builder: fullBuild " + getProject());     //$NON-NLS-1$
    }
    final IJETCompiler compiler = getCompiler(monitor);
    final Set templatesToCompile = new HashSet(); 
    // figure out how much work we must do (for the progress monitor).
    getProject().accept(new IResourceVisitor()
      {
        public boolean visit(IResource resource) throws CoreException
        {
          String templatePath = resource.getProjectRelativePath().toString();
          if (resource.getType() == IResource.FILE && compiler.isTemplate(templatePath))
          {
            templatesToCompile.add(templatePath);
          }
          return true;
        }
      });
    monitor.beginTask(NLS.bind(JET2Messages.JET2Builder_CompilingAll, getProject().getName()), 
      templatesToCompile.size());
    
    for (Iterator i = templatesToCompile.iterator(); i.hasNext();)
    {
      String templatePath = (String)i.next();
      compiler.compile(templatePath);
    }
    
    compiler.finish();
    
    savedState = compiler.getMemento();
    
    monitor.done();
    if(DEBUG) {
      System.out.println("JET2Builder: done fullBuild " + getProject());     //$NON-NLS-1$
    }
    return computeReferencedProjects(compiler.getTagLibaryDependencies());
  }

  private IJETCompiler getCompiler(final IProgressMonitor monitor)
  {
    return new WorkspaceCompiler(getProject(), savedState, monitor);
  }

  /*
   * Load saved compiler state...
   */
  protected void startupOnInitialize()
  {
    super.startupOnInitialize();

//    compilerState = null;

    if (DEBUG) System.out.println("JET2Builder: startupOnInitialize " + getProject()); //$NON-NLS-1$
    try
    {
      IPath fileLocation = savedStateManager.addSaveSaver(this, getProject());

      if (fileLocation != null)
      {
        File savedStateFile = fileLocation.toFile();
        readSavedState(savedStateFile);

      }
    }
    finally
    {
      if (/*compilerState == null && */savedState == null)
      {
        if (DEBUG)
          System.out.println("    compilerState not loaded from saved state"); //$NON-NLS-1$
        // don't have the saved build state, force a full rebuild...
//        compilerState = new JETCompilerState();
        forgetLastBuiltState();
      }
      if (DEBUG)
        System.out.println("JET2Builder: done startupOnInitialize " + getProject()); //$NON-NLS-1$
    }
  }

  /**
   * @param savedStateFile
   */
  private void readSavedState(File savedStateFile)
  {
    try
    {
      ObjectInputStream ois = new ObjectInputStream(new java.io.FileInputStream(savedStateFile));
      final Object readObject = ois.readObject();
//      if (readObject instanceof JETCompilerState)
//      {
//        compilerState = (JETCompilerState)readObject;
//        ois.close();
//        if (DEBUG)
//        {
//          System.out.println("    loaded compilerState from saved state"); //$NON-NLS-1$
//          System.out.print("         taglibs   : ["); //$NON-NLS-1$
//          String[] ids = compilerState.getAllReferencedTagLibraryIds();
//          for (int i = 0; i < ids.length; i++)
//          {
//            if (i != 0)
//              System.out.print(", "); //$NON-NLS-1$
//            System.out.print(ids[i]);
//          }
//          System.out.println("]"); //$NON-NLS-1$
//          System.out.println("         templates : " + compilerState.getTemplateMap()); //$NON-NLS-1$
//        }
//      }
//      else
//      {
        if(readObject instanceof SimplifiedCompiler.SaveStateMemento) {
          savedState = (Serializable)readObject;
        }
        if (DEBUG)
        {
          System.out.println("    loaded compilerState from saved state"); //$NON-NLS-1$
          System.out.println(savedState.toString());
        }
//      }
    }
    catch (FileNotFoundException e)
    {
      // nothing to do, the state was not saved for some reason.
      // NO NOT log this, this will happen whenever a new project occurs.
    }
    catch (IOException e)
    {
      // error reading the file, assume state is unrecoverable.
      // remove the state file.
      JETActivatorWrapper.INSTANCE.log(e);
      savedStateFile.delete();
    }
    catch (ClassNotFoundException e)
    {
      // error in deserializing the file. This should not happen. Log an error, for the record...
      InternalJET2Platform.logError("Could not deserialize JET2Builder Scanner", e); //$NON-NLS-1$
    }
  }
  
  /**
   * @param allReferencedTagLibraryIds
   * @return
   */
  private IProject[] computeReferencedProjects(final String[] allReferencedTagLibraryIds)
  {
    List referencedProjects = new ArrayList(allReferencedTagLibraryIds.length);
    for (int i = 0; i < allReferencedTagLibraryIds.length; i++)
    {
      final IProject refProject = InternalJET2Platform.getDefault().getProjectDefiningTagLibrary(allReferencedTagLibraryIds[i]);
      if(refProject != null)
      {
        referencedProjects.add(refProject);
        if(DEBUG) System.out.println("     depends on:" + refProject); //$NON-NLS-1$
      }
    }
    final IProject[] projects = (IProject[])referencedProjects.toArray(new IProject[referencedProjects.size()]);
    return projects;
  }

  public void doSave(IPath location) throws CoreException
  {
    FileOutputStream fileStream = null;
    ObjectOutputStream objStream = null;
    try
    {
      if(savedState != null) {
        File file = location.toFile();
        file.getParentFile().mkdirs();
        fileStream = new FileOutputStream(file);
        objStream = new ObjectOutputStream(fileStream);
        objStream.writeObject(savedState);
      }
    }
    catch (IOException e)
    {
      throw new CoreException(InternalJET2Platform.newStatus(IStatus.ERROR, e.getLocalizedMessage(), e));
    }
    finally
    {
      if(objStream != null)
      {
        try
        {
          objStream.close();
        }
        catch (IOException e)
        {
          JETActivatorWrapper.INSTANCE.log(e);
        }
      }
      if(fileStream != null)
      {
        try
        {
          fileStream.close();
        }
        catch (IOException e)
        {
          JETActivatorWrapper.INSTANCE.log(e);
        }
      }
    }
  }

  public String getStateFileName()
  {
    return "compilerState.tmp"; //$NON-NLS-1$
  }

}
