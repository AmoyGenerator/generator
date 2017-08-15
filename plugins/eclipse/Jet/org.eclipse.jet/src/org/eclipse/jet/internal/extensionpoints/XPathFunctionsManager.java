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


import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.xpath.XPathFunctionMetaData;


/**
 * Manager for tab library access and iteration with the Eclipse extension registry.
 *
 */
public final class XPathFunctionsManager implements IRegistryChangeListener
{

  public static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/ext/xpathFunctions")).booleanValue(); //$NON-NLS-1$

  private static final String PLUGIN_ID = "org.eclipse.jet"; //$NON-NLS-1$

  private static final String EXTENSION_NAME = "xpathFunctions"; //$NON-NLS-1$

  private static final String EXTENSION_POINT_ID = PLUGIN_ID + "." + EXTENSION_NAME; //$NON-NLS-1$

  private static final String E_FUNCTION = "function"; //$NON-NLS-1$

  private static final String A_FUNCTION_NAME = "name"; //$NON-NLS-1$
  private static final String A_FUNCTION_IMPLEMENTATION = "implementation"; //$NON-NLS-1$
  private static final String A_FUNCTION_MINARGS = "minArgs"; //$NON-NLS-1$
  private static final String A_FUNCTION_MAXARGS = "maxArgs"; //$NON-NLS-1$
  private final Map functionMap = new HashMap();

  /**
   * 
   */
  public XPathFunctionsManager()
  {
    super();
  }

  /**
   * @param configElements
   */
  private void addConfigElements(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_FUNCTION.equals(configElements[i].getName()))
      {
        XPathFunctionMetaData functionData = buildFunctionMetaData(configElements[i]);
        if(functionData != null) {
          if(DEBUG) System.out.println("defining function: " + functionData.getName()); //$NON-NLS-1$
          functionMap.put(functionData.getName(), functionData);
        }
      }
    }
  }

  /**
   * @param configElement
   * @return
   */
  private XPathFunctionMetaData buildFunctionMetaData(IConfigurationElement configElement)
  {
    try
    {
      final String name = configElement.getAttribute(A_FUNCTION_NAME);
      final int minArgs = Integer.parseInt(configElement.getAttribute(A_FUNCTION_MINARGS));
      final int maxArgs = Integer.parseInt(configElement.getAttribute(A_FUNCTION_MAXARGS));
      return new XPathFunctionMetaData(
        name, configElement.getContributor().getName(), 
        new XPathFunctionLazyLoadWrapper(name, configElement, A_FUNCTION_IMPLEMENTATION),
        minArgs, maxArgs);
    }
    catch (NumberFormatException e)
    {
      InternalJET2Platform.log(InternalJET2Platform.newStatus(IStatus.ERROR, "", e)); //$NON-NLS-1$
    }
    catch (NullPointerException e)
    {
      // thrown by XPathFunctionMetaData constructor
      InternalJET2Platform.log(InternalJET2Platform.newStatus(IStatus.ERROR, "", e)); //$NON-NLS-1$
    }
    catch (IllegalArgumentException e)
    {
      // thrown by XPathFunctionMetaData constructor
      InternalJET2Platform.log(InternalJET2Platform.newStatus(IStatus.ERROR, "", e)); //$NON-NLS-1$
    }
    catch (InvalidRegistryObjectException e)
    {
      // just ignore this, we have a stale object
    }
    catch (IllegalStateException e)
    {
      // just ignore this, we have a stale object. BTW, this shouldn't happen, but at least in 3.2M4
      // it does (instead of InvalidRegistryObjectException being thrown.
    }

    return null;
  }

  /**
   * Update the cache of extensions currently loaded
   */
  public void registryChanged(IRegistryChangeEvent event)
  {
    if(DEBUG) System.out.println("Received registry change event"); //$NON-NLS-1$
    if(DEBUG) {
      final IExtensionDelta[] deltas = event.getExtensionDeltas();
      for (int i = 0; i < deltas.length; i++)
      {
        IExtensionDelta id = deltas[i];
        System.out.println(" " + id.getExtension().getContributor().getName() + ": " + id.getExtensionPoint().getUniqueIdentifier() + " (" + (id.getKind() == IExtensionDelta.ADDED ? "added" : "removed") + ")");    //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$//$NON-NLS-6$
        System.out.println("   matches = " + EXTENSION_POINT_ID.equals(id.getExtensionPoint().getUniqueIdentifier())); //$NON-NLS-1$
      }
    }
    
    IExtensionDelta[] deltas = event.getExtensionDeltas(PLUGIN_ID, EXTENSION_NAME);
    if(DEBUG) System.out.println("Found " + deltas.length + " deltas for " + EXTENSION_POINT_ID);  //$NON-NLS-1$//$NON-NLS-2$

    for (int i = 0; i < deltas.length; i++)
    {
      IExtension ext = deltas[i].getExtension();
      if (deltas[i].getKind() == IExtensionDelta.ADDED)
      {
        if(DEBUG) System.out.println("adding config elements"); //$NON-NLS-1$
        addConfigElements(ext.getConfigurationElements());
      }
      else
      {
        if(DEBUG) System.out.println("removing config elements"); //$NON-NLS-1$
        removeConfigElements(ext.getConfigurationElements());
      }
    }
  }

  private void removeConfigElements(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_FUNCTION.equals(configElements[i].getName()))
      {
        String name = configElements[i].getAttribute(A_FUNCTION_NAME);
        if(name != null) {
          if(DEBUG) System.out.println("undefining function: " + name); //$NON-NLS-1$
          functionMap.remove(name);
        }
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
    
    if(DEBUG) System.out.println("shutting down XPath function manager"); //$NON-NLS-1$
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    reg.removeRegistryChangeListener(this);

    functionMap.clear();
    
    timer.done();
  }


  /**
   * Initialize the list of tabLibrary extensions currently loaded.
   *
   */
  public void startup()
  {
    IMethodTimer timer = InternalJET2Platform.getStartupMethodTimer(getClass(), "startup()"); //$NON-NLS-1$
    
    if(DEBUG) System.out.println("starting XPath function manager"); //$NON-NLS-1$
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] configElements = reg.getConfigurationElementsFor(EXTENSION_POINT_ID);
    addConfigElements(configElements);

//    reg.addRegistryChangeListener(this, EXTENSION_POINT_ID);
    reg.addRegistryChangeListener(this, PLUGIN_ID);
    
    timer.done();
  }

  /**
   * Return the function meta data for declared custom functions.
   * @return the functions as an array.
   */
  public XPathFunctionMetaData[] getCustomFunctions() {
    if(DEBUG) System.out.println("Custom functions: " + functionMap.keySet()); //$NON-NLS-1$
    return (XPathFunctionMetaData[])functionMap.values().toArray(new XPathFunctionMetaData[functionMap.size()]);
  }

  public void forceRefresh(String id)
  {
    if(DEBUG) System.out.println("XPathFunctionManager: forced refresh on " + id); //$NON-NLS-1$
    final IExtension[] extensions = Platform.getExtensionRegistry().getExtensions(id);
    for (int i = 0; i < extensions.length; i++)
    {
      if(EXTENSION_POINT_ID.equals(extensions[i].getExtensionPointUniqueIdentifier())) {
        addConfigElements(extensions[i].getConfigurationElements());
      }
    }
    
//    final IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor(PLUGIN_ID, EXTENSION_NAME);
//    for (int i = 0; i < configElements.length; i++)
//    {
//      if(id.equals(configElements[i].getNamespace()))
//      {
//        addConfigElements(new IConfigurationElement[] {configElements[i]});
//      }
//    }
  }
}
