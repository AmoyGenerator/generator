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
package org.eclipse.jet.internal.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.JET2TemplateLoader;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.internal.core.url.URLUtility;
import org.eclipse.jet.internal.extensionpoints.TransformData;
import org.eclipse.jet.internal.extensionpoints.TransformDataFactory;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.transform.IJETBundleManager;
import org.eclipse.jet.transform.IJETRunnable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * A Manager for JET bundles
 */
public class JETBundleManager implements IJETBundleManager
{

  private static class BundleWrapper {
    private final Bundle bundle;
    private int refCount;
    private final IJETBundleProvider provider;
    
    public BundleWrapper(IJETBundleProvider provider, Bundle bundle)
    {
      this.provider = provider;
      this.bundle = bundle;
      refCount = 0;
    }
    
    public void connect()
    {
      ++refCount;
    }
    
    public void disconnect()
    {
      --refCount;
    }
    
    public Bundle getBundle()
    {
      return bundle;
    }

    public boolean isDisconnected()
    {
      return refCount <= 0;
    }

    /**
     * @return Returns the provider.
     */
    public final IJETBundleProvider getProvider()
    {
      return provider;
    }
  }
  /**
   * @param is
   */
  private static void ensureStreamClosed(InputStream is)
  {
    if(is != null)
    {
      try
      {
        is.close();
      }
      catch(IOException e)
      {
        // do nothing
      }
    }
  }
  
  public static JETBundleManifest loadManifest(URL pluginURL) throws IOException, NotABundleException
  {
    URL manifestURL = new URL(pluginURL, JarFile.MANIFEST_NAME);
    
    InputStream manifestStream = null;
    try
    {
      manifestStream = manifestURL.openStream();
      Manifest m = new Manifest(manifestStream);
      return new JETBundleManifest(manifestToProperties(m.getMainAttributes()));
    }
    finally
    {
      ensureStreamClosed(manifestStream);
    }
  }
  
  /**
   * Return a JET bundle descriptor the JAR refered to by <code>bundleURL</code>.
   * @param bundleURL a URL to a bundle JAR
   * @return the bundle descriptor, or <code>null</code> if this is not a JET bundle.
   */
  public static IJETBundleDescriptor getJETBundleDescriptorForJAR(URL bundleURL) 
  {
    IJETBundleDescriptor descriptor = null;
    try
    {
      URL jarURL = URLUtility.jarRootEntryURL(bundleURL);
      JETBundleManifest manifest = JETBundleManager.loadManifest(jarURL);
      TransformData transformData = TransformDataFactory.INSTANCE.createTransformData(manifest.getTransformId(), jarURL);
      
      descriptor = new DynamicJETBundleDescriptor(manifest, transformData, jarURL, bundleURL);
      if(descriptor.getTemplateLoaderClass() == null) {
        descriptor = null;
      }
    }
    catch (MalformedURLException e)
    {
      InternalJET2Platform.logError("Failed to correctly format a jar URL. bundleURL = " + bundleURL.toExternalForm(), e); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      // can't read. Assume it's not a JET bundle.
    }
    catch (NotABundleException e)
    {
      // MANIFEST.MF is not an OSGi bundle, so it's not a JET bundle.
    }
    return descriptor;
  }
  
  private static Properties manifestToProperties(Attributes d) {
    Iterator iter = d.keySet().iterator();
    Properties result = new Properties();
    while (iter.hasNext()) {
        Attributes.Name key = (Attributes.Name) iter.next();
        result.put(key.toString(), d.get(key));
    }
    return result;
}

  private InstalledJETBundleProvider installedBundleProvider;
  
  private ProjectJETBundleProvider projectBundleProvider;
  
  private DeployedJETBundleProvider deployedBundleProvider;
  
  private AdditionalBundleLocationProvider additionalBundleLocationProvider;
  
  private IJETBundleProvider[] providerSearchOrder;
  
  private Map bundlesById = new HashMap();
  
  public JETBundleManager()
  {
    // do nothing
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleManager#getAllJETBundleDescriptors()
   */
  public IJETBundleDescriptor[] getAllJETBundleDescriptors()
  {
    
    final String[] allIds = getAllTransformIds();
    IJETBundleDescriptor[] result = new IJETBundleDescriptor[allIds.length];
    for (int i = 0; i < allIds.length; i++)
    {
      result[i] = getDescriptor(allIds[i]);
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleManager#getDescriptor(java.lang.String)
   */
  public IJETBundleDescriptor getDescriptor(String id)
  {
    IJETBundleDescriptor descriptor = null;
    for (int i = 0; i < providerSearchOrder.length && descriptor == null; i++)
    {
      IJETBundleProvider provider = providerSearchOrder[i];
      descriptor = provider.getDescriptor(id);
    }
    return descriptor;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleManager#getDescriptorForProject(java.lang.String)
   */
  public IJETBundleDescriptor getDescriptorForProject(String name)
  {
    return projectBundleProvider.getDescriptorForProject(name);
  }

//  /**
//   * @param bundleURL
//   * @return
//   * @throws MalformedURLException
//   * @throws IOException
//   */
//  private ResourceBundle getResourceBundle(URL bundleURL)
//  {
//    InputStream isPluginProperties = null;
//    ResourceBundle translationBundle = null;
//    try
//    {
//      URL urlPluginProperties = new URL(bundleURL, "plugin.properties"); //$NON-NLS-1$
//      isPluginProperties = urlPluginProperties.openStream();
//      translationBundle = new PropertyResourceBundle(isPluginProperties);
//    }
//    catch (IOException e)
//    {
//      // didn't work, do nothing;
//    }
//    finally
//    {
//      ensureStreamClosed(isPluginProperties);
//    }
//    return translationBundle;
//  }
  
  public void shutdown()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "shutdown()"); //$NON-NLS-1$
    
    for (Iterator i = bundlesById.values().iterator(); i.hasNext();)
    {
      BundleWrapper wrappedBundle = (BundleWrapper)i.next();
      try
      {
        wrappedBundle.getProvider().unload(wrappedBundle.getBundle());
      }
      catch (BundleException e)
      {
        InternalJET2Platform.logError(JET2Messages.JET2Bundle_CouldNotLoadJetBundle, e);
      }
    }
    bundlesById.clear();
    
    installedBundleProvider.shutdown();
    installedBundleProvider = null;
   
    projectBundleProvider.shutdown();
    projectBundleProvider = null;
    
    deployedBundleProvider.shutdown();
    deployedBundleProvider = null;
    
    additionalBundleLocationProvider.shutdown();
    additionalBundleLocationProvider = null;
    
    providerSearchOrder = null;
    
    timer.done();
  }
  

  public void startup()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    
    // setup the extension 'org.eclipse.jet.transform' listener on loaded Eclipse plug-ins
    installedBundleProvider = new InstalledJETBundleProvider();
    installedBundleProvider.startup();

    projectBundleProvider = new ProjectJETBundleProvider();
    projectBundleProvider.startup();
    
    deployedBundleProvider = new DeployedJETBundleProvider();
    deployedBundleProvider.startup();
    
    additionalBundleLocationProvider = new AdditionalBundleLocationProvider();
    additionalBundleLocationProvider.startup();
    
    // Note: The order here is significant. Changing it will have impacts on
    // which transform is found.
    providerSearchOrder = new IJETBundleProvider[] {
      installedBundleProvider,
      projectBundleProvider,
      additionalBundleLocationProvider,
      deployedBundleProvider,
    };
    
    timer.done();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleManager#getProjectForId(java.lang.String)
   */
  public String getProjectForId(String id)
  {
    return projectBundleProvider.getProjectForId(id);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.IJETBundleManager#run(java.lang.String, org.eclipse.jet.transform.IJETRunnable, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void run(String id, IJETRunnable runnable, IProgressMonitor monitor) throws BundleException
  {
    monitor.beginTask(JET2Messages.JET2Platform_Executing, 100);
    try
    {
      connect(id, new SubProgressMonitor(monitor, 10));
      try
      {
        final IJETBundleDescriptor descriptor = getDescriptor(id);
        JET2TemplateLoader loader = getTemplateLoader(id);
        runnable.run(descriptor, loader, new SubProgressMonitor(monitor, 90));
      }
      finally
      {
        disconnect(id);
      }
    }
    finally
    {
      monitor.done();
    }
    
  }

  private IJETBundleProvider getProvider(String id)
  {
    for (int i = 0; i < providerSearchOrder.length; i++)
    {
      IJETBundleProvider provider = providerSearchOrder[i];
      if(providerSearchOrder[i].getDescriptor(id) != null)
      {
        return provider;
      }
    }
    return null;
  }

  public void connect(final String id, final IProgressMonitor monitor) throws BundleException
  {
    BundleWrapper wrappedBundle = null;
    synchronized(bundlesById)
    {
      wrappedBundle = (BundleWrapper)bundlesById.get(id);
    
      if(wrappedBundle == null)
      {
        IJETBundleProvider provider = getProvider(id);
        if(provider != null)
        { 
          Bundle bundle;
          bundle = provider.load(id, monitor);
          wrappedBundle = new BundleWrapper(provider, bundle);
        
          bundlesById.put(id, wrappedBundle);
        }
      }
      if(wrappedBundle != null)
      {
        wrappedBundle.connect();
      }
      else
      {
        throw new BundleException(JET2Messages.TemplateBundleManager_CannotLoad);
      }
    }
  }

  public void disconnect(String id)
  {
    synchronized(bundlesById)
    {
      BundleWrapper wrappedBundle = (BundleWrapper)bundlesById.get(id);
      if(wrappedBundle == null)
      {
        return;
      }
      
      wrappedBundle.disconnect();
      if(wrappedBundle.isDisconnected())
      {
        bundlesById.remove(id);
        try
        {
          wrappedBundle.getProvider().unload(wrappedBundle.getBundle());
        }
        catch (BundleException e)
        {
          InternalJET2Platform.logError(JET2Messages.JET2Bundle_CouldNotLoadJetBundle, e);
        }
      }
    }
  }

  private URL getTemplateMapURL(String id)
  {
    final BundleWrapper wrappedBundle = (BundleWrapper)bundlesById.get(id);
    if(wrappedBundle != null) {
      return wrappedBundle.getBundle().getResource("jetTemplateMap.properties"); //$NON-NLS-1$
    } else {
      return null;
    }
  }
  
  public JET2TemplateLoader getTemplateLoader(String id) throws BundleException
  {
    synchronized(bundlesById)
    {
      final BundleWrapper wrappedBundle = (BundleWrapper)bundlesById.get(id);
      if(wrappedBundle != null)
      {
        try
        {
          URL mapURL = getTemplateMapURL(id);
          if(mapURL != null) {
            try
            {
              JET2TemplateLoaderImpl loader = new JET2TemplateLoaderImpl(wrappedBundle.getBundle(), mapURL);
              return loader;
            }
            catch (IOException e)
            {
              e.printStackTrace();
            }
          }
          final Class loaderClass = wrappedBundle.getBundle().loadClass(wrappedBundle.getProvider().getDescriptor(id).getTemplateLoaderClass());
          final Object newInstance = loaderClass.newInstance();
          if(newInstance instanceof JET2TemplateLoader)
          {
            return (JET2TemplateLoader)newInstance;
          }
        }
        catch (ClassNotFoundException e)
        {
          throw new BundleException(
            JET2Messages.JET2Bundle_CouldNotLoadLoader, e);
        }
        catch (IllegalAccessException e)
        {
          throw new BundleException(JET2Messages.JET2Bundle_CouldNotInstantiateLoader, e);
        }
        catch (InstantiationException e)
        {
          throw new BundleException(JET2Messages.JET2Bundle_CouldNotInstantiateLoader, e);
        }
      }
    }
    throw new BundleException(JET2Messages.JET2Bundle_CouldNotLoadLoader);
  }

  public String[] getAllTransformIds()
  {
    SortedSet allIds = new TreeSet();
    for (int i = 0; i < providerSearchOrder.length; i++)
    {
      IJETBundleProvider provider = providerSearchOrder[i];
      
      Collection descriptors = provider.getAllJETBundleDescriptors();
      for (Iterator j = descriptors.iterator(); j.hasNext();)
      {
        IJETBundleDescriptor descriptor = (IJETBundleDescriptor)j.next();
        if(!descriptor.isPrivate())
        {
          allIds.add(descriptor.getId());
        }
      }
    }
    return (String[])allIds.toArray(new String[allIds.size()]);
  }

}
