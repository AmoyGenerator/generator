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


import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionMetaData;
import org.eclipse.jet.xpath.XPathFunctionWithContext;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Implement the XPath 'name' function.
 * <p>
 * See <a href="http://www.w3.org/TR/xpath#function-name">http://www.w3.org/TR/xpath#function-name</a>.
 *
 */
public class NameFunction implements XPathFunction, XPathFunctionWithContext
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("name", null, new NameFunction(), 0, 1); //$NON-NLS-1$
  private Context context;

  /**
   * 
   */
  public NameFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {

    String name = ""; //$NON-NLS-1$
    if (args.size() == 0)
    {
      name = context.getContextNodeInspector().nameOf(context.getContextNode());
    }
    else if (!(args.get(0) instanceof NodeSet))
    {
      String msg = JET2Messages.XPath_MustBeNodeNodeSet;
      throw new XPathRuntimeException(MessageFormat.format(msg, new Object []{ "name" })); //$NON-NLS-1$
    }
    else
    {
      NodeSet nodeSet = (NodeSet)args.get(0);
      if (nodeSet.size() != 0)
      {
        Object firstNode = nodeSet.iterator().next();
        name = InspectorManager.getInstance().getInspector(firstNode).nameOf(firstNode);
      }
    }
    return name != null ? name : ""; //$NON-NLS-1$
  }

  public void setContext(Context context)
  {
    this.context = context;
  }

}
