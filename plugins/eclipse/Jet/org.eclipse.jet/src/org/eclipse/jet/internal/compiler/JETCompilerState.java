/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.compiler;


import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jet.compiler.IJETCompilerState;
import org.eclipse.jet.taglib.TagLibraryReference;


/**
 * A holder for all JET compiler build state
 *
 */
public class JETCompilerState implements Serializable, IJETCompilerState
{

  /**
   * 
   */
  private static final long serialVersionUID = 3680891043515763347L;

  private final Map qualifiedNameByTemplatePath = new HashMap();

  private final Map templatePathByQualifiedName = new HashMap();

  private final Set projectTagLibraryReferences = new HashSet();

  private final Map templateTagLibraryReferences = new HashMap();

  /**
   * Construct a new compiler state object, representing a clean compile.
   *
   */
  public JETCompilerState()
  {
    // nothing to do
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#getTemplateMap()
   */
  public Map getTemplateMap()
  {
    return Collections.unmodifiableMap(qualifiedNameByTemplatePath);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#addTemplate(java.lang.String, java.lang.String)
   */
  public void addTemplate(String templatePath, String outputJavaClassName)
  {
    if (qualifiedNameByTemplatePath.containsKey(templatePath))
    {
      removeTemplate(templatePath);
    }
    qualifiedNameByTemplatePath.put(templatePath, outputJavaClassName);
    templatePathByQualifiedName.put(outputJavaClassName, templatePath);
  }


  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#removeTemplate(java.lang.String)
   */
  public void removeTemplate(String templatePath)
  {
    String javaClass = (String)qualifiedNameByTemplatePath.get(templatePath);
    qualifiedNameByTemplatePath.remove(templatePath);
    templatePathByQualifiedName.remove(javaClass);
    templateTagLibraryReferences.remove(templatePath);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#clear()
   */
  public void clear()
  {
    qualifiedNameByTemplatePath.clear();
    templatePathByQualifiedName.clear();
    templateTagLibraryReferences.clear();
    projectTagLibraryReferences.clear();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#getTemplateFromClass(java.lang.String)
   */
  public String getTemplateFromClass(String qualifiedName)
  {
    return (String)templatePathByQualifiedName.get(qualifiedName);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#setProjectTagLibraryReferences(org.eclipse.jet.taglib.TagLibraryReference[])
   */
  public void setProjectTagLibraryReferences(TagLibraryReference[] tagLibraryReferences)
  {
    
    projectTagLibraryReferences.clear();
    for (int i = 0; i < tagLibraryReferences.length; i++)
    {
      projectTagLibraryReferences.add(tagLibraryReferences[i].getTagLibraryId());
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#addTemplateTagLibraryReferences(java.lang.String, org.eclipse.jet.taglib.TagLibraryReference[])
   */
  public void addTemplateTagLibraryReferences(String templatePath, TagLibraryReference[] references)
  {
    Set idRefs = new HashSet();
    for (int i = 0; i < references.length; i++)
    {
      if(!references[i].isAutoImport())
      {
        idRefs.add(references[i].getTagLibraryId());
      }
    }
    this.templateTagLibraryReferences.put(templatePath, idRefs);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.IJETCompilerState#getAllReferencedTagLibraryIds()
   */
  public String[] getAllReferencedTagLibraryIds()
  {
    Set allRefs = new HashSet(projectTagLibraryReferences);
    for (Iterator i = templateTagLibraryReferences.entrySet().iterator(); i.hasNext();)
    {
      Map.Entry entry = (Map.Entry)i.next();
      allRefs.addAll((Set)entry.getValue());
    }
    return (String[])allRefs.toArray(new String[allRefs.size()]);
  }
  
}
