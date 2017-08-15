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
package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.model.util.ProposalType;

public class Collect extends Model
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private List<Model> models = new ArrayList<Model>();

  public Collect(String nodeName)
  {
    super(nodeName);
  }

  public Collect(List<Model> models)
  {
    super("");
    this.models = models;
  }

  @Override
  protected List<ProposalType> getByKey(String key)
  {
    List<ProposalType> list = new ArrayList<ProposalType>();

    for (int i = 0; i < models.size(); i++)
    {
      Model model = models.get(i);
      if(model instanceof Field){
        Field field = (Field)model;
        ProposalType proposalType =  new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_FIELD, field.getName());
        proposalType.setNew(field.getIsNew());
        list.add(proposalType);
      }else if(model instanceof Data){
        ProposalType proposalType =  new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_DATA, ((Data)model).getName());
        proposalType.addValue(((Data)model).getText());
        list.add(proposalType);
      }
    }
    return list;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    List<Model> list = new ArrayList<Model>();
    for (int i = 0; i < models.size(); i++)
    {
      Model model = models.get(i);
      if(model instanceof Field){
        Field field = (Field)model;
        String name = field.getName();
        if(name.equals(key)){
          list.add(field);
        }
      }else if(model instanceof Data){
        Data data = (Data)model;
        String name = data.getName();
        if(name.equals(key)){
          list.add(data);
        }
      }
    }
    return list;
  }

}
