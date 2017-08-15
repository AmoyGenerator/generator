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
package org.eclipse.jet.internal.runtime;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jet.internal.extensionpoints.TransformData;
import org.eclipse.jet.taglib.TagLibraryReference;
import org.eclipse.jet.transform.IJETBundleDescriptor;

/**
 * Implementation of {@link IJETBundleDescriptor}.
 */
public class JETBundleDescriptor implements IJETBundleDescriptor
{

  public static final String MAIN_TEMPLATE_DEFAULT = "templates/control.jet"; //$NON-NLS-1$
  
  private final String id;
  private final String name;
  private final String description;
  private final String modelLoaderId;
  private final String modelExtension;
  private final String overridesId;
  private final String version;
  private final String provider;
  private final String templateLoaderClass;
  private final String mainTemplate;
  private final boolean privateTransform;
  private final String modelSchema;
  private final boolean enableEmbeddedExpressions;
  private final TagLibraryReference[] tagLibraryReferences;

  private final URL baseURL;

  public JETBundleDescriptor(JETBundleManifest jetManifest, TransformData transformData, URL baseURL)
  {
    this.id = jetManifest.getTransformId();
    this.name = jetManifest.getName();
    this.version = jetManifest.getVersion();
    this.provider = jetManifest.getProvider();
    this.baseURL = baseURL;
    
    if(transformData == null)
    {
      this.templateLoaderClass = jetManifest.getTemplateLoaderClassName();
      this.mainTemplate = MAIN_TEMPLATE_DEFAULT;
      this.description = ""; //$NON-NLS-1$
      this.overridesId = null;
      this.modelLoaderId = null;
      this.modelExtension = null;
      this.tagLibraryReferences = new TagLibraryReference[0];
      this.privateTransform = false;
      this.modelSchema = null;
      this.enableEmbeddedExpressions = false;
    }
    else
    {
      this.templateLoaderClass = transformData.getTemplateLoaderClassName();
      this.mainTemplate = transformData.getMainTemplate();
      this.description = transformData.getDescription();
      this.overridesId = transformData.getOverridesId();
      this.modelLoaderId = transformData.getModelLoaderId();
      this.modelExtension = transformData.getModelExtension();
      this.tagLibraryReferences = transformData.getTagLibraryReferences();
      this.privateTransform = transformData.isPrivateTransform();
      this.modelSchema = transformData.getModelSchema();
      this.enableEmbeddedExpressions = transformData.isEnableEmbeddedExpressions();
      
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getId()
   */
  public String getId()
  {
    return id;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getName()
   */
  public String getName()
  {
    return name == null ? id : name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getDescription()
   */
  public String getDescription()
  {
    return description == null ? "" : description; //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getModelLoaderId()
   */
  public String getModelLoaderId()
  {
    return modelLoaderId;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getModelExtension()
   */
  public String getModelExtension()
  {
    return modelExtension;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getOverridesId()
   */
  public String getOverridesId()
  {
    return overridesId;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getVersion()
   */
  public String getVersion()
  {
    return version;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getProvider()
   */
  public String getProvider()
  {
    return provider;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getTagLibraryReferences()
   */
  public TagLibraryReference[] getTagLibraryReferences()
  {
    return tagLibraryReferences;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getTemplateLoaderClass()
   */
  public String getTemplateLoaderClass()
  {
    return templateLoaderClass;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.transform.IJETBundleDescriptor#getMainTemplate()
   */
  public String getMainTemplate()
  {
    return mainTemplate;
  }

  public URL getBaseURL()
  {
    return baseURL;
  }

  public URL getModelSchema() throws MalformedURLException
  {
    return modelSchema == null ? null : new URL(baseURL, modelSchema);
  }

  public boolean isPrivate()
  {
    return privateTransform;
  }

  public boolean isEnableEmbeddedExpressions()
  {
    return enableEmbeddedExpressions;
  }

}
