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


import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;


/**
 * Enumeration of XPath node test kinds.
 *
 */
public abstract class NodeTestKind
{

  /**
   * Corresponds to the node test 'node()'
   */
  public static final NodeTestKind ALL = new NodeTestKind("node") { //$NON-NLS-1$

      public boolean test(NodeKind nodeKind)
      {
        return true;
      }

    };

  /**
   * Corresponds to a name test.
   */
  public static final NodeTestKind PRINCIPAL = new NodeTestKind("PRINCIPAL") { //$NON-NLS-1$

      public boolean test(NodeKind nodeKind)
      {
        // should never get here...
        return false;
      }

    };

  /**
   * Corresponds to a node test 'comment()'
   */
  public static final NodeTestKind COMMENT = new NodeTestKind("comment") { //$NON-NLS-1$

      public boolean test(NodeKind nodeKind)
      {
        return nodeKind == NodeKind.COMMENT;
      }
    };

  /**
   * Corresponds to a node test 'text()'
   */
  public static final NodeTestKind TEXT = new NodeTestKind("text") { //$NON-NLS-1$

      public boolean test(NodeKind nodeKind)
      {
        return nodeKind == NodeKind.TEXT;
      }
    };

  /**
   * Corresponds to a node test 'processing-instruction()' or
   * 'processing-instruction(<I>literal</I>)'
   */
  public static final NodeTestKind PROCESSING_INSTRUCTION = new NodeTestKind("processing-instruction") { //$NON-NLS-1$

      public boolean test(NodeKind nodeKind)
      {
        return nodeKind == NodeKind.PROCESSING_INSTRUCTION;
      }
    };

  private final String displayName;

  /**
   * @param displayName 
   * 
   */
  private NodeTestKind(String displayName)
  {
    super();
    this.displayName = displayName;
  }

  public String toString()
  {
    return displayName;
  }

  public abstract boolean test(INodeInspector.NodeKind nodeKind);
}
