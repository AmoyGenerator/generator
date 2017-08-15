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
package org.eclipse.jet.xpath;


import java.util.Collections;
import java.util.Map;

import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Define the XPath Context
 */
public final class Context
{

  private static DefaultXPathFunctionResolver defaultXPathFunctionResolver;

  private static DefaultXPathFunctionResolver getDefaultFunctionResolver()
  {
    if(defaultXPathFunctionResolver == null) {
      defaultXPathFunctionResolver = new DefaultXPathFunctionResolver();
    }
    return defaultXPathFunctionResolver;
  }

  private final Object contextNode;

  private final int contextPosition;

  private final int contextSize;

  private final InspectorManager inspectorManager;

  private final XPathVariableResolver variableResolver;

  private final IAnnotationManager annotationManager;

  private final XPathFunctionResolver functionResolver;

  public Context(Object contextNode, IAnnotationManager annotationManager)
  {
    this(contextNode, Collections.EMPTY_MAP, annotationManager);
  }

  /**
   * @param contextNode
   * @param contextPosition
   * @param contextSize
   * @param variableResolver
   * @param functionResolver
   * @param annotationManager
   */
  private Context(
    Object contextNode,
    int contextPosition,
    int contextSize,
    XPathVariableResolver variableResolver,
    XPathFunctionResolver functionResolver, 
    IAnnotationManager annotationManager)
  {
    super();
    this.contextNode = contextNode;
    this.contextPosition = contextPosition;
    this.contextSize = contextSize;
    this.variableResolver = variableResolver;
    this.functionResolver = functionResolver;
    this.annotationManager = annotationManager;
    inspectorManager = InspectorManager.getInstance();
  }

  public Context(Object contextNode, final Map variables, IAnnotationManager annotationManager)
  {
    this(contextNode, new XPathVariableResolver()
      {

        public Object resolveVariable(String variableName)
        {
          return variables != null ? variables.get(variableName) : null;
        }

      }, annotationManager);
  }

  public Context(Object contextNode, XPathVariableResolver variableResolver, IAnnotationManager annotationManager)
  {
    this(contextNode, variableResolver, annotationManager, getDefaultFunctionResolver());
  }

  public Context(Object contextNode, XPathVariableResolver variableResolver, IAnnotationManager annotationManager, XPathFunctionResolver functionResolver)
  {
    this(
      contextNode,
      1,
      1,
      variableResolver,
      functionResolver == null ? getDefaultFunctionResolver() : functionResolver,
      annotationManager);
  }

  /**
   * Return the context's annotation manager
   * @return the annotation manager
   * @throws IllegalStateException if the context has not annotation manager
   * @see #hasAnnotationManager()
   */
  public IAnnotationManager getAnnotationManager()
  {
    if (annotationManager == null)
    {
      throw new IllegalStateException();
    }
    return annotationManager;
  }

  public Object getContextNode()
  {
    return contextNode;
  }

  public INodeInspector getContextNodeInspector()
  {
    return inspectorManager.getInspector(contextNode);
  }

  public int getContextPosition()
  {
    return contextPosition;
  }

  public int getContextSize()
  {
    return contextSize;
  }

  public XPathFunctionResolver getFunctionResolver()
  {
    return functionResolver;
  }

  public XPathVariableResolver getVariableResolver()
  {
    return variableResolver;
  }

  /**
   * Test whether the context has an annotation manager
   * @return <code>true</code> if there is an annotation manager.
   */
  public boolean hasAnnotationManager()
  {
    return annotationManager != null;
  }

  public Context newSubContext(Object contextNode2, int contextPosition2, int contextSize2)
  {
    return new Context(contextNode2, contextPosition2, contextSize2, variableResolver, functionResolver, annotationManager);
  }

  public String toString()
  {
    return "Context(" + contextNode.toString() + "," + contextPosition + "," + contextSize + ")"; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
  }
}
