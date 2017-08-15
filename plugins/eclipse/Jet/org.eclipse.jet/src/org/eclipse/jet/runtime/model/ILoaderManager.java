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

import org.eclipse.jet.CoreJETException;

/**
 * Interface to the manager for model loaders.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface ILoaderManager
{
  /**
   * Add the named loader to the manager
   * @param id the loader's unique identifier string
   * @param name a descriptive name of the loader
   * @param factory a factory for creating an instance of the loader.
   * @param dynamic whether the loader supports dynamic matching of file types by implementing {@link IModelLoader#canLoad(String)}.
   */
  public abstract void addLoader(String id, String name, ILoaderFactory factory, boolean dynamic);
  
  /**
   * Remove a loader from the manager. If the loader is managed by this manager
   * then this method has no effect.
   * @param id the unique identifier of the loader to remove.
   */
  public abstract void removeLoader(String id);

  /**
   * Define a specific loader as the default model loader for a file type.
   * @param fileType the file type (extension) for which the loader is the default
   * @param id the id of the default loader
   * @throws IllegalStateException if the file type already has a default loader assigned to it.
   */
  public abstract void setDefaultLoader(String fileType, String id) throws IllegalStateException;
  
  /**
   * Clear the default loader (if any) associated with the file type.
   * @param fileType the file type(extension) for which the loader is the default.
   */
  public abstract void clearDefaultLoader(String fileType);
  
  /**
   * Return a model loader 
   * @param id the unique identifier of the model loader
   * @return the model loader, or <code>null</code> if not found.
   */
  public abstract IModelLoader getModelLoader(String id);
  
  /**
   * Return the default model loader for the given file type.
   * @param fileType the file type (extension)
   * @return the default model loader, or <code>null</code> if there is no default model loader.
   */
  public abstract IModelLoader getDefaultModelLoader(String fileType);
  
  /**
   * Return an array of model loaders that are capable of loading files of
   * the specified type.
   * @param fileType the file type (extension)
   * @return an array of model loader descriptions. This array may be empty, but will not be <code>null</code>.
   */
  public abstract IModelLoaderDescription[] findCompatibleModelLoaders(String fileType);

  /**
   * Return the id of the default model loader for the give file type.
   * @param fileType the file type (extension)
   * @return an model loader id, or <code>null</code> if no loader exists.
   */
  public abstract String getDefaultModelLoaderId(String fileType);

  /**
   * Declare the the model loader represented by <code>id</code> can load files of type <code>fileType</code>.
   * @param id the model loader unique identifier.
   * @param fileType the file type (extension).
   */
  public abstract void addLoaderForType(String id, String fileType);

  /**
   * Forget that the model loader <code>id</code> can load files of type <code>fileType</code>.
   * @param id the model loader unique identifier.
   * @param fileType the file type (extension).
   */
  public abstract void removeLoaderForType(String id, String fileType);

  /**
   * Find an appropriate loader.
   * @param url
   * @param loaderId
   * @param type
   * @return a model loader instance.
   * @throws CoreJETException if a loader cannot be found
   */
  public abstract IModelLoader getLoader(String url, String loaderId, String type) throws CoreJETException;
}
