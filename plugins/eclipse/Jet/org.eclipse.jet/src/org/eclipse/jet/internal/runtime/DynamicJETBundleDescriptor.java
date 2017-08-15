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
package org.eclipse.jet.internal.runtime;

import java.net.URL;

import org.eclipse.jet.internal.extensionpoints.TransformData;

/**
 * Bundle descriptor for a dynmically loadable JET Bundle.
 */
public class DynamicJETBundleDescriptor extends JETBundleDescriptor
{

  private final URL bundleURL;

  public DynamicJETBundleDescriptor(JETBundleManifest manifest, TransformData transformData, URL jarURL, URL bundleURL)
  {
    super(manifest, transformData, jarURL);
    this.bundleURL = bundleURL;
  }

  /**
   * @return Returns the bundleURL.
   */
  public final URL getBundleURL()
  {
    return bundleURL;
  }

}
