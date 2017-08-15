/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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
package org.eclipse.jet.internal.extensionpoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagLibrary;

/**
 * Standard implementation of {@link TagLibrary}.
 */
public class TagLibraryImpl implements TagLibrary
{

  private final String id;
  private final String name;
  private final String stdPrefix;
  private final boolean deprecated;
  private final String description;

  private final Map tagDefintionsByNCName = new HashMap();
  private String[] tagNames = null;

  public TagLibraryImpl(String id, String name, String description, String stdPrefix, boolean deprecated)
  {
    this.id = id;
    this.name = name;
    this.description = description;
    this.stdPrefix = stdPrefix;
    this.deprecated = deprecated;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getDefaultPrefix()
   */
  public String getDefaultPrefix()
  {
    return stdPrefix;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getDescription()
   */
  public String getDescription()
  {
    return description;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getLibraryId()
   */
  public String getLibraryId()
  {
    return id;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getLibraryName()
   */
  public String getLibraryName()
  {
    return name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getTagDefinition(java.lang.String)
   */
  public TagDefinition getTagDefinition(String name)
  {
    if(name == null)
    {
      throw new NullPointerException();
    }
    return (TagDefinition)tagDefintionsByNCName.get(name);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getTagNames()
   */
  public String[] getTagNames()
  {
    if (tagNames == null)
    {
      List tagList = new ArrayList(tagDefintionsByNCName.keySet());
      Collections.sort(tagList);
      tagNames = (String[])tagList.toArray(new String[tagList.size()]);
    }
    return tagNames;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#hasTag(java.lang.String)
   */
  public boolean hasTag(String tagNCName)
  {
    return tagDefintionsByNCName.containsKey(tagNCName);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#isDeprecated()
   */
  public boolean isDeprecated()
  {
    return deprecated;
  }

  public void addTag(TagDefinition tagDefinition)
  {
    tagDefintionsByNCName.put(tagDefinition.getName(), tagDefinition);
  }

}
