/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
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
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.api.Group;
import org.eclipse.jet.core.expressions.IEmbeddedExpression;
import org.eclipse.jet.model.util.ProposalType;


/**
 * An abstract base class for all implementations of {@link CustomTag}.
 *
 */
public abstract class AbstractCustomTag implements CustomTag
{

  private CustomTag parent;

  private JET2Context context = null;

  private TagInfo td = null;

  private JET2Writer out = null;

  /**
   * 
   */
  public AbstractCustomTag()
  {
    super();
  }

  /**
   * @see org.eclipse.jet.taglib.CustomTag#getParent()
   */
  public final CustomTag getParent()
  {
    return parent;
  }

  /**
   * @see org.eclipse.jet.taglib.CustomTag#setParent(org.eclipse.jet.taglib.CustomTag)
   */
  public final void setParent(CustomTag parent)
  {
    this.parent = parent;
  }

  public final void setContext(JET2Context context)
  {
    if (this.context != null)
    {
      throw new IllegalStateException("Context already set."); //$NON-NLS-1$
    }
    this.context = context;
  }

  public final void setTagInfo(TagInfo td)
  {
    if (this.td != null)
    {
      throw new IllegalStateException("TagInfo already set."); //$NON-NLS-1$
    }
    this.td = td;
  }

  public final String getRawAttribute(String name)
  {
    if (this.td == null)
    {
      throw new IllegalStateException("TagInfo not set."); //$NON-NLS-1$
    }
    return td.getAttribute(name);
  }

  public final String getAttribute(String name) throws JET2TagException
  {
    String raw = getRawAttribute(name);
    if (raw != null)
    {
      final IEmbeddedExpression expr = context.getExpressionFactory().createExpression(raw);
      return expr.isText() ? XPathContextExtender.resolveDynamic(raw, context) : expr.evalAsString(context);
    }
    return null;
  }

  public final void setOut(JET2Writer out)
  {
    if (this.out != null)
    {
      throw new IllegalStateException("out already set."); //$NON-NLS-1$
    }
    this.out = out;
  }

  public final JET2Writer getOut()
  {
    return out;
  }
  
  public List<ProposalType> getProposals(Group group, Map<String, Object> contextMap, String attrName, String objectName)
  {
    return new ArrayList<ProposalType>();
  }
  
  public Map<String, Object> getContext(Map<String, Object> contextMap, Group group)
  {
    return new LinkedHashMap<String, Object>();
  }
  
//  protected void setModelOgnlValue(Map<String, Object> contextMap, String objectName, List<ProposalType> proposals, String value)
//  {
//    if(value != null){
//      if(objectName != null && !value.equals(objectName)){
//        value = objectName;
//      }
//      Index index = new Index(value);
//      String content = index.getContent();
//      Index right = index.getRight();
//      if(right == null){
//        for (String key: contextMap.keySet())
//        {
//          if(key.startsWith(content)){
//            Object object = contextMap.get(key);
//            if(object instanceof Entity){
//              ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_ENTITY, key);
//              proposals.add(proposalType);
//            }else if(object instanceof Field){
//              ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_FIELD, key);
//              proposals.add(proposalType);
//            }else if(object instanceof List){
//              List list = (List)object;
//              for(Object obj : list){
//                if(obj instanceof Field){
//                  Field field = (Field)obj;
//                  ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_FIELD, key);
//                  proposalType.setNew(field.isNew());
//                  proposalType.addValue(null);
//                  proposals.add(proposalType);
//                  break;
//                }
//              }
//            }else{
//              try
//              {
//                String convert = ModelOgnlConvert.convertExpression(contextMap, key);
//                if(convert != null){
//                  object = Ognl.getValue(convert, contextMap);
//                }else{
//                  object = Ognl.getValue(value, contextMap);
//                }
//                if(object != null){
//                  ProposalType proposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_OBJECT, key);
//                  String className = object.getClass().getSimpleName();
//                  if(className != null && !className.trim().isEmpty()){
//                    proposalType.setClassName(className);
//                  }
//                  if(object instanceof String){
//                    proposalType.addValue((String)object);
//                  }
//                  proposals.add(proposalType);
//                }
//              }
//              catch (OgnlException e)
//              {
//                 
//              }
//            }
//          }
//        }
//      }else{
//        Object object = contextMap.get(content);
//        if(object instanceof Model){
//          Model model = (Model)object;
//          List<ProposalType> list = model.getByIndexModel(index);
//          proposals.addAll(list);
//        }else if(object instanceof List){
//          List objs = (List)object;
//          for(Object obj : objs){
//            if(obj instanceof Model){
//              Model model = (Model)obj;
//              List<ProposalType> list = model.getByIndexModel(index);
//              mergeProposalTypes(model, proposals, list);
//            }
//          }
//        }
//      }
//    }
//  }
  
//  private void mergeProposalTypes(Model model, List<ProposalType> sources, List<ProposalType> targets){
//    for (int i = 0; i < targets.size(); i++)
//    {
//      ProposalType target = targets.get(i);
//      ProposalType find = null;
//      for (int j = 0; j < sources.size(); j++)
//      {
//        ProposalType source = sources.get(j);
//        if(source.equals(target)){
//          find = source;
//          break;
//        }
//      }
//      
//      String name = null;
//      if(model instanceof Entity){
//        Entity entity = (Entity)model;
//        name = entity.getName();
//      }else if(model instanceof Field){
//        Field field = (Field)model;
//        name = field.getName();
//      }else if(model instanceof Data){
////        Data data = (Data)model;
////        name = data.getName();
//      }
//      
//      List<String> values = target.getValues();
//      
//      if(find == null){
//        if(name != null){
//          target.addTarget(name);
//        }
//        //target.addValue(null);
//        sources.add(target);
//      }else{
//        if(name != null){
//          find.addTarget(name);
//          find.addValues(values);
//        }
//      }
//    }
//  }
  
}
