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
/**
 * <copyright>
 *
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.jet.internal.extensionpoints;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.InternalJET2Platform.IMethodTimer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.model.EclipseExtensionLoaderFactory;
import org.eclipse.jet.runtime.model.ILoaderManager;


/**
 * Manager for 'modelLoaders' Eclipse extension point.
 *
 */
public final class ModelLoaderExtManager implements IRegistryChangeListener
{
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/modelLoaderExtensions")).booleanValue(); //$NON-NLS-1$

  private static final String PLUGIN_ID = "org.eclipse.jet"; //$NON-NLS-1$

  private static final String EXTENSION_NAME = "modelLoaders"; //$NON-NLS-1$

  private static final String EXTENSION_POINT_ID = PLUGIN_ID + "." + EXTENSION_NAME; //$NON-NLS-1$

  private final Map tagLibraries = new HashMap();

  private static final String E_LOADER = "loader"; //$NON-NLS-1$
  private static final String A_LOADER_ID = "id"; //$NON-NLS-1$
  private static final String A_LOADER_NAME = "name"; //$NON-NLS-1$
  private static final String A_LOADER_CLASS = "class"; //$NON-NLS-1$
  private static final String A_LOADER_DYNAMICTYPES = "dynamicTypes"; //$NON-NLS-1$
  private static final String E_DEFAULTTYPELOADER = "defaultTypeLoader"; //$NON-NLS-1$
  private static final String A_DEFAULTTYPELOADER_FILETYPE = "fileType"; //$NON-NLS-1$
  private static final String A_DEFAULTTYPELOADER_ID = "id"; //$NON-NLS-1$
  private static final String E_LOADABLETYPE = "loadableType"; //$NON-NLS-1$
  private static final String A_LOADABLETYPE_FILETYPE = "fileType"; //$NON-NLS-1$
  private static final String A_LOADABLETYPE_ID = "id"; //$NON-NLS-1$
  private static final String E_TYPE = "type"; //$NON-NLS-1$
  private static final String A_TYPE_FILETYPE = "fileType"; //$NON-NLS-1$

  /**
   * 
   */
  public ModelLoaderExtManager()
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
        if(DEBUG)
        {
          System.out.println("ModelLoaderExtManager.registryChanged(add " + ext.getContributor().getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        addConfigElements(ext.getConfigurationElements());
      }
      else
      {
        if(DEBUG) {
          System.out.println("ModelLoaderExtManager.registryChanged(remove " + ext.getContributor().getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
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
    ILoaderManager mgr = JETActivatorWrapper.INSTANCE.getLoaderManager();
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_LOADER.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(A_LOADER_ID);
        String fullId = configElements[i].getDeclaringExtension().getNamespace() + "." + id; //$NON-NLS-1$
        String name = configElements[i].getAttribute(A_LOADER_NAME);
        boolean dynamic = Boolean.valueOf(configElements[i].getAttribute(A_LOADER_DYNAMICTYPES)).booleanValue();
        mgr.addLoader(fullId, name, new EclipseExtensionLoaderFactory(configElements[i], A_LOADER_CLASS), dynamic);
        for (int j = 0; j < configElements[i].getChildren(E_TYPE).length; j++)
        {
          IConfigurationElement typeElement = configElements[i].getChildren(E_TYPE)[j];
          String fileType = typeElement.getAttribute(A_TYPE_FILETYPE);
          mgr.addLoaderForType(fullId, fileType);
        }
      }
      else if(E_LOADABLETYPE.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(A_LOADABLETYPE_ID);
        String fileType = configElements[i].getAttribute(A_LOADABLETYPE_FILETYPE);
        mgr.addLoaderForType(id, fileType);
      }
      else if(E_DEFAULTTYPELOADER.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(A_DEFAULTTYPELOADER_ID);
        String fileType = configElements[i].getAttribute(A_DEFAULTTYPELOADER_FILETYPE);
        
        try
        {
          mgr.setDefaultLoader(fileType, id);
        }
        catch (IllegalStateException e)
        {
          String msg = MessageFormat.format(JET2Messages.ModelLoaderExtManager_TypeAlreadyDefined, new Object[] {fileType, mgr.getDefaultModelLoaderId(fileType), id });
          IStatus status = new Status(IStatus.WARNING, configElements[i].getDeclaringExtension().getNamespace(),IStatus.OK, msg, null  );
          InternalJET2Platform.getDefault().getLog().log(status);
        }
      }
    }
  }


  private void removeConfigElements(IConfigurationElement[] configElements)
  {
    ILoaderManager mgr = JETActivatorWrapper.INSTANCE.getLoaderManager();
    for (int i = 0; i < configElements.length; i++)
    {
      if (E_LOADER.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(A_LOADER_ID);
        String fullId = configElements[i].getDeclaringExtension().getNamespace() + "." + id; //$NON-NLS-1$
        
        mgr.removeLoader(fullId);
        
        for (int j = 0; j < configElements[i].getChildren(E_TYPE).length; j++)
        {
          IConfigurationElement typeElement = configElements[i].getChildren(E_TYPE)[j];
          String fileType = typeElement.getAttribute(A_TYPE_FILETYPE);
          mgr.removeLoaderForType(fullId, fileType);
        }
      }
      else if(E_LOADABLETYPE.equals(configElements[i].getName()))
      {
        String id = configElements[i].getAttribute(A_LOADABLETYPE_ID);
        String fileType = configElements[i].getAttribute(A_LOADABLETYPE_FILETYPE);
        mgr.removeLoaderForType(id, fileType);
      }
      else if(E_DEFAULTTYPELOADER.equals(configElements[i].getName()))
      {
        String fileType = configElements[i].getAttribute(A_DEFAULTTYPELOADER_FILETYPE);
        
        mgr.clearDefaultLoader(fileType);
      }
    }
  }

}
