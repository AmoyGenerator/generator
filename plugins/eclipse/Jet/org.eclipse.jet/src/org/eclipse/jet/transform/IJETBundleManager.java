/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.JET2TemplateLoader;
import org.osgi.framework.BundleException;


/**
 * Define the interface to the manager for loading/unloading and describing JET transform bundles.
 */
public interface IJETBundleManager
{

  /**
   * Return descriptions of all available JET transforms.
   * @return an possibily empty array.
   */
  public abstract IJETBundleDescriptor[] getAllJETBundleDescriptors();

  /**
   * Return the descriptor for a given JET transform.
   * @param id the transform (plug-in) id.
   * @return the descriptor or <code>null</code>.
   */
  public abstract IJETBundleDescriptor getDescriptor(String id);

  /**
   * Return the descriptor for the give JET project
   * @param name the project name.
   * @return the descriptor or <code>null</code>.
   */
  public abstract IJETBundleDescriptor getDescriptorForProject(String name);

  /**
   * Return the Workspace project name for the given JET transform id. If the transform does
   * not reside in a Workspace project, then null is returned.
   * @param id the transform id
   * @return the project name or <code>null</code>.
   */
  public abstract String getProjectForId(String id);

  /**
   * Load the specified JET transform, execute an {@link IJETRunnable}, and then ensure the transform is unloaded again.
   * @param id the transform id
   * @param runnable a runnable
   * @param monitor a progress monitor
   * @throws BundleException TODO
   */
  public abstract void run(String id, IJETRunnable runnable, IProgressMonitor monitor) throws BundleException;
  
  /**
   * Record a connection to the specified JET transform bundle. If this is the first connection, the bundle is
   * loaded.
   * @param id the transform id
   * @param monitor a progress monitor
   * @throws BundleException TODO
   */
  public abstract void connect(String id, IProgressMonitor monitor) throws BundleException;
  
  /**
   * Record a disconnection fromt he specified JET transform bundle. If this is the last connection, the bundle
   * is unloaded.
   * @param id the transform id
   */
  public abstract void disconnect(String id);
  
  /**
   * Return the {@link JET2TemplateLoader} instance for the specified JET Transform. A call to this method
   * must be bracketed by calls to {@link #connect(String, IProgressMonitor)} and {@link #disconnect(String)}.
   * @param id the JET transform
   * @return the template loader instance
   * @throws BundleException TODO
   */
  public abstract JET2TemplateLoader getTemplateLoader(String id) throws BundleException;

  /**
   * Return an array of transform available ids.
   * @return a possibly empty array of strings.
   */
  public abstract String[] getAllTransformIds();

}
