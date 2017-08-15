/**
 * <copyright>
 *
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.taglib.TagLibraryReferenceImpl;
import org.eclipse.jet.taglib.TagLibraryReference;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFactory;


/**
 * A class than translates Extension Registry data into TransformData instances
 */
public class TransformDataFactory
{
  public static final String E_TRANSFORM = "transform"; //$NON-NLS-1$

  public static final String A_TRANSFORM_STARTTEMPLATE = "startTemplate"; //$NON-NLS-1$

  public static final String A_TRANSFORM_TEMPLATELOADERCLASS = "templateLoaderClass"; //$NON-NLS-1$

  public static final String A_TRANSFORM_MODELLOADER = "modelLoader"; //$NON-NLS-1$

  public static final String A_TRANSFORM_MODELEXTENSION = "modelExtension"; //$NON-NLS-1$

  public static final String A_TRANSFORM_OVERRIDES = "overrides"; //$NON-NLS-1$

  public static final String E_TAGLIBRARIES = "tagLibraries"; //$NON-NLS-1$

  public static final String E_IMPORTLIBRARY = "importLibrary"; //$NON-NLS-1$

  public static final String A_IMPORTLIBRARY_ID = "id"; //$NON-NLS-1$

  public static final String A_IMPORTLIBRARY_USEPREFIX = "usePrefix"; //$NON-NLS-1$

  public static final String A_IMPORTLIBRARY_AUTOIMPORT = "autoImport"; //$NON-NLS-1$

  public static final String E_DESCRIPTION = "description"; //$NON-NLS-1$

  public static final TransformDataFactory INSTANCE = new TransformDataFactory();

  private static final String A_TRANSFORM_PRIVATE = "private"; //$NON-NLS-1$

  private static final String A_TRANSFORM_MODELSCHEMA = "modelSchema"; //$NON-NLS-1$

  private static final String A_EMBEDDED_EXPRESSIONS = "enableEmbeddedExpressions"; //$NON-NLS-1$

  private boolean expresionsInitialized = false;

  private XPathExpression xTransform;

  private XPathExpression xTransformModelLoader;

  private XPathExpression xTransformModelExtension;

  private XPathExpression xTransformOverrides;

  private XPathExpression xTransformStartTemplate;

  private XPathExpression xTransformTemplateLoaderClass;
  
  private XPathExpression xTransformPrivate;
  
  private XPathExpression xTransformModelSchema;

  private XPathExpression xDescription;

  private XPathExpression xTagLibrariesImportLibrary;

  private XPathExpression xImportLibraryId;

  private XPathExpression xImportLibraryUsePrefix;

  private XPathExpression xImportLibraryAutoImport;

  private XPathExpression xEmbeddedExpressions;

  private TransformDataFactory()
  {
    // prevent instantiation by others
  }

  private void initXPathExpressions() throws XPathException
  {
    if (!expresionsInitialized)
    {
      final XPath xpath = XPathFactory.newInstance().newXPath(null);

      xTransform = xpath.compile("/plugin/extension[@point = 'org.eclipse.jet.transform']/transform"); //$NON-NLS-1$
      xTransformModelLoader = xpath.compile("@" + A_TRANSFORM_MODELLOADER); //$NON-NLS-1$
      xTransformModelExtension = xpath.compile("@" + A_TRANSFORM_MODELEXTENSION); //$NON-NLS-1$
      xTransformOverrides = xpath.compile("@" + A_TRANSFORM_OVERRIDES); //$NON-NLS-1$
      xTransformStartTemplate = xpath.compile("@" + A_TRANSFORM_STARTTEMPLATE); //$NON-NLS-1$
      xTransformTemplateLoaderClass = xpath.compile("@" + A_TRANSFORM_TEMPLATELOADERCLASS); //$NON-NLS-1$
      xTransformPrivate = xpath.compile("@" + A_TRANSFORM_PRIVATE); //$NON-NLS-1$
      xTransformModelSchema = xpath.compile("@" + A_TRANSFORM_MODELSCHEMA); //$NON-NLS-1$
      xDescription = xpath.compile(E_DESCRIPTION);
      xTagLibrariesImportLibrary = xpath.compile("tagLibraries/importLibrary"); //$NON-NLS-1$

      xImportLibraryId = xpath.compile("@" + A_IMPORTLIBRARY_ID); //$NON-NLS-1$
      xImportLibraryUsePrefix = xpath.compile("@" + A_IMPORTLIBRARY_USEPREFIX); //$NON-NLS-1$
      xImportLibraryAutoImport = xpath.compile("@" + A_IMPORTLIBRARY_AUTOIMPORT); //$NON-NLS-1$
      xEmbeddedExpressions = xpath.compile("@" + A_EMBEDDED_EXPRESSIONS); //$NON-NLS-1$
      
      expresionsInitialized = true;
    }
  }

  public TransformData createTransformData(String transformId, URL pluginURL)
  {
    TransformData transformData = null;

    try
    {
      URL extensionsURL = new URL(pluginURL, "plugin.xml"); //$NON-NLS-1$
      Object resource = JETActivatorWrapper.INSTANCE.getLoaderManager().getLoader(extensionsURL.toExternalForm(), "org.eclipse.jet.xml", null).load(extensionsURL); //$NON-NLS-1$

      initXPathExpressions();

      final NodeSet transformSet = xTransform.evaluateAsNodeSet(resource);

      if (transformSet.size() > 0)
      {
        transformData = createTransformData(transformId, transformSet.iterator().next());
      }
    }
    catch (IOException e)
    {
      // ignore, plugin.xml doesn't exist.
    }
    catch (CoreJETException e)
    {
      InternalJET2Platform.logError("", e); //$NON-NLS-1$
    }
    catch (XPathException e)
    {
      InternalJET2Platform.logError("", e); //$NON-NLS-1$
    }

    return transformData;
  }

  /**
   * @param transformId
   * @param transform
   * @return
   */
  public TransformData createTransformData(String transformId, final Object transform)
  {
    String modelLoaderId = xTransformModelLoader.evaluateAsString(transform);
    String modelExtension = xTransformModelExtension.evaluateAsString(transform);
    String overridesId = xTransformOverrides.evaluateAsString(transform);
    String mainTemplate = xTransformStartTemplate.evaluateAsString(transform);
    String templateLoaderClassName = xTransformTemplateLoaderClass.evaluateAsString(transform);
    String description = xDescription.evaluateAsString(transform);
    String privateTransform = xTransformPrivate.evaluateAsString(transform);
    String modelSchema = xTransformModelSchema.evaluateAsString(transform);
    String strEmbeddedExpressions = xEmbeddedExpressions.evaluateAsString(transform);
    
    final NodeSet importNodes = xTagLibrariesImportLibrary.evaluateAsNodeSet(transform);

    List tlRefs = new ArrayList();
    for (Iterator i = importNodes.iterator(); i.hasNext();)
    {
      Object importCtx = i.next();

      final String tlId = xImportLibraryId.evaluateAsString(importCtx);
      final String tlPrefix = xImportLibraryUsePrefix.evaluateAsString(importCtx);
      boolean autoImport = Boolean.valueOf(xImportLibraryAutoImport.evaluateAsString(importCtx)).booleanValue();

      TagLibraryReference tlRef = new TagLibraryReferenceImpl(tlPrefix, tlId, autoImport);
      tlRefs.add(tlRef);
    }

    TransformData transformData = new TransformData(
      transformId,
      overridesId,
      modelLoaderId,
      modelExtension,
      templateLoaderClassName,
      mainTemplate,
      (TagLibraryReference[])tlRefs.toArray(new TagLibraryReference [tlRefs.size()]),
      description,
      Boolean.valueOf(privateTransform).booleanValue(),
      modelSchema, 
      strEmbeddedExpressions != null ? Boolean.valueOf(strEmbeddedExpressions).booleanValue() : false );
    return transformData;
  }

  /**
   * Create transformData from the passed configuration element
   * @param element an 'transform' configuration element
   * @return a TransformData object, or <code>null</code> if the element is not a 'transform' element.
   * @throws InvalidRegistryObjectException if the configuration element is invalid
   */
  public TransformData createTransformData(final IConfigurationElement element) throws InvalidRegistryObjectException
  {
    TransformData transformData = null;
    if (element != null && E_TRANSFORM.equals(element.getName()))
    {
      String transformId = element.getDeclaringExtension().getNamespace();
      String modelLoaderId = element.getAttribute(A_TRANSFORM_MODELLOADER);
      String modelExtension = element.getAttribute(A_TRANSFORM_MODELEXTENSION);
      String overridesId = element.getAttribute(A_TRANSFORM_OVERRIDES);
      String mainTemplate = element.getAttribute(A_TRANSFORM_STARTTEMPLATE);
      String templateLoaderClassName = element.getAttribute(A_TRANSFORM_TEMPLATELOADERCLASS);
      String privateTransform = element.getAttribute(A_TRANSFORM_PRIVATE);
      String modelSchema = element.getAttribute(A_TRANSFORM_MODELSCHEMA);
      String strEnableEmbeddedExpressions = element.getAttribute(A_EMBEDDED_EXPRESSIONS);
      TagLibraryReference[] tlRefs = getTagLibraryReferences(element);

      String description = getDescription(element);

      transformData = new TransformData(
        transformId,
        overridesId,
        modelLoaderId,
        modelExtension,
        templateLoaderClassName,
        mainTemplate,
        tlRefs,
        description,
        privateTransform == null ? false : Boolean.valueOf(privateTransform).booleanValue(),
        modelSchema, 
        strEnableEmbeddedExpressions != null ? Boolean.valueOf(strEnableEmbeddedExpressions).booleanValue() : false
        );
    }
    return transformData;
  }

  private String getDescription(IConfigurationElement element)
  {
    String description = ""; //$NON-NLS-1$
    final IConfigurationElement[] descElements = element.getChildren(E_DESCRIPTION);
    if (descElements.length > 0)
    {
      description = descElements[0].getValue();
    }
    return description;
  }

  /**
   * @param element
   * @return 
   * @throws InvalidRegistryObjectException
   */
  private TagLibraryReference[] getTagLibraryReferences(final IConfigurationElement element) throws InvalidRegistryObjectException
  {
    List tlRefs = new ArrayList();
    final IConfigurationElement[] tlElements = element.getChildren(E_TAGLIBRARIES);
    for (int j = 0; j < tlElements.length; j++)
    {
      IConfigurationElement tlElement = tlElements[j];

      final IConfigurationElement[] tlImports = tlElement.getChildren(E_IMPORTLIBRARY);
      for (int k = 0; k < tlImports.length; k++)
      {
        final String tlId = tlImports[k].getAttribute(A_IMPORTLIBRARY_ID);
        final String tlPrefix = tlImports[k].getAttribute(A_IMPORTLIBRARY_USEPREFIX);
        boolean autoImport = Boolean.valueOf(tlImports[k].getAttribute(A_IMPORTLIBRARY_AUTOIMPORT)).booleanValue();

        TagLibraryReference tlRef = new TagLibraryReferenceImpl(tlPrefix, tlId, autoImport);
        tlRefs.add(tlRef);
      }
    }
    return (TagLibraryReference[])tlRefs.toArray(new TagLibraryReference [tlRefs.size()]);
  }

}
