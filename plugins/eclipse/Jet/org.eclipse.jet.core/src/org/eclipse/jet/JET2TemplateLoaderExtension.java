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
 * $Id: JET2TemplateLoaderExtension.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet;

/**
 * Extension interface for {@link JET2TemplateLoader} allowing 
 * for delegation of template loading to other loaders
 */
public interface JET2TemplateLoaderExtension
{
  /**
   * Add a template loader to which this loader will delegate
   * when it cannot find a template
   * @param loader a template loader or <code>null</code> to remove the delegate loader.
   */
  public abstract void setDelegateLoader(JET2TemplateLoader loader);
  
  /**
   * Return the template loader to which this loader will delegate
   * when it cannot find a template. The initial delegate loader is <code>null</code>.
   * @return the delegate loader or <code>null</code>.
   */
  public abstract JET2TemplateLoader getDelegateLoader();
}
