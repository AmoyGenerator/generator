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
 * Represent an inconsistency between at child element's parent, and its parent as specified
 * in a method call.
 *
 */
public class InvalidChildException extends Exception
{

  /**
   * 
   */
  private static final long serialVersionUID = 9208184237965314251L;

  private final Object parentElement;

  private final ExpandedName elementName;

  private final Object childElement;

  /**
   * Create an exception instance
   * @param parentElement the parent Element
   * @param elementName the element name
   * @param childElement the child element that does not have parentElement as its parent.
   */
  public InvalidChildException(Object parentElement, ExpandedName elementName, Object childElement)
  {
    this.parentElement = parentElement;
    this.elementName = elementName;
    this.childElement = childElement;
  }

  /**
   * @return Returns the childElement.
   */
  public final Object getChildElement()
  {
    return childElement;
  }

  /**
   * @return Returns the elementName.
   */
  public final ExpandedName getElementName()
  {
    return elementName;
  }

  /**
   * @return Returns the parentElement.
   */
  public final Object getParentElement()
  {
    return parentElement;
  }

}
