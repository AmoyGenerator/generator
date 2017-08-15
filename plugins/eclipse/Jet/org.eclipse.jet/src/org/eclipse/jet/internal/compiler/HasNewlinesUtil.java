/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: HasNewlinesUtil.java,v 1.1 2007/05/22 19:45:33 pelder Exp $
 */
package org.eclipse.jet.internal.compiler;

import org.eclipse.jet.core.parser.ast.JETASTElement;
import org.eclipse.jet.core.parser.ast.JETASTVisitor;
import org.eclipse.jet.core.parser.ast.TextElement;
import org.eclipse.jet.internal.core.NewLineUtil;

/**
 * Utility for testing whether a JET AST has new lines in any of its text elements
 */
public class HasNewlinesUtil extends JETASTVisitor
{
  public static boolean test(JETASTElement element)
  {
    HasNewlinesUtil util = new HasNewlinesUtil();
    element.accept(util);
    
    return util.hasNewLines;
  }
  
  private boolean hasNewLines;
  
  private HasNewlinesUtil() {
    hasNewLines = false;
  }
  
  public boolean visit(TextElement text)
  {
    if(!hasNewLines)
    {
      hasNewLines = NewLineUtil.NEW_LINE_PATTERN.matcher(new String(text.getText())).find();
      
    }
    return super.visit(text);
  }
  
  
}
