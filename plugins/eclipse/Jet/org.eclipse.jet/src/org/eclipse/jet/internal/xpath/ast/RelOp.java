/**
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 */
package org.eclipse.jet.internal.xpath.ast;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.internal.xpath.functions.BooleanFunction;
import org.eclipse.jet.internal.xpath.functions.NumberFunction;
import org.eclipse.jet.internal.xpath.functions.StringFunction;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Implement relational operations.
 *
 */
public abstract class RelOp extends BooleanExpr
{

  private interface ITest
  {
    boolean test(Boolean b, Object o);

    boolean test(Number n, Object o);

    boolean test(String s, Object o);

  }

  private abstract static class RelativeTest implements ITest
  {
    public final boolean test(Boolean b, Object o)
    {
      // relative tests are all done as numbers
      return test(new Double(NumberFunction.evaluate(b)), o);
    }

    public final boolean test(String s, Object o)
    {
      // relative tests are all done as numbers
      return test(new Double(NumberFunction.evaluate(s)), o);
    }
  }

  private static final ITest equalsTest = new ITest()
    {
      public boolean test(Boolean b, Object o)
      {
        return b.booleanValue() == BooleanFunction.evaluate(o);
      }

      public boolean test(Number n, Object o)
      {
        return n.doubleValue() == NumberFunction.evaluate(o);
      }

      public boolean test(String s, Object o)
      {
        return s.equals(StringFunction.evaluate(o));
      }

    };

  private static final ITest notEqualsTest = new ITest()
    {

      public boolean test(Boolean b, Object o)
      {
        return !equalsTest.test(b, o);
      }

      public boolean test(Number n, Object o)
      {
        return !equalsTest.test(n, o);
      }

      public boolean test(String s, Object o)
      {
        return !equalsTest.test(s, o);
      }

    };

  private static final ITest lessThanTest = new RelativeTest()
    {
      public boolean test(Number n, Object o)
      {
        return n.doubleValue() < NumberFunction.evaluate(o);
      }
    };

  private static final ITest lessThanOrEqualTest = new RelativeTest()
    {
      public boolean test(Number n, Object o)
      {
        return n.doubleValue() <= NumberFunction.evaluate(o);
      }
    };

  protected final ExprNode left;

  protected final ExprNode right;

  private final ITest test;

  public static final class Eq extends RelOp
  {

    public Eq(ExprNode left, ExprNode right)
    {
      super(left, right, equalsTest);
    }

    public String opName()
    {
      return "="; //$NON-NLS-1$
    }
  }

  public static final class NotEq extends RelOp
  {

    public NotEq(ExprNode left, ExprNode right)
    {
      super(left, right, notEqualsTest);
    }

    public String opName()
    {
      return "!="; //$NON-NLS-1$
    }
  }

  public static final class Lt extends RelOp
  {

    public Lt(ExprNode left, ExprNode right)
    {
      super(left, right, lessThanTest);
    }

    public String opName()
    {
      return "<"; //$NON-NLS-1$
    }
  }

  public static final class Lte extends RelOp
  {

    public Lte(ExprNode left, ExprNode right)
    {
      super(left, right, lessThanOrEqualTest);
    }

    public String opName()
    {
      return "<="; //$NON-NLS-1$
    }

  }

  public static final class Gt extends RelOp
  {

    public Gt(ExprNode left, ExprNode right)
    {
      // Reversal of left and right is intensional!
      super(right, left, lessThanTest);
    }

    public String opName()
    {
      return ">"; //$NON-NLS-1$
    }

    public String toString()
    {
      // operand reveral is intentional!
      return "(" + right.toString() + opName() + left.toString() + ")"; //$NON-NLS-1$//$NON-NLS-2$
    }
  }

  public static final class Gte extends RelOp
  {

    public Gte(ExprNode left, ExprNode right)
    {
      // Reversal of left and right is intensional!
      super(right, left, lessThanOrEqualTest);
    }

    public String opName()
    {
      return ">="; //$NON-NLS-1$
    }

    public String toString()
    {
      // operand reveral is intentional!
      return "(" + right.toString() + opName() + left.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * 
   * @param left
   * @param right
   * @param test
   */
  private RelOp(ExprNode left, ExprNode right, ITest test)
  {
    super();
    this.left = left;
    this.right = right;
    this.test = test;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.xpath.ast.BooleanNode#evalAsBoolean(org.eclipse.jet.xpath.Context)
   */
  public final boolean evalAsBoolean(Context context)
  {
    Object leftVal = left.evalAsObject(context);
    Object rightVal = right.evalAsObject(context);

    // The XPath specification determines this. 
    // See http://www.w3.org/TR/xpath#booleans for details.
    if (leftVal instanceof NodeSet)
    {
      return nodeSetTest((NodeSet)leftVal, rightVal, false);
    }
    else if (rightVal instanceof NodeSet)
    {
      return nodeSetTest((NodeSet)rightVal, leftVal, true /* argsReversed */);
    }
    else if (leftVal instanceof Boolean)
    {
      return test.test((Boolean)leftVal, rightVal);
    }
    else if (rightVal instanceof Boolean)
    {
      // note intentional arg reversal
      return test.test((Boolean)rightVal, leftVal);
    }
    else if (leftVal instanceof Number)
    {
      return test.test((Number)leftVal, rightVal);
    }
    else if (rightVal instanceof Number)
    {
      // note intentional arg reversal
      return test.test((Number)rightVal, leftVal);
    }
    else
    {
      String leftString = StringFunction.evaluate(leftVal);
      String rightString = StringFunction.evaluate(rightVal);
      return test.test(leftString, rightString);
    }
  }

  private final boolean nodeSetTest(NodeSet leftNodeSet, Object rightObject, boolean argsReversed)
  {
    if (rightObject instanceof NodeSet)
    {
      return nodeSetNodeSetTest(leftNodeSet, (NodeSet)rightObject, argsReversed);
    }
    else if (rightObject instanceof String)
    {
      return nodeSetStringTest(leftNodeSet, (String)rightObject, argsReversed);
    }
    else if (rightObject instanceof Number)
    {
      return nodeSetNumberTest(leftNodeSet, (Number)rightObject, argsReversed);
    }
    else if (rightObject instanceof Boolean)
    {
      return nodeSetBooleanTest(leftNodeSet, (Boolean)rightObject, argsReversed);
    }
    return false;
  }

  private final boolean nodeSetNodeSetTest(NodeSet leftNodeSet, NodeSet rightNodeSet, boolean argsReversed)
  {
    InspectorManager inspectorManager = InspectorManager.getInstance();

    // create a list of left string values...
    List leftStrings = new ArrayList(leftNodeSet.size());
    for (Iterator iter = leftNodeSet.iterator(); iter.hasNext();)
    {
      Object contextNode = (Object)iter.next();
      INodeInspector inspector = inspectorManager.getInspector(contextNode);
      leftStrings.add(inspector.stringValueOf(contextNode));
    }

    // find a right string value that compares successfully with a left value
    for (Iterator i = rightNodeSet.iterator(); i.hasNext();)
    {
      Object contextNode = (Object)i.next();
      INodeInspector inspector = inspectorManager.getInspector(contextNode);
      String rightString = inspector.stringValueOf(contextNode);
      if (leftStrings.contains(rightString))
      {
        return true;
      }
    }
    return false;
  }

  private final boolean nodeSetStringTest(NodeSet leftNodeSet, String rightString, boolean argsReversed)
  {
    InspectorManager inspectorManager = InspectorManager.getInstance();
    for (Iterator iter = leftNodeSet.iterator(); iter.hasNext();)
    {
      Object contextNode = (Object)iter.next();
      INodeInspector inspector = inspectorManager.getInspector(contextNode);
      String leftString = inspector.stringValueOf(contextNode);
      if ((!argsReversed && test.test(leftString, rightString)) || (argsReversed && test.test(rightString, leftString)))
      {
        return true;
      }
    }
    return false;
  }

  private final boolean nodeSetNumberTest(NodeSet leftNodeSet, Number rightNumber, boolean argsReversed)
  {
    InspectorManager inspectorManager = InspectorManager.getInstance();
    for (Iterator iter = leftNodeSet.iterator(); iter.hasNext();)
    {
      Object contextNode = (Object)iter.next();
      INodeInspector inspector = inspectorManager.getInspector(contextNode);
      String leftNodeAsString = inspector.stringValueOf(contextNode);
      Double leftNumber = new Double(NumberFunction.evaluate(leftNodeAsString));
      if ((!argsReversed && test.test(leftNumber, rightNumber)) || (argsReversed && test.test(rightNumber, leftNumber)))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Compare a nodeset to a boolean. According to XPath 1.0, this means the node set
   * is converted to a boolean via the {@link BooleanFunction boolean} function.
   * @param leftNodeSet the node set
   * @param rightBoolean the boolean
   * @param argsReversed if <code>true</code> left and right are reversed during comparison.
   * @return <code>true</code> if test passes, <code>false</code> otherwise.
   */
  private final boolean nodeSetBooleanTest(NodeSet leftNodeSet, Boolean rightBoolean, boolean argsReversed)
  {
    Boolean leftBoolean = Boolean.valueOf(BooleanFunction.evaluate(leftNodeSet));
    if (!argsReversed)
    {
      return test.test(leftBoolean, rightBoolean);
    }
    else
    {
      return test.test(rightBoolean, leftBoolean);

    }
  }

  public abstract String opName();

  public String toString()
  {
    return "(" + left.toString() + opName() + right.toString() + ")"; //$NON-NLS-1$//$NON-NLS-2$
  }

}
