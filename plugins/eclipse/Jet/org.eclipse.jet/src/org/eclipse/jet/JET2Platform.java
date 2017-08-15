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

package org.eclipse.jet;


import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.builder.WorkspaceCompiler;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.RuntimeLoggerContextExtender;
import org.eclipse.jet.internal.runtime.RuntimeTagLogger;
import org.eclipse.jet.runtime.model.ILoaderManager;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.transform.IJETBundleManager;
import org.eclipse.jet.transform.IJETRunnable;
import org.eclipse.jet.transform.TransformContextExtender;
import org.eclipse.jet.xpath.XPathFunctionMetaData;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleException;


/**
 * Utility class for invoking JET Transforms
 *
 */
public class JET2Platform
{

  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
    && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/traceTagExecution")).booleanValue(); //$NON-NLS-1$

  // place option constants here...
  /**
   * The Plugin identifier of the JET2 core plugin, "org.eclipse.jet".
   */
  public static final String PLUGIN_ID = "org.eclipse.jet"; //$NON-NLS-1$

  /**
   * The Project nature assigned to JET2 Transformation projects, "org.eclipse.jet.jet2Nature"
   */
  public static final String JET2_NATURE_ID = JET2Platform.PLUGIN_ID + "." + "jet2Nature"; //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * Return the value of the shell context, suitable for passing to 
   * {@link TransformContextExtender#setShellContext(Object)}. 
   * This method will work, even in the abscence of a shell or even SWT.
   * @return the shell context or <code>null</code> if none is set
   */
  public static final Object getShellContext()
  {
    final Object shell[] = { null };
    try
    {
      // use a runnable to defer class loading of PlatformUI and Display until the runnable is instantiated.
      Runnable runnable = new Runnable()
      {
    
        public void run()
        {
          if (PlatformUI.isWorkbenchRunning())
          {
            Display.getDefault().syncExec(new Runnable()
              {
    
                public void run()
                {
                  final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                  if (activeWorkbenchWindow != null)
                  {
                    shell[0] = activeWorkbenchWindow.getShell();
                  }
                }
              });
          }
        }
    
      };
      runnable.run();
    
    }
    catch (NoClassDefFoundError e)
    {
      // happens if swt or platform core plug-ins are not present, and Display, Shell or PlatformUI cannot be loaded
      // ignore
    }
    
    return shell[0];
  }

  /**
   * @param project
   * @param contextLog
   * @throws CoreException
   */
  private static void addRuntimeProblemMarkersToProject(IProject project, ContextLogEntry contextLog) throws CoreException
  {
    Set fileBuffers = new HashSet();
    final ITextFileBufferManager tfbManager = FileBuffers.getTextFileBufferManager();
    
    // clear off any runtime markers...
    project.deleteMarkers(WorkspaceCompiler.RUNTIME_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
    ContextLogEntry[] problems = contextLog.getChildren();
    try
    {
      for (int i = 0; i < problems.length; i++)
      {
        String templatePath = problems[i].getTemplatePath();
        if (templatePath == null)
        {
          continue;
        }
        IResource errorResource = templatePath != null ? project.findMember(templatePath) : project;

        final IPath location = errorResource.getFullPath();
        if (!fileBuffers.contains(location))
        {
          tfbManager.connect(location, new NullProgressMonitor());
          fileBuffers.add(location);
        }

        final ITextFileBuffer textFileBuffer = tfbManager.getTextFileBuffer(location);

        IMarker marker = errorResource.createMarker(WorkspaceCompiler.RUNTIME_PROBLEM_MARKER);
        switch (problems[i].getSeverity())
        {
          case ContextLogEntry.ERROR:
          marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            break;
          case ContextLogEntry.WARNING:
          marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
            break;
          case ContextLogEntry.INFO:
          marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
            break;
        }
        TagInfo td = problems[i].getTagInfo();
        int line = problems[i].getLine();
        int col = problems[i].getCol();
        if (line > 0 && col > 0)
        {
          marker.setAttribute(IMarker.LINE_NUMBER, line);
          try
          {
            int start = textFileBuffer.getDocument().getLineOffset(td.getLine() - 1) + td.getCol() - 1;
            final IRegion region = new FindReplaceDocumentAdapter(textFileBuffer.getDocument()).find(start, "(?<!\\\\)>", // Finds > not preceded by \\ //$NON-NLS-1$
              true,
              true,
              false,
              true);
            int end = region.getOffset() + region.getLength();
            marker.setAttribute(IMarker.CHAR_START, start);
            marker.setAttribute(IMarker.CHAR_END, end);
          }
          catch (BadLocationException e)
          {
            // do nothing, just won't set the start & end offsets;
          }
        }
        if (problems[i].getMessage() != null)
        {
          marker.setAttribute(IMarker.MESSAGE, problems[i].getMessage());
        }
      }
    }
    finally
    {
      for (Iterator i = fileBuffers.iterator(); i.hasNext();)
      {
        IPath location = (IPath)i.next();
        tfbManager.disconnect(location, new NullProgressMonitor());
      }
    }
  }

  /**
   * @deprecated Use {@link #runTransformOnResource(String,IResource,Map, IProgressMonitor)} instead
   */
  public static IStatus execute(final String id, final IResource resource, final IProgressMonitor monitor)
  {
    return runTransformOnResource(id, resource, null, monitor);
  }

  /**
   * Execute a JET2 transformation
   * @param id
   * @param source
   * @param monitor
   * @return the execution status
   * @deprecated Use {@link #runTransformOnObject(String,Object,Map, IProgressMonitor)} instead
   */
  public static IStatus execute(final String id, final Object source, final IProgressMonitor monitor)
  {
    return runTransformOnObject(id, source, null, monitor);
  }
  
  /**
   * Invoke a JET transform on the passed String representation of the input model.
   * @deprecated Use {@link #runTransformOnString(String,String,IProgressMonitor)} instead
   */
  public static IStatus execute(final String id, final String source, final IProgressMonitor monitor)
  {
    return runTransformOnString(id, source, monitor);
  }

  /**
   * Invoke a JET transform on the passed String representation of an input model.
   * @param id the transform id
   * @param source the string respresentation of the input model.
   * @param kind the kind of model (file extension) the string represents.
   * @param monitor a progress monitor
   * @return the transform's execution status.
   * @deprecated Use {@link #runTransformOnString(String,String,String,Map, IProgressMonitor)} instead
   */
  public static IStatus execute(final String id, final String source, final String kind, final IProgressMonitor monitor) 
  {
    return runTransformOnString(id, source, kind, null, monitor);
  }

  /**
   * Return the XPath functions installed by the 'org.eclipse.jet.xpathFunctions' extension point.
   * @return an array of functions.
   */
  public static XPathFunctionMetaData[] getInstalledXPathFunctions()
  {
    return InternalJET2Platform.getDefault().getXPathFunctionsManager().getCustomFunctions();
  }

  public static IJETBundleManager getJETBundleManager()
  {
    return InternalJET2Platform.getDefault().getBundleManager();
  }

  /**
   * Return the bundle description for the named JET project
   * @param name an Eclipse project name
   * @return a bundle description, or <code>null</code> if the project does not exist or
   * is not a JET project.
   */
  public static IJETBundleDescriptor getProjectDescription(final String name)
  {
    IJETBundleDescriptor result = null;
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    if(isOpenJETProject(project))
    {
      final IJETBundleManager bundleManager = getJETBundleManager();
      result = bundleManager.getDescriptorForProject(name);
    }
    return result;
  }

  /**
   * Test whether a project is an open JET project
   * @param project
   * @return
   */
  private static boolean isOpenJETProject(IProject project)
  {
    try
    {
      return project != null && project.exists() && project.isOpen() && project.hasNature(JET2_NATURE_ID);
    }
    catch (CoreException e)
    {
      return false;
    }
  }

  /**
   * @param id
   * @param contextLog
   * @throws CoreException
   */
  private static void processResults(final String id, final ContextLogEntry contextLog)
  {
    final IJETBundleManager bundleManager = getJETBundleManager();
    String projectName = bundleManager.getProjectForId(id);
    if(projectName != null)
    {
      final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
      // we use this runnable to allow markers to be completely created
      // prior to becoming visible to the platform. See Eclipse FAQ 304
      try
      {
        project.getWorkspace().run(new IWorkspaceRunnable()
          {
            public void run(IProgressMonitor monitor1) throws CoreException
            {
              project.deleteMarkers(
                WorkspaceCompiler.RUNTIME_PROBLEM_MARKER,
                true,
                IResource.DEPTH_INFINITE);
              addRuntimeProblemMarkersToProject(project, contextLog);
            }
          }, null, IWorkspace.AVOID_UPDATE, null);
      }
      catch (CoreException e)
      {
        InternalJET2Platform.logError(JET2Messages.JET2Platform_ErrorMarkingProject, e);
      }
    }
  }

  /**
   * Convert a Context log entry into an IStatus;
   * @param logEntry a log entry
   * @return an IStatus
   */
  public static IStatus toIStatus(ContextLogEntry logEntry)
  {
    final ContextLogEntry[] children = logEntry.getChildren();
//    for (int i = 0; i < children.length; i++)
//    {
//      severity = Math.max(severity, children[i].getSeverity());
//    }
    if(children.length > 0) {
      MultiStatus result = new MultiStatus(JET2Platform.PLUGIN_ID, IStatus.OK, logEntry.getMessage(), logEntry.getException());
      for (int i = 0; i < children.length; i++)
      {
        result.add(toIStatus(children[i]));
      }
      
      
      return result;
    } else {
      int severity = logEntry.getSeverity();
      return new Status(severity, JET2Platform.PLUGIN_ID, IStatus.OK, logEntry.getMessage(), logEntry.getException());
    }
  }
  /**
   * Invoke a JET transform against the passed context
   * @param id the JET Transform id
   * @param context the JET2Context
   * @param monitor a progress monitor
   * @return the transform's execution status
   */
  public static IStatus runTransform(final String id, final JET2Context context, final IProgressMonitor monitor)
  {
    IStatus result;
    monitor.beginTask(JET2Messages.JET2Platform_Executing + id, 100);
    
    try
    {
      final IJETBundleManager bundleManager = getJETBundleManager();
      
      if (bundleManager.getDescriptor(id) == null)
      {
        final String msg = MessageFormat.format(JET2Messages.JET2Platform_TransformNotFound, new Object []{ id });
        result = InternalJET2Platform.newStatus(IStatus.ERROR, msg, null);
        InternalJET2Platform.log(result);
        return result;
      }
      monitor.subTask(JET2Messages.JET2Platform_RetrievingBundle);
      
      if (DEBUG)
      {
        RuntimeLoggerContextExtender rl = RuntimeLoggerContextExtender.getInstance(context);
        rl.addListener(new RuntimeTagLogger()
          {

            public void log(String message, TagInfo td, String templatePath, int level)
            {
              int nlPos = message.indexOf(System.getProperty("line.separator")); //$NON-NLS-1$
              if(nlPos >= 0) {
                message = message.substring(0, nlPos);
              }
              System.out.println((templatePath == null ? "" : templatePath + " ") + (td != null ? td.toString() : "") + ": " + message); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            }
          });
      }

      bundleManager.run(id, new IJETRunnable() {

        public void run(IJETBundleDescriptor descriptor, JET2TemplateLoader templateLoader, IProgressMonitor monitor)
        {
          monitor.beginTask(JET2Messages.JET2Platform_Executing, 100);
          monitor.setTaskName(JET2Messages.JET2Platform_Executing);
          monitor.subTask(descriptor.getId());
          final TransformContextExtender tce = TransformContextExtender.getInstance(context);
          if(tce.getShellContext() == null )
          {
            Object shell = getShellContext();
            if(shell != null)
            {
              tce.setShellContext(shell);
            }
          }
          try
            {
              XPathContextExtender xpe = XPathContextExtender.getInstance(context);
              xpe.addCustomFunctions(getInstalledXPathFunctions());

              tce.setLoader(templateLoader);
              tce.setBundleDescriptor(descriptor);
              if(descriptor.getOverridesId() != null)
              {
                tce.setOverride(descriptor.getOverridesId(), new SubProgressMonitor(monitor, 10));
              }

              BodyContentWriter bodyContentWriter = new BodyContentWriter();

              tce.setProgressMonitor(new SubProgressMonitor(monitor, 50));
              tce.execute(descriptor.getMainTemplate(), bodyContentWriter);
              monitor.subTask(JET2Messages.JET2Platform_CommittingResults);
              tce.commit(new SubProgressMonitor(monitor, 50));
            }
          catch (JET2TagException e)
          {
            context.logError(e);
          }
          finally
          {
            tce.cleanup();
            monitor.done();
          }
        }}, new SubProgressMonitor(monitor, 90));
      
//      Diagnostic diagnostic = context.getLogAsMultiStatus();
      ContextLogEntry contextLog = context.getLogEntries();
      processResults(id, contextLog);
      result = toIStatus(contextLog);
    }
    catch (OperationCanceledException e) 
    {
      result = Status.CANCEL_STATUS;
    }
    catch (BundleException e)
    {
      result = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, e.getLocalizedMessage(), e);
    }
    finally
    {
      monitor.done();
      if(DEBUG) {
        System.out.flush();
      }
    }

    return result;
  }

  /**
   * Execute a JET transform with the passed object as the root of the source model.
   * @param id the JET Transform id
   * @param source the source model root object.
   * @param monitor a progress monitor
   * @return the transform's execution status
   */
  public static IStatus runTransformOnObject(final String id, final Object source, final IProgressMonitor monitor)
  {
    return runTransformOnObject(id, source, null, monitor);
  }

  /**
   * Execute a JET transform with the passed object as the root of the source model.
   * @param id the JET Transform id
   * @param source the source model root object.
   * @param variables A Map of variable names and their values. May be <code>null</code>.
   * @param monitor a progress monitor
   * @return the transform's execution status
   */
  public static IStatus runTransformOnObject(final String id, final Object source, Map variables, final IProgressMonitor monitor)
  {
    final JET2Context context = new JET2Context(source);
    try
    {
      if(variables != null)
      {
      context.setVariables(variables);
      }
      return runTransform(id, context, monitor);
    }
    catch (JET2TagException e)
    {
      return new Status(IStatus.ERROR, id, IStatus.OK, e.getMessage(), e);
    }
  }

  /**
   * Invoke a JET Transform on the pass resource, loading the resource using the transform's model loader.
   * @param id the JET Transform id
   * @param resource the Eclipse Resource to load
   * @param monitor a progress monitor
   * @return the transform's execution status
   */
  public static IStatus runTransformOnResource(final String id, final IResource resource, final IProgressMonitor monitor)
  {
    return runTransformOnResource(id, resource, null, monitor);
  }

  /**
   * Invoke a JET Transform on the pass resource, loading the resource using the transform's model loader.
   * @param id the JET Transform id
   * @param resource the Eclipse Resource to load
   * @param variables A Map of variable names and their values. May be <code>null</code>.
   * @param monitor a progress monitor
   * @return the transform's execution status
   */
  public static IStatus runTransformOnResource(final String id, final IResource resource, Map variables, final IProgressMonitor monitor)
  {
    try
    {
      final IJETBundleDescriptor descriptor = getJETBundleManager().getDescriptor(id);
      final JET2Context context = new JET2Context(null);
      
      if(variables != null)
      {
        context.setVariables(variables);
      }
      
      String loaderId = descriptor.getModelLoaderId();
      String fileType = descriptor.getModelExtension();
      
      WorkspaceContextExtender.loadResourceAsSource(context, resource, loaderId, fileType);

      return runTransform(id, context, monitor);
    }
    catch (JET2TagException e)
    {
      return new Status(IStatus.ERROR, id, IStatus.OK, e.getMessage(), e);
    }
    catch (IOException e)
    {
      // didn't work.
      return new Status(IStatus.ERROR, id, IStatus.OK, e.getLocalizedMessage(), e);
    }
    catch (CoreJETException e)
    {
      return new Status(IStatus.ERROR, id, IStatus.OK, e.getLocalizedMessage(), e);
    }
  }

  /**
   * Invoke a JET transform on the passed String representation of an XML model.
   * @param id the transform id
   * @param source the XML source, as a string
   * @param monitor a progress monitor
   * @return the transform execution status.
   */
  public static IStatus runTransformOnString(final String id, final String source, final IProgressMonitor monitor)
  {
    return runTransformOnString(id, source, "xml", null, monitor); //$NON-NLS-1$
  }
  
  /**
   * Invoke a JET transform on the passed String representation of an input model.
   * @param id the transform id
   * @param source the string respresentation of the input model.
   * @param kind the kind of model (file extension) the string represents.
   * @param monitor a progress monitor
   * @return the transform's execution status.
   */
  public static IStatus runTransformOnString(final String id, final String source, final String kind, final IProgressMonitor monitor) 
  {
    return runTransformOnString(id, source, kind, null, monitor);
  }

  /**
   * Invoke a JET transform on the passed String representation of an input model.
   * @param id the transform id
   * @param source the string respresentation of the input model.
   * @param kind the kind of model (file extension) the string represents.
   * @param variables A Map of variable names and their values. May be <code>null</code>.
   * @param monitor a progress monitor
   * @return the transform's execution status.
   */
  public static IStatus runTransformOnString(final String id, final String source, final String kind, Map variables, final IProgressMonitor monitor) 
  {
    IStatus result = Status.OK_STATUS;

    JET2Context context = new JET2Context(null);
    
    final IJETBundleDescriptor descriptor = getJETBundleManager().getDescriptor(id);
    String modelLoaderID = descriptor.getModelLoaderId();
    
    try
    {
      if(variables != null)
      {
        context.setVariables(variables);
      }
      final Object sourceObject = TransformContextExtender.loadModelFromString(source, modelLoaderID, kind);
      context.setSource(sourceObject);
      result = runTransform(id, context, monitor);
    }
    catch (JET2TagException e)
    {
      return new Status(IStatus.ERROR, id, IStatus.OK, e.getLocalizedMessage(), e);
    }
    catch (CoreJETException e)
    {
      return new Status(IStatus.ERROR, id, IStatus.OK, e.getLocalizedMessage(), e);
    }
    catch (IOException e)
    {
      result = InternalJET2Platform.newStatus(IStatus.ERROR, JET2Messages.JET2Platform_CouldNotParseString, e);
    }

    return result;
  }

  /**
   * Return the model loader manager.
   * @return the model loader manager.
   */
  public static ILoaderManager getModelLoaderManager()
  {
    return JETActivatorWrapper.INSTANCE.getLoaderManager();
  }
  /**
   * 
   */
  private JET2Platform()
  {
    super();
  }
}
