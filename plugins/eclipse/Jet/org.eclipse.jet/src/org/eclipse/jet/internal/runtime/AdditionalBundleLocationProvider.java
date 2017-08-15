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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.JETPreferences;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * BundleProvider for directories referenced in the JET preferences
 */
public class AdditionalBundleLocationProvider implements IJETBundleProvider, IPreferenceChangeListener
{
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/additionalBundleLoading")).booleanValue(); //$NON-NLS-1$

  private IEclipsePreferences defaultsNode;
  private IEclipsePreferences configurationNode;
  private IEclipsePreferences instanceNode;
  private IEclipsePreferences[] searchList;
  private final IPreferencesService service = Platform.getPreferencesService();
  private final List directoryProviders = new ArrayList();

  private String[] parseAdditionalPathString(String stringList) {
    if(stringList == null) return new String[0];
    StringTokenizer st = new StringTokenizer(stringList, File.pathSeparator
            + "\n\r");//$NON-NLS-1$
    List v = new ArrayList();
    while (st.hasMoreElements()) {
        v.add(st.nextElement());
    }
    return (String[]) v.toArray(new String[v.size()]);
}

  
  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#getAllJETBundleDescriptors()
   */
  public Collection getAllJETBundleDescriptors()
  {
    Set allIds = getAllJETBundleIds();
    
    List descriptors = new ArrayList(allIds.size());
    for (Iterator i = allIds.iterator(); i.hasNext();)
    {
      String id = (String)i.next();
      descriptors.add(getDescriptor(id));
    }
    
    return descriptors;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#getAllJETBundleIds()
   */
  public Set getAllJETBundleIds()
  {
    if(DEBUG) System.out.println("AdditionalBundleLocationProvider.getAllJETBundleIds()");; //$NON-NLS-1$
    Set result = new HashSet();
    for (Iterator i = directoryProviders.iterator(); i.hasNext();)
    {
      DirectoryBundleProvider provider = (DirectoryBundleProvider)i.next();
      result.addAll(provider.getAllJETBundleIds());
    }
    if(DEBUG) System.out.println("   " + result); //$NON-NLS-1$
    return result;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#getDescriptor(java.lang.String)
   */
  public IJETBundleDescriptor getDescriptor(String id)
  {
    if(DEBUG) System.out.println("AdditionalBundleLocationProvider.getDescriptor(" + id + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    IJETBundleDescriptor descriptor = null;
    for (Iterator i = directoryProviders.iterator(); i.hasNext() && descriptor == null;)
    {
      DirectoryBundleProvider provider = (DirectoryBundleProvider)i.next();
      descriptor = provider.getDescriptor(id);
    }
    return descriptor;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#load(java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
   */
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

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#shutdown()
   */
  public void shutdown()
  {
    instanceNode.removePreferenceChangeListener(this);
    directoryProviders.clear();

    configurationNode = null;
    instanceNode = null;
    searchList = null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#startup()
   */
  public void startup()
  {
    if(DEBUG) {
      System.out.println("AdditionalBundleLocationProvider.startup()"); //$NON-NLS-1$
    }
    defaultsNode = new DefaultScope().getNode(JET2Platform.PLUGIN_ID);
    configurationNode = new ConfigurationScope().getNode(JET2Platform.PLUGIN_ID);
    instanceNode = new InstanceScope().getNode(JET2Platform.PLUGIN_ID);
    searchList = new IEclipsePreferences[] {
      instanceNode,
      configurationNode,
      defaultsNode,
    };
    
    final String additonalLocations = service.get(JETPreferences.ADDITIONAL_TEMPLATE_JAR_LOCATIONS, null, searchList);
    addTransforms(additonalLocations);
    
    instanceNode.addPreferenceChangeListener(this);
  }


  /**
   * @param additonalLocations
   */
  private void addTransforms(final String additonalLocations)
  {
    if(DEBUG)
    {
      System.out.println("AdditionalBundleLocationProvider.addTransforms(" + additonalLocations + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    String[] dirNames = parseAdditionalPathString(additonalLocations);
    
    
    directoryProviders.clear();
    
    File[] dirs = new File[dirNames.length];
    for (int i = 0; i < dirNames.length; i++)
    {
      dirs[i] = new File(dirNames[i]);
      if(dirs[i].exists() && dirs[i].isDirectory()) 
      {
        directoryProviders.add(new DirectoryBundleProvider(dirs[i]));
      }
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#unload(java.lang.String)
   */
  public void unload(String id) throws BundleException
  {
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleProvider#unload(org.osgi.framework.Bundle)
   */
  public void unload(Bundle bundle) throws BundleException
  {
    if(bundle != null)
    {
      if(DEBUG) System.out.println("[deployedBundle] Unloading " + bundle.getSymbolicName()); //$NON-NLS-1$
      bundle.uninstall();
    }
  }


  public void preferenceChange(PreferenceChangeEvent event)
  {
    if(DEBUG) {
      System.out.println("AdditionalBundleLocationProvider.preferenceChange()"); //$NON-NLS-1$
      System.out.println("  key: " + event.getKey()); //$NON-NLS-1$
      System.out.println("  old: " + event.getOldValue()); //$NON-NLS-1$
      System.out.println("  new: " + event.getNewValue()); //$NON-NLS-1$
    }
    if(JETPreferences.ADDITIONAL_TEMPLATE_JAR_LOCATIONS.equals(event.getKey())) {
      final String additonalLocations = service.get(JETPreferences.ADDITIONAL_TEMPLATE_JAR_LOCATIONS, null, searchList);
      addTransforms(additonalLocations);
    }
  }

}
