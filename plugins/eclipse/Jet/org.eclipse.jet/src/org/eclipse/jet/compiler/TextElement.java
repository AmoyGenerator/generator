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

import org.eclipse.jet.internal.parser.LineInfo;


/**
 * Define a Text Element in the JET2 AST
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.TextElement}
 */
public final class TextElement extends JET2ASTElement
{


  private final org.eclipse.jet.core.parser.ast.TextElement delegate;

  public TextElement(JET2AST jet2ast, org.eclipse.jet.core.parser.ast.TextElement element)
  {
    super(jet2ast, element);
    this.delegate = element;
  }


  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
  }

  /**
   * Return the text content
   * @return the text
   */
  public char[] getText()
  {
    return delegate.getText();
  }
  
  public char[] getRawText()
  {
    return delegate.getRawText();
  }


  public void setTrimLastLine(boolean trim)
  {
    delegate.setTrimLastLine(trim);
  }

  /**
   * @return Returns the trimLastLine.
   */
  public final boolean isTrimLastLine()
  {
    return delegate.isTrimLastLine();
  }

  public void setTrimFirstLine(boolean trim)
  {
    delegate.setTrimFirstLine(trim);
  }

  /**
   * @return Returns the trimFirstLine.
   */
  public final boolean isTrimFirstLine()
  {
    return delegate.isTrimFirstLine();
  }

  /**
   * @return Returns the lines.
   */
  public final LineInfo[] getLines()
  {
    return LineInfo.calculateLines(getText());
  }

}
