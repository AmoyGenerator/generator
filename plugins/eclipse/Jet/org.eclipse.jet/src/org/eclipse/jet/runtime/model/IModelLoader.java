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


import java.io.IOException;
import java.net.URL;


/**
 * Loads an object model given an URL reference to a model, or a string form of that model.
 *
 */
public interface IModelLoader
{
  /**
   * Load a model from the passed URL.
   * @param modelUrl a URL referencing the model to load
   * @return the root of the loaded model.
   * @throws IOException if an error occurs while loading the model.
   */
  public abstract Object load(URL modelUrl) throws IOException;

  /**
   * Load a model from the passed URL.
   * @param modelUrl a URL referencing the model to load
   * @param kind the kind of file (extension) that should be used to contain this content.
   * @return the root of the loaded model.
   * @throws IOException if an error occurs while loading the model.
   */
  public abstract Object load(URL modelUrl, String kind) throws IOException;

  /**
   * Load a model from a String representation of that model
   * @param serializedModel the String form of the model. Will not be <code>null</code>.
   * @param kind the kind of file (extension) that would be used to contain this content.
   * @return the root of the loaded model.
   * @throws IOException if an error occurs while loading the model.
   */
  public abstract Object loadFromString(String serializedModel, String kind) throws IOException;
  
  /**
   * Indicate whether the model loader can handle a particular file kind.
   * @param kind the file kind (extension)
   * @return <code>true</code> if the loader can load this kind of file, <code>false</code> otherwise.
   */
  public abstract boolean canLoad(String kind);
}
