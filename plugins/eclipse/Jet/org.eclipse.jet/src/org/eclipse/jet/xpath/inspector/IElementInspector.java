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
 * Specialization of {@link org.eclipse.jet.xpath.inspector.INodeInspector INodeInspector} for nodes where
 * {@link org.eclipse.jet.xpath.inspector.INodeInspector#getNodeKind(Object) getNodeKind()} returns
 * {@link org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind#ELEMENT ELEMENT}.
 *
 */
public interface IElementInspector extends INodeInspector
{

  /**
   * Return all the attributes for the given contextNode. The implementer
   * may assume the getNodeKind(contextNode) is NodeKind.ELEMENT.
   * @param node an ELEMENT node
   * @return an array of attributes. Return an empty array if there are not matches.
   */
  Object[] getAttributes(Object node);

  /**
   * Return the named attribute for the given contextNode. The implementer
   * may assume the getNodeKind(contextNode) is NodeKind.ELEMENT. 
   * The nameTestExpandedName will be in one of two forms:
   * <bl>
   * <li>{@link ExpandedName#getNamespaceURI()} returns null and {@link ExpandedName#getLocalPart()}
   * is a name.</li>
   * <li>{@link ExpandedName#getNamespaceURI()} returns non-null and {@link ExpandedName#getLocalPart()}
   * is a name.</li>
   * </bl>
   * This method will not be called if the XPath nameTest includes a wild-card character (*).
   * @param node the ELEMENT to search
   * @param nameTestExpandedName the expanded name of the attribute to find
   * @return the attribute, or <code>null</code> if not found.
   */
  Object getNamedAttribute(Object node, ExpandedName nameTestExpandedName);

  /**
   * Create the named attribute on the context node (which is an ELEMENT) and set its
   * value to the passed value.
   * <p>
   * This method is optional. If not implemented, inspectors should throw {@link UnsupportedOperationException}.
   * <p>
   * @param node
   * @param attributeName
   * @param value
   * @return <code>true</code> if the attribute could create the attribute, <code>false</code> otherwise.
   */
  boolean createAttribute(Object node, String attributeName, String value);

  /**
   * Create a new Element under context Node
   * @param node a the ELEMENT under which the new element will be created
   * @param elementName the expanded name for the new element.
   * @param addBeforeThisSibling a child of <code>contextNode</code> or <code>null</code>.
   * If non-null, then the new element is inserted immediately before this element. 
   * If <code>null</code>, the new element will be added at the end of the appropriate collection
   * of element children.
   * <p>
   * This method is optional. If not implemented, inspectors should throw {@link UnsupportedOperationException}.
   * <p>
   * @return the new element, or <code>null</code> if the element could not be created.
   * @throws SimpleElementRequiresValueException 
   * @throws InvalidChildException 
   */
  Object addElement(Object node, ExpandedName elementName, Object addBeforeThisSibling) throws SimpleElementRequiresValueException,
    InvalidChildException;

  /**
   * Remove the specified element from its containing model.
   * <p>
   * This method is optional. If not implemented, inspectors should throw {@link UnsupportedOperationException}.
   * <p>
   * @param node the ELEMENT which is to be removed.
   */
  void removeElement(Object node);

  /**
   * Make a copy of <code>srcElement</code> under <code>tgtParent</code> with the specified name.
   * <p>
   * The implementation should check that <code>tgtParent</code> and <code>srcElement</code>
   * are in the compatible meta-models.  
   * <p>
   * This method is optional. If not implemented, inspectors should throw {@link UnsupportedOperationException}.
   * <p>
   * @param tgtParent 
   * @param srcElement
   * @param name
   * @param recursive
   * @return the newly copied element
   * @throws CopyElementException if the copy cannot be successfully performed.
   */
  Object copyElement(Object tgtParent, Object srcElement, String name, boolean recursive) throws CopyElementException;

  /**
   * Create a new text (simple) element under <code>parentElement</code> with the specified
   * name and content.
   * <p>
   * This method is optional. If not implemented, inspectors should throw {@link UnsupportedOperationException}.
   * <p>
   * @param parentElement
   * @param name
   * @param bodyContent
   * @param asCData if <code>true</code>, add as a CDATA section
   * @return the newly created text element.
   * @throws AddElementException 
   */
  Object addTextElement(Object parentElement, String name, String bodyContent, boolean asCData) throws AddElementException;

  /**
   * Remove the named attribute from the node that represents an element. 
   * <p>
   * This method is optional. If not implemented, inspectors should throw {@link UnsupportedOperationException}.
   * <p>
   * @param node
   * @param name
   */
  void removeAttribute(Object node, String name);
}
