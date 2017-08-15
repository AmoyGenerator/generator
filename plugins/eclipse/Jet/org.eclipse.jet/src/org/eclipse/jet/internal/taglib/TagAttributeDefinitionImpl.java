/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.taglib;


import org.eclipse.jet.taglib.TagAttributeDefinition;


/**
 * Implementation of TagAttributeDefinition.
 *
 */
public class TagAttributeDefinitionImpl implements TagAttributeDefinition
{

  private final String name;

  private final boolean required;

  private final boolean deprecated;

  private final String description;

  private final String type;

  public TagAttributeDefinitionImpl(String name, boolean required, boolean deprecated, String description, String type)
  {
    this.name = name;
    this.required = required;
    this.deprecated = deprecated;
    this.description = description;
    this.type = type == null ? STRING_TYPE : type;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagAttributeDefinition#getName()
   */
  public String getName()
  {
    return name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagAttributeDefinition#isRequired()
   */
  public boolean isRequired()
  {
    return required;
  }

  public boolean isDeprecated()
  {
    return deprecated;
  }

  public String getDescription()
  {
    return description;
  }

  public String getType()
  {
    return type;
  }

}
