/**
 * <copyright>
 *
 * Copyright (c) 2013 IBM Corporation and others.
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
package org.eclipse.jet.taglib;

public class KeyValue
{
  private Object key;

  private Object value;

  public KeyValue(Object key, Object value)
  {
    super();
    this.key = key;
    this.value = value;
  }

  public Object getKey()
  {
    return key;
  }

  public void setKey(Object key)
  {
    this.key = key;
  }

  public Object getValue()
  {
    return value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }
}
