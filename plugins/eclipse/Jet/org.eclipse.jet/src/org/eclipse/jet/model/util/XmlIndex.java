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

public class XmlIndex extends Index
{
  
  private String str;
  
  private String key;
  
  private String query;
  
  private String excludeSelfStr;
  
  public XmlIndex(String str)
  {
    super(str);
    this.str = str;
  }

  @Override
  protected void init()
  {
    super.splitChar = '/';
  }
  
  @Override
  public void parser(String str){
    str = str.trim();
    int index = str.indexOf(splitChar);
    if(index > -1){
      String queryStr = str.substring(index + 1, str.length());
      excludeSelfStr = queryStr;
      //right = new Index(queryStr);
      int lastSplit = queryStr.lastIndexOf('/');
      if(lastSplit > -1){
        query = queryStr.substring(0, lastSplit);
      }
      else{
        query = "";
      }
      content = str.substring(0, index);
    }else{
      content = str;
      excludeSelfStr = "";
    }
    if(content.startsWith("$")){
      key = content.substring(1, content.length());
    }else{
      key = content;
    }
  }
  
  public String getKey()
  {
    return key;
  }

  public String getQuery()
  {
    return query;
  }
  
  public String getExcludeSelfStr()
  {
    return excludeSelfStr;
  }

  @Override
  public String getLastRight()
  {
    String lastRight = null;
    if(str != null){
      int index = str.lastIndexOf('/');
      if(index > -1){
        lastRight = str.substring(index + 1);
      }else{
        lastRight = str;
      }
    }
    return lastRight;
  }
  
}
