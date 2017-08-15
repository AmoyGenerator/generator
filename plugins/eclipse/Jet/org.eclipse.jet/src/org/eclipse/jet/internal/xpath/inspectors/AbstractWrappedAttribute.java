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
package org.eclipse.jet.internal.xpath.inspectors;


/**
 * Abstract implementatino of {@link IWrappedAttribute}.
 */
public abstract class AbstractWrappedAttribute implements IWrappedAttribute
{

  private final Object parent;
  private final String name;

  /**
   * Construct a wrapped attribute whose parent in the given object, and whose
   * name is as specified.
   * @param parent
   * @param name
   */
  public AbstractWrappedAttribute(Object parent, String name)
  {
    this.parent = parent;
    this.name = name;
  }

  public final String getName()
  {
    return name;
  }

  public final Object getParent()
  {
    return parent;
  }

  /**
   * Provide a default implementation of {@link IWrappedAttribute#getDocumentRoot()} that returns
   * <code>null</code>.
   */
  public Object getDocumentRoot()
  {
    return null;
  }
}