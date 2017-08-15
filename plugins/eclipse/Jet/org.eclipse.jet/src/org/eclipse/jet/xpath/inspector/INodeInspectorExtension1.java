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
package org.eclipse.jet.xpath.inspector;


/**
 * An optional extension interface of {@link org.eclipse.jet.xpath.inspector.INodeInspector} allowing
 * faster access to named child nodes.
 * 
 *
 */
public interface INodeInspectorExtension1
{
  /**
   * Return the named children element for the given contextNode. The implementer
   * may assume that getNodeKind(contextNode) is either NodeKind.ELEMENT or NodeKind.ROOT.
   * The nameTestExpandedName will be in one of two forms:
   * <bl>
   * <li>{@link ExpandedName#getNamespaceURI()} returns null and {@link ExpandedName#getLocalPart()}
   * is a name.</li>
   * <li>{@link ExpandedName#getNamespaceURI()} returns non-null and {@link ExpandedName#getLocalPart()}
   * is a name.</li>
   * </bl>
   * This method will not be called if the XPath nameTest includes a wild-card character (*).
   * @param node the ELEMENT or ROOT to search
   * @param nameTestExpandedName the expanded name of the element to find.
   * @return the child element or <code>null</code> if not found.
   */
  Object[] getNamedChildren(Object node, ExpandedName nameTestExpandedName);

}
