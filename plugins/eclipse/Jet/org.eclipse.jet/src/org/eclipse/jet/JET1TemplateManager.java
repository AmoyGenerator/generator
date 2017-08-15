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
 * $Id: JET1TemplateManager.java,v 1.1 2007/05/16 16:44:13 pelder Exp $
 */
package org.eclipse.jet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.transform.IJETBundleManager;
import org.osgi.framework.BundleException;

/**
 * Mananger for operations that dynamically invoke JET1 templates.
 * Manager for invoking dymanic JET1 templates. A replacement for (and simplification of) {@link org.eclipse.emf.codegen.jet.JETEmitter}.
 * Operations implement {@link ITemplateOperation} and are run via {@link #run(String[], org.eclipse.jet.JET1TemplateManager.ITemplateOperation, IProgressMonitor)}.
 * The operation may then dynamically load and execute templates found in the provided plug-ins via the passed {@link ITemplateRunner}.
 * <p>
 * Loads JET1 templates dynamically from Eclipse plug-ins and/or in workspace plug-in projects, so long
 * as the projects were compiled with the org.eclipse.jet compiler (rather than org.eclipse.emf.codegen JET compiler).
 * <p>
 * Provisional API. This API is subject to breaking change or removal.
 * </p>
 */
public class JET1TemplateManager
{

  /**
   * Defines the protocol for an operation that dynamically invokes JET1 templates.
   * 
   * @see JET1TemplateManager#run(String[], org.eclipse.jet.JET1TemplateManager.ITemplateOperation, IProgressMonitor)
   */
  public interface ITemplateRunner {
    /**
     * Execute a JET1 template
     * @param templatePath the template path
     * @param argument the template argument
     * @return the template result
     * @throws IllegalAccessException if templatePath cannot be resolved.
     */
    String generate(String templatePath, Object argument);
    
  }
  

  /**
   * Implementation of {@link ITemplateRunner}.
   */
  private static class JET1TemplateRunnerImpl implements ITemplateRunner {

    private final JET2TemplateLoader[] loaders;
    private final JET2Context context = new JET2Context(null);
    public JET1TemplateRunnerImpl(JET2TemplateLoader[] loaders)
    {
      this.loaders = loaders;
      
    }
    public String generate(String templatePath, Object argument)
    {
      JET2Template template = null;
      for (int i = 0; i < loaders.length && template == null; i++)
      {
        template = loaders[i].getTemplate(templatePath);
      }
      if(template == null) {
        throw new IllegalArgumentException();
      }
      
      BodyContentWriter writer = new BodyContentWriter();
      template.generate(context, writer);
      return writer.getContent();
    }
    
  }

  /**
   * Protocol for a dynamic template operation.
   * 
   * @see JET1TemplateManager#run(String[], org.eclipse.jet.JET1TemplateManager.ITemplateOperation, IProgressMonitor)
   */
  public interface ITemplateOperation {
    /**
     * Run the template operation. The passed template runner provides the operation with access t
     * @param templateRunner 
     */
    void run(final ITemplateRunner templateRunner);
  }
  
  private JET1TemplateManager() {
    // prevent instantiation
  }
  
  /**
   * @param pluginIDs
   */
  private static void checkPlugins(String[] pluginIDs)
  {
    if(pluginIDs == null) {
      throw new NullPointerException();
    }
    if(pluginIDs.length == 0) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < pluginIDs.length; i++)
    {
      if(pluginIDs[i] == null) {
        throw new IllegalArgumentException();
      }
    }
  }
  
  /**
   * Execute a JET templates that are loaded dynamically from the provided Eclipse plug-ins.
   * Each plug-in ID must be the ID of an already loaded plug-in, or of a plug-in in the 
   * Eclipse workspace. The plug-in list is search in order until a template is found.
   * @param pluginIDs a non-empty list of Eclipse plug-in ids.
   * @param operation an operation that will consume the templates
   * @param monitor a progress monitor
   * @throws BundleException if one of the plug-ins cannot be loaded
   * @throws NullPointerException if pluginIDs is <code>null</code>, or if any element is <code>null</code>
   * @throws IllegalArgumentException if pluginIDs has zero length
   */
  public static void run(String[] pluginIDs, ITemplateOperation operation, IProgressMonitor monitor) throws BundleException
  {
    checkPlugins(pluginIDs);
    
    IJETBundleManager bundleManager = JET2Platform.getJETBundleManager();
    List connectedBundles = new ArrayList(pluginIDs.length);
    JET2TemplateLoader[] templateLoaders = new JET2TemplateLoader[pluginIDs.length];
    try {
      for (int i = 0; i < pluginIDs.length; i++)
      {
        String id = pluginIDs[i];
        bundleManager.connect(id, new SubProgressMonitor(monitor, 1));
        connectedBundles.add(id);
        templateLoaders[i] = bundleManager.getTemplateLoader(id);
      }
      ITemplateRunner templateRunner = new JET1TemplateRunnerImpl(templateLoaders);
      
      operation.run(templateRunner);
      
    } finally {
      for (Iterator i = connectedBundles.iterator(); i.hasNext();)
      {
        String id = (String)i.next();
        bundleManager.disconnect(id);
      }
    }
  }
  
  
}
