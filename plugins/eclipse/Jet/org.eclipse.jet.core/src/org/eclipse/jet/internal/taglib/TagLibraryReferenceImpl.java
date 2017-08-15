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
 * $Id: TagLibraryReferenceImpl.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.taglib;

import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryReference;

/**
 * Standard implementation of TagLibraryReference.
 */
public class TagLibraryReferenceImpl implements TagLibraryReference
{

  private final String prefix;
  private final String id;
  private final boolean autoImport;

  public TagLibraryReferenceImpl(String prefix, String id, boolean autoImport)
  {
    this.prefix = prefix;
    this.id = id;
    this.autoImport = autoImport;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibraryReference#getPrefix()
   */
  public String getPrefix()
  {
    return prefix;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibraryReference#getTagLibraryId()
   */
  public String getTagLibraryId()
  {
    return id;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibraryReference#getTagLibrary()
   */
  public TagLibrary getTagLibrary()
  {
    throw new UnsupportedOperationException("Unsupported"); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.TagLibraryReference#isAutoImport()
   */
  public boolean isAutoImport()
  {
    return autoImport;
  }

}
