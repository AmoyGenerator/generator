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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EachElement implements IForEach, Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Object eachElement;
  
  private String value;
  
  private IForEach parent;
  
  public EachElement(Object eachElement, String value){
    super();
    this.eachElement = eachElement;
    this.value = value;
  }

  public Object getEachElement(){
    return eachElement;
  }

  public String getValue(){
    return value;
  }

  public void setValue(String value){
    this.value = value;
  }

  public void setEachElement(Object eachElement){
    this.eachElement = eachElement;
  }

  public void addChild(IForEach child) {
  }

  public void removeChild(IForEach child) {
  }
  
  public List<IForEach> getChildren() {
    return new ArrayList<IForEach>();
  }

  public IForEach getParent() {
    return parent;
  }

  public boolean hasChild() {
    return false;
  }

  public void setParent(IForEach parent) {
    this.parent = parent;
  }

  @Override
  public EachElement clone()
  {
    try {
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream oo = new ObjectOutputStream(bo);
      oo.writeObject(this);
      ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
      ObjectInputStream oi = new ObjectInputStream(bi);
      return (EachElement) oi.readObject();
  } catch (Exception e) {
      e.printStackTrace();
  } 
  return this;
  }

}
