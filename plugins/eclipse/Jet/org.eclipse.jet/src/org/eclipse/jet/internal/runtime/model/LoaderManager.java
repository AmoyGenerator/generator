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
package org.eclipse.jet.internal.runtime.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.runtime.model.ILoaderFactory;
import org.eclipse.jet.runtime.model.ILoaderManager;
import org.eclipse.jet.runtime.model.IModelLoader;
import org.eclipse.jet.runtime.model.IModelLoaderDescription;

/**
 * Standard implementation of {@link ILoaderManager}.
 */
public class LoaderManager implements ILoaderManager
{
  private static class LoaderData
  {
    private final String name;
    private final ILoaderFactory factory;
    private IModelLoader loader = null;
    private final boolean dynamic;

    /**
     * @return Returns the factory.
     */
    public final ILoaderFactory getFactory()
    {
      return factory;
    }

    /**
     * @return Returns the name.
     */
    public final String getName()
    {
      return name;
    }

    public LoaderData(String name, ILoaderFactory factory, boolean dynamic)
    {
      this.name = name;
      this.factory = factory;
      this.dynamic = dynamic;
      
    }

    public IModelLoader getLoader()
    {
      if(loader == null)
      {
        loader = (IModelLoader)factory.create();
      }
      return loader;
    }
    
    public boolean canHandle(String fileType)
    {
      if(dynamic)
      {
        IModelLoader theLoader = getLoader();
        
        return theLoader != null ? theLoader.canLoad(fileType) : false;
      }
      else
      {
        return false;
      }
    }
    
    public String toString()
    {
      return name + " (dynamic=" + dynamic + ")";  //$NON-NLS-1$//$NON-NLS-2$
    }
  }

  /**
   * A Map&lt;String,LoaderData&gt;
   */
  private final Map loaders = new HashMap();
  
  /**
   * A Map&lt;String(filetype), String(loaderid)&gt;
   */
  private final Map defaultLoaders = new HashMap();
  
  /**
   * A Map&lt;String(fileType), Set&lt;String(loaderId)&gt; &gt;
   */
  private final Map modelLoadersByType = new HashMap();
  
  /**
   * 
   */
  public LoaderManager()
  {
    // do nothing
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.ILoaderManager#addLoader(java.lang.String, java.lang.String, org.eclipse.jet.runtime.model.ILoaderFactory)
   */
  public void addLoader(String id, String name, ILoaderFactory factory, boolean dynamic)
  {
    loaders.put(id, new LoaderData(name, factory, dynamic));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.ILoaderManager#removeLoader(java.lang.String)
   */
  public void removeLoader(String id)
  {
    loaders.remove(id);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.ILoaderManager#getModelLoader(java.lang.String)
   */
  public IModelLoader getModelLoader(String id)
  {
    final LoaderData data = (LoaderData)loaders.get(id);
    return data != null ? data.getLoader() : null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.ILoaderManager#findDefaultModelLoader(java.lang.String)
   */
  public IModelLoader getDefaultModelLoader(String fileType)
  {
    String id = (String)defaultLoaders.get(fileType);
    return id != null ? getModelLoader(id) : null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.ILoaderManager#findCompatibleModelLoaders(java.lang.String)
   */
  public IModelLoaderDescription[] findCompatibleModelLoaders(String fileType)
  {
    List result = new ArrayList();

    /**
     * Look for dynamic model loaders...
     */
    for (Iterator i = loaders.entrySet().iterator(); i.hasNext();)
    {
      Map.Entry entry = (Map.Entry)i.next();
      LoaderData data = (LoaderData)entry.getValue();
      if(data.canHandle(fileType)){
        result.add(new ModelLoaderDescription((String)entry.getKey(), data.getName()));
      }
    }
    
    /*
     * Look for staticly declared model loaders
     */
    Set staticLoaderIds = (Set)modelLoadersByType.get(fileType);
    if(staticLoaderIds != null)
    {
      List staticLoaders = new ArrayList(staticLoaderIds.size());
      for (Iterator i = staticLoaderIds.iterator(); i.hasNext();)
      {
        String id = (String)i.next();
        final LoaderData loaderData = (LoaderData)loaders.get(id);
        if(loaderData != null)
        {
          staticLoaders.add(new ModelLoaderDescription(id, loaderData.getName()));
        }
      }
      result.addAll(staticLoaders);
    }
    
    return (IModelLoaderDescription[])result.toArray(new IModelLoaderDescription[result.size()]);
  }

  public void setDefaultLoader(String fileType, String id)
  {
    if(defaultLoaders.containsKey(fileType))
    {
      throw new IllegalStateException();
    }
    defaultLoaders.put(fileType, id);
  }

  public void clearDefaultLoader(String fileType)
  {
    defaultLoaders.remove(fileType);
  }

  public String getDefaultModelLoaderId(String fileType)
  {
    return (String)defaultLoaders.get(fileType);
  }

  public void addLoaderForType(String id, String fileType)
  {
    Set loaderSet = (Set)modelLoadersByType.get(fileType);
    if(loaderSet == null)
    {
      loaderSet = new HashSet();
      modelLoadersByType.put(fileType, loaderSet);
    }
    loaderSet.add(id);
  }

  public void removeLoaderForType(String id, String fileType)
  {
    Set loaderSet = (Set)modelLoadersByType.get(fileType);
    if(loaderSet != null)
    {
      loaderSet.remove(id);
    }
  }

  public IModelLoader getLoader(String url, String loaderId, String type) throws CoreJETException
  {
    IModelLoader loader = null;
    if (loaderId == null)
    {
      if (type == null && url != null)
      {
        int index = url.lastIndexOf('.');
        if (index != -1)
        {
          type = url.substring(index + 1);
        }
      }
      loaderId = getDefaultModelLoaderId(type);
    }    
    
    if(loaderId == null) {
      final IModelLoaderDescription[] candidateLoaders = findCompatibleModelLoaders(type);
      if (candidateLoaders.length == 1)
      {
        loaderId = candidateLoaders[0].getId();
      }
      else if (candidateLoaders.length == 0)
      {
//        final String msg = MessageFormat.format("Could not find a loader for ''{0}''", new Object []{ url });
//        throw new CoreJETException(new BasicDiagnostic(Diagnostic.ERROR, JET2Platform.PLUGIN_ID, IStatus.OK, msg, null));
        loaderId = "org.eclipse.jet.emfxml"; //$NON-NLS-1$
      }
      else
      {
        final String msg = MessageFormat.format(JET2Messages.LoaderManager_MultipleLoaders, 
          new Object []{ url, candidateLoaders.toString() });
        throw new CoreJETException(msg);
      }
    }

    loader = getModelLoader(loaderId);
    
    if(loader == null)
    {
      final String msg = MessageFormat.format(JET2Messages.LoaderManager_CouldNotFindLoader, new Object []{ url });
      throw new CoreJETException(msg);
    }
    
    return loader;
  }

}
