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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.IAnnotationManager;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.INodeInspectorExtension1;
import org.eclipse.jet.xpath.inspector.InspectorManager;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;


/**
 * Class implementing XPath Axes.
 *
 */
public abstract class Axis
{

  /**
   * Interface defining a function that is invoked if a particular object
   * has an annotation.
   * @see Axis#hasAnnotations(Context, Object, AnnotationAction)
   */
  private interface AnnotationAction
  {

    public Object doAction(Object annotation, IElementInspector inspector);
  }

  /**
   * Test whether the given context object has annotations, and if so
   * invoke {@link AnnotationAction#doAction(Object, IElementInspector)}.
   * @param context the XPath Context
   * @param contextNode the context node
   * @param action the action to perform
   * @return the return value of {@link AnnotationAction#doAction(Object, IElementInspector) doAction}
   * or <code>null</code> if the action is not invoked.
   */
  protected final Object hasAnnotations(Context context, Object contextNode, AnnotationAction action)
  {
    if (context.hasAnnotationManager())
    {
      IAnnotationManager annotationManager = context.getAnnotationManager();
      if (annotationManager.hasAnnotations(contextNode))
      {
        final Object annotation = annotationManager.getAnnotationObject(contextNode);
        IElementInspector inspector = (IElementInspector)InspectorManager.getInstance().getInspector(annotation);
        return action.doAction(annotation, inspector);
      }
    }
    return null;
  }

  private static final class ParentAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      NodeSet result = new NodeSetImpl(1);
      
      addParent(context, result);

      nodeTest.filter(result);

      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "parent"; //$NON-NLS-1$
    }
  }

  private static final class AncestorAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final NodeSet result = new NodeSetImpl();
      
      addAncestors(context, result);

      nodeTest.filter(result);

      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "ancestor"; //$NON-NLS-1$
    }
  }

  private static final class AncestorOrSelfAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final NodeSet result = new NodeSetImpl();
      
      result.add(context.getContextNode());
      
      addAncestors(context, result);

      nodeTest.filter(result);

      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "ancestor-of-self"; //$NON-NLS-1$
    }
  }

  private static final class AttributeAxis extends Axis
  {

    public NodeSet evaluate(final NodeTest nodeTest, Context context)
    {
      NodeSet result = NodeSetImpl.EMPTY_SET;
      final Object contextNode = context.getContextNode();
      INodeInspector inspector = context.getContextNodeInspector();
      if (inspector instanceof IElementInspector && inspector.getNodeKind(contextNode) == INodeInspector.NodeKind.ELEMENT)
      {
        // getAttributes only applies to ELEMENTs
        IElementInspector elementInspector = (IElementInspector)inspector;
        if (nodeTest.isSimpleNameTest())
        {
          Object namedAttribute = elementInspector.getNamedAttribute(contextNode, nodeTest.getNameTestExpandedName());
          if (namedAttribute == null)
          {
            // check for annotations...
            namedAttribute = hasAnnotations(context, contextNode, new AnnotationAction()
              {

                public Object doAction(Object annotation, IElementInspector inspector)
                {
                  return inspector.getNamedAttribute(annotation, nodeTest.getNameTestExpandedName());
                }

              });
          }
          if (namedAttribute != null)
          {
            result = new NodeSetImpl();
            result.add(namedAttribute);
          }
        }
        else
        {
          Object[] attributes = elementInspector.getAttributes(contextNode);
          result = arrayToNodeSet(attributes);
          // add in annotations...
          Object[] annAttributes = (Object[])hasAnnotations(context, contextNode, new AnnotationAction()
            {

              public Object doAction(Object annotation, IElementInspector inspector)
              {

                return inspector.getAttributes(annotation);
              }
            });
          if (annAttributes != null)
          {
            result.addAll(Arrays.asList(annAttributes));
          }
          nodeTest.filter(result);
        }
      }
      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ATTRIBUTE;
    }

    public String getAxisName()
    {
      return "attribute"; //$NON-NLS-1$
    }
  }

  private static final class ChildAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      NodeSet result = NodeSetImpl.EMPTY_SET;
      Object contextNode = context.getContextNode();
      INodeInspector inspector = context.getContextNodeInspector();
      NodeKind nodeKind = inspector.getNodeKind(contextNode);
      if (nodeKind == INodeInspector.NodeKind.ELEMENT || nodeKind == INodeInspector.NodeKind.ROOT)
      {
        if (nodeTest.isSimpleNameTest() && inspector instanceof INodeInspectorExtension1)
        {
          Object[] children = ((INodeInspectorExtension1)inspector).getNamedChildren(contextNode, nodeTest.getNameTestExpandedName());
          if (children != null)
          {
            result = arrayToNodeSet(children);
          }
        }
        else
        {
          Object[] children = inspector.getChildren(contextNode);
          result = arrayToNodeSet(children);
          nodeTest.filter(result);

        }
      }
      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "child"; //$NON-NLS-1$
    }

  }

  private static final class SelfAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      NodeSet nodeSet = new NodeSetImpl(1);
      nodeSet.add(context.getContextNode());
      nodeTest.filter(nodeSet);
      return nodeSet;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "self"; //$NON-NLS-1$
    }

  }

  private static final class DescendantAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final NodeSet result = new NodeSetImpl();
      
      addAllDescendants(context, result);
      
      nodeTest.filter(result);
      
      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "descendant"; //$NON-NLS-1$
    }

  }

  private static final class DescendantOrSelfAxis extends Axis
  {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      NodeSet result = new NodeSetImpl();
      result.add(context.getContextNode());
      
      addAllDescendants(context, result);
      
      nodeTest.filter(result);
      
      return result;
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }

    public String getAxisName()
    {
      return "descendant-or-self"; //$NON-NLS-1$
    }

  }
  
  private static final class FollowingSiblingAxis extends Axis {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final NodeSet result = new NodeSetImpl();
      addFollowingSiblings(context, result);
      
      nodeTest.filter(result);
      return result;
    }


    public String getAxisName()
    {
      return "following-sibling"; //$NON-NLS-1$
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }
    
  }

  private static final class FollowingAxis extends Axis {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final List ancestors = new ArrayList();
      addAncestors(context, ancestors);
      final NodeSet result = new NodeSetImpl();
      for (Iterator i = ancestors.iterator(); i.hasNext();)
      {
        // XXX It's not this simple. For each node in the following
        // siblings, need to add the sibling, and then all the
        // descendants.
//        addFollowingSiblings(context, result);
        final List siblings = new ArrayList();
        addFollowingSiblings(context, siblings);
        for (Iterator j = siblings.iterator(); j.hasNext();)
        {
          Object sibling = (Object)j.next();
          result.add(sibling);
          addAllDescendants(context.newSubContext(sibling, 1, 1), result);
        }
        context = context.newSubContext((Object)i.next(), 1, 1);
      }
      
      nodeTest.filter(result);
      return result;
    }


    public String getAxisName()
    {
      return "following"; //$NON-NLS-1$
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }
    
  }

  private static final class PrecedingSiblingAxis extends Axis {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final NodeSet result = new NodeSetImpl();
      addPrecedingSiblings(context, result);
      
      nodeTest.filter(result);
      return result;
    }


    public String getAxisName()
    {
      return "preceding-sibling"; //$NON-NLS-1$
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }
    
  }

  private static final class PrecedingAxis extends Axis {

    public NodeSet evaluate(NodeTest nodeTest, Context context)
    {
      final List ancestors = new ArrayList();
      addAncestors(context, ancestors);
      final NodeSet result = new NodeSetImpl();
      for (Iterator i = ancestors.iterator(); i.hasNext();)
      {
        // XXX It's not this simple. For each node in the following
        // siblings, need to add the sibling, and then all the
        // descendants.
//        addFollowingSiblings(context, result);
        final List siblings = new ArrayList();
        addPrecedingSiblings(context, siblings);
        for (Iterator j = siblings.iterator(); j.hasNext();)
        {
          Object sibling = (Object)j.next();
          addAllDescendantsReversed(context.newSubContext(sibling, 1, 1), result);
          result.add(sibling);
        }
        context = context.newSubContext((Object)i.next(), 1, 1);
      }
      
      nodeTest.filter(result);
      return result;
    }


    public String getAxisName()
    {
      return "preceding"; //$NON-NLS-1$
    }

    public NodeKind principalNodeKind()
    {
      return NodeKind.ELEMENT;
    }
    
  }

  private static final Axis childAxis = new ChildAxis();

  private static final Axis attributeAxis = new AttributeAxis();

  private static final Axis parentAxis = new ParentAxis();

  private static final Axis selfAxis = new SelfAxis();

  private static final Axis descendantAxis = new DescendantAxis();

  private static final Axis descendantOrSelfAxis = new DescendantOrSelfAxis();
  
  private static final Axis ancestorAxis = new AncestorAxis();

  private static final Axis ancestorOrSelfAxis = new AncestorOrSelfAxis();
  
  private static final Axis followingSiblingAxis = new FollowingSiblingAxis();

  private static final Axis followingAxis = new FollowingAxis();
  
  private static final Axis precedingSiblingAxis = new PrecedingSiblingAxis();

  private static final Axis precedingAxis = new PrecedingAxis();

  private static final Map axisByNameMap = new HashMap(15);
  static
  {
    axisByNameMap.put(childAxis.getAxisName(), childAxis);
    axisByNameMap.put(attributeAxis.getAxisName(), attributeAxis);
    axisByNameMap.put(parentAxis.getAxisName(), parentAxis);
    axisByNameMap.put(selfAxis.getAxisName(), selfAxis);
    axisByNameMap.put(descendantOrSelfAxis.getAxisName(), descendantOrSelfAxis);
    axisByNameMap.put(descendantAxis.getAxisName(), descendantAxis);
    axisByNameMap.put(ancestorAxis.getAxisName(), ancestorAxis);
    axisByNameMap.put(ancestorOrSelfAxis.getAxisName(), ancestorOrSelfAxis);
    axisByNameMap.put(followingSiblingAxis.getAxisName(), followingSiblingAxis);
    axisByNameMap.put(followingAxis.getAxisName(), followingAxis);
    axisByNameMap.put(precedingSiblingAxis.getAxisName(), precedingSiblingAxis);
    axisByNameMap.put(precedingAxis.getAxisName(), precedingAxis);

  }

  protected static void addAllDescendants(Context context, Collection result)
  {
    final NodeSet children = childAxis().evaluate(NodeTest.allNodes(), context);
    
    int position = 1;
    for (Iterator i = children.iterator(); i.hasNext();position++)
    {
      final Object object = (Object)i.next();
      result.add(object);
      final Context subContext = context.newSubContext(object, position, children.size());
      addAllDescendants(subContext,result);
      
    }
  }

  protected static void addAllDescendantsReversed(Context context, Collection result)
  {
    final List children = new ArrayList(childAxis().evaluate(NodeTest.allNodes(), context));
    Collections.reverse(children);
    
    int position = 1;
    for (Iterator i = children.iterator(); i.hasNext();position++)
    {
      final Object object = (Object)i.next();
      final Context subContext = context.newSubContext(object, position, children.size());
      addAllDescendantsReversed(subContext,result);
      result.add(object);
      
    }
  }

  protected static void addParent(Context context, final Collection result)
  {
    Object obj = context.getContextNode();

    INodeInspector inspector = context.getContextNodeInspector();
    Object parent = inspector.getParent(obj);

    if (parent != null)
    {
      result.add(parent);
    }
    return;
  }

  protected static void addAncestors(Context context, final Collection result)
  {
    Object obj = context.getContextNode();

    INodeInspector inspector = context.getContextNodeInspector();
    
    Object parent = inspector.getParent(obj);

    if (parent != null)
    {
      result.add(parent);
      addAncestors(context.newSubContext(parent, 1, 1), result);
    }
    return;
  }

  protected static void addFollowingSiblings(Context context, final Collection result)
  {
    final INodeInspector inspector = context.getContextNodeInspector();
    final Object contextNode = context.getContextNode();
    final NodeKind nodeKind = inspector.getNodeKind(contextNode);
    if(nodeKind != NodeKind.ATTRIBUTE && nodeKind != NodeKind.NAMESPACE) {
      final Object parent = inspector.getParent(contextNode);
      final List children = Arrays.asList(inspector.getChildren(parent));
      int index = children.indexOf(contextNode);
      result.addAll(children.subList(index + 1, children.size()));
    }
  }

  protected static void addPrecedingSiblings(Context context, final Collection result)
  {
    final INodeInspector inspector = context.getContextNodeInspector();
    final Object contextNode = context.getContextNode();
    final NodeKind nodeKind = inspector.getNodeKind(contextNode);
    if(nodeKind != NodeKind.ATTRIBUTE && nodeKind != NodeKind.NAMESPACE) {
      final Object parent = inspector.getParent(contextNode);
      final List children = Arrays.asList(inspector.getChildren(parent));
      int index = children.indexOf(contextNode);
      final List precedingChildren = children.subList(0, index);
      Collections.reverse(precedingChildren);
      result.addAll(precedingChildren);
    }
  }

  /**
   * 
   */
  public Axis()
  {
    super();
  }

  public abstract NodeSet evaluate(NodeTest nodeTest, Context context);

  public abstract NodeKind principalNodeKind();

  public abstract String getAxisName();

  public static Axis childAxis()
  {
    return childAxis;
  }

  public static Axis attributeAxis()
  {
    return attributeAxis;
  }

  public static Axis parentAxis()
  {
    return parentAxis;
  }

  public static Axis descendant()
  {
    return descendantAxis;
  }

  public static Axis descendantOrSelf()
  {
    return descendantOrSelfAxis;
  }

  public static Axis axisByName(String axisName)
  {
    return (Axis)axisByNameMap.get(axisName);
  }

  public static Axis selfAxis()
  {
    return selfAxis;
  }

  public String toString()
  {
    return getAxisName() + "::"; //$NON-NLS-1$
  }

  /**
   * @param attributes
   * @return
   */
  protected NodeSet arrayToNodeSet(Object[] attributes)
  {
    NodeSet result;
    result = new NodeSetImpl(attributes.length);
    for (int i = 0; i < attributes.length; i++)
    {
      result.add(attributes[i]);
    }
    return result;
  }

  public static Axis ancestor()
  {
    return ancestorAxis;
  }

  public static Axis ancestorOrSelf()
  {
    return ancestorOrSelfAxis;
  }

  public static Axis followingSibling()
  {
    return followingSiblingAxis;
  }

  public static Axis following()
  {
    return followingAxis;
  }

  public static Axis precedingSibling()
  {
    return precedingSiblingAxis;
  }

  public static Axis preceding()
  {
    return precedingAxis;
  }

}
