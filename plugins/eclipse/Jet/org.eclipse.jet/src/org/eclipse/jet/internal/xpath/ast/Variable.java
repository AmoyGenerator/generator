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
package org.eclipse.jet.internal.xpath.ast;


import java.text.MessageFormat;

import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.jet.xpath.XPathVariableResolver;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Represent an XPath variable access
 *
 */
public class Variable extends ExprNode
{

  private final String name;

  /**
   * 
   */
  public Variable(String name)
  {
    super();
    this.name = name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.ExprNode#evalAsObject(org.eclipse.jet.xpath.Context)
   */
  public Object evalAsObject(Context context)
  {
    XPathVariableResolver resolver = context.getVariableResolver();
    Object varValue = resolver.resolveVariable(name);

    if (varValue == null)
    {
      // interpret a null value as meaning the variable is not defined
      String msg = JET2Messages.XPath_VariableUndefined;
      throw new XPathRuntimeException(MessageFormat.format(msg, new Object []{ name }));
    }

    if (varValue instanceof String || varValue instanceof NodeSet || varValue instanceof Boolean || varValue instanceof Number)
    {
      // nothing to to, already "packaged" for expression evaluation...
    }
    else
    {
      INodeInspector inspector = InspectorManager.getInstance().getInspector(varValue);
      if (inspector != null && inspector.getNodeKind(varValue) != null)
      {
        // an inspector recognized the object, so put it into a NodeSet
        NodeSet nodeSet = new NodeSetImpl(1);
        nodeSet.add(varValue);
        varValue = nodeSet;
      }
      else
      {
        // we don't know what we have, just return it, and see what happens
      }
    }
    return varValue;
  }

  public String getVariableName()
  {
    return name;
  }

  public String toString()
  {
    return "$" + getVariableName(); //$NON-NLS-1$
  }
}
