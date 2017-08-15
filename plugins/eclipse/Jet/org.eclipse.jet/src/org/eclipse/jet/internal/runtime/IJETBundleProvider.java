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

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Interface for JETBundle providers managed by {@link JETBundleManager}.
 */
public interface IJETBundleProvider
{

  public abstract IJETBundleDescriptor getDescriptor(String id);
  
  public abstract Set getAllJETBundleIds();
  
  public abstract Collection getAllJETBundleDescriptors();
  
  public abstract void startup();
  
  public abstract void shutdown();
  
  public abstract Bundle load(String id, IProgressMonitor monitor) throws BundleException;
  
  /**
   * @deprecated
   * @param id
   * @throws BundleException
   */
  public abstract void unload(String id) throws BundleException;
  
  public abstract void unload(Bundle bundle) throws BundleException;
}
