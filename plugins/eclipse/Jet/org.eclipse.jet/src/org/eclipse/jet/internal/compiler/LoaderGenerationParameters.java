/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.jet.internal.compiler;

import java.util.Map;

/**
 * Parameter object of legacy JET template.
 */
public class LoaderGenerationParameters
{

  private final String packageName;
  private final String className;
  private final Map templateMap;

  /**
   * 
   */
  public LoaderGenerationParameters(String packageName, String className, Map templateMap)
  {
    super();
    this.packageName = packageName;
    this.className = className;
    this.templateMap = templateMap;
  }

  /**
   * @return Returns the className.
   */
  public final String getClassName()
  {
    return className;
  }

  /**
   * @return Returns the packageName.
   */
  public final String getPackageName()
  {
    return packageName;
  }

  /**
   * @return Returns the templateMap.
   */
  public final Map getTemplateMap()
  {
    return templateMap;
  }

}
