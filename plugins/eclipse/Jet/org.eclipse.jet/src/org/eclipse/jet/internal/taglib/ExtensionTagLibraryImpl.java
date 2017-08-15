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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.taglib.CustomTagKind;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagInstanceFactory;
import org.eclipse.jet.taglib.TagLibrary;


/**
 * Implementation of {@link TagLibrary} based on an {@link IConfigurationElement}.
 *
 */
public class ExtensionTagLibraryImpl implements TagLibrary
{

  private static final String TAG_FACTORY_ATTRIBUTE = "tagFactory"; //$NON-NLS-1$

  private static final String DEPRECATED__ATTR = "deprecated"; //$NON-NLS-1$

  private static final String STANDARD_PREFIX__ATTR = "standardPrefix"; //$NON-NLS-1$

  private static final String NAME__ATTR = "name"; //$NON-NLS-1$

  private static final String DESCRIPTION__ELEMENT = "description"; //$NON-NLS-1$

  private static final String OTHER_TAG_ELEMENT = "otherTag"; //$NON-NLS-1$

  private static final String FUNCTION_TAG_ELEMENT = "functionTag"; //$NON-NLS-1$

  private static final String ITERATING_TAG_ELEMENT = "iteratingTag"; //$NON-NLS-1$

  private static final String CONDITIONAL_TAG_ELEMENT = "conditionalTag"; //$NON-NLS-1$

  private static final String EMPTY_TAG_ELEMENT = "emptyTag"; //$NON-NLS-1$

  private static final String CONTAINER_TAG_ELEMENT = "containerTag"; //$NON-NLS-1$

  private static final String TAG_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

  private static final Map tagToKindMap = new HashMap(5);
  static
  {
    tagToKindMap.put(OTHER_TAG_ELEMENT, CustomTagKind.OTHER);
    tagToKindMap.put(FUNCTION_TAG_ELEMENT, CustomTagKind.FUNCTION);
    tagToKindMap.put(ITERATING_TAG_ELEMENT, CustomTagKind.ITERATING);
    tagToKindMap.put(CONDITIONAL_TAG_ELEMENT, CustomTagKind.CONDITIONAL);
    tagToKindMap.put(EMPTY_TAG_ELEMENT, CustomTagKind.EMPTY);
    tagToKindMap.put(CONTAINER_TAG_ELEMENT, CustomTagKind.CONTAINER);
  }

  private final String id;

  private final boolean deprecated;

  private final String name;

  private final String description;

  private final Map tagMap;

  private final String standardPrefix;

  private String[] tagNames = null;

  private static final String[] EMPTY_TAGNAME_ARRAY = new String [0];

  private final TagInstanceFactory tagInstanceFactory;

  public ExtensionTagLibraryImpl(String id, IConfigurationElement configElement)
  {
    this.id = id;

    deprecated = Boolean.valueOf(configElement.getAttribute(DEPRECATED__ATTR)).booleanValue();

    standardPrefix = configElement.getAttribute(STANDARD_PREFIX__ATTR);
    name = configElement.getAttribute(NAME__ATTR);

    description = getDescription(configElement);

    IConfigurationElement[] children = configElement.getChildren();

    tagMap = new HashMap(children.length);

    for (int i = 0; i < children.length; i++)
    {
      String tagName = children[i].getAttribute(TAG_NAME_ATTRIBUTE);
      CustomTagKind kind = (CustomTagKind)tagToKindMap.get(children[i].getName());
      if (kind != null && tagName != null && !tagMap.containsKey(tagName))
      {
        tagMap.put(tagName, ExtensionTagDefinitionImpl.createInstance(tagName, children[i], kind, this));
      }
    }

    TagInstanceFactory factory = null;
    try
    {
      if(configElement.getAttribute(TAG_FACTORY_ATTRIBUTE) != null) {
        factory = (TagInstanceFactory)configElement.createExecutableExtension(TAG_FACTORY_ATTRIBUTE);
      }
    }
    catch (CoreException e)
    {
      // ignore
      InternalJET2Platform.log(e.getStatus());
    }
    tagInstanceFactory = factory;
  }

  /**
   * Return the content of a child "description" element, or the empty string if not found.
   * @param configElement the parent description element.
   * @return the description or the empty string.
   */
  static String getDescription(IConfigurationElement configElement)
  {
    final IConfigurationElement[] descriptions = configElement.getChildren(DESCRIPTION__ELEMENT);
    String desc = ""; //$NON-NLS-1$
    if (descriptions != null && descriptions.length > 0)
    {
      desc = descriptions[0].getValue();
    }
    return desc == null ? "" : desc; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getLibraryId()
   */
  public String getLibraryId()
  {
    return id;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#getTagDefinition(java.lang.String)
   */
  public TagDefinition getTagDefinition(String name)
  {
    return (TagDefinition)tagMap.get(name);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibrary#newTagElement(java.lang.String)
   */
  public String[] getTagNames()
  {
    if (tagNames == null)
    {
      List tagList = new ArrayList(tagMap.keySet());
      Collections.sort(tagList);
      tagNames = (String[])tagList.toArray(EMPTY_TAGNAME_ARRAY);
    }
    return tagNames;
  }

  public boolean hasTag(String tagNCName)
  {
    return tagMap.containsKey(tagNCName);
  }

  public boolean isDeprecated()
  {
    return deprecated;
  }

  public String getLibraryName()
  {
    return name;
  }

  public String getDefaultPrefix()
  {
    return standardPrefix == null ? "" : standardPrefix; //$NON-NLS-1$
  }

  public String getDescription()
  {
    return description;
  }

  public TagInstanceFactory getTagInstanceFactory()
  { 
    return tagInstanceFactory;
  }

}
