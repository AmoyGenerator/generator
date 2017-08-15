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

package org.eclipse.jet.internal.runtime;



import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.extensionpoints.TransformDataFactory;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;


/**
 * Eclpise extension registry listener for the 'org.eclipse.jet.transform' extension point.
 * 
 */
public final class InstalledJETBundleProvider implements IRegistryChangeListener, IJETBundleProvider
{

  private static final String PLUGIN_ID = "org.eclipse.jet"; //$NON-NLS-1$

  private static final String EXTENSION_NAME = "transform"; //$NON-NLS-1$

  private static final String EXTENSION_POINT_ID = PLUGIN_ID + "." + EXTENSION_NAME; //$NON-NLS-1$

  /**
   * Map&lt;String, IJETBundleDescription&gt; where String is a transformId.
   */
  private final Map pluginBundleDescriptorsById = Collections.synchronizedMap(new HashMap());

  /**
   * 
   */
  public InstalledJETBundleProvider()
  {
  }

  /**
   * Initialize the list of tabLibrary extensions currently loaded.
   * @param bundleManager 
   * 
   */
  public void startup()
  {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    
    addConfigElements(registry.getConfigurationElementsFor(EXTENSION_POINT_ID));

    registry.addRegistryChangeListener(this, PLUGIN_ID);
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
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    registry.removeRegistryChangeListener(this);
    registry = null;
  }

  /**
   * @param configElements
   */
  private void addConfigElements(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      final IConfigurationElement element = configElements[i];
      if (TransformDataFactory.E_TRANSFORM.equals(element.getName()))
      {
        String transformId = element.getDeclaringExtension().getNamespace();
        addJETPlugin(transformId, element);
      }
    }
  }

  private void removeConfigElements(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      final IConfigurationElement element = configElements[i];
      if (TransformDataFactory.E_TRANSFORM.equals(element.getName()))
      {
        String transformId = element.getDeclaringExtension().getNamespace();
        removeJETPlugin(transformId);

      }
    }
  }

  public Set getAllJETBundleIds()
  {
    return pluginBundleDescriptorsById.keySet();
  }

  public IJETBundleDescriptor getDescriptor(String id)
  {
    return (IJETBundleDescriptor)pluginBundleDescriptorsById.get(id);
  }
  
  public void addJETPlugin(String id, IConfigurationElement transformElement)
  {
    try
    {
      final Bundle bundle = Platform.getBundle(id);
      final JETBundleManifest manifest = new JETBundleManifest(bundle.getHeaders(Platform.getNL()));
      pluginBundleDescriptorsById.put(id, 
        new JETBundleDescriptor(manifest, TransformDataFactory.INSTANCE.createTransformData(transformElement), 
          bundle.getEntry("/"))); //$NON-NLS-1$
    }
    catch (InvalidRegistryObjectException e)
    {
      e.printStackTrace();
    }
    catch (NotABundleException e)
    {
      e.printStackTrace();
    }
  }
  
  public void removeJETPlugin(String id)
  {
    pluginBundleDescriptorsById.remove(id);
  }

  public Bundle load(String id, IProgressMonitor monitor)
  {
    Bundle bundle = null;
    if(pluginBundleDescriptorsById.containsKey(id))
    {
      bundle = Platform.getBundle(id);
    }
    return bundle;
  }

  public void unload(String id)
  {
    // nothing to do...
  }

  public void unload(Bundle bundle) throws BundleException
  {
    // nothing to do...
  }

  public Collection getAllJETBundleDescriptors()
  {
    return pluginBundleDescriptorsById.values();
  }

}
