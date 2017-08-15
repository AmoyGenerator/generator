/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.jet.compiler;


import java.util.Map;

import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryManager;
import org.eclipse.jet.taglib.TagLibraryReference;


/**
 * A class to manage usage of tag libraries in a JET2 template.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.TagLibraryUsageManager}
 */
public class TagLibraryUsageManager
{

  private final org.eclipse.jet.core.parser.ast.TagLibraryUsageManager delegate;

  /**
   * @param predefinedLibraryMap a map of predefined prefixes and their tag libary ids.
   * 
   */
  public TagLibraryUsageManager(Map predefinedLibraryMap)
  {
    delegate = new org.eclipse.jet.core.parser.ast.TagLibraryUsageManager(predefinedLibraryMap,
      new ITagLibraryResolver() {
        public TagLibrary getLibrary(String tagLibraryID)
        {
          return TagLibraryManager.getInstance().getTagLibrary(tagLibraryID);
        }
    });
  }

  /**
   * Test whether a prefix can be created.
   * <p>
   * Conditions where this can succeed:
   * <bl>
   * <li>prefix is not defined in either the predefined library map or the library map.</li>
   * </bl>
   * </p>
   * @param prefix a prefix
   * @param id the tag libary id.
   * @return <code>true</code> if the prefix can be created.
   */
  public boolean canDefinePrefix(String prefix, String id)
  {

    return delegate.canDefinePrefix(prefix, id);
  }

  public boolean isLibraryDefined(String libaryId)
  {
    return delegate.isLibraryDefined(libaryId);
  }

  public String getLibraryIdFromPrefix(String prefix)
  {
    return delegate.getLibraryIdFromPrefix(prefix);
  }

  /**
   * Defined a prefix for a tag library id. If a prefix for the tag library id
   * is already defined in the predefined librarys map, then that prefix is removed.
   * @param prefix a prefix
   * @param libraryId a library id
   */
  public void add(String prefix, String libraryId)
  {
    delegate.add(prefix, libraryId);
  }

  /**
   * Return the TagDefinition of the selected tag.
   * @param tagName the QName of the tag
   * @return the TagDefinition
   */
  public TagDefinition getTagDefinition(String tagName)
  {
    return delegate.getTagDefinition(tagName);
  }

  /**
   * Test whether the tag name passed is know to this manager
   * @return <code>true</code> if the tag matches one of the libraries, <code>false</code> otherwise.
   */
  public boolean isKnownTag(String tagName)
  {
    return delegate.isKnownTag(tagName);
  }

  /**
   * Test whether the tag name passed is known to be an invalid name by the manager. The tag name
   * is known to be invalid if it has a known prefix, but an unknown unqualified tag name.
   * @return <code>true</code> if the tag matches one of the libraries, <code>false</code> otherwise.
   */
  public boolean isKnownInvalidTagName(String tagName) {
    return delegate.isKnownInvalidTagName(tagName);
    
  }
  
  public TagLibraryReference[] getTagLibraryReferences()
  {
    return delegate.getTagLibraryReferences();
  }

  public String[] getKnownTagNames()
  {
    return delegate.getKnownTagNames();
  }
}
