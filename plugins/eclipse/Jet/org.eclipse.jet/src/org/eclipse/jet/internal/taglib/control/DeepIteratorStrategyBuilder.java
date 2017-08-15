/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: DeepIteratorStrategyBuilder.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.osgi.util.NLS;

/**
 * Factory for {@link IResultSet} implementations
 */
public class DeepIteratorStrategyBuilder
{

  private final JET2Context context;
  private final String select;
  private String var;
  private String filter;
  private Object contextObject;
  private boolean allowDuplicates = true;
  private String varStatus;
  private final String tagName;
  private static final int DEPTH_FIRST_TRAVERSAL = 1;
  private static final int BREADTH_FIRST_TRAVERSAL = 2;
  private int traversal = DEPTH_FIRST_TRAVERSAL;

  public DeepIteratorStrategyBuilder(JET2Context context, String tagName, String select) {
    this.context = context;
    this.tagName = tagName;
    this.select = select;
    this.contextObject = XPathContextExtender.getInstance(context).currentXPathContextObject();
  }
  
  public DeepIteratorStrategyBuilder var(String var) {
    this.var = var;
    return this;
  }
  
  public DeepIteratorStrategyBuilder filter(String filter) {
    this.filter = filter;
    return this;
  }
  
  public DeepIteratorStrategyBuilder contextObject(Object contextObject) {
    this.contextObject = contextObject;
    return this;
  }

  public DeepIteratorStrategyBuilder allowDuplicates(boolean allowDuplicates)
  {
    this.allowDuplicates = allowDuplicates;
    return this;
  }
  
  public IDeepIterateStrategy build() {
    if(traversal == DEPTH_FIRST_TRAVERSAL) {
      return new PreorderStrategy(this);      
    } else {
      return new BreadthFirstStrategy(this);
    }
  }

  /**
   * @return Returns the context.
   */
  final JET2Context getContext()
  {
    return context;
  }

  /**
   * @return Returns the select.
   */
  final String getSelect()
  {
    return select;
  }

  /**
   * @return Returns the var.
   */
  final String getVar()
  {
    return var;
  }

  /**
   * @return Returns the filter.
   */
  final String getFilter()
  {
    return filter;
  }

  /**
   * @return Returns the contextObject.
   */
  final Object getContextObject()
  {
    return contextObject;
  }

  /**
   * @return Returns the allowDuplicates.
   */
  final boolean isAllowDuplicates()
  {
    return traversal == DEPTH_FIRST_TRAVERSAL ? allowDuplicates : false;
  }

  public DeepIteratorStrategyBuilder varStatus(String varStatus)
  {
    
    this.varStatus = varStatus;
    return this;
  }

  /**
   * @return Returns the varStatus.
   */
  final String getVarStatus()
  {
    return varStatus;
  }

  final String getTagName()
  {
    return tagName;
  }

  public DeepIteratorStrategyBuilder traversal(String traversal) throws JET2TagException
  {
    if("depthFirst".equals(traversal)) { //$NON-NLS-1$
      this.traversal = DEPTH_FIRST_TRAVERSAL;
    } else if("breadthFirst".equals(traversal)) { //$NON-NLS-1$
      this.traversal = BREADTH_FIRST_TRAVERSAL;
    } else if (traversal != null){
      throw new JET2TagException(NLS.bind(Messages.DeepIteratorStrategyBuilder_UnrecognizedTraversalStrategy, 
        new Object[] {traversal, "depthFirst", "breadthFirst"}));  //$NON-NLS-1$//$NON-NLS-2$
    }
    return this;
  }
  
}
