/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: CustomTagFactory.java,v 1.2 2007/04/04 15:52:08 pelder Exp $
 */
package org.eclipse.jet.internal.runtime;

import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;

/**
 * Protocol supported by some TagDefinition instances indicating that a tag definition can be created.
 * @see org.eclipse.jet.taglib.TagDefinition
 * @see CustomTag
 */
public interface CustomTagFactory
{
  /**
   * Create a tag instance
   * @return the custom tag instance
   * @throws JET2TagException if the tag instance cannot be created.
   */
  public abstract CustomTag newTagElement() throws JET2TagException;

}
