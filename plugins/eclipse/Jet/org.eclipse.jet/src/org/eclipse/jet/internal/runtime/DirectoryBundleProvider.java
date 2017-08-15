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
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Bundle Provider for bundles stored in a directory.
 */
public class DirectoryBundleProvider implements IJETBundleProvider
{
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/additionalBundleLoading")).booleanValue(); //$NON-NLS-1$
  
  private final File directory;

  /**
   * 
   * @param directoryLocation
   */
  public DirectoryBundleProvider(String directoryLocation)
  {
    this(new File(directoryLocation));
  }

  public DirectoryBundleProvider(File directory)
  {
    if(!directory.exists() || !directory.isDirectory())
    {
      throw new IllegalArgumentException();
    }
    this.directory = directory;
    System.out.println("DirectoryBundleProvider.DirectoryBundleProvider(" + directory.getAbsolutePath() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public Collection getAllJETBundleDescriptors()
  {
    final Map descriptorsById = getDescriptorsById();
    return descriptorsById.values();
  }

  /**
   * @return
   */
  private Map getDescriptorsById()
  {
    final Map descriptorsById = new HashMap();
    directory.listFiles(new FileFilter()  {

      public boolean accept(File pathname)
      {
        final BundleJarInfo jarInfo = BundleJarInfo.createBundleJarInfo(pathname.getName());
        
        if(jarInfo != null)
        {
          try
          {
            final URL bundleURL = pathname.toURL();
            final IJETBundleDescriptor descriptor = JETBundleManager.getJETBundleDescriptorForJAR(bundleURL);
            if(descriptor != null) 
            {
              descriptorsById.put(jarInfo.getBundleSymbolicName(), descriptor);
            }
          }
          catch (MalformedURLException e)
          {
            // should not have happened.
          }
        }
        
        return false;
      }});
    return descriptorsById;
  }

  public Set getAllJETBundleIds()
  {
    if(DEBUG) System.out.println("DirectoryBundleProvider.getAllJETBundleIds()"); //$NON-NLS-1$
    
    final Map descriptorsById = getDescriptorsById();
    if(DEBUG) System.out.println("   ids: " + descriptorsById.keySet()); //$NON-NLS-1$
    return descriptorsById.keySet();
  }

  private URL getBundleURL(final String id) 
  {

    File[] jars = directory.listFiles(new FilenameFilter() {

      public boolean accept(File dir, String name)
      {
        final BundleJarInfo jarInfo = BundleJarInfo.createBundleJarInfo(name);
        return jarInfo != null && jarInfo.getBundleSymbolicName().equals(id);
      }});
    
    URL url = null;
    if(jars.length > 0)
    {
      try
      {
        url = jars[0].toURL();
      }
      catch (MalformedURLException e)
      {
        // do nothing...
      }
    }
    
    return url;
  }
  
  public IJETBundleDescriptor getDescriptor(String id)
  {
    if(DEBUG) System.out.println("DirectoryBundleProvider.getDescriptor("+ id + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    URL bundleURL = getBundleURL(id);
    IJETBundleDescriptor descriptor = null;
    
    if(bundleURL != null)
    {
      descriptor = JETBundleManager.getJETBundleDescriptorForJAR(bundleURL);
    }
    
    if(DEBUG) System.out.println("   " + (descriptor == null ? "not found" : "found")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    return descriptor;
  }

  public Bundle load(String id, IProgressMonitor monitor) throws BundleException
  {
    URL bundleURL = getBundleURL(id);
    Bundle bundle = null;
    if(bundleURL != null)
    {
      bundle = InternalJET2Platform.getDefault().getJETBundleInstaller().installBundle(bundleURL);
    }
    return bundle;
  }

  public void shutdown()
  {
  }

  public void startup()
  {
  }

  public void unload(String id) throws BundleException
  {
  }

  public void unload(Bundle bundle) throws BundleException
  {
    if(bundle != null)
    {
      bundle.uninstall();
    }
  }


}
