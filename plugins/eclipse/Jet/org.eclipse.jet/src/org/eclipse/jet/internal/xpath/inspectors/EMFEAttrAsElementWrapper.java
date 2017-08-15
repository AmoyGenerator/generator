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
package org.eclipse.jet.internal.xpath.inspectors;


import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;


/**
 * Wrapper for EAttributes that have ExtendedMetaData indicating they are Elements.
 *
 */
public class EMFEAttrAsElementWrapper
{

  private final Setting setting;
  private final int index;

  public EMFEAttrAsElementWrapper(Setting setting)
  {
    this(setting, -1);
  }

  public EMFEAttrAsElementWrapper(Setting setting, int index)
  {
    this.setting = setting;
    this.index = index;
  }

  public final Object getValue()
  {
    
    final Object object = setting.get(true);
    if(setting.getEStructuralFeature().isMany()) {
      EList list = (EList)object;
      return 0 <= index && index < list.size() ? list.get(index) : null;
    }
    else
    {
      return object;
    }
  }
  
  public final EStructuralFeature getFeature()
  {
    return setting.getEStructuralFeature();
  }
  
  public final EObject getEObject()
  {
    return setting.getEObject();
  }

  public void remove()
  {
    if(setting.getEStructuralFeature().isMany())
    {
      EList list = (EList)setting.get(true);
      if(index >= 0 && index < list.size())
      {
        list.remove(index);
      }
    }
    else 
    {
      setting.unset();
    }
  }
}
