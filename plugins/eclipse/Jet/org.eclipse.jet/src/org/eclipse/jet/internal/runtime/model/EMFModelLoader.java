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

package org.eclipse.jet.internal.runtime.model;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl;
import org.eclipse.jet.runtime.model.IModelLoader;


/**
 * Load EMF Models
 *
 */
public class EMFModelLoader implements IModelLoader
{

  private static final String EMPTY_STRING = ""; //$NON-NLS-1$

  /**
   * Return the logial XML Root of the EMF resource, as defined by the EObjectInspector.
   * If the loaded resource results from an XSD-based schema, then instance of the ECore class
   * that corresponds the the XML Document is returned. Otherwise, the Resource itself is returned.
   * @param emfResource the EMF Resource examine
   * @return the object that is the logical document root
   */
  private Object getDocumentRoot(Resource emfResource)
  {
    EObject first = (EObject)emfResource.getContents().get(0);

    if (EMPTY_STRING.equals(ExtendedMetaData.INSTANCE.getName(first.eClass())))
    {
      // its an EMF representation of a document root, return it...
      return first;
    }
    else
    {
      // otherwise, return the resource itself...
      return emfResource;

    }
  }

  /**
   * Create the standard load options for EMF Resources. This method includes a request that
   * EMF ExtendedMetaData be respected.
   * @return
   */
  private Map getLoadOptions()
  {
    Map options = new HashMap();
    options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
    options.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
    return options;
  }

  /**
   * @return
   */
  private ResourceSet getResourceSet()
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    // register the generic XML resource factory...
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", //$NON-NLS-1$
      new GenericXMLResourceFactoryImpl());
    return resourceSet;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL)
   */
  public Object load(URL modelUrl) throws IOException
  {
    URI emfURI = URI.createURI(modelUrl.toExternalForm());
    ResourceSet resourceSet = getResourceSet();

    Resource emfResource = resourceSet.createResource(emfURI);

    Map options = getLoadOptions();
    emfResource.load(options);

    return getDocumentRoot(emfResource);
  }

  public Object load(URL modelUrl, String kind) throws IOException
  {
    URI emfURI = URI.createURI(modelUrl.toExternalForm());
    ResourceSet resourceSet = getResourceSet();

    final Resource.Factory factory = (Factory)resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().get(kind);
    
    // add the URI's file extension to the resource set's factory registry, and create the resource
    // via the resource set, otherwise, the resource is not part of the resource set !?!?
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(emfURI.fileExtension(), factory);
//  Resource emfResource = factory.createResource(emfURI);
    Resource emfResource = resourceSet.createResource(emfURI);

    Map options = getLoadOptions();
    emfResource.load(options);

    return getDocumentRoot(emfResource);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#loadFromString(java.lang.String)
   */
  public Object loadFromString(String serializedModel, String kind) throws IOException
  {
    URI emfURI = URI.createURI("temp://in-memory-string.xml"); //$NON-NLS-1$

    ResourceSet resourceSet = getResourceSet();

    final Resource.Factory factory = (Factory)resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().get(kind);
    Resource emfResource = factory.createResource(emfURI);

    Map options = getLoadOptions();

    emfResource.load(new ByteArrayInputStream(serializedModel.getBytes("UTF-8")), options); //$NON-NLS-1$

    return getDocumentRoot(emfResource);
  }

  public boolean canLoad(String kind)
  {
    return Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey(kind);
  }

}
