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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.taglib;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.taglib.CustomTagKind;
import org.eclipse.jet.taglib.TagAttributeDefinition;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagLibrary;


/**
 * Implementation of TagDefinition.
 *
 */
public class TagDefinitionImpl implements TagDefinition
{

  private final String name;

  private final CustomTagKind kind;

  private final Map attributeDefinitions;

  private final boolean deprecated;

  private final boolean customContentProcessing;

  private final boolean allowAsEmpty;

  private final TagLibrary tagLibrary;

  private final String description;

  private final boolean removeWhenContainingLineIsEmpty;

  public TagDefinitionImpl(TagLibrary tagLibrary, String tagName, CustomTagKind kind,
    String description, boolean customContentProcessing, boolean allowAsEmpty, 
    boolean deprecated, boolean removeWhenContainingLineIsEmpty )
  {
    this.tagLibrary = tagLibrary;
    this.name = tagName;
    this.kind = kind;
    this.description = description;
    this.customContentProcessing = customContentProcessing;
    this.allowAsEmpty = allowAsEmpty;
    this.deprecated = deprecated;
    this.removeWhenContainingLineIsEmpty = removeWhenContainingLineIsEmpty;
    //update by yuxin 
    //attributeDefinitions = new HashMap();
    attributeDefinitions = new LinkedHashMap();
  }

  /**
   * @param tagAttributeDefinition
   */
  public void addTagAttribute(TagAttributeDefinition tagAttributeDefinition)
  {
    attributeDefinitions.put(tagAttributeDefinition.getName(), tagAttributeDefinition);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagDefinition#getName()
   */
  public final String getName()
  {
    return name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagDefinition#getKind()
   */
  public CustomTagKind getKind()
  {
    return kind;
  }

  public TagAttributeDefinition getAttributeDefinition(String attrName)
  {
    return (TagAttributeDefinition)attributeDefinitions.get(attrName);
  }

  public List getAttributeDefinitions()
  {
    List list = new ArrayList(attributeDefinitions.values());
    //remove by yuxin
//    Collections.sort(list, new Comparator()
//      {
//
//        public int compare(Object o1, Object o2)
//        {
//          TagAttributeDefinition ad1 = (TagAttributeDefinition)o1;
//          TagAttributeDefinition ad2 = (TagAttributeDefinition)o2;
//          return ad1.getName().compareTo(ad2.getName());
//        }
//      });
    return list;
  }

  public boolean isDeprecated()
  {
    return deprecated;
  }

  public boolean requiresNewWriter()
  {
    boolean required = false;
    if (kind == CustomTagKind.FUNCTION)
    {
      required = true;
    }
    else if (customContentProcessing
      && (kind == CustomTagKind.CONTAINER || kind == CustomTagKind.CONDITIONAL || kind == CustomTagKind.ITERATING))
    {
      required = true;
    }
    return required;
  }

  public boolean isEmptyTagAllowed()
  {
    boolean allowed = false;
    if (kind == CustomTagKind.EMPTY || kind == CustomTagKind.OTHER)
    {
      allowed = true;
    }
    else if (allowAsEmpty && (kind == CustomTagKind.CONTAINER || kind == CustomTagKind.CONDITIONAL || kind == CustomTagKind.ITERATING))
    {
      allowed = true;
    }
    return allowed;
  }

  public boolean isContentAllowed()
  {
    boolean allowed = false;
    if (kind == CustomTagKind.FUNCTION || kind == CustomTagKind.CONTAINER || kind == CustomTagKind.CONDITIONAL
      || kind == CustomTagKind.ITERATING || kind == CustomTagKind.OTHER)
    {
      allowed = true;
    }
    return allowed;
  }

  public TagLibrary getTagLibrary()
  {
    return tagLibrary;
  }

  public String getDescription()
  {
    return description;
  }
  
  public boolean removeWhenContainingLineIsEmpty()
  {
      return removeWhenContainingLineIsEmpty;
  }
}
