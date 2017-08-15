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
package org.eclipse.jet.xpath;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jet.internal.xpath.functions.BooleanFunction;
import org.eclipse.jet.internal.xpath.functions.NumberFunction;
import org.eclipse.jet.internal.xpath.functions.StringFunction;

/**
 * Utility class for common XPath operations.
 */
public final class XPathUtil
{

  /**
     * Adapter for Arrays and Collections into XPath NodeSets
     */
  protected static class NodeSetAdapter implements NodeSet
  {

    private final Collection collection;

    public NodeSetAdapter(Object[] objectArray)
    {
      this(Arrays.asList(objectArray));
    }
    
    public NodeSetAdapter(Collection collection)
    {
      this.collection = collection;
    }

    /* (non-Javadoc)
     * @see java.util.Set#add(java.lang.Object)
     */
    public boolean add(Object o)
    {
      return collection.add(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c)
    {
      return collection.addAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#clear()
     */
    public void clear()
    {
      collection.clear();
    }

    /* (non-Javadoc)
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(Object o)
    {
      return collection.contains(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c)
    {
      return collection.containsAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty()
    {
      return collection.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Set#iterator()
     */
    public Iterator iterator()
    {
      return collection.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Set#remove(java.lang.Object)
     */
    public boolean remove(Object o)
    {
      return collection.remove(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c)
    {
      return collection.removeAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c)
    {
      return collection.retainAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#size()
     */
    public int size()
    {
      return collection.size();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray()
     */
    public Object[] toArray()
    {
      return collection.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a)
    {
      return collection.toArray(a);
    }

  }
  
  private XPathUtil()
  {
     // prevent instantiation
  }

  /**
   * Invoke the <a href="http://www.w3.org/TR/xpath#function-string">XPath 'string' function</a> on the argument.
   * @param object the argument to convert to a string. Cannot be <code>null</code>
   * @return the result of the XPath 'string' function.
   * @throws NullPointerException if <code>object</code> is <code>null</code>.
   */
  public static String xpathString(Object object)
  {
    if(object == null)
    {
      throw new NullPointerException();
    }
    return StringFunction.evaluate(object);
  }

  /**
   * Invoke the <a href="http://www.w3.org/TR/xpath#function-boolean">XPath 'boolean' function</a> on the argument.
   * @param object the argument to convert to a boolean. Cannot be <code>null</code>
   * @return the result of the XPath 'boolean' function.
   * @throws NullPointerException if <code>object</code> is <code>null</code>.
   */
  public static boolean xpathBoolean(Object object)
  {
    if(object == null)
    {
      throw new NullPointerException();
    }
    return BooleanFunction.evaluate(object);
  }
  
  /**
   * Invoke the <a href="http://www.w3.org/TR/xpath#function-number">XPath 'number' function</a> on the argument.
   * @param object the argument to convert to a number (double). Cannot be <code>null</code>
   * @return the result of the XPath 'number' function.
   * @throws NullPointerException if <code>object</code> is <code>null</code>.
   */
  public static double xpathNumber(Object object)
  {
    if(object == null)
    {
      throw new NullPointerException();
    }
    return NumberFunction.evaluate(object);
  }
  
  /**
   * Return a {@link NodeSet} wrapper for the given array.
   * @param objectArray an array of objects.
   * @return a node set
   */
  public static NodeSet asNodeSet(Object[] objectArray) {
    return new NodeSetAdapter(objectArray);
  }
  
  /**
   * Return a {@link NodeSet} wrapper for the given collection
   * @param collection a collection
   * @return a node set
   */
  public static NodeSet asNodeSet(Collection collection) {
    return new NodeSetAdapter(collection);
  }
}
