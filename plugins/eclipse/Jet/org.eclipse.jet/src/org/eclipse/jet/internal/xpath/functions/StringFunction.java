/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.jet.internal.xpath.functions;


import java.util.Collection;
import java.util.List;

import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Implement the XPath 'string' function.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-string">http://www.w3.org/TR/xpath#function-string</a>.
 *
 */
public class StringFunction implements XPathFunction, XPathFunctionWithContext
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("string", null, new StringFunction(), 0, 1); //$NON-NLS-1$
  private Context context;

  /**
   * 
   */
  public StringFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    if (args == null || args.size() > 1)
    {
      throw new IllegalArgumentException();
    }
    Object arg;
    if (args.size() == 0)
    {
      arg = context.getContextNode();
    }
    else
    {
      arg = args.get(0);
    }
    return evaluate(arg);
  }

  public static String evaluate(Object object)
  {
    if(object == null)
    {
      return ""; //$NON-NLS-1$
    }
    else if (object instanceof String)
    {
      return (String)object;
    }
    else if (object instanceof Boolean)
    {
      return ((Boolean)object).toString();
    }
    else if (object instanceof Double)
    {
      final Double dbl = (Double)object;
      if(dbl.isNaN()) {
        return "NaN"; //$NON-NLS-1$
      } else if(dbl.compareTo(new Double(0D)) == 0 || dbl.compareTo(new Double(-0D)) == 0) {
        return "0"; //$NON-NLS-1$
      } else if(dbl.doubleValue() == Double.POSITIVE_INFINITY) {
        return "Infinity"; //$NON-NLS-1$
      } else if(dbl.doubleValue() == Double.NEGATIVE_INFINITY) {
        return "-Infinity"; //$NON-NLS-1$
      } else if(dbl.doubleValue() == Math.rint(dbl.doubleValue())) {
        // integer values format display without a decimal place
        return String.valueOf(dbl.longValue());
      } else {
        return dbl.toString();
      }
    }
    else if (object instanceof Collection)
    {
      Collection collection = (Collection)object;
      if (collection.size() == 0)
      {
        return ""; //$NON-NLS-1$
      }
      else
      {
        return evaluate(collection.iterator().next());
      }
    }
    else
    {
      // didn't reconize it, see if we have an inspector for the object...
      INodeInspector inspector = InspectorManager.getInstance().getInspector(object);
      if (inspector != null)
      {
        return inspector.stringValueOf(object);
      }
      else
      {
        // give up, an just call Java toString
        return object.toString();
      }
    }
  }

  public void setContext(Context context)
  {
    this.context = context;
  }
}
