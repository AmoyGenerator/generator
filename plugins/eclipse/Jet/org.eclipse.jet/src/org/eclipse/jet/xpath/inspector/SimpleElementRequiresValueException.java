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
 * Represent at attempt to create a simple Element (one with only text context) without
 * specifying a value.
 *
 */
public class SimpleElementRequiresValueException extends Exception
{

  /**
   * 
   */
  private static final long serialVersionUID = 6365737075872415885L;

  private final Object parentElement;

  private final ExpandedName elementName;

  /**
   * Construct an exception instance
   * @param parentElement the ELEMENT object that would contain the simple element.
   * @param elementName the name of the simple Element whose creation failed.
   */
  public SimpleElementRequiresValueException(Object parentElement, ExpandedName elementName)
  {
    this.parentElement = parentElement;
    this.elementName = elementName;
  }

  /**
   * @return Returns the parentElement.
   */
  public final Object getParentElement()
  {
    return parentElement;
  }

  /**
   * @return Returns the elementName.
   */
  public final ExpandedName getElementName()
  {
    return elementName;
  }

}
