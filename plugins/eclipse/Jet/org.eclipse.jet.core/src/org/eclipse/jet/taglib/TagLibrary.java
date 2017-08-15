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
 * $Id: TagLibrary.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;




/**
 * Provide access to the tags in a tag library.
 *
 * <P>
 * This interface is not intended to be implemented by clients.
 * </P>
 */
public interface TagLibrary
{

  /**
   * Return the string identifier of the tag library.
   * @return the tag library id
   */
  public abstract String getLibraryId();

  public abstract String getLibraryName();

  public abstract String getDefaultPrefix();

  public abstract String getDescription();

  /**
   * Return the {@link TagDefinition} for the named tag.
   * @param name the name of a tag in the tag library. 
   * @return the definitions or <code>null</code> if the named tag is in the library
   * @throws NullPointerException if <code>name</code> is null.
   */
  public abstract TagDefinition getTagDefinition(String name);

  /**
   * Return a sort array of tags in the library
   * @return an array of Strings; an empty array is returned if the library has no tags.
   */
  public abstract String[] getTagNames();

  /**
   * Test if the named tag is in the tag library.
   * @param tagNCName
   * @return if the tag is defined by the library
   */
  public abstract boolean hasTag(String tagNCName);

  /**
   * Test if the tag library has been deprecated.
   * @return <code>true</code> if the library has been deprecated, <code>false</code> otherwise.
   */
  public abstract boolean isDeprecated();
}
