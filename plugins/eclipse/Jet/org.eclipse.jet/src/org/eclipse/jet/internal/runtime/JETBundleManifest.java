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

import java.util.Dictionary;

/**
 * Wraps a Dictionary representing the MANIFEST.MF file for a JET bundle.
 */
public class JETBundleManifest extends BundleManifest
{

  public static final String JET2_TRANSFORM_LOADER_ATTRIBUTE = "JET2-TransformClass"; //$NON-NLS-1$

  /**
   * @throws NotABundleException 
   * 
   */
  public JETBundleManifest(Dictionary manifest) throws NotABundleException
  {
    super(manifest);
  }

  public String getTransformId()
  {
    return getSymbolicName();
  }

  public String getTemplateLoaderClassName()
  {
    return (String)manifest.get(JETBundleManifest.JET2_TRANSFORM_LOADER_ATTRIBUTE);
  }
}
