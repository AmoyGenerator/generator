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
package org.eclipse.jet.transform;

import org.eclipse.jet.JET2Platform;

/**
 * Declares constants for the JET Launch configuration and its attributes
 */
public class JETLaunchConstants
{

  /**
   * The JET Launch configuration ID
   */
  public static final String CONFIG_ID = JET2Platform.PLUGIN_ID + ".jet2Transformation"; //$NON-NLS-1$
  /**
   * The name of the 'source' attribute in the launch configuration
   */
  public static final String SOURCE = JET2Platform.PLUGIN_ID + "." + "source"; //$NON-NLS-1$ //$NON-NLS-2$
  /**
   * The name of the 'id' attribute in the launch configuration.
   */
  public static final String ID = JET2Platform.PLUGIN_ID + "." + "id"; //$NON-NLS-1$//$NON-NLS-2$
  /**
   * The name of the 'logFilterLevel' attribute in the lauch configuration.
   */
  public static final String LOG_FILTER_LEVEL = JET2Platform.PLUGIN_ID + "." + "logFilterLevel"; //$NON-NLS-1$//$NON-NLS-2$

  /**
   * 
   */
  private JETLaunchConstants()
  {
    super();
  }

}
