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
package org.eclipse.jet.internal.xpath;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jet.xpath.NodeSet;


/**
 * Implement 'NodeSet' by wrapping {@link java.util.LinkedHashSet}.
 *
 */
public class NodeSetImpl implements NodeSet
{

  public static final NodeSet EMPTY_SET = new NodeSetImpl(true/* immutable */);

  private final Set linkedHashSet;

  /**
   * 
   */
  public NodeSetImpl()
  {
    linkedHashSet = new LinkedHashSet();
  }

  public NodeSetImpl(int capacity)
  {
    linkedHashSet = new LinkedHashSet(capacity);
  }

  private NodeSetImpl(boolean immutable)
  {
    linkedHashSet = Collections.EMPTY_SET;
  }

  /* (non-Javadoc)
   * @see java.util.Set#add(java.lang.Object)
   */
  public boolean add(Object arg0)
  {
    return linkedHashSet.add(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#addAll(java.util.Collection)
   */
  public boolean addAll(Collection arg0)
  {
    return linkedHashSet.addAll(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#clear()
   */
  public void clear()
  {
    linkedHashSet.clear();
  }

  /* (non-Javadoc)
   * @see java.util.Set#contains(java.lang.Object)
   */
  public boolean contains(Object arg0)
  {
    return linkedHashSet.contains(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#containsAll(java.util.Collection)
   */
  public boolean containsAll(Collection arg0)
  {
    return linkedHashSet.containsAll(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#equals(java.lang.Object)
   */
  public boolean equals(Object arg0)
  {
    return linkedHashSet.equals(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#hashCode()
   */
  public int hashCode()
  {
    return linkedHashSet.hashCode();
  }

  /* (non-Javadoc)
   * @see java.util.Set#isEmpty()
   */
  public boolean isEmpty()
  {
    return linkedHashSet.isEmpty();
  }

  /* (non-Javadoc)
   * @see java.util.Set#iterator()
   */
  public Iterator iterator()
  {
    return linkedHashSet.iterator();
  }

  /* (non-Javadoc)
   * @see java.util.Set#remove(java.lang.Object)
   */
  public boolean remove(Object arg0)
  {
    return linkedHashSet.remove(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#removeAll(java.util.Collection)
   */
  public boolean removeAll(Collection arg0)
  {
    return linkedHashSet.removeAll(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#retainAll(java.util.Collection)
   */
  public boolean retainAll(Collection arg0)
  {
    return linkedHashSet.retainAll(arg0);
  }

  /* (non-Javadoc)
   * @see java.util.Set#size()
   */
  public int size()
  {
    return linkedHashSet.size();
  }

  /* (non-Javadoc)
   * @see java.util.Set#toArray()
   */
  public Object[] toArray()
  {
    return linkedHashSet.toArray();
  }

  /* (non-Javadoc)
   * @see java.util.Set#toArray(java.lang.Object[])
   */
  public Object[] toArray(Object[] arg0)
  {
    return linkedHashSet.toArray(arg0);
  }

  public String toString()
  {
    return linkedHashSet.toString();
  }
}
