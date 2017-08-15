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
 * $Id: JET2TemplateManager.java,v 1.2 2007/09/17 20:30:26 pelder Exp $
 */
package org.eclipse.jet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jet.transform.IJETBundleManager;
import org.osgi.framework.BundleException;

/**
 * Mananger for operations that dynamically invoke JET2 templates.
 * Operations implement {@link ITemplateOperation} and are run via {@link #run(String[], org.eclipse.jet.JET2TemplateManager.ITemplateOperation)}.
 * The operation may then dynamically load and execute templates found in the provided plug-ins via the passed {@link ITemplateRunner}.
 * <p>
 * Provisional API. This API is subject to breaking change or removal.
 * </p>
 */
public class JET2TemplateManager
{

  /**
   * Defines the protocol for an operation that dynamically invokes JET2 templates.
   * 
   * @see JET2TemplateManager#run(String[], JET2TemplateManager.ITemplateOperation)
   */
  public interface ITemplateRunner {
    /**
     * Execute a JET2 template
     * @param templatePath the template path
     * @param context the JET invocation context
     * @param out the JET2 writer
     * @throws IllegalArgumentException if templatePath cannot be resolved.
     */
    void generate(String templatePath, JET2Context context, JET2Writer out);
    
  }
  

  /**
   * Implementation of {@link ITemplateRunner}.
   */
  private static class TemplateRunnerImpl implements ITemplateRunner {

    private final JET2TemplateLoader[] loaders;
    public TemplateRunnerImpl(JET2TemplateLoader[] loaders)
    {
      this.loaders = loaders;
      
    }
    public void generate(String templatePath, JET2Context context, JET2Writer out)
    {
      JET2Template template = null;
      for (int i = 0; i < loaders.length && template == null; i++)
      {
        template = loaders[i].getTemplate(templatePath);
      }
      if(template == null) {
        throw new IllegalArgumentException();
      }
      
      template.generate(context, out);
    }
    
  }

  /**
   * Protocol for a dynamic template operation.
   * 
   * @see JET2TemplateManager#run(String[], JET2TemplateManager.ITemplateOperation)
   */
  public interface ITemplateOperation {
    /**
     * Run the template operation. The passed template runner provides the operation with access t
     * @param templateRunner 
     */
    void run(final ITemplateRunner templateRunner);
  }
  
  private JET2TemplateManager() {
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
   * @throws BundleException if one of the plug-ins cannot be loaded
   * @throws NullPointerException if pluginIDs is <code>null</code>, or if any element is <code>null</code>
   * @throws IllegalArgumentException if pluginIDs has zero length
   */
  public static void run(String[] pluginIDs, ITemplateOperation operation) throws BundleException
  {
    checkPlugins(pluginIDs);
    
    IJETBundleManager bundleManager = JET2Platform.getJETBundleManager();
    List connectedBundles = new ArrayList(pluginIDs.length);
    JET2TemplateLoader[] templateLoaders = new JET2TemplateLoader[pluginIDs.length];
    try {
      for (int i = 0; i < pluginIDs.length; i++)
      {
        String id = pluginIDs[i];
        bundleManager.connect(id, new NullProgressMonitor());
        connectedBundles.add(id);
        templateLoaders[i] = bundleManager.getTemplateLoader(id);
      }
      ITemplateRunner templateRunner = new TemplateRunnerImpl(templateLoaders);
      
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
