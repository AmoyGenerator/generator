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

public class ForElement implements IForEach, Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String var;
  
  private String value;
  
  private Object forElement;
  
  private IForEach parent;
  
  private List<IForEach> children = new ArrayList<IForEach>();

  public ForElement(String var, String value, Object forElement)
  {
    super();
    this.var = var;
    this.value = value;
    this.forElement = forElement;
  }
  
  public String getVar(){
    return var;
  }

  public void setVar(String var){
    this.var = var;
  }

  public String getValue(){
    return value;
  }

  public void setValue(String value){
    this.value = value;
  }

  public Object getForElement(){
    return forElement;
  }

  public void setForElement(Object forElement){
    this.forElement = forElement;
  }

  public void addChild(IForEach child) {
    children.add(child);
    child.setParent(this);
  }

  public void addChild(List<IForEach> children) {
    if(children == null){
      return;
    }
    for (int i = 0; i < children.size(); i++){
      IForEach child = children.get(i);
      addChild(child);
    }
  }
  
  public void addChild(int index, IForEach child) {
    children.add(index, child);
    child.setParent(this);
  }
  
  public void removeChild(IForEach child) {
    children.remove(child);
  }
  
  public List<IForEach> getChildren() {
    return children;
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
  public ForElement clone(){
    try {
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream oo = new ObjectOutputStream(bo);
      oo.writeObject(this);
      ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
      ObjectInputStream oi = new ObjectInputStream(bi);
      return (ForElement) oi.readObject();
  } catch (Exception e) {
      e.printStackTrace();
  }
  return this;
  }

}
