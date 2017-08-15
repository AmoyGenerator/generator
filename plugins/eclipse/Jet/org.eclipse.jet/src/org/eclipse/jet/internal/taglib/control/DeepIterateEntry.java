/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: DeepIterateEntry.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

public class DeepIterateEntry {
  
  private final Object object;
  private final int depth;
  private boolean leaf;

  public DeepIterateEntry(Object object, int depth) {
    this.object = object;
    this.depth = depth;
  }

  /**
   * @return Returns the depth.
   */
  public int getDepth()
  {
    return depth;
  }

  /**
   * @return Returns the object.
   */
  public Object getObject()
  {
    return object;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((object == null) ? 0 : object.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DeepIterateEntry other = (DeepIterateEntry)obj;
    if (object == null)
    {
      if (other.object != null)
        return false;
    }
    else if (!object.equals(other.object))
      return false;
    return true;
  }

  /**
   * @return Returns the leaf.
   */
  final boolean isLeaf()
  {
    return leaf;
  }

  /**
   * @param leaf The leaf to set.
   */
  final void setLeaf(boolean leaf)
  {
    this.leaf = leaf;
  }
  
  
}