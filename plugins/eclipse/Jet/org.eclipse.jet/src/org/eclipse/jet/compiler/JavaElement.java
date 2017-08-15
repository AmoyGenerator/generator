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

/**
 * Abstract representation of JET2 AST elements that contain Java code
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.JavaElement}
 */
public abstract class JavaElement extends JET2ASTElement
{

  private final org.eclipse.jet.core.parser.ast.JavaElement delegate;


  public JavaElement(JET2AST jet2ast, org.eclipse.jet.core.parser.ast.JavaElement delegate)
  {
    super(jet2ast, delegate);
    this.delegate = delegate;
  }

  /**
   * The document relative offset of the Java code within the element.
   * @return the start offset
   */
  public final int getJavaStart()
  {
    return delegate.getJavaStart();
  }

  /**
   * The document relative offset of the first character after the Java code. 
   * @return the end offset
   */
  public final int getJavaEnd()
  {
    return delegate.getJavaEnd();
  }

  /**
   * Return the Java content of the element
   * @return the Java content
   */
  public String getJavaContent()
  {
    return delegate.getJavaContent();
  }
}
