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
package org.eclipse.jet.transform;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jet.taglib.TagLibraryReference;

/**
 * Provide descriptive information about a JET Project/transform/bundle.
 */
public interface IJETBundleDescriptor
{

  /**
   * Return the unique identifier of the bundle.
   * @return the bundle's unique identifier
   */
  public abstract String getId();
  
  /**
   * Return the bundle's descriptive name.
   * @return the bundle's descriptive name
   */
  public abstract String getName();
  
  /**
   * Return a description of the bundle.
   * @return a string
   */
  public abstract String getDescription();
  
  /**
   * Return the id of the model loader the bundle expects to use.
   * @return a string or <code>null</code> if the bundle does not specify a model loader.
   */
  public abstract String getModelLoaderId();
  
  /**
   * Return the model extension that the bundle expects its input to conform to. 
   * @return a string or <code>null</code> if the bundle does not specific a model extension.
   */
  public abstract String getModelExtension();
  
  /**
   * Return the id of a JET bundle that this bundle overrides.
   * @return a string or <code>null</code> if the bundle does not override another bundle.
   */
  public abstract String getOverridesId();
  
  /**
   * Return the bundle version string.
   * @return a string, typically of the form n.n.n.string.
   */
  public abstract String getVersion();
  
  /**
   * Return the name of the entity that created the bundle.
   * @return a string
   */
  public abstract String getProvider();
  
  /**
   * Return an array containing the tag libraries imported by the bundle.
   * @return an array references, or the empty array.
   */
  public abstract TagLibraryReference[] getTagLibraryReferences();
  
  /**
   * Return the template loader class for the bundle.
   * @return a fully qualified Java class name, or <code>null</code> if not specified.
   */
  public abstract String getTemplateLoaderClass();
  
  /**
   * Return a bundle relative path to the main template for the bundle.
   * @return a bundle relative path to a template, or <code>null</code> if not specified.
   */
  public abstract String getMainTemplate();
  
  /**
   * Return the base URL of from which all bundle content may be accessed.
   * @return a URL
   */
  public abstract URL getBaseURL();
  
  /**
   * Tests whether the transform is declared to be private. Private
   * transforms are not shown in JET UIs, or returned from JET APIs that
   * return lists of available transforms.
   * @return <code>true</code> for private, <code>false</code> otherwise.
   */
  public abstract boolean isPrivate();
  
  /**
   * Return the URL of model schema defined by the transform.
   * @return the URL for the model schema, or <code>null</code> if no schema is provided
   * @throws MalformedURLException if the model schema URL is malformed.
   */
  public abstract URL getModelSchema() throws MalformedURLException;
  
  /**
   * Tests whether embedded language expressions are allowed in the transforms templates.
   * @return
   */
  public abstract boolean isEnableEmbeddedExpressions();
}
