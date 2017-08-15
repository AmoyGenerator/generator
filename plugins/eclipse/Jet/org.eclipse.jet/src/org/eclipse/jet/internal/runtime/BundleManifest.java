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

import java.util.Dictionary;

import org.osgi.framework.Constants;

/**
 * A wrapper on a Bundle manifest.
 */
public class BundleManifest
{
  protected final Dictionary manifest;
  
  public BundleManifest(Dictionary manifest) throws NotABundleException
  {
    if(manifest.get(Constants.BUNDLE_SYMBOLICNAME) == null) {
      throw new NotABundleException(manifest);
    }
    this.manifest = manifest;
  }
  
  /**
   * @return
   */
  public String getSymbolicName()
  {
    final String rawId = (String)manifest.get(Constants.BUNDLE_SYMBOLICNAME);
    int index = rawId.indexOf(';');
    
    return index >= 0 ? rawId.substring(0, index) : rawId;
  }
  
  public String getName()
  {
    return (String)manifest.get(Constants.BUNDLE_NAME);
  }
  
  public String getVersion()
  {
    return (String)manifest.get(Constants.BUNDLE_VERSION);
  }
  
  public String getProvider()
  {
    return (String)manifest.get(Constants.BUNDLE_VENDOR);
  }
  
  
}
