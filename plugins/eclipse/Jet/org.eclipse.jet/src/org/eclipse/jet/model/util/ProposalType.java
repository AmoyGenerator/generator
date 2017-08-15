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
package org.eclipse.jet.model.util;

import java.util.ArrayList;
import java.util.List;

public class ProposalType
{
  public static int STYLE_MODEL = 0;
  public static int STYLE_GUESS = 1;
  public static int STYLE_CONTEXT = 2;

  public static final int TYPE_DATA = 10;
  public static final int TYPE_FIELD = 11;
  public static final int TYPE_XML = 12;
  public static final int TYPE_ENTITY = 13;
  public static final int TYPE_PROPERTY = 14;
  public static final int TYPE_KEY = 15;
  public static final int TYPE_OBJECT = 16;

  private int style;
  private int type;
  private String string;
  private String displayString;
  private String info;

  private boolean isNew;
  private List<String> values = new ArrayList<String>();

  private String className;

  private boolean showValue = true;

  private List<String> targets = new ArrayList<String>();

  public ProposalType(int style, int type, String string)
  {
    super();
    this.style = style;
    this.type = type;
    this.string = string;
    formatDisplayString();
  }

  public void formatDisplayString(){
    displayString = string;
    if(showValue){

      int size = values.size();
      if(size > 1){
        displayString = displayString + " [=='";
        for (int i = 0; i < size; i++)
        {
          String value = values.get(i);
          displayString = displayString + value;
          if(i != size - 1){
            displayString = displayString + ", ";
          }
        }
        displayString = displayString + "']";
      }else if(size == 1){
        String value = values.get(0);
        if(value != null && !value.trim().isEmpty()){
          displayString = displayString + " [=='" + value + "']";
        }else{
          if(type == TYPE_PROPERTY || type == TYPE_DATA)
            displayString = displayString + " [==null]";
        }
      }
    }
    if(type == TYPE_ENTITY){
      displayString = displayString + " - " + "Entity";
    } if(type == TYPE_XML){
      displayString = displayString + " - " + "Xml";
    }else if(type == TYPE_FIELD){
      displayString = displayString + " - " + "Field";
    }else if(type == TYPE_PROPERTY){
      displayString = displayString + " - " + "String";
    }else if(type == TYPE_DATA){
      displayString = displayString + " - " + "String";
    }else if(type == TYPE_KEY){
      displayString = displayString + " - " + "Key";
    }else if(type == TYPE_OBJECT){
      if(className != null && !className.trim().isEmpty()){
        displayString = displayString + " - " + className;
      }else{
        displayString = displayString + " - " + "Object";
      }
    }
    int size = targets.size();
    if(size > 0){
      displayString = displayString + " (";
      for (int i = 0; i < size; i++)
      {
        String target = targets.get(i);
        displayString = displayString + target;
        if(i != size - 1){
          displayString = displayString + ", ";
        }
      }
      displayString = displayString + ")";
    }
  }

  public void addTarget(String target){
    targets.add(target);
    formatDisplayString();
  }

  public int getStyle()
  {
    return style;
  }

  public void setStyle(int style)
  {
    this.style = style;
  }

  public int getType()
  {
    return type;
  }

  public void setType(int type)
  {
    this.type = type;
  }

  public String getString()
  {
    return string;
  }

  public void setString(String string)
  {
    this.string = string;
  }

  public String getDisplayString()
  {
    return displayString;
  }

  public void setDisplayString(String displayString)
  {
    this.displayString = displayString;
  }

  public String getInfo()
  {
    return info;
  }

  public void setInfo(String info)
  {
    this.info = info;
  }

  public boolean isNew()
  {
    return isNew;
  }

  public void setNew(boolean isNew)
  {
    this.isNew = isNew;
  }

  public List<String> getValues()
  {
    return values;
  }

  public void addValue(String value)
  {
    values.add(value);
    formatDisplayString();
  }

  public void addValues(List<String> values)
  {
    this.values.addAll(values);
    formatDisplayString();
  }

  public boolean isShowValue()
  {
    return showValue;
  }

  public String getClassName()
  {
    return className;
  }

  public void setClassName(String className)
  {
    this.className = className;
    formatDisplayString();
  }

  public void setShowValue(boolean showValue)
  {
    this.showValue = showValue;
    formatDisplayString();
  }

  @Override
  public int hashCode()
  {
    return 1;
  }

  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof ProposalType){
      ProposalType proposalType = (ProposalType)obj;
      String string = proposalType.getString();
      int type = proposalType.getType();
      if(this.string != null && string != null){
        if(this.string.equals(string) && this.type == type){
          return true;
        }
      }
    }
    return false;
  }

}
