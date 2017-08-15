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
 * $Id: AbstrateDeepIterateStrategy.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;


/**
 * Abstract implementation of {@link IDeepIterateStrategy}.
 */
public abstract class AbstrateDeepIterateStrategy implements IDeepIterateStrategy
{

  protected final JET2Context context;
  protected final XPathContextExtender xpc;
  protected final String select;
  protected final String filter;
  protected final String var;
  protected Set loopDetectionStack;
  protected Collection entries;
  protected final boolean allowDuplicates;
  protected final Object initialContextObject;
  protected final String deepIterateTagName;
  protected final String varStatus;

  public AbstrateDeepIterateStrategy(DeepIteratorStrategyBuilder builder)
  {
    context = builder.getContext();
    xpc = XPathContextExtender.getInstance(context);
    select = builder.getSelect();
    filter = builder.getFilter();
    var = builder.getVar();
    varStatus = builder.getVarStatus();
    allowDuplicates = builder.isAllowDuplicates();
    initialContextObject = builder.getContextObject();
    deepIterateTagName = builder.getTagName();
  }

  protected boolean satisfiesFilter(DeepIterateEntry entry)
  {
    if (filter != null && var != null)
    {
      context.setVariable(var, entry.getObject());
    }
    if(filter != null && varStatus != null) {
      context.setVariable(varStatus, new DeepIterateTag.LoopStatus(entry.getDepth(), entry.isLeaf()));
    }
    return filter != null ? xpc.resolveTest(entry.getObject(), filter) : true;
  }

  protected boolean createsRecursiveLoop(DeepIterateEntry entry)
  {
    return loopDetectionStack.contains(entry);
  }

  protected boolean isDuplicateEntry(DeepIterateEntry n)
  {
  
    return !allowDuplicates && entries.contains(n);
  }

  protected Object[] selectNodes(Object contextObject, int depth)
  {
    if (var != null)
    {
      context.setVariable(var, contextObject);
    }
    if(varStatus != null) {
      context.setVariable(varStatus, new DeepIterateTag.LoopStatus(depth));
    }
    return xpc.resolve(contextObject, select);
  }

  protected abstract void doSearch();

  public final Collection search()
  {
      try
      {
  //      entries = allowDuplicates ? (Collection)new LinkedHashSet() : (Collection)new LinkedList();
        // for performance on removal during filtering, always use a Linked HashSet()
        entries = new LinkedHashSet();
        loopDetectionStack = new HashSet();
        doSearch();
        return entries;
      }
      finally
      {
        // clean up
        loopDetectionStack = null;
        entries = null;
      }
    }

}