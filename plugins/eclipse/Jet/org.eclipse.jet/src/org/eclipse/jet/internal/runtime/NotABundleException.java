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
 * Exception thrown when a {@link BundleManifest} is constructed against
 * a manifest that is not an Eclipse Bundle.
 */
public class NotABundleException extends Exception
{

  private final Dictionary manifest;

  public NotABundleException(Dictionary manifest)
  {
    this.manifest = manifest;
  }

  /**
   * 
   */
  private static final long serialVersionUID = -344501343498059244L;

  /**
   * A dictionary of Manifest values passed to {@link BundleManifest#BundleManifest(Dictionary)}.
   * @return Returns the manifest.
   */
  public final Dictionary getManifest()
  {
    return manifest;
  }

}
