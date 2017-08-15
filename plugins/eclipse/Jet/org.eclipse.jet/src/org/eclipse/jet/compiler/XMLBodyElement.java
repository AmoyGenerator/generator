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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.core.parser.ast.BodyElement;

/**
 * Define a JET2 XML Element that has a begin-tag and end-tag, and zero or more body elements.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.XMLBodyElement}
 */
public final class XMLBodyElement extends XMLElement
{

  private List wrappedBodyElements = null;

  private final org.eclipse.jet.core.parser.ast.XMLBodyElement delegate;

  public XMLBodyElement(JET2AST jet2ast, org.eclipse.jet.core.parser.ast.XMLBodyElement element)
  {
    super(jet2ast, (org.eclipse.jet.core.parser.ast.XMLElement)element);
    this.delegate = element;
  }

  /**
   * Return a read-only list of JET2 elements contained by this element.
   * @return a List of {@link JET2ASTElement} instances. The empty list is returned if there are no elements.
   */
  public final List getBodyElements()
  {
    if (wrappedBodyElements == null)
    {
      List delegateBodyElements = delegate.getBodyElements();
      if(delegateBodyElements.size() == 0) {
        wrappedBodyElements = Collections.EMPTY_LIST;
      } else {
        wrappedBodyElements = new ArrayList(delegateBodyElements.size());
        for (Iterator i = delegateBodyElements.iterator(); i.hasNext();)
        {
          BodyElement bodyElement = (BodyElement)i.next();
          wrappedBodyElements.add(getAst().wrap(bodyElement));
        }
      }
    }
    return wrappedBodyElements;
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
    for (Iterator i = getBodyElements().iterator(); i.hasNext();)
    {
      JET2ASTElement element = (JET2ASTElement)i.next();
      element.accept(visitor);
    }
    visitor.endVisit(this);

  }

  void setEndTag(XMLBodyElementEnd endTag)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @return Returns the endTag.
   */
  public final XMLBodyElementEnd getEndTag()
  {
    return (XMLBodyElementEnd)getAst().wrap(delegate.getEndTag());
  }
  
  public String toString()
  {
    return delegate.toString();
  }

  public JET2ASTElement getNextElement()
  {
    
    return getAst().wrap(delegate.getNextElement());
  }
}
