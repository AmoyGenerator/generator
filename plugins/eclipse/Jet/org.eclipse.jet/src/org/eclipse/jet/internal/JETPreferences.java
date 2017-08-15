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
package org.eclipse.jet.internal;

import org.eclipse.jet.JET2Platform;

/**
 * Utility class defining JET Preference default values and keys
 */
public class JETPreferences
{

  private static final String NS = JET2Platform.PLUGIN_ID + "."; //$NON-NLS-1$
  public static final String PROJECT_SPECIFIC_SETTINGS = NS + "projectPrefs"; //$NON-NLS-1$
  public static final String ADDITIONAL_TEMPLATE_JAR_LOCATIONS = NS + "additionalJarLocations"; //$NON-NLS-1$
  public static final String CONSOLE_ERROR_COLOR = NS + "consoleErrorColor"; //$NON-NLS-1$
  public static final String CONSOLE_WARNING_COLOR = NS + "consoleWarningColor"; //$NON-NLS-1$
  public static final String CONSOLE_INFO_COLOR = NS + "consoleInfoColor"; //$NON-NLS-1$
  public static final String CONSOLE_TRACE_COLOR = NS + "consoleTraceColor"; //$NON-NLS-1$
  public static final String CONSOLE_DEBUG_COLOR = NS + "consoleDebugColor"; //$NON-NLS-1$
  /**
   * 
   */
  private JETPreferences()
  {
    super();
  }

}
