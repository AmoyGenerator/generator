/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: WorkspaceTemplateResolverBuilder.java,v 1.2 2007/05/01 19:49:11 pelder Exp $
 */
package org.eclipse.jet.internal.parser.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jet.core.parser.DefaultTemplateResolver;
import org.eclipse.jet.core.parser.ITemplateResolver;

/**
 * Builder for a template resolver that leverages knowledge from the Eclipse
 * workspace if appropriate.
 */
public final class WorkspaceTemplateResolverBuilder {
  private final List baseLocations = new ArrayList();

  /**
   * Create a builder with the given eclipse project as the base location
   * @param project an {@link IProject}
   */
  public WorkspaceTemplateResolverBuilder(IProject project)
  {
    // don't use project.getLocationURI() - this is a file:/ URI
    // Instead, create a platform:/resource/ URI
    try
    {
      URI uri = new URI("platform", "/resource/" + project.getName() + "/", null);
      baseLocations.add(uri);
    }
    catch (URISyntaxException e)
    {
      // This convoluted code is Java 1.4 friendly. new IllegalArgumentException(e) is only in 1.5 and later
      IllegalArgumentException wrapper = new IllegalArgumentException();
      wrapper.initCause(e);
      throw wrapper;
    }
  }
  
  /**
   * Add alternative locations as URIs
   * @param baseLocations
   * @return
   */
  public WorkspaceTemplateResolverBuilder addAltBaseLocations(URI[] baseLocations)
  {
    this.baseLocations.addAll(Arrays.asList(baseLocations));
    return this;
  }
  
  /**
   * Create the template resolver
   * @return the template resolver
   */
  public ITemplateResolver build()
  {
    return new DefaultTemplateResolver.Builder(getBaseLocations())
      .templateResolverHelperFactory(WorkspaceTemplateResolverHelperFactory.getInstance())
      .build();
  }

  /**
   * Return all base locations as URI
   * @return
   */
  private URI[] getBaseLocations()
  {
    return (URI[])baseLocations.toArray(new URI[baseLocations.size()]);
  }
}