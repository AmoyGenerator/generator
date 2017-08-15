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
 * $Id: DuplicateGeneratedClassException.java,v 1.1 2007/04/04 14:53:54 pelder Exp $
 */
package org.eclipse.jet.internal.core.compiler;

/**
 * Describe an exception where a JET template explicitly specifies the same
 * fully qualified Java class name as another JET template
 */
public class DuplicateGeneratedClassException extends Exception
{

  private final String templatePath;
  private final String otherTemplatePath;
  private final String fullyQualifiedJavaClassName;

  /**
   * Create a DuplicateGeneratedClassException
   * @param templatePath the templatePath that specifies a common generated Java class.
   * @param otherTemplatePath the other template Path that previously declared the 
   * @param fullyQualifiedJavaClassName the common fully qualified Java class name specified by the templates
   */
  public DuplicateGeneratedClassException(String templatePath, String otherTemplatePath, String fullyQualifiedJavaClassName)
  {
    this.templatePath = templatePath;
    this.otherTemplatePath = otherTemplatePath;
    this.fullyQualifiedJavaClassName = fullyQualifiedJavaClassName;
  }

  /**
   * 
   */
  private static final long serialVersionUID = 8154326381507911174L;

  /**
   * Return the fully qualifed Java class name specified by both templates
   * @return the fully qualified Java class name
   */
  public String getFullyQualifiedJavaClassName()
  {
    return fullyQualifiedJavaClassName;
  }

  /**
   * Return the template path of the template already specifying the common generated Java class.
   * @return a template path
   */
  public String getOtherTemplatePath()
  {
    return otherTemplatePath;
  }

  /**
   * Return the template path of a template being compiled when the exception was detected.
   * @return a template path
   */
  public String getTemplatePath()
  {
    return templatePath;
  }

}
