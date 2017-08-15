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
 * $Id: BreadthFirstStrategy.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.osgi.util.NLS;

/**
 * Implement a bread first deep traversal strategy
 */
public class BreadthFirstStrategy extends AbstrateDeepIterateStrategy implements IDeepIterateStrategy
{

  public BreadthFirstStrategy(DeepIteratorStrategyBuilder builder)
  {
   super(builder);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.control.IDeepIterateStrategy#checkDeepContentAllowed()
   */
  public void checkDeepContentAllowed() throws JET2TagException
  {
    if(!supportsDeepContent()) {
      throw new JET2TagException(NLS.bind(Messages.BreadthFirstStrategy_DeepContentNotAllowed, deepIterateTagName, "breadthFirst")); //$NON-NLS-1$
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.taglib.control.IDeepIterateStrategy#supportsDeepContent()
   */
  public boolean supportsDeepContent()
  {
    return false;
  }

  protected void doSearch()
  {
    LinkedList queue = new LinkedList();
    
    DeepIterateEntry initialEntry = new DeepIterateEntry(initialContextObject, 0);
    queue.add(initialEntry);
    
    while(queue.size() > 0) {
      final DeepIterateEntry entry = (DeepIterateEntry)queue.removeFirst();
      
      // process the entry...
      if(entry.getDepth() > 0) {
        entries.add(entry);
      }
      
      boolean hasChildren = false;
      final Object[] nodes = this.selectNodes(entry.getObject(), entry.getDepth() + 1);
      for (int i = 0; i < nodes.length; i++)
      {
        final DeepIterateEntry child = new DeepIterateEntry(nodes[i], entry.getDepth() + 1);
        if(!this.isDuplicateEntry(child)) {
          queue.addLast(child);
          hasChildren = true;
        }
      }
      entry.setLeaf(!hasChildren);
    }
    
    for (Iterator i = entries.iterator(); i.hasNext();)
    {
      DeepIterateEntry entry = (DeepIterateEntry)i.next();
      if(!this.satisfiesFilter(entry)) {
        i.remove();
      }
    }
  }

}
