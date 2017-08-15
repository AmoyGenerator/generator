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

import org.eclipse.jet.runtime.model.IModelLoaderDescription;


/**
 * Standard implementation of {@link IModelLoaderDescription}. This class
 * is immutable, and is hence thread safe.
 */
public class ModelLoaderDescription implements IModelLoaderDescription
{

  
  private final String id;
  private final String name;

  /**
   * 
   */
  public ModelLoaderDescription(String id, String name)
  {
    super();
    this.id = id;
    this.name = name;
  }

  public String getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public int hashCode()
  {
    return id.hashCode();
  }
  
  public boolean equals(Object o)
  {
    if(!(o instanceof ModelLoaderDescription)) {
      return false;
    } else {
      return id.equals(((ModelLoaderDescription)o).getId());
    }
    
  }
}
