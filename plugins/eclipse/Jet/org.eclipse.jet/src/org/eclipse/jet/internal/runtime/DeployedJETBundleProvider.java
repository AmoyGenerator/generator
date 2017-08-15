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
import java.text.MessageFormat;
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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Provider for JET Bundles deployed by the org.eclipse.jet.deployedTransforms extension point.
 */
public class DeployedJETBundleProvider implements IJETBundleProvider, IRegistryChangeListener
{
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/pluginBundleLoading")).booleanValue(); //$NON-NLS-1$

  private static final String PLUGIN_ID = JET2Platform.PLUGIN_ID;

  private static final String EXTENSION_NAME = "deployedTransforms"; //$NON-NLS-1$

  private static final String EXTENSION_POINT_ID = PLUGIN_ID + "." + EXTENSION_NAME; //$NON-NLS-1$

  private static final String DEPLOYEDTRANSFORM_ELEMENT = "deployedTransform"; //$NON-NLS-1$

  private static final String ID_ATTRIBUTE = "id"; //$NON-NLS-1$

  private static final String BUNDLE_ATTRIBUTE = "bundle"; //$NON-NLS-1$

  private final Map descriptorsById = Collections.synchronizedMap(new HashMap());

  /**
   * 
   */
  public DeployedJETBundleProvider()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#getAllJETBundleIds()
   */
  public Set getAllJETBundleIds()
  {
    return descriptorsById.keySet();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#getDescriptor(java.lang.String)
   */
  public IJETBundleDescriptor getDescriptor(String id)
  {
    return (IJETBundleDescriptor)descriptorsById.get(id);
  }

  public void shutdown()
  {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    reg.removeRegistryChangeListener(this);

    descriptorsById.clear();
  }

  public void startup()
  {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] configElements = reg.getConfigurationElementsFor(EXTENSION_POINT_ID);
    addTransforms(configElements);

    reg.addRegistryChangeListener(this, EXTENSION_POINT_ID);
  }

  public void registryChanged(IRegistryChangeEvent event)
  {
    IExtensionDelta[] deltas = event.getExtensionDeltas(EXTENSION_POINT_ID);

    for (int i = 0; i < deltas.length; i++)
    {
      IExtension ext = deltas[i].getExtension();
      if (deltas[i].getKind() == IExtensionDelta.ADDED)
      {
        addTransforms(ext.getConfigurationElements());
      }
      else
      {
        removeTransforms(ext.getConfigurationElements());
      }
    }
  }

  private void removeTransforms(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (DEPLOYEDTRANSFORM_ELEMENT.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(ID_ATTRIBUTE);
        if (id != null)
        {
          if (DEBUG)
          {
            InternalJET2Platform.debugMessage(this, "removeTransforms(" + id + ")"); //$NON-NLS-1$//$NON-NLS-2$
          }
          descriptorsById.remove(id);
        }
      }
    }
  }

  private void addTransforms(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (DEPLOYEDTRANSFORM_ELEMENT.equals(configElements[i].getName()))
      {
        // Ignore the 'id' attribute, and get the bundle id from the bundle itself.
//        String id = configElements[i].getAttribute(ID_ATTRIBUTE);
        String jet2Bundle = configElements[i].getAttribute(BUNDLE_ATTRIBUTE);
        if (jet2Bundle != null)
        {
          Bundle declaringBundle = Platform.getBundle(configElements[i].getDeclaringExtension().getNamespace());
          URL bundleURL = declaringBundle.getEntry(jet2Bundle);
          IJETBundleDescriptor descriptor = JETBundleManager.getJETBundleDescriptorForJAR(bundleURL);
          if(descriptor != null)
          {
            descriptorsById.put(descriptor.getId(), descriptor);
            if (DEBUG)
            {
              InternalJET2Platform.debugMessage(this, "addTransforms(" + descriptor.getId() + ", " + bundleURL + ")"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
            }
          }
        }
        else
        {
          String msg = JET2Messages.PluginDeployedTemplateBundleSupplier_BadExtensionElement;

          InternalJET2Platform.log(new Status(
            IStatus.ERROR,
            configElements[i].getDeclaringExtension().getNamespace(),
            IStatus.OK,
            MessageFormat.format(msg, new Object []{ DEPLOYEDTRANSFORM_ELEMENT, EXTENSION_POINT_ID }),
            null));
        }
      }
    }
  }

  public Bundle load(String id, IProgressMonitor monitor) throws BundleException
  {
    Bundle bundle = null;
    final DynamicJETBundleDescriptor descriptor = (DynamicJETBundleDescriptor)getDescriptor(id);
    if(descriptor != null)
    {
      bundle = InternalJET2Platform.getDefault().getJETBundleInstaller().installBundle(descriptor.getBundleURL());
      if(DEBUG) System.out.println("[deployedBundle] Loaded " + bundle.getSymbolicName()); //$NON-NLS-1$
    }
    return bundle;
  }

  public void unload(String id) throws BundleException
  {
    if(descriptorsById.containsKey(id))
    {
      Bundle bundle = Platform.getBundle(id);
      unload(bundle);
    }
  }

  public void unload(Bundle bundle) throws BundleException
  {
    if(bundle != null)
    {
      if(DEBUG) System.out.println("[deployedBundle] Unloading " + bundle.getSymbolicName()); //$NON-NLS-1$
      bundle.uninstall();
    }
  }

  public Collection getAllJETBundleDescriptors()
  {
    return descriptorsById.values();
  }

}
