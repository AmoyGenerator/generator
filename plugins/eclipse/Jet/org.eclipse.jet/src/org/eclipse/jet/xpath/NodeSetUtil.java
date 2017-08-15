/**
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 */
package org.eclipse.jet.xpath;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jet.xpath.NodeSet;

/**
 * Utility class for creating XPath NodeSets from Java collections and arrays.
 * @since 0.9.0
 */
public final class NodeSetUtil {

	/**
	 * Adaptor/wrapper for a Java collection
	 */
	private static class NodeSetAdaptor implements NodeSet {
		
		private Collection collection;
	
		/**
		 * Construct a NodeSetAdaptor
		 * @param collection
		 */
		public NodeSetAdaptor(Collection collection) {
			this.collection = collection;
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#add(java.lang.Object)
		 */
		public boolean add(Object o) {
			return collection.add(o);
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#addAll(java.util.Collection)
		 */
		public boolean addAll(Collection c) {
			return collection.addAll(c);
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#clear()
		 */
		public void clear() {
			collection.clear();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#contains(java.lang.Object)
		 */
		public boolean contains(Object o) {
			return collection.contains(o);
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#containsAll(java.util.Collection)
		 */
		public boolean containsAll(Collection c) {
			return collection.containsAll(c);
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			return collection.equals(o);
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return collection.hashCode();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#isEmpty()
		 */
		public boolean isEmpty() {
			return collection.isEmpty();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#iterator()
		 */
		public Iterator iterator() {
			return collection.iterator();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#remove(java.lang.Object)
		 */
		public boolean remove(Object o) {
			return collection.remove(o);
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#removeAll(java.util.Collection)
		 */
		public boolean removeAll(Collection c) {
			return collection.removeAll(c);
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#retainAll(java.util.Collection)
		 */
		public boolean retainAll(Collection c) {
			return collection.retainAll(c);
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#size()
		 */
		public int size() {
			return collection.size();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#toArray()
		 */
		public Object[] toArray() {
			return collection.toArray();
		}
	
		/* (non-Javadoc)
		 * @see java.util.Set#toArray(java.lang.Object[])
		 */
		public Object[] toArray(Object[] a) {
			return collection.toArray(a);
		}
	}
	
	/**
	 * Constructe a NodeSetUtil
	 */
	private NodeSetUtil() {
		// do nothing, except prevent instantiation
	}

	/**
	 * Wrap a Java {@link Collection} as a JET XPath {@link NodeSet}.
	 * @param collection a non-null, but possibly empty collection
	 * @return a node set
	 * @throws NullPointerException if <code>collection</code> is <code>null</code>
	 */
	public static NodeSet asNodeSet(Collection collection) {
		if(collection == null) {
			throw new NullPointerException();
		}
		return new NodeSetAdaptor(collection);
	}

	/**
	 * Wrap a Java array as a JET XPath {@link NodeSet}.
	 * @param array a non-null, but possibly empty array.
	 * @return a node set
	 * @throws NullPointerException if <code>array</code> is <code>null</code>
	 */
	public static NodeSet asNodeSet(Object[] array) {
		if(array == null) {
			throw new NullPointerException();
		}
		return new NodeSetAdaptor(Arrays.asList(array));
	}

}
