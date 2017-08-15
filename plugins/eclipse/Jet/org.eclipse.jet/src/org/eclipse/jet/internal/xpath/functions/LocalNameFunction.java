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
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Implement the XPath 'local-name' function.
 *
 */
public class LocalNameFunction implements XPathFunction, XPathFunctionWithContext
{

  public static final XPathFunctionMetaData FUNCTION_META_DATA = new XPathFunctionMetaData("local-name", null, new LocalNameFunction(), 0, 1); //$NON-NLS-1$
  private Context context;

  /**
   * 
   */
  public LocalNameFunction()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    ExpandedName en = null;
    if (args.size() == 0)
    {
      en = context.getContextNodeInspector().expandedNameOf(context.getContextNode());
    }
    else if (!(args.get(0) instanceof NodeSet))
    {
      String msg = JET2Messages.XPath_MustBeNodeNodeSet;
      throw new XPathRuntimeException(MessageFormat.format(msg, new Object []{ "local-name" })); //$NON-NLS-1$
    }
    else
    {
      NodeSet nodeSet = (NodeSet)args.get(0);
      if (nodeSet.size() != 0)
      {
        Object firstNode = nodeSet.iterator().next();
        en = InspectorManager.getInstance().getInspector(firstNode).expandedNameOf(firstNode);
      }
    }
    return en != null ? en.getLocalPart() : ""; //$NON-NLS-1$
  }

  public void setContext(Context context)
  {
    this.context = context;
  }

}
