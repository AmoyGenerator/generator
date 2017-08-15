/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: SortFunction.java,v 1.1 2008/04/22 02:12:56 pelder Exp $
 */
package org.eclipse.jet.internal.xpath.functions.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFactory;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.jet.xpath.XPathUtil;

/**
 * Implementation of sort(NodeSet,String key) XPath function
 */
public class SortFunction implements XPathFunction, XPathFunctionWithContext
{

  private static class XPathComparator implements Comparator
  {
    private final XPathExpression xpathExpr;

    public XPathComparator(XPathExpression xpathExpr)
    {
      this.xpathExpr = xpathExpr;
    }

    public int compare(Object o1, Object o2)
    {
      Object r1 = xpathExpr.evaluate(o1);
      Object r2 = xpathExpr.evaluate(o2);
      if(r1 instanceof Comparable) {
        return ((Comparable)r1).compareTo(r2);
      } else {
        // not comparable. Try casting to strings...
        String s1 = XPathUtil.xpathString(r1);
        String s2 = XPathUtil.xpathString(r2);
        return s1.compareTo(s2);
      }
    }
  }
  
  private static class DescendingXPathComparator extends XPathComparator
  {

    public DescendingXPathComparator(XPathExpression xpathExpr)
    {
      super(xpathExpr);
    }
    
    public int compare(Object o1, Object o2)
    {
      return super.compare(o2, o1);
    }
    
  }

  private static class ChainedComparator implements Comparator
  {
    private final Comparator[] comparatorChain;

    public ChainedComparator(Comparator[] comparatorChain)
    {
      this.comparatorChain = comparatorChain;
      
    }

    public int compare(Object o1, Object o2)
    {
      for (int i = 0; i < comparatorChain.length; i++)
      {
        int result = comparatorChain[i].compare(o1, o2);
        if(result != 0) {
          return result;
        }
      }
      return 0;
    }
    
  }
  private Context context;

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    Object rawNodeSet = args.get(0);
    List nodes;
    if(rawNodeSet instanceof Collection) {
      nodes = new ArrayList((Collection)rawNodeSet);
    } else if(rawNodeSet.getClass().isArray()) {
      nodes = Arrays.asList((Object[])rawNodeSet);
    } else {
      // only one object, don't need to sort it at all.
      return Collections.singleton(rawNodeSet);
    }
    try
    {
      Comparator comparator = createComparator(args.subList(1, args.size()));

      Collections.sort(nodes, comparator);
      return nodes;
    }
    catch (XPathException e)
    {
      throw new XPathRuntimeException(e);
    }
  }

  private Comparator createComparator(List keyExprList) throws XPathException
  {
    final XPath xpath = createXPathEnv();
    
    final Comparator[] comparators = new Comparator[keyExprList.size()];
    
    for (int i = 0; i < comparators.length; i++)
    {
      final String keyExpr = XPathUtil.xpathString(keyExprList.get(i));
      final int orderIdx = keyExpr.lastIndexOf("::"); //$NON-NLS-1$
      final String ordering = orderIdx >= 0 ? keyExpr.substring(orderIdx) : null;
      if("::descending".equalsIgnoreCase(ordering)) { //$NON-NLS-1$
        comparators[i] = new DescendingXPathComparator(xpath.compile(keyExpr.substring(0, orderIdx))); 
      } else if("::ascending".equalsIgnoreCase(ordering)) { //$NON-NLS-1$
        comparators[i] = new XPathComparator(xpath.compile(keyExpr.substring(0, orderIdx)));
      } else {
        comparators[i] = new XPathComparator(xpath.compile(keyExpr));
      }
    }
    
    
   return comparators.length == 1 ? comparators[0] : new ChainedComparator(comparators);
  }

  private XPath createXPathEnv()
  {
    XPath xpath = XPathFactory.newInstance().newXPath(context.getAnnotationManager());
    xpath.setXPathVariableResolver(context.getVariableResolver());
    xpath.setXPathFunctionResolver(context.getFunctionResolver());
    return xpath;
  }

  public void setContext(Context context)
  {
    this.context = context;
  }

}
