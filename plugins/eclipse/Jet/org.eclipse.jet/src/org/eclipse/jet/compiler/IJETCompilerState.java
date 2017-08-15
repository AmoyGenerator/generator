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
 * $Id$
 * /
 *******************************************************************************/
package org.eclipse.jet.compiler;


import java.util.Map;

import org.eclipse.jet.taglib.TagLibraryReference;


/**
 * Provides access to the JET compiler state. Saving the compiler state, and then passing it
 * to a new instances of {@link JET2Compiler} will allow the compiler to do a correct incremental
 * compilation. 
 * @deprecated Use {@link org.eclipse.jet.core.parser.ast.JETASTParser}
 */
public interface IJETCompilerState
{

  /**
   * Return a map of fully qualified Java class names keyed by project relative template path (as a string).
   * @return an unmodifiable map.
   */
  public abstract Map getTemplateMap();

  /**
   * Add the template and fully qualified Java class name to the template map. This method is generally called by 
   * the compiler.
   * @param templatePath the project relative template path.
   * @param outputJavaClassName the fully qualified class name of the compiled Java output.
   */
  public abstract void addTemplate(String templatePath, String outputJavaClassName);

  /**
   * Remove the template from the template map. If the template is not in the template map, then nothing
   * happens.
   * @param templatePath the project relative template path.
   */
  public abstract void removeTemplate(String templatePath);

  /**
   * Clear the compiler state, returning it to its initial condition.
   *
   */
  public abstract void clear();

  /**
   * Return the project relative template name given a fully qualified class name.
   * @param qualifiedName a fully qualified Java class name
   * @return the corresponding template path, or <code>null</code>
   */
  public abstract String getTemplateFromClass(String qualifiedName);

  public abstract void setProjectTagLibraryReferences(TagLibraryReference[] tagLibraryReferences);

  public abstract void addTemplateTagLibraryReferences(String templatePath, TagLibraryReference[] references);

  public abstract String[] getAllReferencedTagLibraryIds();

}
