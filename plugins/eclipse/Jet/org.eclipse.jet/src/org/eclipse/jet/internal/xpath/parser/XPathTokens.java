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
package org.eclipse.jet.internal.xpath.parser;


/**
 * XPath tokens
 *
 */
public class XPathTokens
{

  public static final Token LITERAL = new Token("LITERAL"); //$NON-NLS-1$

  public static final Token DOLLAR_SIGN = new Token("$"); //$NON-NLS-1$

  public static final Token QNAME = new Token("QNANE"); //$NON-NLS-1$

  public static final Token LPAREN = new Token("("); //$NON-NLS-1$

  public static final Token RPAREN = new Token(")"); //$NON-NLS-1$

  public static final Token NCNAME = new Token("NCNAME"); //$NON-NLS-1$

  public static final Token LBRACKET = new Token("["); //$NON-NLS-1$

  public static final Token RBRACKET = new Token("]"); //$NON-NLS-1$

  public static final Token COMMA = new Token(","); //$NON-NLS-1$

  public static final Token DOT_DOT = new Token(".."); //$NON-NLS-1$

  public static final Token DOT = new Token("."); //$NON-NLS-1$

  public static final Token AT_SIGN = new Token("@"); //$NON-NLS-1$

  public static final Token COLON_COLON = new Token("::"); //$NON-NLS-1$

  public static final Token COLON = new Token(":"); //$NON-NLS-1$

  public static final Token ASTERISK = new Token("*"); //$NON-NLS-1$

  public static final Token PLUS_SIGN = new Token("+"); //$NON-NLS-1$

  public static final Token HYPHEN = new Token("-"); //$NON-NLS-1$

  public static final Token SLASH_SLASH = new Token("//"); //$NON-NLS-1$

  public static final Token SLASH = new Token("/"); //$NON-NLS-1$

  public static final Token EQUALS = new Token("="); //$NON-NLS-1$

  public static final Token NOT_EQUALS = new Token("!="); //$NON-NLS-1$

  public static final Token LT_EQUALS = new Token("<="); //$NON-NLS-1$

  public static final Token LT = new Token("<"); //$NON-NLS-1$

  public static final Token GT_EQUALS = new Token(">="); //$NON-NLS-1$

  public static final Token GT = new Token(">"); //$NON-NLS-1$

  public static final Token NUMBER = new Token("NUMBER"); //$NON-NLS-1$

  public static final Token OR_BAR = new Token("|"); //$NON-NLS-1$

  /**
   * 
   */
  private XPathTokens()
  {
    super();
  }

}
