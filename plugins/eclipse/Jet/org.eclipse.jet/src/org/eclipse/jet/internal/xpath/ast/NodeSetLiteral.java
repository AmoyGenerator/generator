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


import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;


/**
 * A Testing artifact - allows creation of a hard-coded node-set.
 *
 */
public class NodeSetLiteral extends NodeSetExpr
{

  private final NodeSet set;

  /**
   * 
   */
  public NodeSetLiteral(NodeSet set)
  {
    super();
    this.set = set;
  }

  public NodeSet evalAsNodeSet(Context context)
  {
    return set;
  }

}
