/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: MyGenericXMLResourceFactoryImpl.java,v 1.1 2008/04/23 18:57:35 pelder Exp $
 */
package org.eclipse.jet.internal.runtime.model;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.SAXXMLHandler;
import org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Override of {@link GenericXMLResourceFactoryImpl} that installs a JET customized
 * {@link EntityResolver}.
 */
public class MyGenericXMLResourceFactoryImpl extends GenericXMLResourceFactoryImpl
{

  /* (non-Javadoc)
   * @see org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl#createResource(org.eclipse.emf.common.util.URI)
   */
  public Resource createResource(URI uri)
  {
    // get our entity resolver...
    final EntityResolver entityResolver = EntityResolverFactory.getEntityResolver(uri.toString());
    
    // Create a customized XMLResourceImpl that uses it.
    final XMLResource actualResource = new XMLResourceImpl(uri) {
      protected XMLLoad createXMLLoad()
      {
        return new XMLLoadImpl(createXMLHelper()) {
          
          protected DefaultHandler makeDefaultHandler()
          {
            return new SAXXMLHandler(resource,helper,options) {
              public InputSource resolveEntity(String publicId, String systemId) throws SAXException
              {
                
                try
                {
                  return entityResolver.resolveEntity(publicId, systemId);
                }
                catch (IOException e)
                {
                  throw new SAXException(e);
                }
              }
            };
          }
          
        };
      }
    };

    // Copy properties of the XMLResource as created by the super class
    final XMLResource protoResource = (XMLResource)super.createResource(uri);
    actualResource.setEncoding(protoResource.getEncoding());
    actualResource.getDefaultLoadOptions().putAll(protoResource.getDefaultLoadOptions());
    actualResource.getDefaultSaveOptions().putAll(protoResource.getDefaultSaveOptions());
    
    return actualResource;
  }

}
