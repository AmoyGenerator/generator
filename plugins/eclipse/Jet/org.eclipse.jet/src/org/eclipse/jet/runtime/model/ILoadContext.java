/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
 * $Id$
 */

package org.eclipse.jet.runtime.model;


import java.net.URL;


/**
 * A named URL context for model loading.
 *
 */
public interface ILoadContext
{

  /**
   * Return the name assigned to the context URL.
   * @return a non-null, non-empty string.
   */
  public abstract String getName();

  /**
   * Return the URL of the defined context.
   * @return a URL.
   */
  public abstract URL getContextUrl();

}
