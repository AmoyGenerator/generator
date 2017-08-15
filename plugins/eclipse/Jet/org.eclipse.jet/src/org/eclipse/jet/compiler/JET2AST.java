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


import java.util.HashMap;
import java.util.Map;

import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.core.parser.ast.JETDirective;
import org.eclipse.jet.core.parser.ast.JavaDeclaration;
import org.eclipse.jet.core.parser.ast.JavaExpression;
import org.eclipse.jet.core.parser.ast.JavaScriptlet;


/**
 * The root object of JET AST trees and a factory for JET nodes in that tree.
 * @deprecated Since 0.8.0, use {@link JETAST}
 *
 */
final class JET2AST
{

  private final JETAST delegateAST;
  private final Map newToWrapped = new HashMap(); 
  /**
   * Create an instance
   */
  public JET2AST()
  {
    delegateAST = new JETAST();
  }
  
  public JET2AST(JETAST delegateAST)
  {
    this.delegateAST = delegateAST;
  }


  JETAST getDelegateAST()
  {
    return delegateAST;
  }


  public JET2ASTElement wrap(org.eclipse.jet.core.parser.ast.JETASTElement bodyElement)
  {
    JET2ASTElement wrapper = (JET2ASTElement)newToWrapped.get(bodyElement);
    if(wrapper != null) {
      return wrapper;
    }
    if (bodyElement instanceof org.eclipse.jet.core.parser.ast.Comment)
    {
      wrapper = new Comment(this, (org.eclipse.jet.core.parser.ast.Comment)bodyElement);
    }
    else if (bodyElement instanceof JavaDeclaration)
    {
      wrapper = new JET2Declaration(this, (JavaDeclaration)bodyElement);
    }
    else if (bodyElement instanceof JavaExpression)
    {
      wrapper = new JET2Expression(this, (JavaExpression)bodyElement);
    }
    else if (bodyElement instanceof JavaScriptlet)
    {
      wrapper = new JET2Scriptlet(this, (JavaScriptlet)bodyElement);
    }
    else if (bodyElement instanceof JETDirective)
    {
      wrapper = new JET2Directive(this, (JETDirective)bodyElement);
    }
    else if (bodyElement instanceof org.eclipse.jet.core.parser.ast.TextElement)
    {
      wrapper = new TextElement(this, (org.eclipse.jet.core.parser.ast.TextElement)bodyElement);
    }
    else if (bodyElement instanceof org.eclipse.jet.core.parser.ast.XMLBodyElementEnd)
    {
      wrapper = new XMLBodyElementEnd(this, (org.eclipse.jet.core.parser.ast.XMLBodyElementEnd)bodyElement);
    }
    else if (bodyElement instanceof org.eclipse.jet.core.parser.ast.XMLBodyElement)
    {
      wrapper = new XMLBodyElement(this, (org.eclipse.jet.core.parser.ast.XMLBodyElement)bodyElement);
    }
    else if (bodyElement instanceof org.eclipse.jet.core.parser.ast.XMLEmptyElement)
    {
      wrapper = new XMLEmptyElement(this, (org.eclipse.jet.core.parser.ast.XMLEmptyElement)bodyElement);
    } else if(bodyElement instanceof org.eclipse.jet.core.parser.ast.JETCompilationUnit) {
      wrapper = new JET2CompilationUnit(this, (JETCompilationUnit)bodyElement);
    }
    newToWrapped.put(bodyElement, wrapper);
    return wrapper;
  }

  public Problem wrap(org.eclipse.jet.core.parser.ast.Problem problem)
  {
    Problem wrapped = (Problem)newToWrapped.get(problem);
    if(wrapped == null) {
      wrapped = new Problem(problem);
      newToWrapped.put(problem, wrapped);
    }
    return  wrapped;
  }

}
