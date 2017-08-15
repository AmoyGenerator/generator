/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.runtime;


import java.net.URL;

import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;


/**
 * Manage compiled JET2 template bundles, providing utilities for loading and unloading them
 * into the workbench..
 *
 */
public class JETBundleInstaller
{

  private BundleContext bundleContext = null;

  /**
   * 
   */
  public JETBundleInstaller()
  {
    super();
  }

  /**
   * Startup the manager. This method should be called only from the hosting plugin's startup method.
   * @param initBundleContext the hosting plugin's {@link BundleContext}, as supplied to the plugin startup method.
   */
  public void startup(BundleContext initBundleContext)
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    
    this.bundleContext = initBundleContext;
    
    timer.done();
  }

  /**
   * Shut down the manager. This method should be called only by the hosting plugin's shutdown method.
   *
   */
  public void shutdown()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "shutdown()"); //$NON-NLS-1$
    
    this.bundleContext = null;
    
    timer.done();
  }

  /**
   * Install an OSGi bundle.
   * @param bundleURL the external form of the URL of a bundle to install.
   * @return the OSGi bundle the loaded OSGi bundle
   * @throws BundleException if the bundle cannot be loaded.
   */
  public Bundle installBundle(URL bundleURL) throws BundleException
  {
    final Bundle bundle = bundleContext.installBundle(bundleURL.toExternalForm());
    
    // once the bundle is dynamically loaded, force the extension point managers
    // to refresh extensions in the loaded bundle.
    // OSGi asynchronously broadcasts registry updates, but this is not prompt enough,
    // and there is no mechanism for waiting for these
    // Calling getResource ensures that the registry information actually gets loaded...
    bundle.getResource("plugin.xml"); //$NON-NLS-1$
    InternalJET2Platform.getDefault().getXPathFunctionsManager().forceRefresh(bundle.getSymbolicName());
    InternalJET2Platform.getDefault().getTagLibManager().forceRefresh(bundle.getSymbolicName());

    return bundle;
  }

  /**
   * Uninstall a JET2 bundle
   * @param bundle the bundle to unload
   * @throws BundleException if the bundle cannot be unloaded
   */
  public void uninstallBundle(Bundle bundle) throws BundleException
  {
      bundle.uninstall();
  }
}
