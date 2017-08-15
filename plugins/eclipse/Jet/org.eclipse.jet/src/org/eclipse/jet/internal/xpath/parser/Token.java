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
 * Class representing a recognized token of an {@link IScanner}.
 */
public final class Token
{

  private static final int KIND_UNDEFINED = 1;

  private static final int KIND_WHITESPACE = 2;

  private static final int KIND_EOF = 3;

  private static final int KIND_OTHER = 4;

  public static final Token UNDEFINED_TOKEN = new Token(KIND_UNDEFINED, "UNDEFINED"); //$NON-NLS-1$

  public static final Token WHITESPACE_TOKEN = new Token(KIND_WHITESPACE, "WHITESPACE"); //$NON-NLS-1$

  public static final Token EOF_TOKEN = new Token(KIND_EOF, "EOF"); //$NON-NLS-1$

  private final int tokenKind;

  private final String displayName;

  private Token(int kind, String tokenData)
  {
    tokenKind = kind;
    this.displayName = tokenData;
  }

  public Token(String tokenData)
  {
    this(KIND_OTHER, tokenData);
  }

  public boolean isEOF()
  {
    return tokenKind == KIND_EOF;
  }

  public boolean isUndefined()
  {
    return tokenKind == KIND_UNDEFINED;
  }

  public boolean isWhitespace()
  {
    return tokenKind == KIND_WHITESPACE;
  }

  public boolean isOther()
  {
    return tokenKind == KIND_OTHER;
  }

  public String toString()
  {
    return displayName;
  }

}
