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
 * $Id: UniqueNameGenerator.java,v 1.1 2007/04/04 14:53:54 pelder Exp $
 */
package org.eclipse.jet.internal.core.compiler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.core.parser.ast.JETCompilationUnit;


/**
 * The class is responsible for managing the generation of Java class names for
 * JET templates
 */
public class UniqueNameGenerator
{

  private final Map pathToFQJavaClassMap;

  private final String defaultJavaPackage;

  private final Map fqJavaClassToPathMap;

  public UniqueNameGenerator(Map pathToFQJavaClassMap, Map fqJavaClassToPathMap, String defaultJavaPackage)
  {
    this.pathToFQJavaClassMap = new HashMap(pathToFQJavaClassMap);
    this.fqJavaClassToPathMap = new HashMap(fqJavaClassToPathMap);
    this.defaultJavaPackage = defaultJavaPackage;

  }

  /**
   * Ensure the the compilation unit has Java output package and class 
   * set. If not, generate names for these, taking into account that
   * a name may have been generated for them already.
   * @param templatePath the template path of the compilation unit
   * @param cu the compilation unit
   */
  public void ensureJavaOutputSet(String templatePath, JETCompilationUnit cu) throws DuplicateGeneratedClassException
  {
    String fqn;

    // See if we already have a name for this...
    final String existingQualifiedClass = (String)pathToFQJavaClassMap.get(templatePath);
    if (existingQualifiedClass == null)
    {
      // we haven't generated a name before, do it now...

      if (cu.getOutputJavaPackage() == null)
      {
        cu.setOutputJavaPackage(defaultJavaPackage);
      }
      if (cu.getOutputJavaClassName() == null)
      {
        // check for conflicts because we have name mangled
        // or for identically named files in different directories.
        String baseClassName = makeJavaClassName(templateBaseName(templatePath));

        String className = baseClassName;
        String potentialQualifiedName = makeFullyQualifiedJavaName(cu.getOutputJavaPackage(), className);

        for (int i = 0; fqJavaClassToPathMap.get(potentialQualifiedName) != null; i++)
        {
          className = baseClassName + "_" + i; //$NON-NLS-1$
          potentialQualifiedName = makeFullyQualifiedJavaName(cu.getOutputJavaPackage(), className);
        }

        cu.setOutputJavaClassName(className);
        fqn = makeFullyQualifiedJavaName(cu.getOutputJavaPackage(), cu.getOutputJavaClassName());
      }
      else
      {
        // check for a name collision
        fqn = makeFullyQualifiedJavaName(cu.getOutputJavaPackage(), cu.getOutputJavaClassName());
        final String otherTemplatePath = (String)fqJavaClassToPathMap.get(fqn);
        if (otherTemplatePath != null)
        {
          throw new DuplicateGeneratedClassException(templatePath, otherTemplatePath, fqn);
        }
      }
    }
    else
    {
      fqn = existingQualifiedClass;
      int index = existingQualifiedClass.lastIndexOf('.');
      String existingPackage = index == -1 ? "" : existingQualifiedClass.substring(0, index); //$NON-NLS-1$
      String existingClass = index == -1 ? existingQualifiedClass : existingQualifiedClass.substring(index + 1);

      if (cu.getOutputJavaPackage() == null)
      {
        cu.setOutputJavaPackage(existingPackage);
      }
      if (cu.getOutputJavaClassName() == null)
      {
        cu.setOutputJavaClassName(existingClass);
      }
    }

    // update the maps
    fqJavaClassToPathMap.put(fqn, templatePath);
    pathToFQJavaClassMap.put(templatePath, fqn);
  }

  /**
   * Return the template base name, that is the template name with extension and path prefix removed.
   * @param templatePath a template path
   * @return the base name
   */
  private String templateBaseName(String templatePath)
  {
    int lastSlash = templatePath.lastIndexOf('/');
    String baseFile = lastSlash == -1 ? templatePath : templatePath.substring(lastSlash + 1);
    int lastDot = baseFile.lastIndexOf('.');
    return lastDot == -1 ? baseFile : baseFile.substring(0, lastDot);
  }

  /**
   * Make a a Java class name from the base template name
   * @param name the base template name (path and extension removed)
   * @return
   */
  private String makeJavaClassName(String name)
  {
    StringBuffer result = new StringBuffer("_jet_"); //$NON-NLS-1$
    for (int i = 0; i < name.length(); i++)
    {
      char c = name.charAt(i);
      if (Character.isJavaIdentifierPart(c))
      {
        result.append(c);
      }
    }
    return result.toString();
  }

  /**
   * Make a fully qualified Java class name given the possibly null or empty Java package
   * and a non-empty Java class name.
   * @param javaPackage a Java package name or the empty string or <code>null</code>
   * @param javaClassName a Java class name
   * @return a fully qualified Java class name
   */
  private String makeFullyQualifiedJavaName(String javaPackage, String javaClassName)
  {
    return javaPackage == null || javaPackage.trim().length() == 0 ? javaClassName : javaPackage + '.' + javaClassName;
  }

  /**
   * Return a copy for the map of template paths to fully qualified class names
   * @return a map of fully qualified class names (String) keyed by template path (String)
   */
  public Map getPathToFQNMap()
  {
    return new HashMap(pathToFQJavaClassMap);
  }

  /**
   * Return a copy of the map of fully qualified class names to the corresponding JET template path.
   * @return a map of template paths (String) keyd by fully qualified class name
   */
  public Map getFQNToPathMap()
  {
    return new HashMap(fqJavaClassToPathMap);
  }

  /**
   * Return the assets generated by the compiler for the given templatePath
   * @param templatePath a template path
   * @return a possibly empty array of file paths relative
   */
  public String getGeneratedOutputPath(String templatePath)
  {
    final String fqn = (String)pathToFQJavaClassMap.get(templatePath);
    return fqn == null ? null : fqn.replace('.', '/') + ".java"; //$NON-NLS-1$
  }

  /**
   * Clean the unique name manager.
   * @return the list of generated Java Class names the manager has forgotten.
   */
  public List clean()
  {
    List removedFQJavaClasses = new ArrayList(fqJavaClassToPathMap.keySet());
    fqJavaClassToPathMap.clear();
    pathToFQJavaClassMap.clear();
    return removedFQJavaClasses;
  }

  /**
   * 
   * @param templatePath
   * @return
   */
  public String remove(String templatePath)
  {
    String outputPath = getGeneratedOutputPath(templatePath);
    if (outputPath != null)
    {
      String fqName = (String)pathToFQJavaClassMap.remove(templatePath);
      fqJavaClassToPathMap.remove(fqName);
    }
    return outputPath;
  }
}
