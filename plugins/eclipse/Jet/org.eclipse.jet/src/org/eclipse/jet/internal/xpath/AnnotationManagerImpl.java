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

package org.eclipse.jet.internal.xpath;


import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.jet.xpath.IAnnotationManager;


/**
 * IAnnotationManager implementation for JET2.
 *
 */
public class AnnotationManagerImpl implements IAnnotationManager
{

  private final Map annotationMap = new IdentityHashMap();

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.IAnnotationManager#getAnnotationObject(java.lang.Object)
   */
  public Object getAnnotationObject(Object modelObject)
  {
    Object annotation = annotationMap.get(modelObject);
    if (annotation == null)
    {
      annotation = XMLTypeFactory.eINSTANCE.createAnyType();
      annotationMap.put(modelObject, annotation);
    }
    return annotation;
  }

  public boolean hasAnnotations(Object modelObject)
  {
    return annotationMap.containsKey(modelObject);
  }

}
