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
package org.eclipse.jet.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;

/**
 * Manager for state saved and restored by the jet plugin
 */
public class SavedStateManager implements ISaveParticipant
{
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/savedState")).booleanValue(); //$NON-NLS-1$

  public static interface IStateSaver 
  {

    /**
     * The name of the file to which state information will be saved as
     * a relative path.
     * @return a relative path
     */
    String getStateFileName();

    /**
     * Save the state to the location (absolute path) provided
     * @param location the location (absolute path) of the saved state file
     */
    void doSave(IPath location) throws CoreException;

    /**
     * The project for which the saver is saving data
     * @return
     */
    IProject getProject();
  }
  
  private ISavedState savedState;
  /**
   * Map<IProject,WeakReference<IStateSaver>> that tracks which JET projects and their state savers 
   */
  private final Map stateSaverByProject = new HashMap();
  
  
  private final Plugin plugin;
  /**
   * Track if a new save number is required. A doProjectSave save will request a new save number 
   * if this value is true, and then set this flag to false. 
   * This flag will be reset to true at the end of a successful full save.
   */
  private boolean needNewSaveNumber = true;

  /**
   * @param plugin 
   * 
   */
  public SavedStateManager(Plugin plugin)
  {
    this.plugin = plugin;
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.ISaveParticipant#doneSaving(org.eclipse.core.resources.ISaveContext)
   */
  public void doneSaving(ISaveContext context)
  {
    if(context.getKind() == ISaveContext.FULL_SAVE) {
      // delete the old saved state since it is not necessary any more
      final int previousSaveNumber = context.getPreviousSaveNumber();
      
      final Path saveDirectory = getSaveDirectory(previousSaveNumber);
      final IPath location = plugin.getStateLocation().append(saveDirectory);
      final File file = location.toFile();
      if(file.exists()) {
        deleteDirectory(file);
      }
      
      // check for directories to purge because of bug 201583...
      final File stateDir = plugin.getStateLocation().toFile();
      if (stateDir.exists() && stateDir.isDirectory())
      {
        final String currentSaveDirectory = getSaveDirectory(context.getSaveNumber()).toString();
        final File[] orphanedSaveDirs = stateDir.listFiles(new FilenameFilter()
          {

            public boolean accept(File dir, String name)
            {
              return name.startsWith("save-") && !name.equals(currentSaveDirectory); //$NON-NLS-1$
            }
          });
        for (int i = 0; i < orphanedSaveDirs.length; i++)
        {
          deleteDirectory(orphanedSaveDirs[i]);
        }
      }
    }
  }
  
  private void deleteDirectory(File dir) {
    if(dir.exists() && dir.isDirectory())
    {
      File[] files = dir.listFiles();
      for (int i = 0; files != null && i < files.length; i++)
      {
        if(files[i].isDirectory()) {
          deleteDirectory(files[i]);
        } else {
          files[i].delete();
        }
      }
      dir.delete();
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.ISaveParticipant#prepareToSave(org.eclipse.core.resources.ISaveContext)
   */
  public void prepareToSave(ISaveContext context) throws CoreException
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.ISaveParticipant#rollback(org.eclipse.core.resources.ISaveContext)
   */
  public void rollback(ISaveContext context)
  {
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.resources.ISaveParticipant#saving(org.eclipse.core.resources.ISaveContext)
   */
  public void saving(ISaveContext context) throws CoreException
  {
    switch (context.getKind())
    {
      case ISaveContext.FULL_SAVE:
        // save the plug-in state
        doFullSave(context, stateSaverByProject.values());
        break;
      case ISaveContext.PROJECT_SAVE:
        // get the project related to this save operation
        IProject project = context.getProject();
        
        // Don' bother saving the project. It gets lost in the next full save anyhow.
        // worst case is that it gets rebuilt on open
//        WeakReference ref = (WeakReference)stateSaverByProject.get(project);
//        if(ref != null) {
//          doProjectSave(context, (IStateSaver)ref.get());
//        }
        // PROJECT_SAVE happens only when the project is closed, forget about this project
        stateSaverByProject.remove(project);
        break;
      case ISaveContext.SNAPSHOT:
        // do nothing for snapshot saves...
        break;
    }
  }

  /**
   * @param context
   * @param projectSavers
   * @throws IllegalStateException
   * @throws CoreException
   */
  private void doFullSave(ISaveContext context, Collection projectSavers) throws CoreException
  {
    for (Iterator i = projectSavers.iterator(); i.hasNext();)
    {
      final WeakReference ref = (WeakReference)i.next();
      final IStateSaver saver = (IStateSaver)ref.get();
      if(saver != null) 
      {
        doProjectSave(context, saver);
      }
    }
    
    // reset this flag so that next full save or project save will request a new save number.
    needNewSaveNumber = true;
  }

  /**
   * @param context
   * @param saver
   * @throws CoreException
   */
  private void doProjectSave(ISaveContext context, final IStateSaver saver) throws CoreException
  {
    if(needNewSaveNumber) {
      context.needSaveNumber();
      needNewSaveNumber = false;
    }
    IPath logicalPath = getLogicalStatePath(saver);
   
    final IPath realPath = getActualPath(logicalPath, context.getSaveNumber());
    context.map(logicalPath, realPath);
    
    final IPath location = plugin.getStateLocation().append(realPath);
 
    saver.doSave(location);
  }

  /**
   * @param path
   * @param saveNumber
   * @return
   */
  private IPath getActualPath(IPath logicalPath, int saveNumber)
  {
    IPath saveDirPath = getSaveDirectory(saveNumber);
    
    final IPath realPath = saveDirPath.append(logicalPath);
    return realPath;
  }

  /**
   * @param saver
   * @return
   */
  private IPath getLogicalStatePath(IStateSaver saver)
  {
    IPath logicalPath = new Path(saver.getProject().getName()).append(saver.getStateFileName());
    return logicalPath;
  }

  /**
   * Return the directory to which the state will be saved
   * @param saveNumber the save number
   * @return a path relative to the plug-in's workspace save location
   */
  private Path getSaveDirectory(int saveNumber)
  {
    return new Path("save-" + Integer.toString(saveNumber)); //$NON-NLS-1$
  }

  public void startup()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    
    if(DEBUG) System.out.println("SavedStateManager.startup()"); //$NON-NLS-1$
    try
    {
      savedState = ResourcesPlugin.getWorkspace().addSaveParticipant(plugin, this);
      if(DEBUG && savedState != null) System.out.println("   found saved state: " + savedState.getSaveNumber()); //$NON-NLS-1$
    }
    catch (CoreException e)
    {
      savedState = null;
    }
    
    timer.done();
  }
  
  public void shutdown()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "shutdown()"); //$NON-NLS-1$
    
    if(DEBUG) System.out.println("SavedStateManager.shutdown()"); //$NON-NLS-1$
    ResourcesPlugin.getWorkspace().removeSaveParticipant(plugin);
    
    timer.done();
  }
  
  public IPath addSaveSaver(IStateSaver saver ,IProject project)
  {
    stateSaverByProject.put(project, new WeakReference(saver));
    IPath initialLocation = null;
    if(savedState != null)
    {
      final int saveNumber = savedState.getSaveNumber();
      final IPath realPath = getActualPath(getLogicalStatePath(saver), saveNumber);
      initialLocation = plugin.getStateLocation().append(realPath);
    }
    if(DEBUG) System.out.println("SavedStateManager.addSaveSaver(): " + project + ", " + initialLocation);  //$NON-NLS-1$//$NON-NLS-2$
    return initialLocation;
  }

}
