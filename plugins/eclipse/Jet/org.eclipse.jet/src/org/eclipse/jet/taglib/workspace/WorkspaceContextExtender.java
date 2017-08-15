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

package org.eclipse.jet.taglib.workspace;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.RuntimeLoggerContextExtender;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.transform.TransformContextExtender;
import org.eclipse.jet.transform.TransformContextListener;


/**
 * An extension to the JET2Context for collecting workspace actions (file writes etc) and
 * performing them at the end of a JET2 transformation.
 *
 */
public final class WorkspaceContextExtender
{
  private static String PRIVATE_CONTEXT_DATA_KEY = WorkspaceContextExtender.class.getName();

  private static final String RESOURCE_VAR_PREFIX = "org.eclipse.jet.resource."; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the project relative path of the resource's parent.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getParent()
   * @see IResource#getProject()
   */
  public static final String VAR_RESOURCE_PARENT_PROJECT_RELATIVE_PATH = RESOURCE_VAR_PREFIX + "parent.projectRelativePath"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the full path of the resource's parent.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getParent()
   * @see IResource#getFullPath()
   */
  public static final String VAR_RESOURCE_PARENT_FULL_PATH = RESOURCE_VAR_PREFIX + "parent.fullPath"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the raw location of the resource's parent.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getParent()
   * @see IResource#getRawLocation()
   */
  public static final String VAR_RESOURCE_PARENT_RAW_LOCATION = RESOURCE_VAR_PREFIX + "parent.rawLocation"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the location of the resource's parent.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getParent()
   * @see IResource#getLocation()
   */
  public static final String VAR_RESOURCE_PARENT_LOCATION = RESOURCE_VAR_PREFIX + "parent.location"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the name of the resource's parent.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getParent()
   */
  public static final String VAR_RESOURCE_PARENT_NAME = RESOURCE_VAR_PREFIX + "parent.name"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing file name (with extension removed) of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getName()
   * @see IResource#getFileExtension()
   */
  public static final String VAR_RESOURCE_FILE_NAME = RESOURCE_VAR_PREFIX + "fileName"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the file extension of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getFileExtension()
   */
  public static final String VAR_RESOURCE_FILE_EXTENSION = RESOURCE_VAR_PREFIX + "fileExtension"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the project relative path of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getProjectRelativePath()
   */
  public static final String VAR_RESOURCE_PROJECT_RELATIVE_PATH = RESOURCE_VAR_PREFIX + "projectRelativePath"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the full path of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getFullPath()
   */
  public static final String VAR_RESOURCE_FULL_PATH = RESOURCE_VAR_PREFIX + "fullPath"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the name of the project containing the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getProject()
   */
  public static final String VAR_RESOURCE_PROJECT_NAME = RESOURCE_VAR_PREFIX + "project.name"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the raw location of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getRawLocation()
   */
  public static final String VAR_RAW_LOCATION = RESOURCE_VAR_PREFIX + "rawLocation"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the location of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getLocation()
   */
  public static final String VAR_RESOURCE_LOCATION = RESOURCE_VAR_PREFIX + "location"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the type of the resource. The value of the context
   * variable will be one of 'file', 'folder' or 'project'.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getType()
   */
  public static final String VAR_RESOURCE_TYPE = RESOURCE_VAR_PREFIX + "type"; //$NON-NLS-1$
  /**
   * Name of the context variable referencing the name of the resource.
   * @see #loadResourceAsSource(JET2Context, IResource, String, String)
   * @see IResource#getName()
   */
  public static final String VAR_RESOURCE_NAME = RESOURCE_VAR_PREFIX + "name"; //$NON-NLS-1$

  private static final class ContextData implements TransformContextListener
  {

    public final List workspaceActions = new ArrayList();

    public final Stack containerStack = new Stack();

    public final List finalWorkspaceActions = new ArrayList();

    public ContextData()
    {
      // do nothing
    }

    private void checkCanceled(IProgressMonitor monitor) {
      if(monitor != null && monitor.isCanceled()) {
        throw new OperationCanceledException();
      }
    }
    
    public void commit(final JET2Context context, IProgressMonitor monitor) throws JET2TagException
    {
      final int actionsToExecuteCount = workspaceActions.size() + finalWorkspaceActions.size();
      final List actionsToExecute = new ArrayList(actionsToExecuteCount);
      actionsToExecute.addAll(workspaceActions);
      actionsToExecute.addAll(finalWorkspaceActions);
      
      TransformContextExtender tce = TransformContextExtender.getInstance(context);
      monitor.beginTask(JET2Messages.WorkspaceContextExtender_Commiting, 1 + actionsToExecuteCount);
      monitor.setTaskName(JET2Messages.WorkspaceContextExtender_Commiting);
      try
      {
        monitor.subTask(JET2Messages.WorkspaceContextExtender_ConfirmingTeamAccess);
        final List filesToValidateEdit = new ArrayList(actionsToExecute.size());
        for (Iterator i = actionsToExecute.iterator(); i.hasNext();)
        {
          checkCanceled(monitor);
          final IWorkspaceAction action = (IWorkspaceAction)i.next();
          if(isActionRequired(context, action)) {
            doActionRequiresValidateEdit(context, action, filesToValidateEdit);
          } else {
            // won't execute this action, not contents have changed...
            i.remove();
          }
        }
        IStatus validateOk = ResourcesPlugin.getWorkspace().validateEdit(
          (IFile[])filesToValidateEdit.toArray(new IFile [filesToValidateEdit.size()]),
          tce.getShellContext());

        if (!validateOk.isOK())
        {
          // it would be nice to co-relate this message to a particular action, but 
          // the validateEdit API doesn't give us much
          throw new JET2TagException(validateOk.getMessage());
        }
        monitor.worked(1);

        monitor.subTask(JET2Messages.WorkspaceContextExtender_WritingFiles);
        ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
          {

            public void run(final IProgressMonitor writingMonitor) throws CoreException
            {
              writingMonitor.beginTask(JET2Messages.WorkspaceContextExtender_WritingFiles, actionsToExecute.size());
              writingMonitor.setTaskName(JET2Messages.WorkspaceContextExtender_WritingFiles);
              try
              {
                for (Iterator i = actionsToExecute.iterator(); i.hasNext();)
                {
                  checkCanceled(writingMonitor);
                  final IWorkspaceAction action = (IWorkspaceAction)i.next();
                  writingMonitor.subTask(action.getTagInfo().displayString());
                  doActionPerformAction(context, action, new SubProgressMonitor(writingMonitor, 1));
                }
              }
              finally
              {
                writingMonitor.done();
              }
            }
          }, new SubProgressMonitor(monitor, actionsToExecute.size()));
      }
      catch (CoreException e)
      {
        throw new JET2TagException(e);
      }
      finally
      {
        monitor.done();
      }
    }

    protected boolean isActionRequired(JET2Context context, IWorkspaceAction action)
    {
      final IResource resource = getActionResource(action);
      if(resource != null && !(resource instanceof IFile)) {
        // For non-files, action is required only if resource does not exist
        return !resource.exists();
      }
      if(!(action instanceof IWorkspaceActionExtension)) {
        // Can't determine if contents are changed, must execute the action...
        return true;
      }
      // attempt to read the existing contents....
      IWorkspaceActionExtension ext = (IWorkspaceActionExtension)action;
      final IFile file = (IFile)resource;
      InputStream existingContents = null;
      if (file != null && file.exists())
      {
        try
        {
          existingContents = file.getContents();
        }
        catch (CoreException e)
        {
          // exception reading existing contents, fall through...
        }
      }
      
      return ActionsUtil.finalizeAndTestForChange(file, ext.getContentWriter(), existingContents);
    }

    private IResource getActionResource(IWorkspaceAction action)
    {
      IResource resource;
      try
      {
        resource = action.getResource();
      }
      catch (JET2TagException e)
      {
        resource = null;
      }
      return resource;
    }

    /**
     * @param context
     * @param action
     * @param filesToValidateEdit
     */
    protected void doActionRequiresValidateEdit(final JET2Context context, final IWorkspaceAction action, final List filesToValidateEdit)
    {
      SafeRunner.run(new ISafeRunnable() {

        public void handleException(Throwable exception)
        {
          context.logError(action.getTemplatePath(), action.getTagInfo(), null, exception);
        }

        public void run() throws Exception
        {
          try
          {
            if (action.requiresValidateEdit())
            {
              filesToValidateEdit.add(action.getResource());
            }
          }
          catch (JET2TagException e)
          {
            handleException(e);
          }
        }});
    }

    /**
     * @param context
     * @param action
     * @throws JET2TagException
     */
    protected void doActionCommitted(final JET2Context context, final IWorkspaceAction action) throws JET2TagException
    {
      final IResource resource = getActionResource(action);
      if(action instanceof IWorkspaceActionExtension) {
          ActionsUtil.contentCommitted(((IWorkspaceActionExtension)action).getContentWriter(), resource);
      }
      
      if(resource != null) {
        String msgPattern;
        switch(resource.getType()) {
          case IResource.FILE:
            msgPattern = JET2Messages.WsFileFromWriterAction_WritingFile;
            break;
          case IResource.FOLDER:
            msgPattern = JET2Messages.WsFolderAction_CreatingFolder;
            break;
          case IResource.PROJECT:
            msgPattern = JET2Messages.WsProjectAction_CreatingProject;
            break;
          default:
            msgPattern = null;
        }
        if (msgPattern != null)
        {
          final String fileMessage = MessageFormat.format(msgPattern, 
            new Object []{ resource.getFullPath().makeRelative().toString() });
          RuntimeLoggerContextExtender.log(
            context,
            fileMessage,
            action.getTagInfo(),
            action.getTemplatePath(),
            RuntimeLoggerContextExtender.INFO_LEVEL);
        }
      }
    }

    /**
     * @param context
     * @param action
     * @param monitor
     */
    protected void doActionPerformAction(final JET2Context context, final IWorkspaceAction action, final IProgressMonitor monitor)
    {
      SafeRunner.run(new ISafeRunnable() {
    
        public void handleException(Throwable exception)
        {
          context.logError(action.getTemplatePath(), action.getTagInfo(), null, exception);
        }
    
        public void run() throws Exception
        {
          try
          {
            boolean written;
            if(action instanceof IWorkspaceAction2) {
              written = ((IWorkspaceAction2)action).performActionIfRequired(monitor);
            } else {
              action.performAction(monitor);
              written = true;
            }
            if(written) {
              doActionCommitted(context, action);
            }
          }
          catch (JET2TagException e)
          {
            handleException(e);
          }
          finally {
            monitor.done();
          }
        }});
    }

  }

  /**
   * Create a workspace context extender on the passed context.
   * @param context a JET2Context. Cannot be <code>null</code>.
   * @deprecated Since 0.9.0, use {@link WorkspaceContextExtender#getInstance(JET2Context)} instead.
   */
  public WorkspaceContextExtender(JET2Context context)
  {
    this(context, getInstance(context).contextData);
  }

  private WorkspaceContextExtender(JET2Context context, ContextData contextData)
  {
    this.contextData = contextData;
    TransformContextExtender.getInstance(context).addListener(contextData);
  }

  private final ContextData contextData;
  
  /**
   * Add a new action to be executed when the JET2 transform commits.
   * @param action the action to execute
   */
  public void addAction(IWorkspaceAction action)
  {
    contextData.workspaceActions.add(action);
  }

  /**
   * Add a new action to be executed after all actions added view {@link #addAction(IWorkspaceAction)}
   * are executed when the JET2 transform commits. Final actions behave identically to other actions,
   * except that the occur after all 'normal' actions. In particular, final actions:
   * <bl>
   * <li>participate in the team environment validateEdit phase.</li>
   * <li>are performed within the same Eclipse workspace operation as other actions.</li>
   * </bl>
   * @param action the action to execute
   */
  public void addFinalAction(IWorkspaceAction action)
  {
    contextData.finalWorkspaceActions.add(action);
  }
  /**
   * Push a default container onto the stack of default containers.
   * @param container
   */
  public void pushContainer(IContainer container)
  {
    contextData.containerStack.push(container);
  }

  /**
   * Remove the top-most container on the default containers stack.
   * @throws JET2TagException if the container stack is empty.
   */
  public void popContainer() throws JET2TagException
  {
    if (contextData.containerStack.isEmpty())
    {
      throw new JET2TagException(JET2Messages.WorkspaceContextExtender_NoParent);
    }
    contextData.containerStack.pop();
  }

  /**
   * Test whether the container stack has an entry for resolving relative paths.
   * @return <code>true</code> if the container stack is not empty, <code>false</code> otherwise.
   */
  public boolean existsContainer()
  {
    return !contextData.containerStack.isEmpty();
  }
  
  /**
   * Get the top most container from the default containers stack without doing a pop.
   * @return the default container
   * @throws JET2TagException if the container stack is empty.
   */
  public IContainer getContainer() throws JET2TagException
  {
    if (contextData.containerStack.isEmpty())
    {
      throw new JET2TagException(JET2Messages.WorkspaceContextExtender_NoParent);
    }
    return (IContainer)contextData.containerStack.peek();
  }

  /**
   * Return the workspace context extender for the given JET context.
   * @param context the JET context.
   * @return the Workspace Context extender
   */
  public static WorkspaceContextExtender getInstance(JET2Context context)
  {
    if(context == null) {
      throw new NullPointerException();
    }
    WorkspaceContextExtender ex = (WorkspaceContextExtender)context.getPrivateData(PRIVATE_CONTEXT_DATA_KEY);
    if (ex == null)
    {
      ex = new WorkspaceContextExtender(context, new ContextData());
      context.addPrivateData(PRIVATE_CONTEXT_DATA_KEY, ex);
    }
    return ex;
  }

  /**
   * Load the passed IResource, and set it as the source of the passed JET2Context.
   * This method delegates to {@link TransformContextExtender#loadModel(URL, String, String)} and then
   * sets a number of context variables describing the loaded resource. Finally, the root of the loaded
   * resource is set as the context source by invoking {@link JET2Context#setSource(Object)}.
   * <p>
   * The following context variables are set for all resources:
   * <bl>
   * <li>org.eclipse.jet.resource.name - the name of the resource. See {@link #VAR_RESOURCE_NAME}.</li>
   * <li>org.eclipse.jet.resource.type - the type of the resource. See {@link #VAR_RESOURCE_TYPE}.</li>
   * <li>org.eclipse.jet.resource.location - the location of the resource. See {@link #VAR_RESOURCE_LOCATION}.</li>
   * <li>org.eclipse.jet.resource.rawLocation - the raw location of the resource. See {@link #VAR_RAW_LOCATION}.</li>
   * <li>org.eclipse.jet.resource.project.name - the project name of the resource. See {@link #VAR_RESOURCE_PROJECT_NAME}.</li>
   * <li>org.eclipse.jet.resource.fullPath - the full path of the resource. See {@link #VAR_RESOURCE_FULL_PATH}.</li>
   * <li>org.eclipse.jet.resource.projectRelativePath - the project relative path of the resource. See {@link #VAR_RESOURCE_PROJECT_RELATIVE_PATH}.</li>
   * <li>org.eclipse.jet.resource.fileExtension - the file extension of the resource. See {@link #VAR_RESOURCE_FILE_EXTENSION}.</li>
   * <li>org.eclipse.jet.resource.fileName - the file name (without extension) of the resource. See {@link #VAR_RESOURCE_FILE_NAME}.</li>
   * </bl>
   * </p>
   * <p>
   * The following variables are set for resources of type file and folder only:
   * <bl>
   * <li>org.eclipse.jet.resource.parent.name - the name of the resource's parent. See {@link #VAR_RESOURCE_NAME}.</li>
   * <li>org.eclipse.jet.resource.parent.location - the location of the resource's parent. See {@link #VAR_RESOURCE_LOCATION}.</li>
   * <li>org.eclipse.jet.resource.parent.rawLocation - the raw location of the resource's parent. See {@link #VAR_RAW_LOCATION}.</li>
   * <li>org.eclipse.jet.resource.parent.fullPath - the full path of the resource's parent. See {@link #VAR_RESOURCE_FULL_PATH}.</li>
   * <li>org.eclipse.jet.resource.parent.projectRelativePath - the project relative path of the resource's parent. See {@link #VAR_RESOURCE_PROJECT_RELATIVE_PATH}.</li>
   * </bl>
   * </p>
   * <p>
   * In general, the names of the properties correspond to getX() functions of {@link IResource}.
   * </p>
   * @see IResource#getName()
   * @see IResource#getType()
   * @see IResource#getLocation()
   * @see IResource#getRawLocation()
   * @see IResource#getProject()
   * @see IResource#getFullPath()
   * @see IResource#getProjectRelativePath()
   * @see IResource#getFileExtension()
   * @see IResource#getParent()
   * 
   * @param context the JET2Context into which the loaded model will be set as source.
   * @param resource the resource to load
   * @param resourceLoaderId the id of the model loader to use, or <code>null</code>.
   * @param resourceType the type of the resource, or <code>null</code>.
   * @throws CoreJETException if the resource cannot be loaded
   * @throws IOException if the loader fails to load the resource.
   */
  public static void loadResourceAsSource(JET2Context context, IResource resource, String resourceLoaderId, String resourceType) throws CoreJETException,
    IOException
  {
    final URL url = new URL("platform:/resource" + resource.getFullPath()); //$NON-NLS-1$
    Object source = TransformContextExtender.loadModel(url, resourceLoaderId, resourceType);
    
    context.setSource(source);
    
    // setup context variables describing the loaded resource.
    final String name = resource.getName();
    try
    {
      context.setVariable(VAR_RESOURCE_NAME, name);
      context.setVariable(VAR_RESOURCE_TYPE, getResourceTypeString(resource.getType()));
      context.setVariable(VAR_RESOURCE_LOCATION, resource.getLocation().toString());
      context.setVariable(VAR_RAW_LOCATION, resource.getRawLocation() != null ? resource.getRawLocation().toString() : resource.getLocation().toString());
      context.setVariable(VAR_RESOURCE_PROJECT_NAME, resource.getProject().getName());
      context.setVariable(VAR_RESOURCE_FULL_PATH, resource.getFullPath().toString());
      context.setVariable(VAR_RESOURCE_PROJECT_RELATIVE_PATH, resource.getProjectRelativePath().toString());
      final String fileExtension = resource.getFileExtension();
      context.setVariable(VAR_RESOURCE_FILE_EXTENSION, fileExtension);
      context.setVariable(VAR_RESOURCE_FILE_NAME, 
        fileExtension == null ? name : name.substring(0, name.length() - fileExtension.length() - 1));
      switch (resource.getType())
      {
        case IResource.FILE:
        case IResource.FOLDER:
          context.setVariable(VAR_RESOURCE_PARENT_NAME, resource.getParent().getName());
          context.setVariable(VAR_RESOURCE_PARENT_LOCATION, resource.getParent().getLocation().toString());
          context.setVariable(VAR_RESOURCE_PARENT_RAW_LOCATION, 
                resource.getParent().getRawLocation() == null  
                    ? resource.getParent().getLocation().toString() 
                    : resource.getParent().getRawLocation().toString());
          context.setVariable(VAR_RESOURCE_PARENT_FULL_PATH, resource.getParent().getFullPath().toString());
          context.setVariable(VAR_RESOURCE_PARENT_PROJECT_RELATIVE_PATH, resource.getParent().getProjectRelativePath().toString());
          break;
        case IResource.PROJECT:
          break;
      }
    }
    catch (JET2TagException e)
    {
      InternalJET2Platform.logError("Invalid variable name", e); //$NON-NLS-1$
    }
  }


  private static String getResourceTypeString(int type)
  {
    switch (type)
    {
      case IResource.FILE:
        return "file"; //$NON-NLS-1$
      case IResource.FOLDER:
        return "folder"; //$NON-NLS-1$
      case IResource.PROJECT:
        return "project"; //$NON-NLS-1$
      case IResource.ROOT:
        return "root"; //$NON-NLS-1$
      default:
        return "unknown"; //$NON-NLS-1$
    }
  }


}
