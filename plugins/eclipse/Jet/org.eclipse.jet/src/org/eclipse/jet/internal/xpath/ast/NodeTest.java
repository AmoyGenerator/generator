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


import java.util.Iterator;
import java.util.Set;

import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;


/**
 * Represent a node-test
 *
 */
public abstract class NodeTest
{

  private static final class AllNodesTest extends NodeTest
  {

    public void filter(Set nodeSet)
    {
      // nothing to do
    }

    public String toString()
    {
      return "node()"; //$NON-NLS-1$
    }
  }

  private static class SimpleKindNodeTest extends NodeTest
  {

    protected final NodeTestKind testKind;

    public SimpleKindNodeTest(NodeTestKind testKind)
    {
      if (testKind == null)
      {
        throw new NullPointerException();
      }
      this.testKind = testKind;
    }

    public void filter(Set nodeSet)
    {
      InspectorManager inspectorMgr = InspectorManager.getInstance();
      for (Iterator i = nodeSet.iterator(); i.hasNext();)
      {
        Object obj = (Object)i.next();
        INodeInspector inspector = inspectorMgr.getInspector(obj);
        if (!testKind.test(inspector.getNodeKind(obj)))
        {
          i.remove();
        }
      }
    }

    public String toString()
    {
      return testKind.toString() + "()"; //$NON-NLS-1$
    }
  }

  public static final class SpecificPINodeTest extends SimpleKindNodeTest
  {

    private final String piLiteral;

    public SpecificPINodeTest(String piLiteral)
    {
      super(NodeTestKind.PROCESSING_INSTRUCTION);
      this.piLiteral = piLiteral;
    }

    public void filter(Set nodeSet)
    {
      super.filter(nodeSet);
      InspectorManager inspectorMgr = InspectorManager.getInstance();
      for (Iterator i = nodeSet.iterator(); i.hasNext();)
      {
        Object obj = (Object)i.next();
        INodeInspector inspector = inspectorMgr.getInspector(obj);
        ExpandedName expandedName = inspector.expandedNameOf(obj);
        if (!piLiteral.equals(expandedName.getLocalPart()))
        {
          i.remove();
        }
      }
    }

    public String toString()
    {
      if (piLiteral == null)
      {
        return super.toString();
      }
      return testKind.toString() + "(" + piLiteral + ")"; //$NON-NLS-1$//$NON-NLS-2$
    }
  }

  public static final class NameTest extends SimpleKindNodeTest
  {

    private final ExpandedName testName;

    private final NodeKind principalNodeKind;

    public NameTest(NodeKind principalNodeKind, ExpandedName testName)
    {
      super(NodeTestKind.PRINCIPAL);
      this.principalNodeKind = principalNodeKind;
      this.testName = testName;
    }

    public void filter(Set nodeSet)
    {
      InspectorManager inspectorMgr = InspectorManager.getInstance();
      for (Iterator i = nodeSet.iterator(); i.hasNext();)
      {
        Object obj = (Object)i.next();
        INodeInspector inspector = inspectorMgr.getInspector(obj);

        if (inspector == null || inspector.getNodeKind(obj) != principalNodeKind || !inspector.testExpandedName(obj, testName))
        {
          i.remove();
        }
      }
    }

    public boolean isSimpleNameTest()
    {
      return !testName.hasWildCards();
    }

    public ExpandedName getNameTestExpandedName()
    {
      return testName;
    }

    public String toString()
    {
      return testName.toString();
    }
  }

  private static final NodeTest allNodesTest = new AllNodesTest();

  private static final NodeTest textNodesTest = new SimpleKindNodeTest(NodeTestKind.TEXT);

  private static final NodeTest commentNodesTest = new SimpleKindNodeTest(NodeTestKind.COMMENT);

  private static final NodeTest allPIsNodesTest = new SimpleKindNodeTest(NodeTestKind.PROCESSING_INSTRUCTION);

  /**
   * 
   */
  private NodeTest()
  {
    super();
  }

  /**
   * Filter a node set by applying this node test.
   * @param nodeSet the node set to filter. Cannot be <code>null</code>.
   */
  public abstract void filter(Set nodeSet);

  public static NodeTest allNodes()
  {
    return allNodesTest;
  }

  public static NodeTest textNodes()
  {
    return textNodesTest;
  }

  public static NodeTest commentNodes()
  {
    return commentNodesTest;
  }

  public static NodeTest allProcessingInstructionNodes()
  {
    return allPIsNodesTest;
  }

  public static NodeTest processingInstructionNodes(String piLiteral)
  {
    return new SpecificPINodeTest(piLiteral);
  }

  public static NodeTest nameTest(INodeInspector.NodeKind principalKind, ExpandedName testName)
  {
    return new NameTest(principalKind, testName);
  }

  public boolean isSimpleNameTest()
  {
    return false;
  }

  public ExpandedName getNameTestExpandedName()
  {
    return null;
  }
}
