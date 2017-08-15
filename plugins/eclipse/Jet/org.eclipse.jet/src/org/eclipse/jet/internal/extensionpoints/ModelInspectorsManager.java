/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.extensionpoints;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.xpath.inspector.InspectorManager;
import org.osgi.framework.Bundle;


/**
 * Manager for tab library access and iteration with the Eclipse extension registry.
 *
 */
public final class ModelInspectorsManager implements IRegistryChangeListener
{

  private static final String PLUGIN_ID = "org.eclipse.jet"; //$NON-NLS-1$

  private static final String EXTENSION_NAME = "modelInspectors"; //$NON-NLS-1$

  private static final String EXTENSION_POINT_ID = PLUGIN_ID + "." + EXTENSION_NAME; //$NON-NLS-1$

  private final Map tagLibraries = new HashMap();

  private static final String E_INSPECTOR = "inspector"; //$NON-NLS-1$
  private static final String A_INSPECTOR_CLASS = "class"; //$NON-NLS-1$
  private static final String E_INSPECTS = "inspects"; //$NON-NLS-1$
  private static final String A_INSPECTS_CLASS = "class"; //$NON-NLS-1$

  /**
   * 
   */
  public ModelInspectorsManager()
  {
    super();
  }

  /**
   * Initialize the list of tabLibrary extensions currently loaded.
   *
   */
  public void startup()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] configElements = reg.getConfigurationElementsFor(EXTENSION_POINT_ID);
    addConfigElements(configElements);

    reg.addRegistryChangeListener(this, PLUGIN_ID);
    
    timer.done();
  }

  /**
   * Update the cache of extensions currently loaded
   */
  public void registryChanged(IRegistryChangeEvent event)
  {
    IExtensionDelta[] deltas = event.getExtensionDeltas(EXTENSION_POINT_ID);

    for (int i = 0; i < deltas.length; i++)
    {
      IExtension ext = deltas[i].getExtension();
      if (deltas[i].getKind() == IExtensionDelta.ADDED)
      {
        addConfigElements(ext.getConfigurationElements());
      }
      else
      {
        removeConfigElements(ext.getConfigurationElements());
      }
    }
  }

  /**
   * Release all held resources in preparation for shutting down.
   *
   */
  public void shutdown()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "shutdown()"); //$NON-NLS-1$
    
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    reg.removeRegistryChangeListener(this);

    tagLibraries.clear();
    
    timer.done();
  }

  /**
   * @param configElements
   */
  private void addConfigElements(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_INSPECTOR.equals(configElements[i].getName()))
      {
        String contributorId = configElements[i].getDeclaringExtension().getNamespace();
        final Bundle bundle = Platform.getBundle(contributorId);
        
        final String inspectorClassName = configElements[i].getAttribute(A_INSPECTOR_CLASS);
        
        final IConfigurationElement[] children = configElements[i].getChildren(E_INSPECTS);
        List inspectables = new ArrayList(children.length);
        for (int j = 0; j < children.length; j++)
        {
          final String inspectableClassName = children[j].getAttribute(A_INSPECTS_CLASS);
          inspectables.add(inspectableClassName);
        }
        try
        {
          final Class inspectorClass = bundle.loadClass(inspectorClassName);
          final String[] inspectableClasses = (String[])inspectables.toArray(new String[inspectables.size()]);
          
          InspectorManager.getInstance().registerInspector(inspectableClasses, inspectorClass);
        }
        catch (ClassNotFoundException e)
        {
          e.printStackTrace();
        }
      }
    }
  }


  private void removeConfigElements(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_INSPECTOR.equals(configElements[i].getName()))
      {
        final String inspectorClassName = configElements[i].getAttribute(A_INSPECTOR_CLASS);
        
        final IConfigurationElement[] children = configElements[i].getChildren(E_INSPECTS);
        List inspectables = new ArrayList(children.length);
        for (int j = 0; j < children.length; j++)
        {
          final String inspectableClassName = children[j].getAttribute(A_INSPECTS_CLASS);
          inspectables.add(inspectableClassName);
        }
        final String[] inspectableClasses = (String[])inspectables.toArray(new String[inspectables.size()]);
          
        InspectorManager.getInstance().unregisterInspector(inspectableClasses, inspectorClassName);
      }
    }
  }
}
