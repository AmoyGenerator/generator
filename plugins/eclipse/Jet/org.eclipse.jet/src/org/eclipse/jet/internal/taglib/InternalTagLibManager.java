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

package org.eclipse.jet.internal.taglib;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.internal.extensionpoints.TagLibraryDataFactory;
import org.eclipse.jet.taglib.TagLibrary;


/**
 * Manager for tab library access and iteration with the Eclipse extension registry.
 *
 */
public final class InternalTagLibManager implements IRegistryChangeListener
{

  private static final String PLUGIN_ID = "org.eclipse.jet"; //$NON-NLS-1$

  private static final String EXTENSION_NAME = "tagLibraries"; //$NON-NLS-1$

  private static final String EXTENSION_POINT_ID = PLUGIN_ID + "." + EXTENSION_NAME; //$NON-NLS-1$

  private final Map tagLibraries = new HashMap();

  private static final String E_TAGLIBRARY = "tagLibrary"; //$NON-NLS-1$

  private static final String A_TAGLIBRARY_ID = "id"; //$NON-NLS-1$

  /**
   * 
   */
  public InternalTagLibManager()
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
    addTagLibraries(configElements);

    reg.addRegistryChangeListener(this, PLUGIN_ID);
    
    timer.done();
  }

  /**
   * @param configElements
   */
  private void addTagLibraries(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_TAGLIBRARY.equals(configElements[i].getName()))
      {
        final String namespace = getNamespace(configElements[i]);
        final TagLibrary tagLibrary = TagLibraryDataFactory.INSTANCE.createTagLibrary(namespace, configElements[i]);
        String libraryId = tagLibrary.getLibraryId();
        if (!tagLibraries.containsKey(libraryId))
        {
          tagLibraries.put(libraryId, tagLibrary);
        }
        else
        {
          // TODO: log duplicate tag library
        }
      }
    }
  }

  /**
   * @param element
   * @return
   * @throws InvalidRegistryObjectException
   */
  private String getNamespace(final IConfigurationElement element) throws InvalidRegistryObjectException
  {
    return element.getDeclaringExtension().getNamespace();
  }

  /**
   * Update the cache of extensions currently loaded
   */
  public void registryChanged(IRegistryChangeEvent event)
  {
    IExtensionDelta[] deltas = event.getExtensionDeltas(PLUGIN_ID, EXTENSION_NAME);

    for (int i = 0; i < deltas.length; i++)
    {
      IExtension ext = deltas[i].getExtension();
      if (deltas[i].getKind() == IExtensionDelta.ADDED)
      {
        addTagLibraries(ext.getConfigurationElements());
      }
      else
      {
        removeTagLibraries(ext.getConfigurationElements());
      }
    }
  }

  private void removeTagLibraries(IConfigurationElement[] configElements)
  {
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_TAGLIBRARY.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(A_TAGLIBRARY_ID);
        if (id != null)
        {
          String libraryId = getNamespace(configElements[i]) + "." + id; //$NON-NLS-1$
          if (tagLibraries.containsKey(libraryId))
          {
            tagLibraries.remove(libraryId);
          }
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
    
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    reg.removeRegistryChangeListener(this);

    tagLibraries.clear();
    
    timer.done();
  }

  public org.eclipse.jet.taglib.TagLibrary getTagLibrary(String id)
  {
    if (id == null)
    {
      throw new NullPointerException();
    }

    return (org.eclipse.jet.taglib.TagLibrary)tagLibraries.get(id);
  }

  public String[] getKnownTagLibraryIds()
  {
    return (String[])tagLibraries.keySet().toArray(new String [tagLibraries.size()]);
  }
  
  public void forceRefresh(String id)
  {
    final IExtension[] extensions = Platform.getExtensionRegistry().getExtensions(id);
    for (int i = 0; i < extensions.length; i++)
    {
      if(EXTENSION_POINT_ID.equals(extensions[i].getExtensionPointUniqueIdentifier())) {
        addTagLibraries(extensions[i].getConfigurationElements());
      }
    }
  }
}
