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
package org.eclipse.jet.taglib;

import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.taglib.workspace.ActionsUtil;

/**
 * Utility class with various functions useful to tag implementations
 */
public class TagUtil
{

  /**
   * 
   */
  private TagUtil()
  {
    super();
  }

  /**
   * Return the contents of the text file refered to by location.
   * @param location the full file system location of the resource.
   * @return the contents as a string
   * @throws CoreException if an error occurs while reading the file
   * @throws IllegalArgumentException if <code>location</code> does not refer to an existing workspace file.
   */
  public static String getContents(IPath location) throws CoreException 
  {
    final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
    if(file != null) {
      String encoding = file.getCharset();
      try
      {
        return ActionsUtil.readTextFile(file.getLocationURI().toURL(), encoding);
      }
      catch (JET2TagException e)
      {
        final Exception cause = (Exception)e.getCause();
        throw new CoreException(InternalJET2Platform.newStatus(IStatus.ERROR, cause.getMessage(), cause));
      }
      catch (MalformedURLException e)
      {
        throw new CoreException(InternalJET2Platform.newStatus(IStatus.ERROR, e.getMessage(), e));
      }
    } else {
      throw new IllegalArgumentException(location.toString());
    }
  }

}
