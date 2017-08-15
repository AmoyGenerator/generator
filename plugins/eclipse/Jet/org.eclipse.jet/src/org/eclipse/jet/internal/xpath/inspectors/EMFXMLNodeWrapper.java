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
package org.eclipse.jet.internal.xpath.inspectors;


import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;


/**
 * Wrapper for EMF XMLType Comment objects found in FeatureMap's.
 *
 */
public class EMFXMLNodeWrapper
{

  private final Object parent;

  private final String text;

  private final NodeKind nodeKind;

  /**
   * @param parent the parent of the comment
   * @param text the comment text
   * @param nodeKind TODO
   * 
   */
  public EMFXMLNodeWrapper(Object parent, String text, INodeInspector.NodeKind nodeKind)
  {
    super();
    this.parent = parent;
    this.text = text;
    this.nodeKind = nodeKind;
  }

  /**
   * @return Returns the parent.
   */
  public final Object getParent()
  {
    return parent;
  }

  /**
   * @return Returns the text.
   */
  public final String getText()
  {
    return text;
  }

  /**
   * @return Returns the nodeKind.
   */
  public final NodeKind getNodeKind()
  {
    return nodeKind;
  }

}
