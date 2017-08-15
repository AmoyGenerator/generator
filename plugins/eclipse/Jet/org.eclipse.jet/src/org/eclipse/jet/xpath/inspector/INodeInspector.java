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
 * Interface providing the XPath parser with information on a particular object. An inspector is specific
 * to classes of a particular Java type. The inspector will only receive objects of the types for which it is registered.
 * <p>
 * Inspectors may be manually registered via {@link InspectorManager#registerInspector(Class[], Class)}, or via the
 * <code>org.eclipse.jet.modelInsecptors</code> extension point.
 * </p>
 * <p>
 * An inspector's responsibilies are
 * <bl>
 * <li>Map each received object to one of the XPath node kinds, as defined in {@link NodeKind}. 
 * For any given object, {@link #getNodeKind(Object)} should always return the same value.</li>
 * <li>Implement the inspector functions consistently with the XPath 1.0 data model node to which the object is mapped.</li>
 * </bl>
 * </p>
 * <p>
 * An inspector may choose to implement the following extension interfaces:
 * <bl>
 * <li>{@link INodeInspectorExtension1} - to provide name-based searching of children. This optional extension may provide 
 * efficient resolution of name-based XPath navigations such as <code>$x/foo</code>.</li>
 * <li>{@link IElementInspector} - provide extra information for objects that are mapped to XPath Elements. This interface must
 * be implemented if the node inspector's implementation of {@link #getNodeKind(Object)} returns {@link NodeKind#ELEMENT}.
 * </bl>
 * </p>
 */
public interface INodeInspector
{

  /**
   * A type safe enumeration of node kinds recognized by the JET XPath engine.
   */
  public static final class NodeKind
  {
    private final String displayString;

    /**
     * A node that is the root of a document. 
     * See the XPath 1.0 Data Model <a href="http://www.w3.org/TR/xpath#root-node">Root Node</a>.
     */
    public static final NodeKind ROOT = new NodeKind("ROOT"); //$NON-NLS-1$

    /**
     * A node that is the element of a document. 
     * See the XPath 1.0 Data Model <a href="http://www.w3.org/TR/xpath#element-nodes">Element Nodes</a>.
     */
    public static final NodeKind ELEMENT = new NodeKind("ELEMENT"); //$NON-NLS-1$

    /**
     * A node that is an attribute of an element node.
     * See the XPath 1.0 Data Model <a href="http://www.w3.org/TR/xpath#attribute-nodes">Attribute Nodes</a>.
     */
    public static final NodeKind ATTRIBUTE = new NodeKind("ATTRIBUTE"); //$NON-NLS-1$

    /**
     * A node that is identifies a namespace for an element node.
     * See the XPath 1.0 Data Model <a href="http://www.w3.org/TR/xpath#namespace-nodes">Namespace Nodes</a>.
     */
    public static final NodeKind NAMESPACE = new NodeKind("NAMESPACE"); //$NON-NLS-1$

    /**
     * A node that contains textual information, which in turn is contained by an element node.
     * See XPath 1.0 Data Model <a href="http://www.w3.org/TR/xpath#section-Text-Nodes">Text Nodes</a>
     */
    public static final NodeKind TEXT = new NodeKind("TEXT"); //$NON-NLS-1$

    /**
     * A node that contains processing instructions, and is contained by a root node or and element node.
     * See XPath 1.0 Data model <a href="http://www.w3.org/TR/xpath#section-Processing-Instruction-Nodes">Processing Instruction Nodes</a>
     */
    public static final NodeKind PROCESSING_INSTRUCTION = new NodeKind("PROCESSING_INSTRUCTION"); //$NON-NLS-1$

    /**
     * A node that contains a document comment, and is contained by either a root node or an element node.
     * See XPath 1.0 Data model <a href="http://www.w3.org/TR/xpath#section-Comment-Nodes">Comment Nodes</a>.
     */
    public static final NodeKind COMMENT = new NodeKind("COMMENT"); //$NON-NLS-1$

    private NodeKind(String displayString)
    {
      this.displayString = displayString;
    }

    public String toString()
    {
      return displayString;
    }
  }

  /**
   * Return the XPath node kind of the passed object.
   * @param obj an object to inspect. Will not be <code>null</code>.
   * @return the node kind. May be <code>null</code> if not recognized.
   */
  NodeKind getNodeKind(Object obj);;

  /**
   * Return the parent of the passed object
   * @param obj an object, will not be <code>null</code>.
   * @return the parent, which may be <code>null</code>.
   */
  Object getParent(Object obj);

  /**
   * Return the string value of the passed node.
   * @param node the node. Will not be <code>null</code>.
   * @return the string value. Will not be <code>null</code>, but may be the empty string.
   */
  String stringValueOf(Object node);

  /**
   * Return the expanded name of the node.
   * @param node a node from which an expanded name is sought
   * @return an ExpandedName instance, or <code>null</code> if the node has not expanded name.
   */
  ExpandedName expandedNameOf(Object node);

  /**
   * Determine whether the name of the node matches the passed expanded name.
   * @param testName an expanded name
   * @return <code>true</code> if there is a match, <code>false</code> otherwise.
   */
  boolean testExpandedName(Object node, ExpandedName testName);
  /**
   * Return the document root for the contenxt node.
   * @param node the node. Will not be <code>null</code>.
   * @return the document root, or <code>null</code> if a root cannot be determined.
   */
  Object getDocumentRoot(Object node);

  /**
   * Return the child nodes of the context node.
   * @param node the node. Will not be <code>null</code>.
   * @return a possibly empty array of objects. Will not be <code>null</code>.
   */
  Object[] getChildren(Object node);

  /**
   * Return the 'name' of the node.
   * @param node
   * @return the 'name' of the node, of <code>null</code> if the node has no name.
   */
  String nameOf(Object node);

}
