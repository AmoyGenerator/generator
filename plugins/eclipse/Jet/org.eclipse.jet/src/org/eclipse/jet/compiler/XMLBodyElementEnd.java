/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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


/**
 * Represent the closing tag of an XML Tag with a body.
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.XMLBodyElementEnd}
 */
public class XMLBodyElementEnd extends JET2ASTElement
{

  private XMLBodyElement startTag;
  private final org.eclipse.jet.core.parser.ast.XMLBodyElementEnd delegate;


  public XMLBodyElementEnd(JET2AST jet2ast, org.eclipse.jet.core.parser.ast.XMLBodyElementEnd end)
  {
    super(jet2ast, end);
    this.delegate = end;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
  }

  /**
   * @return Returns the startTag.
   */
  public final XMLBodyElement getStartTag()
  {
    return startTag;
  }

  /**
   * @param startTag The startTag to set.
   */
  public final void setStartTag(XMLBodyElement startTag)
  {
    throw new UnsupportedOperationException();
  }

  public JET2ASTElement getPrevElement()
  {
    return getAst().wrap(delegate.getPrevElement());
  }
  
  public String toString()
  {
    return delegate.toString();
  }
}
