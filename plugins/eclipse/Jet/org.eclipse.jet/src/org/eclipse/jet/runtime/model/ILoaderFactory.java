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
package org.eclipse.jet.runtime.model;


/**
 * Factory for an IModelLoader instance.
 * 
 * <p>
 * Clients may choose to implement this class.
 * </p>
 */
public interface ILoaderFactory
{

  /**
   * Create a new instance of the model loader.
   * @return an IModelLoader instance or <code>null</code> if the loader cannot be created.
   */
  public abstract IModelLoader create();
}
