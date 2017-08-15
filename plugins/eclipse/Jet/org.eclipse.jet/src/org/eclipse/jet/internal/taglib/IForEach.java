/**
 * <copyright>
 *
 * Copyright (c) 2015 IBM Corporation and others.
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
package org.eclipse.jet.internal.taglib;

import java.util.List;

public interface IForEach{
  
  public void setValue(String value);

  public String getValue();
  
  public void addChild(IForEach child);
  
  public void removeChild(IForEach child);
  
  public List<IForEach> getChildren();
  
  public boolean hasChild();
  
  public IForEach getParent();
  
  public void setParent(IForEach parent);
  
  public IForEach clone();
  
}
