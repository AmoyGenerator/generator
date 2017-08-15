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
package org.eclipse.jet.internal.runtime;

import java.net.URL;

import org.eclipse.jet.internal.extensionpoints.TransformData;

/**
 * Define a specialization of JETBundleDescriptor for project-based transforms. 
 */
public class ProjectJETBundleDescriptor extends JETBundleDescriptor
{

  private final String projectName;

  public ProjectJETBundleDescriptor(JETBundleManifest manifest, TransformData transformData, URL projectURL, String projectName)
  {
    super(manifest, transformData, projectURL);
    this.projectName = projectName;
  }

  /**
   * @return Returns the projectName.
   */
  public final String getProjectName()
  {
    return projectName;
  }

}
