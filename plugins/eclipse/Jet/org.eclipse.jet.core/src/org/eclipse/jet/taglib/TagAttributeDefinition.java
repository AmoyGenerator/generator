/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: TagAttributeDefinition.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;


/**
 * Expose the details of a Custom Tag attribute
 *
 */
public interface TagAttributeDefinition
{

  /**
   * An attribute whose value is any legal string. Value is "string".
   */
  public static final String STRING_TYPE = "string"; //$NON-NLS-1$

  /**
   * An attribute whose value is either "true" or "false". Value is "boolean".
   */
  public static final String BOOLEAN_TYPE = "boolean"; //$NON-NLS-1$

  /**
   * An attribute whose value is an XPath expression. "Value is "xpath".
   */
  public static final String XPATH_TYPE = "xpath"; //$NON-NLS-1$

  /**
   * Return the attribute's name
   * @return the attribute name
   */
  public abstract String getName();

  /**
   * Return the attribute's description if provided
   * @return the description or an empty string.
   */
  public abstract String getDescription();

  /**
   * Return the attributes's type
   * @return the attribute's type
   * @see #BOOLEAN_TYPE
   * @see #STRING_TYPE
   * @see #XPATH_TYPE
   */
  public abstract String getType();

  /**
   * Test if the element is required
   * @return <code>true</code> if the element is required, <code>false</code> otherwise.
   */
  public abstract boolean isRequired();

  /**
   * Test if the element has been deprecated
   * @return <code>true</code> if the element has been deprecated, <code>false</code> otherwise.
   */
  public abstract boolean isDeprecated();

}
