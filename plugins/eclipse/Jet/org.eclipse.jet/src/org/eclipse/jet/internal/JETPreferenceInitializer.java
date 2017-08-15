/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
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
 * /
 *******************************************************************************/
package org.eclipse.jet.internal;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.core.compiler.JETCompilerOptions;

/**
 * Preference initializer
 */
public class JETPreferenceInitializer extends AbstractPreferenceInitializer
{

  /**
   * 
   */
  public JETPreferenceInitializer()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
   */
  public void initializeDefaultPreferences()
  {
    final IEclipsePreferences node = new DefaultScope().getNode(JET2Platform.PLUGIN_ID);

    // save the compiler defaults...
    Map compilerOptions = JETCompilerOptions.getDefaultCompilerOptions();
    for (Iterator i = compilerOptions.entrySet().iterator(); i.hasNext();)
    {
      Map.Entry entry = (Map.Entry)i.next();
      String key = (String)entry.getKey();
      node.put(key, entry.getValue().toString());
    }
    
    node.putBoolean(JETPreferences.PROJECT_SPECIFIC_SETTINGS, false);
    
    node.put(JETPreferences.CONSOLE_ERROR_COLOR, "255,0,0"); // Red //$NON-NLS-1$
    node.put(JETPreferences.CONSOLE_WARNING_COLOR, "250,100,0"); // Orange //$NON-NLS-1$
    node.put(JETPreferences.CONSOLE_INFO_COLOR, "0,0,0"); // Black //$NON-NLS-1$
    node.put(JETPreferences.CONSOLE_TRACE_COLOR, "128,128,128"); // Dark grey //$NON-NLS-1$
    node.put(JETPreferences.CONSOLE_DEBUG_COLOR, "192,192,192"); // Light grey //$NON-NLS-1$
  }
}
