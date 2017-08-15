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

package org.eclipse.jet.internal.runtime.model;


import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jet.internal.core.url.URLUtility;
import org.eclipse.jet.runtime.model.ILoadContext;


/**
 * Define the 'transform' load context.
 *
 */
public class TransformLoadContext implements ILoadContext
{

  private final URL contextURL;

  public TransformLoadContext(URL bundleURL) throws MalformedURLException
  {
    if(bundleURL.getFile().endsWith(".jar")) { //$NON-NLS-1$
      this.contextURL = URLUtility.jarRootEntryURL(bundleURL);
    } else {
      this.contextURL = bundleURL;
    }

  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IDefinedContext#getName()
   */
  public String getName()
  {
    return "transform"; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IDefinedContext#getContextUrl()
   */
  public URL getContextUrl()
  {
    return contextURL;
  }

}
