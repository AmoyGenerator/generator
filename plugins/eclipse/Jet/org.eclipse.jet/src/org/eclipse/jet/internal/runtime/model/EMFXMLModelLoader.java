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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jet.runtime.model.IModelLoader;

/**
 * Loader for XML documents using the EMF generic XML support.
 */
public class EMFXMLModelLoader implements IModelLoader
{

  private static final String XML_TYPE = "xml"; //$NON-NLS-1$
  private static final String EMPTY_STRING = ""; //$NON-NLS-1$

  private final static class MyExtendedMetaData extends BasicExtendedMetaData {
    
    
    public MyExtendedMetaData(Registry registry)
    {
      super(registry);
    }

    public EPackage getPackage(String namespace)
    {
      // Check the demand registry for packages. BasicExtendedMetaData does not do this.
      // As a result, demand created packages (packages corresponding to XML namespaces
      // for which there is no schema) are lost. This override finds them and thus
      // allows parsing of this category of document.
      EPackage ePkg = super.getPackage(namespace);
      if(ePkg == null) {
        ePkg = demandRegistry.getEPackage(namespace);
      }
      return ePkg;
    }
  }
  /**
   * 
   */
  public EMFXMLModelLoader()
  {
    super();
  }

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
   * @param resourceSet TODO
   * @return
   */
  private Map getLoadOptions(ResourceSet resourceSet)
  {
    Map options = new HashMap();
    options.put(XMLResource.OPTION_EXTENDED_META_DATA, new MyExtendedMetaData(resourceSet.getPackageRegistry()));
    return options;
  }

  /**
   * @return
   */
  private ResourceSet getResourceSet()
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    // The following code could replace MyGenericXMLResourceFactoryImpl once the following conditions are met:
    // 1) JET depends on EMF 2.4 or later
    // 2) EMF implements code in XMLHandler.resolveEntity(String,String) to set publicId and baseLocation
//    resourceSet.setURIConverter(new ExtensibleURIConverterImpl(){
//      public InputStream createInputStream(URI uri, Map options) throws IOException
//      {
//        if(options != null && options.containsKey("publicId") && options.containsKey("baseLocation")) {
//          final URIResolverProxy resolver = EntityResolverFactory.getURIResolver();
//          if(resolver != null) {
//            final String publicId = (String)options.get("publicId");
//            final String baseLocation = (String)options.get("baseLocation");
//            final String systemId = resolver.resolve(baseLocation, publicId, uri.toString());
//            return super.createInputStream(URI.createURI(systemId));
//          }
//        }
//        return super.createInputStream(uri, options);
//      }
//    });
    // register the generic XML resource factory...
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(XML_TYPE,
      new MyGenericXMLResourceFactoryImpl());
    return resourceSet;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL)
   */
  public Object load(URL modelUrl) throws IOException
  {
    return load(modelUrl, XML_TYPE);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL, java.lang.String)
   */
  public Object load(URL modelUrl, String kind) throws IOException
  {
    URI emfURI = URI.createURI(modelUrl.toExternalForm());
    ResourceSet resourceSet = getResourceSet();

    final Resource.Factory factory = (Factory)resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().get(XML_TYPE);
    
    // add the URI's file extension to the resource set's factory registry, and create the resource
    // via the resource set, otherwise, the resource is not part of the resource set !?!?
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(emfURI.fileExtension(), factory);
//    Resource emfResource = factory.createResource(emfURI);
    Resource emfResource = resourceSet.createResource(emfURI);

    Map options = getLoadOptions(resourceSet);
    emfResource.load(options);

    return getDocumentRoot(emfResource);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#loadFromString(java.lang.String, java.lang.String)
   */
  public Object loadFromString(String serializedModel, String kind) throws IOException
  {
    URI emfURI = URI.createURI("temp://in-memory-string.xml"); //$NON-NLS-1$

    ResourceSet resourceSet = getResourceSet();

    if(kind == null) {
      kind = XML_TYPE;
    }
    final Resource.Factory factory = (Factory)resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().get(kind);
    Resource emfResource = factory.createResource(emfURI);
    Map options = getLoadOptions(resourceSet);
    emfResource.load(new ByteArrayInputStream(serializedModel.getBytes("UTF-8")), options); //$NON-NLS-1$

    return getDocumentRoot(emfResource);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#canLoad(java.lang.String)
   */
  public boolean canLoad(String kind)
  {
    throw new UnsupportedOperationException();
  }

}
