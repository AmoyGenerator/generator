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
package org.eclipse.jet.xpath.inspector;


/**
 * Define the XPath ExpandedName.
 *
 */
public final class ExpandedName
{

  private static final String STAR = "*"; //$NON-NLS-1$

  /**
   * Defines the expanded name that matches all other expanded names.
   */
  public static final ExpandedName ALL = new ExpandedName(STAR);

  private final String namespaceURI;

  private final String localPart;

  private final boolean wild;

  private final boolean localWild;

  public ExpandedName(String localPart)
  {
    this(null, localPart);
  }

  public ExpandedName(String namespaceURI, String localPart)
  {
    if (localPart == null)
    {
      throw new NullPointerException("localPart"); //$NON-NLS-1$
    }
    this.namespaceURI = namespaceURI;
    this.localPart = localPart;
    this.wild = namespaceURI == null && STAR.equals(localPart);
    this.localWild = namespaceURI != null && STAR.equals(localPart);
  }

  public String getLocalPart()
  {
    return localPart;
  }

  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  public boolean equals(Object other)
  {
    if (other instanceof ExpandedName)
    {
      ExpandedName otherEN = (ExpandedName)other;
      if (wild || otherEN.wild)
      {
        return true;
      }
      boolean sameNamespace = false;
      if (namespaceURI == null && otherEN.namespaceURI == null)
      {
        sameNamespace = true;
      }
      else if (namespaceURI != null && namespaceURI.equals(otherEN.namespaceURI))
      {
        sameNamespace = true;
      }
      return sameNamespace && (localWild || otherEN.localWild || localPart.equals(otherEN.localPart));
    }
    return false;
  }

  public int hashCode()
  {
    return (namespaceURI == null ? 0 : namespaceURI.hashCode()) + 13 * localPart.hashCode();
  }

  public String toString()
  {
    return namespaceURI == null ? localPart : namespaceURI + ":" + localPart; //$NON-NLS-1$
  }

  public boolean hasWildCards()
  {
    return wild || localWild;
  }
}
