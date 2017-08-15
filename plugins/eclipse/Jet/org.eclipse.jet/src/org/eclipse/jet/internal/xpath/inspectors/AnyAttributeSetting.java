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


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;


/**
 * Implementation of EStructuralFeature.Setting that works with AnyAttribute objects.
 */
public final class AnyAttributeSetting implements EStructuralFeature.Setting
{
  private final FeatureMap.Entry entry;

  private final EObject type;

  public AnyAttributeSetting(FeatureMap.Entry entry, EObject type)
  {
    super();
    this.entry = entry;
    this.type = type;
  }

  public EObject getEObject()
  {
    return type;
  }

  public EStructuralFeature getEStructuralFeature()
  {
    return entry.getEStructuralFeature();
  }

  public Object get(boolean resolve)
  {
    return type.eGet(entry.getEStructuralFeature(), resolve);
  }

  public void set(Object newValue)
  {
    type.eSet(entry.getEStructuralFeature(), newValue);
  }

  public boolean isSet()
  {
    return type.eIsSet(entry.getEStructuralFeature());
  }

  public void unset()
  {
    type.eUnset(entry.getEStructuralFeature());
  }

  public String toString()
  {
    return entry.toString();
  }
}