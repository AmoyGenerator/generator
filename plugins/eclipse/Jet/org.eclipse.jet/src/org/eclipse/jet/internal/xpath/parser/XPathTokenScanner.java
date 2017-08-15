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


import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.xpath.XPathLexicalException;


/**
 * Lexical scanner for XPath expressions.
 *
 */
public class XPathTokenScanner
{

  public static final int EOF = -1;

  private final String input;

  private int nextOffset;

  private TokenRule[] tokenRules = new TokenRule []{
    new WhitespaceRule(),
    new StringLiteralRule(),
    new QNameRule(),
    new NCNameRule(),
    new NumberRule(),
    new SymbolicTokenRule("(", XPathTokens.LPAREN), //$NON-NLS-1$
    new SymbolicTokenRule(")", XPathTokens.RPAREN), //$NON-NLS-1$
    new SymbolicTokenRule("[", XPathTokens.LBRACKET), //$NON-NLS-1$
    new SymbolicTokenRule("]", XPathTokens.RBRACKET), //$NON-NLS-1$
    new SymbolicTokenRule("..", XPathTokens.DOT_DOT), //$NON-NLS-1$
    new SymbolicTokenRule(".", XPathTokens.DOT), //$NON-NLS-1$
    new SymbolicTokenRule("@", XPathTokens.AT_SIGN), //$NON-NLS-1$
    new SymbolicTokenRule("::", XPathTokens.COLON_COLON), //$NON-NLS-1$
    new SymbolicTokenRule(":", XPathTokens.COLON), //$NON-NLS-1$
    new SymbolicTokenRule(",", XPathTokens.COMMA), //$NON-NLS-1$
    new SymbolicTokenRule("$", XPathTokens.DOLLAR_SIGN), //$NON-NLS-1$
    new SymbolicTokenRule("*", XPathTokens.ASTERISK), //$NON-NLS-1$
    new SymbolicTokenRule("+", XPathTokens.PLUS_SIGN), //$NON-NLS-1$
    new SymbolicTokenRule("-", XPathTokens.HYPHEN), //$NON-NLS-1$
    new SymbolicTokenRule("//", XPathTokens.SLASH_SLASH), //$NON-NLS-1$
    new SymbolicTokenRule("/", XPathTokens.SLASH), //$NON-NLS-1$
    new SymbolicTokenRule("=", XPathTokens.EQUALS), //$NON-NLS-1$
    new SymbolicTokenRule("!=", XPathTokens.NOT_EQUALS), //$NON-NLS-1$
    new SymbolicTokenRule("<=", XPathTokens.LT_EQUALS), //$NON-NLS-1$
    new SymbolicTokenRule("<", XPathTokens.LT), //$NON-NLS-1$
    new SymbolicTokenRule(">=", XPathTokens.GT_EQUALS), //$NON-NLS-1$
    new SymbolicTokenRule(">", XPathTokens.GT), //$NON-NLS-1$
    new SymbolicTokenRule("|", XPathTokens.OR_BAR), //$NON-NLS-1$
  };

  private int tokenStart = -1;

  private int tokenEnd = -1;

  private interface TokenRule
  {

    Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException;

  }

  /**
   * Recognizes a quoted string: "[^"]*" or '[^']*'.
   * 
   */
  private static class StringLiteralRule implements TokenRule
  {

    public Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException
    {

      int first = scanner.next();
      if (first != '\'' && first != '"')
      {
        return Token.UNDEFINED_TOKEN;
      }
      int ch = scanner.next();
      for (; ch != EOF; ch = scanner.next())
      {
        if(ch == first) {
          if(scanner.peek() == first) {
            scanner.next(); // consume quote char and continue...
            // escaped quote, continue...
            continue;
          }
          break;
        }
        // next
      }
      if (ch != first)
      {
        throw new XPathLexicalException(JET2Messages.XPathTokenScanner_UnterminatedStringLiteral);
      }
      return XPathTokens.LITERAL;
    }

  }

  /**
   * Recognizes a QName: NCName ':' NCName.
   *
   */
  private static class QNameRule implements TokenRule
  {

    public Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException
    {
      int ch = scanner.next();
      if (!isNCNameStartChar(ch))
      {
        return Token.UNDEFINED_TOKEN;
      }
      ch = scanner.next();
      for (; isNCNameChar(ch); ch = scanner.next())
      {
        // next...
      }
      if (ch != ':')
      {
        return Token.UNDEFINED_TOKEN;
      }
      ch = scanner.next();
      if (!isNCNameStartChar(ch))
      {
        return Token.UNDEFINED_TOKEN;
      }
      for (ch = scanner.next(); isNCNameChar(ch); ch = scanner.next())
      {
        // next...
      }
      scanner.prev();
      return XPathTokens.QNAME;
    }

    private boolean isNCNameChar(int ch)
    {
      return Character.isLetterOrDigit((char)ch) || ch == '.' || ch == '-' || ch == '_';
    }

    private boolean isNCNameStartChar(int c)
    {
      return Character.isLetter((char)c) || c == '_';
    }

  }

  /**
   * Recognizes a QName: NCName
   *
   */
  private static class NCNameRule implements TokenRule
  {

    public Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException
    {
      int ch = scanner.next();
      if (!isNCNameStartChar(ch))
      {
        return Token.UNDEFINED_TOKEN;
      }
      ch = scanner.next();
      for (; isNCNameChar(ch); ch = scanner.next())
      {
        // next...
      }
      scanner.prev();
      return XPathTokens.NCNAME;
    }

    private boolean isNCNameChar(int ch)
    {
      return Character.isLetterOrDigit((char)ch) || ch == '.' || ch == '-' || ch == '_';
    }

    private boolean isNCNameStartChar(int c)
    {
      return Character.isLetter((char)c) || c == '_';
    }
  }

  /**
   * Recognize whitespace.
   */
  private static class WhitespaceRule implements TokenRule
  {

    public Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException
    {
      int ch = scanner.next();
      if (!Character.isWhitespace((char)ch))
      {
        return Token.UNDEFINED_TOKEN;
      }
      for (ch = scanner.next(); Character.isWhitespace((char)ch); ch = scanner.next())
      {
        // next...
      }
      scanner.prev();

      return Token.WHITESPACE_TOKEN;
    }
  }

  private static class NumberRule implements TokenRule
  {

    public Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException
    {
      boolean decimalPointFound = false;
      int ch = scanner.next();
      if (ch == '.')
      {
        decimalPointFound = true;
        ch = scanner.next();
      }
      if (!Character.isDigit((char)ch))
      {
        return Token.UNDEFINED_TOKEN;
      }

      for (ch = scanner.next(); Character.isDigit((char)ch) || (ch == '.' && !decimalPointFound); ch = scanner.next())
      {
        if (ch == '.')
        {
          decimalPointFound = true;
        }
      }
      scanner.prev();
      return XPathTokens.NUMBER;
    }

  }

  /**
   * Recognize a token that is a symbolic character.
   *
   */
  private static class SymbolicTokenRule implements TokenRule
  {

    private final String symbol;

    private final Token token;

    public SymbolicTokenRule(String symbol, Token token)
    {
      this.symbol = symbol;
      this.token = token;

    }

    public Token evaluate(XPathTokenScanner scanner) throws XPathLexicalException
    {
      for (int i = 0; i < symbol.length(); i++)
      {
        int ch = scanner.next();
        if (ch != symbol.charAt(i))
        {
          return Token.UNDEFINED_TOKEN;
        }
      }
      return token;
    }

  }

  /**
   * 
   */
  public XPathTokenScanner(String input)
  {
    super();
    this.input = input;
    nextOffset = 0;

  }

  public int peek()
  {
    int next = nextOffset < input.length() ? input.charAt(nextOffset) : EOF;
    return next;
  }

  public void prev()
  {
    --nextOffset;

  }

  public void addTokenError(String string)
  {

  }

  public int next()
  {
    int next = nextOffset < input.length() ? input.charAt(nextOffset) : EOF;
    ++nextOffset;
    return next;
  }

  public Token nextToken() throws XPathLexicalException
  {
    int savedNextOffset = nextOffset;
    for (int i = 0; i < tokenRules.length; i++)
    {
      Token token = tokenRules[i].evaluate(this);
      if (token != null && token.isWhitespace())
      {
        // ignore the whitespace and continue
        savedNextOffset = nextOffset;
      }
      else if (token != null && !token.isUndefined())
      {
        tokenStart = savedNextOffset;
        tokenEnd = nextOffset;
        return token;
      }
      nextOffset = savedNextOffset;
    }
    if (nextOffset >= input.length())
    {
      return Token.EOF_TOKEN;
    }
    else
    {
      return Token.UNDEFINED_TOKEN;
    }
  }

  public String getTokenText()
  {
    if (tokenStart == -1 || tokenEnd == -1)
    {
      throw new IllegalStateException();
    }
    return input.substring(tokenStart, tokenEnd);
  }

  public void putBackToken()
  {
    if (tokenStart == -1 || tokenEnd == -1)
    {
      throw new IllegalStateException();
    }

    nextOffset = tokenStart;
    tokenStart = tokenEnd = -1;
  }

  public int getTokenStart()
  {
    return tokenStart;
  }

  public int getTokenEnd()
  {
    return tokenEnd;
  }
}
