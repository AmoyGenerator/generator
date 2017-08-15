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
 * $Id: TagLibraryReference.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.taglib;

/**
 * Represent a reference to a tag library by a template or a transform.
 */
public interface TagLibraryReference
{

  /**
   * Return the prefix that is mapped to the tag library.
   * @return a string
   */
  public abstract String getPrefix();
  
  /**
   * Return the tag library id.
   * @return a string
   */
  public abstract String getTagLibraryId();
  
  /**
   * Return the TagLibrary description object.
   * @return an TagLibrary instance
   */
  public abstract TagLibrary getTagLibrary();
  
  /**
   * Test whether the tag library is automatically imported bundle templates.
   * @return <code>true</code> if the library is an auto imported library.
   */
  public abstract boolean isAutoImport();
}
