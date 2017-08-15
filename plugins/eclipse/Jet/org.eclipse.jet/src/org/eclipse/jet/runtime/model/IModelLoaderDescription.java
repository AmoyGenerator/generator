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
 * A description of a model loader
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface IModelLoaderDescription
{
  /**
   * Return the unique identifier of the model loader.
   * @return the unique id.
   */
  public abstract String getId();
  
  /**
   * Return the descriptive name of the model laoder.
   * @return the descriptive name
   */
  public abstract String getName();
}
