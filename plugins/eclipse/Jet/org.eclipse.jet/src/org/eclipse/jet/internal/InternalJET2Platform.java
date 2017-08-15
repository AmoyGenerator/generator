/**
 * <copyright>
 *
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.core.ContextLogEntryFactoryManager;
import org.eclipse.jet.internal.core.equinox.EquinoxContextLogEntryFactory;
import org.eclipse.jet.internal.core.expressions.EmbeddedExpressionLanguageManager;
import org.eclipse.jet.internal.extensionpoints.ModelInspectorsManager;
import org.eclipse.jet.internal.extensionpoints.ModelLoaderExtManager;
import org.eclipse.jet.internal.extensionpoints.PluginProjectMonitor;
import org.eclipse.jet.internal.extensionpoints.WorkspaceTagLibraryManager;
import org.eclipse.jet.internal.extensionpoints.XPathFunctionsManager;
import org.eclipse.jet.internal.runtime.JETBundleInstaller;
import org.eclipse.jet.internal.runtime.JETBundleManager;
import org.eclipse.jet.internal.taglib.InternalTagLibManager;
import org.eclipse.jet.internal.xpath.EmbeddedXPath;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.transform.IJETBundleManager;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class to be used in the desktop.
 */
public class InternalJET2Platform extends EMFPlugin.EclipsePlugin
{

  /**
   * Protocol for startup/shutdown method timer
   */
  public interface IMethodTimer {
    public void done();
  }
  
  /**
   * Implementation of method timer
   */
  private static class MethodTimer implements IMethodTimer {
    
    private final Class clazz;
    private final String methodName;
    private final long startMilliseconds;

    public MethodTimer(Class clazz, String methodName) {
      this.clazz = clazz;
      this.methodName = methodName;
      this.startMilliseconds = System.currentTimeMillis();
      System.out.println(clazz.getName() + "." + methodName + " - entering at " + startMilliseconds); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void done()
    {
      long endMillis = System.currentTimeMillis();
      long millis = endMillis - startMilliseconds;
      System.out.println(clazz.getName() + "." + methodName + " - exiting at " + endMillis + " - " + millis + "ms"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
  }
  
  /**
   * A do nothing method timer
   */
  private static final IMethodTimer NULL_METHOD_TIMER =  new IMethodTimer() {

    public void done(){}
    
  };

  /**
   * When <code>true</code>, indicates that startup/shutdown tracing is active
   */
  private static boolean DEBUG_STARTUP = Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/startup")).booleanValue(); //$NON-NLS-1$

  //The shared instance.
  private static InternalJET2Platform plugin;

  private InternalTagLibManager tagLibManager = null;

  private JETBundleInstaller jetBundleInstaller = null;

  private XPathFunctionsManager xpathFunctionsManager = null;
  
  private ModelLoaderExtManager modelLoaderExtManager = null;
  
  private ModelInspectorsManager modelInspectorExtManager = null;
  
  private JETBundleManager bundleManager = null;
 
  private PluginProjectMonitor pluginProjectMonitor = null;
  
  private WorkspaceTagLibraryManager workspaceTagLibraryManager = null;
  
  private SavedStateManager savedStateManager = null;

  private BundleContext bundleContext;

  /**
   * Return a method timer that is active only when DEBUG_STARTUP is true.
   * @param clazz
   * @param methodName
   * @return
   */
  public static IMethodTimer getStartupMethodTimer(Class clazz, String methodName) {
    if(DEBUG_STARTUP) {
      return new MethodTimer(clazz, methodName);
    } else {
      return NULL_METHOD_TIMER;
    }
  }

  /**
   * Returns the shared instance.
   * @return the plugin instance
   */
  public static InternalJET2Platform getDefault()
  {
    return plugin;
  }

  /**
   * Write a message of the supplied severity to the Eclipse log
   * @param severity
   * @param msg
   * @param e
   */
  private static void log(int severity, String msg, Exception e)
  {
    getDefault().getLog().log(newStatus(severity, msg, e));
  }

  /**
   * Write an status object to the Eclipse error log.
   * @param result
   */
  public static void log(IStatus result)
  {
    getDefault().getLog().log(result);

  }

  /**
   * Write an error message to the Eclipse log
   * @param msg
   */
  public static void logError(String msg)
  {
    logError(msg, null);
  }

  /**
   * Write an error message to the Eclipse log
   * @param msg
   * @param e
   */
  public static void logError(String msg, Exception e)
  {
    log(IStatus.ERROR, msg, e);
  }

  /**
   * Write an info message to the Eclipse log.
   * @param msg
   */
  public static void logInfo(String msg)
  {
    logInfo(msg, null);
  }

  /**
   * Write an info message to the Eclipse log
   * @param msg
   * @param e
   */
  public static void logInfo(String msg, Exception e)
  {
    log(IStatus.INFO, msg, e);
  }

  /**
   * Write an error message to the Eclipse log
   * @param msg
   */
  public static void logWarning(String msg)
  {
    logWarning(msg, null);
  }

  /**
   * Log a warning message to the Eclipse log
   * @param msg a message, may be <code>null</code>.
   * @param e an exeception, may be <code>null</code>.
   */
  public static void logWarning(String msg, Exception e)
  {
    log(IStatus.WARNING, msg, e);
  }

  /**
   * Create a new IStatus wrapping the passed exception & message. The status refers to the JET2 core
   * plugin.
   * @param severity one of the {@link IStatus} severity codes.
   * @param msg the error message.
   * @param e the exception, may be <code>null</code>.
   * @return the new Status
   */
  public static IStatus newStatus(int severity, String msg, Exception e)
  {
    return new Status(severity, JET2Platform.PLUGIN_ID, IStatus.OK, msg == null ? "" : msg, e); //$NON-NLS-1$
  }

  /**
   * The constructor.
   */
  public InternalJET2Platform()
  {
    plugin = this;
    
    JETActivatorWrapper.INSTANCE.setPlugin(this);
  }

  /**
   * Return the location where the compiled bundles corresponding to JET2 workspace projects are stored.
   * @return the location (absolute path) of the compiled bundles.
   */
  public IPath getCompiledBundleLocation()
  {
    IPath stateLocation = getStateLocation();
    IPath compiledBundleLocation = stateLocation.append("compiledBundles"); //$NON-NLS-1$
    compiledBundleLocation.toFile().mkdirs();
    return compiledBundleLocation;
  }

  /**
   * Return tag library meta information for the specified tag library id.
   * @param id a tag library id (plugin id + "." + tag library id)
   * @return the tab library data or <code>null</code> if the id was not found.
   */
  public TagLibrary getTagLibrary(String id)
  {
    checkTagLibManager();

    return tagLibManager.getTagLibrary(id);
  }

  /**
   * Return tag library meta information for the specified tag library id. Only tag libraries
   * declared in open plug-in projects in the workspace are considered.
   * @param id a tag library id (plugin id + "." + tag library id)
   * @return the tab library data or <code>null</code> if the id was not found.
   */
  public TagLibrary getWorkspaceTagLibrary(String id)
  {
    checkWorkspaceTagLibraryManager();

    return workspaceTagLibraryManager.getTagLibrary(id);
  }
  
  /**
   * Return the project defining the tag library.
   * @param id the tag library id
   * @return the project or <code>null</code> if the tag library is not defined by a workspace project.
   */
  public IProject getProjectDefiningTagLibrary(String id)
  {
    checkWorkspaceTagLibraryManager();

    return workspaceTagLibraryManager.getProjectDefiningTagLibrary(id);
  }
  /**
   * Return the manager of transform bundles (compiled JET2 tranformations).
   * @return the transform bundle manager
   */
  public JETBundleInstaller getJETBundleInstaller()
  {
    checkJETBundleInstaller();

    return jetBundleInstaller;
  }

  /**
   * This method is called upon plug-in activation
   * @param context the OSGi bundle context
   * @throws Exception if the plugin cannot start up
   */
  public void start(BundleContext context) throws Exception
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "start(BundleContext)"); //$NON-NLS-1$
    
    super.start(context);

    this.bundleContext = context;
    
    EmbeddedExpressionLanguageManager.getInstance().addLanguage("jet.xpath", new EmbeddedXPath()); //$NON-NLS-1$

    // start the model inspectors early, they are used by other managers...
    modelInspectorExtManager = new ModelInspectorsManager();
    modelInspectorExtManager.startup();
    
    // start the model loader early, it is used by other managers...
    modelLoaderExtManager = new ModelLoaderExtManager();
    modelLoaderExtManager.startup();

    ContextLogEntryFactoryManager.setFactory(new EquinoxContextLogEntryFactory(Platform.getAdapterManager()));

    timer.done();
  }

  private synchronized void checkJETBundleInstaller()
  {
    if(jetBundleInstaller == null) {
      jetBundleInstaller = new JETBundleInstaller();
      jetBundleInstaller.startup(bundleContext);
    }
  }

  private synchronized void checkSavedStateManager()
  {
    if(savedStateManager == null) {
      savedStateManager = new SavedStateManager(this);
      savedStateManager.startup();
    }
  }

  private synchronized void checkWorkspaceTagLibraryManager()
  {
    if(workspaceTagLibraryManager == null) {
      pluginProjectMonitor = new PluginProjectMonitor();
      workspaceTagLibraryManager = new WorkspaceTagLibraryManager();
      workspaceTagLibraryManager.startup(pluginProjectMonitor);
      pluginProjectMonitor.startup();
    }
  }

  private synchronized void checkBundleManager()
  {
    if(bundleManager == null) {
      bundleManager = new JETBundleManager();
      bundleManager.startup();
    }
  }

  private synchronized void checkTagLibManager()
  {
    if(tagLibManager == null) {
      tagLibManager = new InternalTagLibManager();
      tagLibManager.startup();
    }
  }

  private synchronized void checkXPathFunctionManager()
  {
    if(xpathFunctionsManager == null) {
      // start the XPath function extensions early, they are used by other managers...
      xpathFunctionsManager = new XPathFunctionsManager();
      xpathFunctionsManager.startup();
    }
  }

  /**
   * This method is called when the plug-in is stopped
   * @param context the OSGi bundle context
   * @throws Exception if the plugin cannot shutdown
   */
  public synchronized void stop(BundleContext context) throws Exception
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "stop(BundleContext)"); //$NON-NLS-1$
    
    if(tagLibManager != null) {
      tagLibManager.shutdown();
      tagLibManager = null;
    }

    if(jetBundleInstaller != null) {
      jetBundleInstaller.shutdown();
      jetBundleInstaller = null;
    }
    
    if(xpathFunctionsManager != null) {
      xpathFunctionsManager.shutdown();
      xpathFunctionsManager = null;
    }
    
    if(modelLoaderExtManager != null) {
      modelLoaderExtManager.shutdown();
      modelLoaderExtManager = null;
    }

    if(modelInspectorExtManager != null) {
      modelInspectorExtManager.shutdown();
      modelInspectorExtManager = null;
    }
    
    if(bundleManager != null) {
      bundleManager.shutdown();
      bundleManager = null;
    }
    
    if(workspaceTagLibraryManager != null) {
      workspaceTagLibraryManager.shutdown();
      workspaceTagLibraryManager = null;
    }
    if(pluginProjectMonitor != null) {
      pluginProjectMonitor.shutdown();
      pluginProjectMonitor = null;
    }

    if(savedStateManager != null) {
      savedStateManager.shutdown();
      savedStateManager = null;
    }

    // shut down the plug-in itself last
    super.stop(context);
    plugin = null;

    timer.done();
  }

  /**
   * Utility function for writing Debug output
   * @param issuer the issuer of the message, usually <code>this</code>, but may
   * also be a Class object.
   * @param message the debug message.
   */
  public static void debugMessage(Object issuer, String message)
  {
    Class msgClass = null;
    if (issuer instanceof Class)
    {
      msgClass = (Class)issuer;
    }
    else
    {
      msgClass = issuer.getClass();
    }

    System.err.println(msgClass.getName() + ": " + message); //$NON-NLS-1$
  }

  public String[] getKnownTagLibraryIds()
  {
    checkTagLibManager();

    return tagLibManager.getKnownTagLibraryIds();
  }

  /**
   * @return Returns the xpathFunctionsManager.
   */
  public final XPathFunctionsManager getXPathFunctionsManager()
  {
    checkXPathFunctionManager();
    
    return xpathFunctionsManager;
  }

  /**
   * @return Returns the tagLibManager.
   */
  public final InternalTagLibManager getTagLibManager()
  {
    checkTagLibManager();

    return tagLibManager;
  }
  
  /**
   * @return Returns the bundleManager.
   */
  public final IJETBundleManager getBundleManager()
  {
    checkBundleManager();
    
    return bundleManager;
  }

  public final SavedStateManager getSavedStateManager()
  {
    checkSavedStateManager();
    
    return savedStateManager;
  }
}
