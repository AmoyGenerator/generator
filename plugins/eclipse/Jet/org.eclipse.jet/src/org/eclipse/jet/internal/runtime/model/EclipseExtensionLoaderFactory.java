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
package org.eclipse.jet.internal.runtime.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.runtime.model.ILoaderFactory;
import org.eclipse.jet.runtime.model.IModelLoader;

/**
 * An implementation of {@link ILoaderFactory} that delegates to the Eclipse extension
 * registry to create the factory.
 */
public class EclipseExtensionLoaderFactory implements ILoaderFactory
{

  private final IConfigurationElement configElement;
  private final String loaderProperty;

  /**
   * 
   */
  public EclipseExtensionLoaderFactory(IConfigurationElement configElement, String loaderProperty)
  {
    this.configElement = configElement;
    this.loaderProperty = loaderProperty;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.ILoaderFactory#create()
   */
  public IModelLoader create()
  {
    try
    {
      return (IModelLoader)configElement.createExecutableExtension(loaderProperty);
    }
    catch (CoreException e)
    {
      InternalJET2Platform.getDefault().getLog().log(e.getStatus()); 
      return null;
    }
  }

}
