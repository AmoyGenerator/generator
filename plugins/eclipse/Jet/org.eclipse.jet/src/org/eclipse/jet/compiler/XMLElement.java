/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
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
 * /
 *******************************************************************************/

package org.eclipse.jet.compiler;


import java.util.Map;

import org.eclipse.jet.taglib.TagDefinition;

/**
 * An abstract implementation representing all XML-based elements in the JET2 AST
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.XMLElement}
 *
 */
public abstract class XMLElement extends JET2ASTElement
{

  private final org.eclipse.jet.core.parser.ast.XMLElement delegate;

  public XMLElement(JET2AST jet2ast, org.eclipse.jet.core.parser.ast.XMLElement element)
  {
    super(jet2ast, element);
    this.delegate = element;
  }

  /**
   * Return the QName of the delegate
   * @return a string
   */
  public final String getName()
  {
    return delegate.getName();
  }

  /**
   * Return a read-only map of the attributes (name to value map)
   * @return a Map with String keys (attribute name) and String values
   */
  public final Map getAttributes()
  {
    return delegate.getAttributes();
  }

  /**
   * Return the NCName (unqualified name) of the delegate.
   * @return the name with any XML namespace prefix (<i>prefix</i>:) removed
   */
  public String getTagNCName()
  {
    return delegate.getTagNCName();
  }

  /**
   * Return the XML Namespace prefixe of the delegate
   * @return the namespace prefix or the empty string if there is no namespace prefix.
   */
  public String getNSPrefix()
  {
    return delegate.getNSPrefix();
  }

  /**
   * @return Returns the td.
   */
  public final TagDefinition getTagDefinition()
  {
    return delegate.getTagDefinition();
  }

  /**
   * Return the underlying new AST object to which this one delegates.
   * Useful in mashing old AST code into new.
   * <p>
   * Intended for internal use only.
   * </p>
   * @return the underlying {@link org.eclipse.jet.core.parser.ast.XMLElement}
   */
  public org.eclipse.jet.core.parser.ast.XMLElement getDelegate()
  {
    return delegate;
  }

}
