/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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

package org.eclipse.jet.transform;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2TemplateLoader;
import org.eclipse.jet.JET2TemplateLoaderExtension;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.TagFactoryImpl;
import org.eclipse.jet.internal.runtime.model.TransformLoadContext;
import org.eclipse.jet.internal.runtime.model.WorkspaceLoadContext;
import org.eclipse.jet.runtime.model.ILoadContext;
import org.eclipse.jet.runtime.model.IModelLoader;
import org.eclipse.jet.taglib.JET2TagException;
import org.osgi.framework.BundleException;


/**
 * Extender to the JET2Context for supporting JET2 Transforms
 *
 */
public final class TransformContextExtender
{

  private static String PRIVATE_CONTEXT_DATA_KEY = TransformContextExtender.class.getName();
  private final org.eclipse.jet.transform.TransformContextExtender.ContextData contextData;
  private final JET2Context context;
  private static final class ContextData
  {

    public final Stack templateStack = new Stack();

    public JET2TemplateLoader loader;

    public List listeners = new ArrayList();

    public Object shellContext = null;

    public IJETBundleDescriptor descriptor = null;
    
    public final List connectedTransforms = new ArrayList();

    public IProgressMonitor monitor;
    
  }

  /**
   * Create a TransformContextExtender for the current context.
   * @param context the context.
   * @deprecated Since 0.9.0 use {@link TransformContextExtender#getInstance(JET2Context)}.
   */
  public TransformContextExtender(JET2Context context)
  {
    this(context, getInstance(context).contextData);
  }

  private TransformContextExtender(JET2Context context, ContextData contextData)
  {
    this.context = context;
    this.contextData = contextData;
    if(context.getTagFactory() == null) {
      context.setTagFactory(new TagFactoryImpl(context));
    }
  }

  public String getId()
  {
    return contextData.descriptor.getId();
  }

  /**
   * Return the path of the current executing template
   * @return the current template path or the empty string if there is not currently executing template
   */
  public String getTemplatePath()
  {
    return contextData.templateStack.isEmpty() ? "" : (String)contextData.templateStack.peek(); //$NON-NLS-1$
  }

  /**
   * Execute the named template, writing all template output to the passed writer.
   * Equivalent to execute(templatePath, false, writer).
   * @param templatePath the project relative path of the template to load
   * @param writer the writer to which the template output will be written
   * @throws JET2TagException if an execution error occurs
   * @see #execute(String, boolean, JET2Writer)
   */
  public void execute(String templatePath, JET2Writer writer) throws JET2TagException
  {
    execute(templatePath, false, writer);
  }
  
  
  /**
   * Execute the named template, writing all template output to the passed writer.
   * If the passed progress monitor is canceled, an {@link OperationCanceledException} will
   * be thrown.
   * @param templatePath the project relative path of the template to load
   * @param useSuper if true, attempt to load the template from override transformation, if it exists
   * @param writer the writer to which the template output will be written
   * @param monitor A progress monitor
   * @throws JET2TagException if an execution error occurs or the template cannot be found
   * @throws OperationCanceledException if the monitor is canceled.
   */
  private void executeWithMonitor(String templatePath, boolean useSuper, JET2Writer writer, IProgressMonitor monitor) throws JET2TagException
  {
    final IProgressMonitor savedMonitor = this.contextData.monitor;
    this.contextData.monitor = monitor;
    try {
      monitor.beginTask(JET2Messages.JET2Platform_ExecutingTemplates, IProgressMonitor.UNKNOWN);
      monitor.setTaskName(JET2Messages.JET2Platform_ExecutingTemplates);
      monitor.subTask(templatePath);
      doExecute(templatePath, useSuper, writer);
      checkCanceled();
    } finally {
      monitor.done();
      this.contextData.monitor = savedMonitor;
    }
  }

  /**
   * Check whether the transformation has been canceled and throw a {@link OperationCanceledException}
   * if so.
   * 
   */
  public void checkCanceled() {
    if(this.contextData.monitor != null && this.contextData.monitor.isCanceled()) {
      throw new OperationCanceledException();
    }
  }
  
  /**
   * Test whether the transformation has been canceled.
   * @return <code>true</code> if the transformation has been canceled, <code>false</code> otherwise
   */
  public boolean isCanceled() {
    return this.contextData.monitor != null && this.contextData.monitor.isCanceled();
  }
  
  /**
   * Provide a progress monitor for template execution. If provided, templates are executed with
   * a progress monitor and may through an {@link OperationCanceledException}.
   * Note that clients typically do not need to call this method as it is initialized from
   * {@link JET2Platform#runTransform(String, JET2Context, IProgressMonitor)} and its variants.
   * @param monitor a progress monitor
   * @throws IllegalStateException if a progress monitor has already been installed
   * @see #execute(String, JET2Writer)
   * @see #execute(String, boolean, JET2Writer)
   * @see #checkCanceled()
   * @see #isCanceled()
   */
  public void setProgressMonitor(IProgressMonitor monitor) {
    if(this.contextData.monitor != null) { 
      throw new IllegalStateException();
    }
    this.contextData.monitor = monitor;    
  }
  
  /**
   * Execute the named template, writing all template output to the passed writer.
   * If the transformation context executing this method has passed a progress monitor, then an
   * appropriate subprogress monitor will be created for this template.
   * @param templatePath the project relative path of the template to load
   * @param useSuper if true, attempt to load the template from override transformation, if it exists
   * @param writer the writer to which the template output will be written
   * @throws JET2TagException if an execution error occurs or the template cannot be found
   * @see #setProgressMonitor(IProgressMonitor)
   */
  public void execute(String templatePath, boolean useSuper, JET2Writer writer) throws JET2TagException
  {
    if(this.contextData.monitor != null) {
      executeWithMonitor(templatePath, useSuper, writer, new SubProgressMonitor(this.contextData.monitor, 1));
    } else {
      doExecute(templatePath, useSuper, writer);
    }
    
  }

  /**
   * @param templatePath
   * @param useSuper
   * @param writer
   * @throws JET2TagException
   */
  private void doExecute(String templatePath, boolean useSuper, JET2Writer writer) throws JET2TagException
  {
    contextData.templateStack.push(templatePath);
    context.setTemplatePath(getTemplatePath());
    try
    {
      JET2TemplateLoader templateLoader = contextData.loader;
      if(useSuper) {
        templateLoader = templateLoader instanceof JET2TemplateLoaderExtension ? ((JET2TemplateLoaderExtension)templateLoader).getDelegateLoader() : null;
      }
      JET2Template template = templateLoader != null ? templateLoader.getTemplate(templatePath) : null;

      if (template == null)
      {
        String msg = JET2Messages.JET2Context_CouldNotFindTemplate;
        throw new JET2TagException(MessageFormat.format(msg, new Object []{ templatePath }));
      }
      template.generate(context, writer);
    }
    finally
    {
      contextData.templateStack.pop();
      context.setTemplatePath(getTemplatePath());
    }
  }

  public JET2TemplateLoader getLoader()
  {
    return contextData.loader;
  }

  /**
   * Set the template loader
   * @param loader
   */
  public void setLoader(JET2TemplateLoader loader)
  {
    if (contextData.loader != null)
    {
      throw new IllegalStateException("loader already set"); //$NON-NLS-1$
    }
    contextData.loader = loader;
  }

  /**
   * Add a listener to the merge context events (commit, logStatistics,
   * getSummary)
   * 
   * @param listener
   */
  public void addListener(TransformContextListener listener)
  {
    if (!contextData.listeners.contains(listener))
    {
      contextData.listeners.add(listener);
    }
  }

  /**
   * Remove a listener of merge context events.
   * 
   * @see #addListener(TransformContextListener)
   * @param listener
   */
  public void removeListener(TransformContextListener listener)
  {
    contextData.listeners.remove(listener);
  }

  /**
   * Commit changes.
   * @param monitor a progress monitor
   *
   */
  public void commit(IProgressMonitor monitor)
  {
    monitor.beginTask(JET2Messages.JET2Context_CommittingActions, contextData.listeners.size());
    try
    {
      for (Iterator i = contextData.listeners.iterator(); i.hasNext();)
      {
        checkCanceled();
        TransformContextListener listener = (TransformContextListener)i.next();

        try
        {
          listener.commit(context, new SubProgressMonitor(monitor, 1));
        }
        catch (JET2TagException e)
        {
          context.logError(e);
        }
        monitor.worked(1);
      }

    }
    finally
    {
      monitor.done();
    }
  }

  /**
   * Return the current shell context or <code>null</code> if none is set
   * @return Returns the shellContext.
   * @see #getShellContext()
   */
  public final Object getShellContext()
  {
    return contextData.shellContext;
  }

  /**
   * Set the <code>org.eclipse.swt.widgets.Shell</code> in which any dialogs
   * should be open. If <code>null</code> is passed, or if this method is
   * not called, then no dialogs will be displayed, and the transformation
   * will make suitable choices in lieu of displaying a dialog.
   * <p> 
   * An <code>Object</code>
   * is passed to avoid dependencies on the SWT plugins. This method is used
   * primarily to support the use of {@link org.eclipse.core.resources.IWorkspace#validateEdit(org.eclipse.core.resources.IFile[], java.lang.Object)}
   * although the shell context may be used by other task, too.
   * @param shellContext The shellContext to set, which must be <code>null</code>
   * or an instance of org.eclipse.swt.widgets.Shell.
   * 
   */
  public final void setShellContext(Object shellContext)
  {
    if (contextData.shellContext != null)
    {
      throw new IllegalStateException("shellContext already set"); //$NON-NLS-1$
    }
    contextData.shellContext = shellContext;
  }

  /**
   * Set the description of the transform bundle.
   * @param descriptor the bundle descriptor.
   * @throws IllegalStateException if the bundle descriptor has already been set.
   */
  public void setBundleDescriptor(IJETBundleDescriptor descriptor)
  {
    if (contextData.descriptor != null)
    {
      throw new IllegalStateException();
    }
    contextData.descriptor = descriptor;
    context.setJETBundleId(descriptor.getId());
  }
  
  /**
   * Return the bundle descriptor of the JET transform bundle.
   * @return a bundle descriptor
   */
  public IJETBundleDescriptor getBundleDescriptor()
  {
    return contextData.descriptor;
  }
  
  /**
   * Return the base URL to use given an urlContext constant. The supported URL contexts
   * are:
   * <bl>
   * <li><code>"transform"</code> - relative to the currently executing transform</li>
   * <li><code>"workspace"</code> - relative to the current eclipse workspace</li>
   * <li><code>null</code> - equivalent to <code>"transform"</code></li>
   * </bl>
   * @param urlContext one of "transform", "workspace" or <code>null</code>
   * @return the base URL
   * @throws JET2TagException
   */
  public URL getBaseURL(String urlContext) throws JET2TagException
  {
    ILoadContext definedContext = null;
    if (urlContext == null || "transform".equalsIgnoreCase(urlContext)) { //$NON-NLS-1$
      try
      {
        definedContext = new TransformLoadContext(getBundleDescriptor().getBaseURL());
      }
      catch (MalformedURLException e)
      {
        throw new JET2TagException(e);
      }
    }
    else if ("workspace".equalsIgnoreCase(urlContext)) { //$NON-NLS-1$
      definedContext = new WorkspaceLoadContext();
    }
    else
    {
      final String msg = JET2Messages.LoadTag_UnknownSrcContext;
      throw new JET2TagException(MessageFormat.format(msg, new Object []{ urlContext }));
    }

    URL baseURL = definedContext.getContextUrl();
    return baseURL;
  }

  /**
   * Return the TransformContextExtender for the passed context.
   * @param context a JET context
   * @return the TransformContextExtender
   */
  public static TransformContextExtender getInstance(JET2Context context)
  {
    if(context == null) {
      throw new NullPointerException();
    }
    TransformContextExtender ex = (TransformContextExtender)context.getPrivateData(PRIVATE_CONTEXT_DATA_KEY);
    if (ex == null)
    {
      ex = new TransformContextExtender(context, new ContextData());
      context.addPrivateData(PRIVATE_CONTEXT_DATA_KEY, ex);
    }
    return ex;
  }

  /**
   * Load a model, given an URL to a model, and optionally the model loader and the file type of the model.
   * @param modelURL the URL of the model to load
   * @param modelLoaderID the model loader id, or <code>null</code> if the model loader is to be determined
   * from the file type.
   * @param fileType the file type to use in selecting the model loader, or <code>null</code> if the file type
   * is to be extracted from the model URL (by finding a file extension).
   * @return the root of the loaded model
   * @throws IOException if the model could not be read.
   * @throws CoreJETException if an appropriate model loader could not be found.
   */
  public static Object loadModel(final URL modelURL, String modelLoaderID, String fileType) 
  throws IOException, CoreJETException
  {
    final IModelLoader loader = JETActivatorWrapper.INSTANCE.getLoaderManager().getLoader(modelURL.toExternalForm(), modelLoaderID, fileType);
    Object source = fileType == null ? loader.load(modelURL) : loader.load(modelURL, fileType);
    return source;
  }

  /**
   * Load a model from a string form.
   * @param modelContent the model content, in its string form.
   * @param modelLoaderID the model loader id, or <code>null</code> if the loader is to be calculated from
   *  the file type.
   * @param fileType the type of the file to load, or <code>null</code> if the type is irrelevant to the loader.
   * @return the root of the loaded model
   * @throws CoreJETException if an appropriate model loader could not be found.
   * @throws IOException if the model could not be read.
   */
  public static Object loadModelFromString(String modelContent, String modelLoaderID, String fileType) 
  throws CoreJETException, IOException
  {
    final IModelLoader loader = JETActivatorWrapper.INSTANCE.getLoaderManager()
        .getLoader(null, modelLoaderID, fileType);
    Object source = loader.loadFromString(modelContent, fileType);
    return source;
    
  }

  public void setOverride(String id, IProgressMonitor monitor) throws JET2TagException
  {
    final IJETBundleManager bundleManager = JET2Platform.getJETBundleManager();
    try
    {
      bundleManager.connect(id, monitor != null ? monitor : new NullProgressMonitor());
      contextData.connectedTransforms.add(id);
      final JET2TemplateLoader delegateLoader = bundleManager.getTemplateLoader(id);
      
      final JET2TemplateLoader loader = getLoader();
      if(loader instanceof JET2TemplateLoaderExtension)
      {
        JET2TemplateLoaderExtension ext = (JET2TemplateLoaderExtension)loader;
        ext.setDelegateLoader(delegateLoader);
      }
      else
      {
        final String msg = JET2Messages.TransformContextExtender_NeedsRebuildForOverride;
        throw new JET2TagException(MessageFormat.format(msg, new Object[] {getId()}));
      }
    }
    catch (BundleException e)
    {
      throw new JET2TagException(e);
    }
  }

  public void cleanup()
  {
    // clean-up load transforms...
    final IJETBundleManager bundleManager = JET2Platform.getJETBundleManager();
    for (Iterator i = contextData.connectedTransforms.iterator(); i.hasNext();)
    {
      String id = (String)i.next();
      bundleManager.disconnect(id);
    }
  }
  
  /**
   * Invoke another JET transformation
   * @param id the JET transformation ID
   * @throws JET2TagException if an execution error occurs
   */
  public void runSubTransform(String id) throws JET2TagException
  {
    runSubTransform(id, new BodyContentWriter());
  }

  /**
   * Invoke another JET transformation, recording the main template results in the specified writer
   * @param id the JET transformation ID
   * @param writer a template writer
   * @throws JET2TagException if an execution error occurs
   */
  public void runSubTransform(String id, final BodyContentWriter writer) throws JET2TagException
  {
    final IJETBundleManager bundleManager = JET2Platform.getJETBundleManager();
    final IJETBundleDescriptor savedDescriptor = contextData.descriptor;
    final JET2TemplateLoader savedLoader = contextData.loader;
    try
    {
      bundleManager.connect(id, new NullProgressMonitor());
      contextData.connectedTransforms.add(id);
      final JET2TemplateLoader loader = bundleManager.getTemplateLoader(id);
      final IJETBundleDescriptor descriptor = bundleManager.getDescriptor(id);
      
      contextData.loader = loader;
      contextData.descriptor = descriptor;
      
      if(descriptor.getOverridesId() != null)
      {
        setOverride(descriptor.getOverridesId(), new NullProgressMonitor());
      }
      
      execute(descriptor.getMainTemplate(), writer);

    }
    catch (BundleException e)
    {
      throw new JET2TagException(e);
    }
    finally
    {
      contextData.descriptor = savedDescriptor;
      contextData.loader = savedLoader;
    }
  }
  
}
