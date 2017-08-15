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

import org.eclipse.jet.taglib.TagLibraryReference;

/**
 * A invariant class that contains information from the Eclipse extension registry for the
 * extension point 'org.eclipse.jet.transform'.
 */
public class TransformData
{

  private final String transformId;
  private final String overridesId;
  private final String modelLoaderId;
  private final String modelExtension;
  private final String templateLoaderClassName;
  private final String mainTemplate;
  private final TagLibraryReference[] tlRefs;
  private final String description;
  private final boolean privateTransform;
  private final String modelSchema;
  private final boolean enabledEmbeddedExpressions;

  /**
   * Construct a TransformData object.
   * @param transformId
   * @param overridesId
   * @param modelLoaderId
   * @param modelExtension
   * @param templateLoaderClassName
   * @param mainTemplate
   * @param tlRefs
   * @param description 
   * @param enabledEmbeddedExpressions TODO
   */
  public TransformData(String transformId, String overridesId, 
    String modelLoaderId, String modelExtension, String templateLoaderClassName, 
    String mainTemplate, TagLibraryReference[] tlRefs, String description,
    boolean privateTransform, String modelSchema, boolean enabledEmbeddedExpressions)
  {
    this.transformId = transformId;
    this.privateTransform = privateTransform;
    this.modelSchema = modelSchema;
    this.enabledEmbeddedExpressions = enabledEmbeddedExpressions;
    this.overridesId = overridesId == null || overridesId.length() == 0 ? null : overridesId;
    this.modelLoaderId = modelLoaderId == null || modelLoaderId.length() == 0 ? null : modelLoaderId;
    this.modelExtension = modelExtension == null || modelExtension.length() == 0 ? null : modelExtension;
    this.templateLoaderClassName = templateLoaderClassName;
    this.mainTemplate = mainTemplate;
    this.tlRefs = tlRefs;
    this.description = description;
  }

  /**
   * @return Returns the mainTemplate.
   */
  public final String getMainTemplate()
  {
    return mainTemplate;
  }

  /**
   * @return Returns the modelExtension.
   */
  public final String getModelExtension()
  {
    return modelExtension;
  }

  /**
   * @return Returns the modelLoaderId.
   */
  public final String getModelLoaderId()
  {
    return modelLoaderId;
  }

  /**
   * @return Returns the overridesId.
   */
  public final String getOverridesId()
  {
    return overridesId;
  }

  /**
   * @return Returns the templateLoaderClassName.
   */
  public final String getTemplateLoaderClassName()
  {
    return templateLoaderClassName;
  }

  /**
   * @return Returns the tlRefs.
   */
  public final TagLibraryReference[] getTagLibraryReferences()
  {
    return tlRefs;
  }

  /**
   * @return Returns the transformId.
   */
  public final String getTransformId()
  {
    return transformId;
  }

  /**
   * @return Returns the description.
   */
  public final String getDescription()
  {
    return description;
  }

  /**
   * @return the model schema or <code>null</code> if none was specified.
   */
  public final String getModelSchema()
  {
    return modelSchema;
  }

  /**
   * 
   * @return <code>true</code> if the transform is private.
   */
  public final boolean isPrivateTransform()
  {
    return privateTransform;
  }

  public boolean isEnableEmbeddedExpressions()
  {
    return enabledEmbeddedExpressions;
  }

}
