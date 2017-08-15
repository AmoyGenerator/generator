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

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.jet.internal.runtime.model.LoaderManager;
import org.eclipse.jet.runtime.model.ILoaderManager;

/**
 * A wrapper for the Eclipse Activator {@link InternalJET2Platform} that
 * permits Eclipse-free running of parts of JET.
 */
public class JETActivatorWrapper extends EMFPlugin
{

  private InternalJET2Platform plugin;
  
  private final ILoaderManager loaderManager;
    
  public static final JETActivatorWrapper INSTANCE = new JETActivatorWrapper();
  
  /**
   * 
   */
  private JETActivatorWrapper()
  {
    super(new ResourceLocator[] {});
    
    loaderManager = new LoaderManager();
  }

  /* (non-Javadoc)
   * @see org.eclipse.emf.common.EMFPlugin#getPluginResourceLocator()
   */
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  void setPlugin(InternalJET2Platform thePlugin)
  {
    plugin = thePlugin;
  }
  
  public InternalJET2Platform getPlugin()
  {
    return plugin;
  }
  
  public ILoaderManager getLoaderManager()
  {
    return loaderManager;
  }
}
