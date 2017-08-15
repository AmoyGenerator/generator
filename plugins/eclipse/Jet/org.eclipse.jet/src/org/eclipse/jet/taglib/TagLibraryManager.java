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


import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform;


/**
 * Access point for information on all declared tag libraries.
 *
 */
public class TagLibraryManager
{

  public static final String STANDARD_CONTROL_TAGS_ID = JET2Platform.PLUGIN_ID + ".controlTags"; //$NON-NLS-1$

  public static final String STANDARD_JAVA_TAGS_ID = JET2Platform.PLUGIN_ID + ".javaTags"; //$NON-NLS-1$

  public static final String STANDARD_FORMAT_TAGS_ID = JET2Platform.PLUGIN_ID + ".formatTags"; //$NON-NLS-1$

  public static final String STANDARD_WORKSPACE_TAGS_ID = JET2Platform.PLUGIN_ID + ".workspaceTags"; //$NON-NLS-1$
 
  
  private static TagLibraryManager instance = null;

  private TagLibraryManager()
  {
    // do nothing
  }

  /**
   * Return the instance of the tag library manager.
   * @return the singleton instance
   */
  public static TagLibraryManager getInstance()
  {
    if (instance == null)
    {
      instance = new TagLibraryManager();
    }
    return instance;
  }

  /**
   * Find the TabLibrary declaration given the tag library id.
   * @param id a tag library id.
   * @return the tag library instance, or <code>null</code> if id is not known.
   */
  public TagLibrary getTagLibrary(String id)
  {
    return getTagLibrary(id, true);
  }

  /**
   * Return the tag library declaration given the tag library id. 
   * <p>
   * If <code>includeWorkspaceDeclarations</code>
   * is <code>true</code>, then workspace plug-in projects will be searched for tag library definitions prior to
   * installed plug-ins. This option is useful for compilers and editors.
   * </p> 
   * @param id a tag library id.
   * @param includeWorkspaceDeclarations include tag libraries declared in plug-ins in the current workspace
   * @return the tag library instance, or <code>null</code> if id is not known.
   */
  public TagLibrary getTagLibrary(String id, boolean includeWorkspaceDeclarations)
  {
    TagLibrary result = null;
    if(includeWorkspaceDeclarations){
      result = InternalJET2Platform.getDefault().getWorkspaceTagLibrary(id);
    }
    if(result == null)
    {
      result = InternalJET2Platform.getDefault().getTagLibrary(id);
    }
    return result;
  }
  
  public String[] getKnownLibraryIds()
  {
    return InternalJET2Platform.getDefault().getKnownTagLibraryIds();
  }

}
