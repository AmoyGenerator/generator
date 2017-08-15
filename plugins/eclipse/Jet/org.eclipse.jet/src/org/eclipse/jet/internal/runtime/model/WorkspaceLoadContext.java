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

import org.eclipse.jet.runtime.model.ILoadContext;


/**
 * Defines a context named 'workspace' whose value is "platform:/resource/".
 *
 */
public class WorkspaceLoadContext implements ILoadContext
{

  private static URL contextURL = null;

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IDefinedContext#getName()
   */
  public String getName()
  {
    return "workspace"; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IDefinedContext#getContextUrl()
   */
  public URL getContextUrl()
  {
    if (contextURL == null)
    {
      try
      {
        contextURL = new URL("platform:/resource/"); //$NON-NLS-1$
      }
      catch (MalformedURLException e)
      {
        throw new RuntimeException("Internal Error: Failed to create contextURL", e); //$NON-NLS-1$
      }
    }
    return contextURL;
  }

}
