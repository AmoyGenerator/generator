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
 * $Id: JET2TemplateLoader.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet;

/**
 * A loader for templates within a transform.
 */
public interface JET2TemplateLoader
{

  /**
   * Return an instance of the specified template, or <code>null</code>.
   * <p>
   * This class is not typically implemented by clients. The JET2 compiler will
   * emit an instance of this class for each JET2 project.
   * </p>
   * @param templatePath a transform project relative path to the template.
   * @return the template instance or <code>null</code> if the template cannot be found
   */
  public JET2Template getTemplate(String templatePath);

}
