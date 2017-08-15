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
package org.eclipse.jet.compiler;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.core.compiler.JETCompilerOptions;


/**
 * A singleton that managers JET project compile options.
 */
public class CompileOptionsManager
{

  private static CompileOptionsManager manager = null;
  private final IEclipsePreferences configurationNode;
  private final IEclipsePreferences instanceNode;
  private final IEclipsePreferences defaultsNode;

  public static CompileOptionsManager getInstance()
  {
    if (manager == null)
    {
      manager = new CompileOptionsManager();
    }
    return manager;
  }

  /**
   * 
   */
  private CompileOptionsManager()
  {
    super();
    defaultsNode = new DefaultScope().getNode(JET2Platform.PLUGIN_ID);
    configurationNode = new ConfigurationScope().getNode(JET2Platform.PLUGIN_ID);
    instanceNode = new InstanceScope().getNode(JET2Platform.PLUGIN_ID);

  }

  public Map getOptionsForProject(IProject project)
  {
    IScopeContext projectScope = new ProjectScope(project);
    final IEclipsePreferences projectNode = projectScope.getNode(JET2Platform.PLUGIN_ID);
    IEclipsePreferences[] searchList = new IEclipsePreferences[] {
      projectNode,
      instanceNode,
      configurationNode,
      defaultsNode,
    };
    final IPreferencesService service = Platform.getPreferencesService();
    
    Set keys = JETCompilerOptions.getDefaultCompilerOptions().keySet();
    Map result = new HashMap(keys.size());
    for (Iterator i = keys.iterator(); i.hasNext();)
    {
      String key = (String)i.next();
      final String value = service.get(key, null, searchList);
      result.put(key, value);
    }
    return result;
  }

  public static Map getOptions(IProject project)
  {
    return getInstance().getOptionsForProject(project);
  }

}
