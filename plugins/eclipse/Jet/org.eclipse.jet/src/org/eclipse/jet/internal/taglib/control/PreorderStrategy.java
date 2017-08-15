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
 * $Id: PreorderStrategy.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;


import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.osgi.util.NLS;


/**
 * @author pelder
 */
public class PreorderStrategy extends AbstrateDeepIterateStrategy implements IDeepIterateStrategy
{

  public PreorderStrategy(DeepIteratorStrategyBuilder builder)
  {
    super(builder);
  }

  private boolean search(Object contextObject, int depth)
  {
    boolean hasChildren = false;
    Object[] nodes = selectNodes(contextObject, depth);
    for (int i = 0; i < nodes.length; i++)
    {
      final DeepIterateEntry entry = new DeepIterateEntry(nodes[i], depth + 1);

      if (isDuplicateEntry(entry))
      {
        continue;
      }
      if (createsRecursiveLoop(entry))
      {
        continue;
      }
      // if we get here, then this is not a leaf...
      hasChildren = true;
      // create the entry, and tentatively add it to the result...
      entries.add(entry);
      
      // search out child entries...
      loopDetectionStack.add(entry);
      boolean nHasChildren = search(entry.getObject(), depth + 1);
      loopDetectionStack.remove(entry);
      
      // mark whether node is a leaf...
      entry.setLeaf(!nHasChildren);
      
      if(!satisfiesFilter(entry)) {
        // remove the entry...
        entries.remove(entry);
      }
    }
    return hasChildren;
  }


  /**
   * @return
   */
  protected void doSearch()
  {
    search(initialContextObject, 0);
  }

  public boolean supportsDeepContent()
  {
    // c:deepContent is permitted if there is no filter...
    return filter == null;
  }

  public void checkDeepContentAllowed() throws JET2TagException
  {
    if(!supportsDeepContent()) {
      throw new JET2TagException(NLS.bind(Messages.PreorderStrategy_DeepContentTagNotAllowed, deepIterateTagName));
    }

  }

}
