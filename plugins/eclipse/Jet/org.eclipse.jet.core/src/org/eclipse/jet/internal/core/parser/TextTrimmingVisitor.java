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
 * $Id: TextTrimmingVisitor.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.core.parser;


import org.eclipse.jet.core.parser.ast.Comment;
import org.eclipse.jet.core.parser.ast.JETASTElement;
import org.eclipse.jet.core.parser.ast.JETASTVisitor;
import org.eclipse.jet.core.parser.ast.JETDirective;
import org.eclipse.jet.core.parser.ast.JavaDeclaration;
import org.eclipse.jet.core.parser.ast.JavaExpression;
import org.eclipse.jet.core.parser.ast.JavaScriptlet;
import org.eclipse.jet.core.parser.ast.TextElement;
import org.eclipse.jet.core.parser.ast.XMLBodyElement;
import org.eclipse.jet.core.parser.ast.XMLBodyElementEnd;
import org.eclipse.jet.core.parser.ast.XMLEmptyElement;


/**
 * A JET AST Visitor that strips whitespace and new-lines
 * 
 */
public class TextTrimmingVisitor extends JETASTVisitor 
{
  /**
   * 
   */
  public TextTrimmingVisitor()
  {
    super();
  }

  private void checkAndStrip(JETASTElement element)
  {
    if(element.removeLineWhenOtherwiseEmpty())
    {
      JETASTElement prev = element.getPrevElement();
      JETASTElement next = element.getNextElement();
      if(endsWithEmtpyLine(prev) && startsWithEmptyLine(next))
      {
        trimLastLine(prev);
        trimFirstLine(next);
      }
    }
  }

  private void trimLastLine(JETASTElement element)
  {
    if(element instanceof TextElement)
    {
      TextElement text = (TextElement)element;
      text.setTrimLastLine(true);
    }
  }

  private void trimFirstLine(JETASTElement element)
  {
    if(element instanceof TextElement)
    {
      TextElement text = (TextElement)element;
      text.setTrimFirstLine(true);
    }
  }

  private boolean startsWithEmptyLine(JETASTElement element)
  {
    if(element instanceof TextElement)
    {
      TextElement text = (TextElement)element;
      final LineInfo[] lines = text.getLines();
      if(lines.length > 0 && lines[0].hasDelimiter())
      {
        LineInfo line = lines[0];
        if(new String(text.getRawText(), line.getStart(), line.getEnd() - line.getStart()).trim().length() == 0)
        {
          // first line is all whitespace
          return true;
        }
      }
    }
    else if(element == null)
    {
      return true;
    }
    return false;
  }

  private boolean endsWithEmtpyLine(JETASTElement element)
  {
    if(element instanceof TextElement)
    {
      TextElement text = (TextElement)element;
      final LineInfo[] lines = text.getLines();
      if(lines.length > 0)
      {
        LineInfo line = lines[lines.length - 1];
        if(line.hasDelimiter()) {
          // last line has a delimiter => this element start on col # 1
          return true;
        }
        else if( lines.length > 1 && new String(text.getRawText(), line.getStart(), line.getEnd() - line.getStart()).trim().length() == 0)
        {
          // last line is all whitespace
          return true;
        }
      }
      else
      {
        return false;
      }
    }
    else if(element == null)
    {
      return true;
    }
    return false;
  }

  public boolean visit(JavaDeclaration declaration)
  {
    checkAndStrip(declaration);
    return true;
  }

  public boolean visit(JETDirective directive)
  {
    checkAndStrip(directive);
    return true;
  }

  public boolean visit(JavaExpression expression)
  {
    checkAndStrip(expression);
    return true;
  }

  public boolean visit(JavaScriptlet scriptlet)
  {
    checkAndStrip(scriptlet);
    return true;
  }

  public boolean visit(XMLEmptyElement xmlEmptyElement)
  {
    checkAndStrip(xmlEmptyElement);
    return true;
  }

  public boolean visit(XMLBodyElement xmlBodyElement)
  {
    checkAndStrip(xmlBodyElement);
    return true;
  }

  public boolean visit(XMLBodyElementEnd xmlBodyElementEnd)
  {
    checkAndStrip(xmlBodyElementEnd);
    return true;
  }

  public boolean visit(Comment comment)
  {
    checkAndStrip(comment);
    return true;
  }
}
