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
 * $Id: ResourceTemplateResolverHelper.java,v 1.1 2007/04/04 14:59:33 pelder Exp $
 */
package org.eclipse.jet.internal.parser.resources;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.ITemplateResolverHelper;

/**
 * @author pelder
 */
public class ResourceTemplateResolverHelper implements ITemplateResolverHelper
{

  private final IContainer container;

  public ResourceTemplateResolverHelper(IContainer container)
  {
    this.container = container;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.parser.resources.ITemplateResolverHelper#createTemplateInput(java.net.URI, java.lang.String)
   */
  public ITemplateInput createTemplateInput(String templatePath)
  {
    return new ResourceTemplateInput(container.getFile(new Path(templatePath)));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.parser.resources.ITemplateResolverHelper#inputExists(java.net.URI, java.lang.String)
   */
  public boolean inputExists(String templatePath)
  {
    return container.getFile(new Path(templatePath)).exists();
  }

}
