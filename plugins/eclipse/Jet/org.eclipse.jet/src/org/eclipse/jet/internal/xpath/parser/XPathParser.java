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
package org.eclipse.jet.internal.xpath.parser;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.xpath.ast.Axis;
import org.eclipse.jet.internal.xpath.ast.BinaryOp;
import org.eclipse.jet.internal.xpath.ast.ContextNode;
import org.eclipse.jet.internal.xpath.ast.ExprNode;
import org.eclipse.jet.internal.xpath.ast.FilterExpr;
import org.eclipse.jet.internal.xpath.ast.Function;
import org.eclipse.jet.internal.xpath.ast.LogicalOp;
import org.eclipse.jet.internal.xpath.ast.NodeSetCast;
import org.eclipse.jet.internal.xpath.ast.NodeSetExpr;
import org.eclipse.jet.internal.xpath.ast.NodeTest;
import org.eclipse.jet.internal.xpath.ast.NumberLiteral;
import org.eclipse.jet.internal.xpath.ast.Predicate;
import org.eclipse.jet.internal.xpath.ast.RelOp;
import org.eclipse.jet.internal.xpath.ast.Root;
import org.eclipse.jet.internal.xpath.ast.Step;
import org.eclipse.jet.internal.xpath.ast.StringLiteral;
import org.eclipse.jet.internal.xpath.ast.UnaryMinus;
import org.eclipse.jet.internal.xpath.ast.UnionExpr;
import org.eclipse.jet.internal.xpath.ast.Variable;
import org.eclipse.jet.xpath.NamespaceContext;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionResolver;
import org.eclipse.jet.xpath.XPathLexicalException;
import org.eclipse.jet.xpath.XPathSyntaxException;
import org.eclipse.jet.xpath.inspector.ExpandedName;


/**
 * Parser for XPath expressions
 *
 */
public class XPathParser
{

  private static final String STAR = "*"; //$NON-NLS-1$

  private final XPathFunctionResolver functionResolver;

  private final TokenInstance[] tokenInstances;

  private final String input;

  private int nextTokenIndex;

  private final NamespaceContext nsContext;

  /**
   * @throws XPathLexicalException 
   * 
   */
  public XPathParser(String input, XPathFunctionResolver functionResolver, NamespaceContext nsContext) throws XPathLexicalException
  {
    super();
    this.input = input;
    this.functionResolver = functionResolver;
    this.nsContext = nsContext;

    TokenInstance[] result = scanTokens(input);
    this.tokenInstances = result;

    this.nextTokenIndex = 0;
  }

  public XPathParser(String input, XPathFunctionResolver functionResolver) throws XPathLexicalException
  {
    this(input, functionResolver, null);
  }

  /**
   * @param input
   * @return
   * @throws XPathLexicalException
   */
  private TokenInstance[] scanTokens(String input) throws XPathLexicalException
  {
    XPathTokenScanner scan = new XPathTokenScanner(input);

    final List tokens = new ArrayList();
    Token token;
    for (token = scan.nextToken(); token != Token.EOF_TOKEN && token != Token.UNDEFINED_TOKEN; token = scan.nextToken())
    {
      tokens.add(new TokenInstance(token, scan.getTokenStart(), scan.getTokenEnd()));
    }
    if (token != Token.EOF_TOKEN)
    {
      throw new XPathLexicalException(JET2Messages.XPath_UnrecognizedToken + scan.getTokenStart());
    }
    TokenInstance[] result = (TokenInstance[])tokens.toArray(new TokenInstance [tokens.size()]);
    return result;
  }

  public ExprNode primaryExpr() throws XPathSyntaxException
  {
    Token next = peekNext();
    ExprNode expr = null;
    if (next == XPathTokens.DOLLAR_SIGN)
    {
      expr = variableReference();
    }
    else if (next == XPathTokens.LITERAL)
    {
      expr = literal();
    }
    else if (next == XPathTokens.NUMBER)
    {
      expr = number();
    }
    else if (next == XPathTokens.LPAREN)
    {
      consumeToken();
      ExprNode result = expr();
      next = peekNext();
      if (next != XPathTokens.RPAREN)
      {
        throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new String[] {")"})); //$NON-NLS-1$
      }
      consumeToken();
      expr = result;
    }
    else if (next == XPathTokens.NCNAME && peek(2) == XPathTokens.LPAREN)
    {
      expr = functionCall();
    }
    //		if(expr == null) { 
    //			throw new XPathSyntaxException("Expected: $variable, '...', \"...\", number, function()");
    //		}
    return expr;
  }

  public ExprNode functionCall() throws XPathSyntaxException
  {
    Token name = peek(1);
    Token lparen = peek(2);
    if (name != XPathTokens.QNAME && name != XPathTokens.NCNAME && lparen != XPathTokens.LPAREN)
    {
      return null;
    }
    String functionName = consumeToken();
    consumeToken(); // LPAREN

    List args = new ArrayList();
    for (Token t = peekNext(); t != XPathTokens.RPAREN && !t.isEOF() && !t.isUndefined(); t = peekNext())
    {
      args.add(expr());
      if (peekNext() == XPathTokens.COMMA)
      {
        consumeToken();
      }
      else if (peekNext() != XPathTokens.RPAREN)
      {
        throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ ")" })); //$NON-NLS-1$
      }
    };
    if (peekNext() != XPathTokens.RPAREN)
    {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ ", )" })); //$NON-NLS-1$
    }

    consumeToken(); // RPAREN
    XPathFunction function = functionResolver.resolveFunction(functionName, args.size());
    if (function == null)
    {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_UnknownFunction, new Object[]{ functionName}));
    }
    return new Function(functionName, function, args);
  }

  public ExprNode expr() throws XPathSyntaxException
  {
    return orExpr();
  }

  public ExprNode orExpr() throws XPathSyntaxException
  {
    ExprNode result = andExpr();
    while (peekNext() == XPathTokens.NCNAME && "or".equals(peekTokenText())) { //$NON-NLS-1$
      consumeToken();
      ExprNode rightExpr = andExpr();
      result = new LogicalOp.Or(result, rightExpr);
    }
    return result;
  }

  public ExprNode andExpr() throws XPathSyntaxException
  {
    ExprNode result = equalityExpr();
    while (peekNext() == XPathTokens.NCNAME && "and".equals(peekTokenText())) { //$NON-NLS-1$
      consumeToken();
      ExprNode rightExpr = equalityExpr();
      result = new LogicalOp.And(result, rightExpr);
    }
    return result;
  }

  public ExprNode equalityExpr() throws XPathSyntaxException
  {
    ExprNode result = relationalExpr();
    for (Token t = peekNext(); t == XPathTokens.EQUALS || t == XPathTokens.NOT_EQUALS; t = peekNext())
    {
      consumeToken();
      ExprNode rightExpr = relationalExpr();
      if (t == XPathTokens.EQUALS)
      {
        result = new RelOp.Eq(result, rightExpr);
      }
      else
      {
        result = new RelOp.NotEq(result, rightExpr);

      }
    }
    return result;
  }

  public ExprNode relationalExpr() throws XPathSyntaxException
  {
    ExprNode result = additiveExpr();
    for (Token t = peekNext(); t == XPathTokens.LT || t == XPathTokens.LT_EQUALS || t == XPathTokens.GT || t == XPathTokens.GT_EQUALS; t = peekNext())
    {
      consumeToken();
      ExprNode rightExpr = additiveExpr();
      if (t == XPathTokens.LT)
      {
        result = new RelOp.Lt(result, rightExpr);
      }
      else if (t == XPathTokens.LT_EQUALS)
      {
        result = new RelOp.Lte(result, rightExpr);
      }
      else if (t == XPathTokens.GT)
      {
        result = new RelOp.Gt(result, rightExpr);
      }
      else if (t == XPathTokens.GT_EQUALS)
      {
        result = new RelOp.Gte(result, rightExpr);
      }
    }
    return result;
  }

  public ExprNode additiveExpr() throws XPathSyntaxException
  {
    ExprNode result = multiplicativeExpr();
    for (Token t = peekNext(); t == XPathTokens.PLUS_SIGN || t == XPathTokens.HYPHEN; t = peekNext())
    {
      consumeToken();
      ExprNode rightExpr = multiplicativeExpr();
      if (t == XPathTokens.PLUS_SIGN)
      {
        result = new BinaryOp.Add(result, rightExpr);
      }
      else if (t == XPathTokens.HYPHEN)
      {
        result = new BinaryOp.Subtract(result, rightExpr);
      }
    }
    return result;
  }

  public ExprNode multiplicativeExpr() throws XPathSyntaxException
  {
    ExprNode result = unaryExpr();
    for (Token t = peekNext(); t == XPathTokens.ASTERISK || t == XPathTokens.NCNAME && "mod".equals(peekTokenText()) //$NON-NLS-1$
      || t == XPathTokens.NCNAME && "div".equals(peekTokenText()); t = peekNext()) { //$NON-NLS-1$
      String op = consumeToken();
      ExprNode rightExpr = unaryExpr();
      if (t == XPathTokens.ASTERISK)
      {
        result = new BinaryOp.Multiply(result, rightExpr);
      }
      else if ("mod".equals(op)) { //$NON-NLS-1$
        result = new BinaryOp.Mod(result, rightExpr);
      }
      else if ("div".equals(op)) { //$NON-NLS-1$
        result = new BinaryOp.Div(result, rightExpr);
      }
    }
    return result;
  }

  public ExprNode unaryExpr() throws XPathSyntaxException
  {
    int unaryMinusCount = 0;
    for (Token t = peekNext(); t == XPathTokens.HYPHEN; t = peekNext())
    {
      consumeToken();
      unaryMinusCount++;
    }
    ExprNode result = unionExpr();

    for (int i = 0; i < unaryMinusCount; i++)
    {
      result = new UnaryMinus(result);
    }

    return result;
  }

  public ExprNode unionExpr() throws XPathSyntaxException
  {
    ExprNode result = pathExpr();
    for (Token t = peekNext(); t == XPathTokens.OR_BAR; t = peekNext())
    {
      consumeToken();
      NodeSetExpr leftExpr = castToNodeSetExpr(result);
      NodeSetExpr rightExpr = castToNodeSetExpr(pathExpr());
      result = new UnionExpr(leftExpr, rightExpr);
    }
    return result;
  }

  public Predicate predicate() throws XPathSyntaxException
  {
    if (peekNext() != XPathTokens.LBRACKET)
    {
      return null;
    }
    consumeToken(); // [
    ExprNode expr = expr();
    if (peekNext() != XPathTokens.RBRACKET)
    {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ "]" })); //$NON-NLS-1$
    }
    consumeToken(); // ]
    return new Predicate(expr);
  }

  public NodeSetExpr locationPath() throws XPathSyntaxException
  {
    NodeSetExpr anchorNode;
    boolean optionalRelativeLocationPath = false;
    if (peekNext() == XPathTokens.SLASH)
    {
      consumeToken();
      anchorNode = new Root();
      optionalRelativeLocationPath = true;
    }
    else if (peekNext() == XPathTokens.SLASH_SLASH)
    {
      consumeToken();
      anchorNode = new Step(new Root(), Axis.descendantOrSelf(), NodeTest.allNodes());
    }
    else
    {
      anchorNode = new ContextNode();
    }

    NodeSetExpr relativeLocationPath = relativeLocationPath(anchorNode);
    if (optionalRelativeLocationPath && relativeLocationPath == null)
    {
      return anchorNode;
    }
    else if (relativeLocationPath != null)
    {
      return relativeLocationPath;
    }
    else
    {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ JET2Messages.XPathParser_relativeLocation }));
    }
  }

  /**
   * @param left
   * @return
   * @throws XPathSyntaxException
   */
  private NodeSetExpr relativeLocationPath(final NodeSetExpr left) throws XPathSyntaxException
  {
    NodeSetExpr result = step(left);
    if (result != null)
    {
      while (true)
      {
        if (peekNext() == XPathTokens.SLASH)
        {
          consumeToken();
          result = step(result);
        }
        else if (peekNext() == XPathTokens.SLASH_SLASH)
        {
          consumeToken();
          result = step(new Step(result, Axis.descendantOrSelf(), NodeTest.allNodes()));
        }
        else
        {
          break;
        }
      }
    }
    return result;
  }

  public ExprNode pathExpr() throws XPathSyntaxException
  {
    ExprNode filterExpr = filterExpr();
    if (filterExpr != null)
    {
      if (peekNext() == XPathTokens.SLASH)
      {
        // build normal axis step
        consumeToken();
        return relativeLocationPath(castToNodeSetExpr(filterExpr));
      }
      else if (peekNext() == XPathTokens.SLASH_SLASH)
      {
        // build a descendent-or-self axis
        consumeToken();
        return relativeLocationPath(new Step(castToNodeSetExpr(filterExpr), Axis.descendantOrSelf(), NodeTest.allNodes()));
      }
      else
      {
        return filterExpr;
      }
    }
    else
    {
      return locationPath();
    }
  }

  public NodeSetExpr step(NodeSetExpr leftNodeExpr) throws XPathSyntaxException
  {
    // first look for abbreviated steps: . and ..
    if (peekNext() == XPathTokens.DOT)
    {
      consumeToken();
      // . is equivalent to self::node()
      return new Step(leftNodeExpr, Axis.selfAxis(), NodeTest.allNodes());
    }
    else if (peekNext() == XPathTokens.DOT_DOT)
    {
      consumeToken();
      // .. is equivalent to parent::node()
      return new Step(leftNodeExpr, Axis.parentAxis(), NodeTest.allNodes());
    }

    // this is a standard step...
    // Look for the Axis Specifier.
    // assume the child:: axis, unless we get an explicit indication of another axis.
    Axis axis = Axis.childAxis();
    boolean axisSet = false;
    if (peekNext() == XPathTokens.AT_SIGN)
    {
      consumeToken();
      // an abbreviated attribute axis.
      axis = Axis.attributeAxis();
      axisSet = true;
    }
    else if (peek(1) == XPathTokens.NCNAME && peek(2) == XPathTokens.COLON_COLON)
    {
      // explicitly named axis.
      String axisName = consumeToken();
      consumeToken(); // ::
      axis = Axis.axisByName(axisName);
      if (axis == null)
      {
        throw new XPathSyntaxException(JET2Messages.XPath_UnknownAxis + axisName);
      }
      axisSet = true;
    }
    // Look for the Node Test...
    NodeTest nodeTest;
    if (peekNext() == XPathTokens.ASTERISK)
    {
      consumeToken();
      nodeTest = NodeTest.nameTest(axis.principalNodeKind(), new ExpandedName(STAR));
    }
    else if (peek(1) == XPathTokens.NCNAME && peek(2) == XPathTokens.COLON && peek(3) == XPathTokens.ASTERISK)
    {
      String ncName = consumeToken();
      consumeToken();
      consumeToken();
      nodeTest = NodeTest.nameTest(axis.principalNodeKind(), new ExpandedName(ncName, STAR));
    }
    else if (peekNext() == XPathTokens.QNAME)
    {
      String qName = consumeToken();
      int colonPos = qName.indexOf(':');
      final String prefix = qName.substring(0, colonPos);
      String nsURI = nsContext == null ? null : nsContext.getNamespaceURI(prefix);
      if (nsURI == null)
      {
        throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_UnknownNSPrefix, new Object []{ prefix }));
      }
      nodeTest = NodeTest.nameTest(axis.principalNodeKind(), new ExpandedName(nsURI, qName.substring(colonPos + 1)));
    }
    else if (peek(1) == XPathTokens.NCNAME && peek(2) == XPathTokens.LPAREN)
    {
      String ncName = consumeToken();
      // look for a node-test quasi function...
      consumeToken(); // (
      if ("node".equals(ncName)) { //$NON-NLS-1$
        nodeTest = NodeTest.allNodes();
      }
      else if ("comment".equals(ncName)) { //$NON-NLS-1$
        nodeTest = NodeTest.commentNodes();
      }
      else if ("text".equals(ncName)) { //$NON-NLS-1$
        nodeTest = NodeTest.textNodes();
      }
      else if ("processing-instruction".equals(ncName)) { //$NON-NLS-1$
        String literal = null;
        if (peekNext() == XPathTokens.LITERAL)
        {
          String rawLiteral = consumeToken();
          literal = unescape(rawLiteral.substring(1, rawLiteral.length() - 1), rawLiteral.charAt(0));
        }
        if (literal != null)
        {
          nodeTest = NodeTest.processingInstructionNodes(literal);
        }
        else
        {
          nodeTest = NodeTest.allProcessingInstructionNodes();
        }
      }
      else
      {
        throw new XPathSyntaxException(JET2Messages.XPath_UnknownNodeTest + ncName);
      }
      if (peekNext() != XPathTokens.RPAREN)
      {
        throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ ")" })); //$NON-NLS-1$
      }
      consumeToken(); // )
    }
    else if (peekNext() == XPathTokens.NCNAME)
    {
      String ncName = consumeToken();
      nodeTest = NodeTest.nameTest(axis.principalNodeKind(), new ExpandedName(ncName));
    }
    else if (axisSet)
    {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ "*, NCNAME, QNAME, NCNAME::*" })); //$NON-NLS-1$
    }
    else
    {
      return null;
    }

    // look for predicates...
    List predicates = new ArrayList();
    Predicate predicate = null;
    while ((predicate = predicate()) != null)
    {
      predicates.add(predicate);
    }
    // construct the node
    NodeSetExpr result = predicates.size() > 0 ? 
      new Step(leftNodeExpr, axis, nodeTest, (Predicate[])predicates.toArray(new Predicate[predicates.size()]))
      : new Step(leftNodeExpr, axis, nodeTest);
      
    return result;
  }

  public ExprNode filterExpr() throws XPathSyntaxException
  {
    ExprNode primaryExpr = primaryExpr();

    if (peekNext() == XPathTokens.LBRACKET)
    {
      // must have a node set in order to use predicates.
      NodeSetExpr expr = castToNodeSetExpr(primaryExpr);
      Predicate predicate = null;
      while ((predicate = predicate()) != null)
      {
        expr = new FilterExpr(expr, predicate);
      }
      return expr;
    }
    else
    {
      return primaryExpr;
    }

  }

  /**
   * Generate expression does as necessary to convert an ExprNode to a NodeSetExpr.
   * In general, only Variable nodes can be cast to node set expressions. If the
   * passed ExprNode is not already a NodeSetExpr or VariableExpr, then an exception
   * is thrown.
   * @param expr the node to cast a NodeSetExpr.
   * @return the result.
   * @throws XPathSyntaxException if the expression node cannot be cast to a nodeSetExpr
   */
  private NodeSetExpr castToNodeSetExpr(ExprNode expr) throws XPathSyntaxException
  {
    if (expr instanceof NodeSetExpr)
    {
      return (NodeSetExpr)expr;
    }
    else if (expr instanceof Variable || expr instanceof Function)
    {
      return new NodeSetCast(expr);
    }
    else
    {
      // FIXME Should do this as a post-parse semantic analysis
      throw new XPathSyntaxException(JET2Messages.XPath_ExpressionMustBeNodeSet);
    }
  }

  public ExprNode number()
  {
    if (peekNext() != XPathTokens.NUMBER)
    {
      return null;
    }
    String literal = consumeToken();
    return new NumberLiteral(Double.valueOf(literal).doubleValue());
  }

  public ExprNode literal()
  {
    if (peekNext() != XPathTokens.LITERAL)
    {
      return null;
    }

    String literal = consumeToken();
    return new StringLiteral(unescape(literal.substring(1, literal.length() - 1), literal.charAt(0)));
  }

  private String unescape(String substring, char quoteChar)
  {
    final String regexDQuote = "\"\""; //$NON-NLS-1$
    final String regexSQuote = "''"; //$NON-NLS-1$
    final String regexQuotes = quoteChar == '"' ? regexDQuote : regexSQuote;
    return substring.replaceAll(regexQuotes, Character.toString(quoteChar));
  }

  public ExprNode variableReference() throws XPathSyntaxException
  {
    Token token = peekNext();
    if (token != XPathTokens.DOLLAR_SIGN)
    {
      return null;
    }
    consumeToken();
    if (peekNext() != XPathTokens.QNAME && peekNext() != XPathTokens.NCNAME)
    {
      throw new XPathSyntaxException(MessageFormat.format(JET2Messages.XPath_Expected, new Object []{ "QName NCName" })); //$NON-NLS-1$
    }
    String variableName = consumeToken();
    return new Variable(variableName);
  }

  private String consumeToken()
  {
    if (nextTokenIndex >= tokenInstances.length)
    {
      throw new IllegalStateException();
    }
    TokenInstance ti = tokenInstances[nextTokenIndex++];
    return ti.getTokenText(input);
  }

  public Token peekNext()
  {
    return peek(1);
  }

  private Token peek(int i)
  {
    int index = nextTokenIndex + i - 1;
    return index >= tokenInstances.length ? Token.EOF_TOKEN : tokenInstances[index].token;
  }

  private String peekTokenText()
  {
    return nextTokenIndex >= tokenInstances.length ? null : tokenInstances[nextTokenIndex].getTokenText(input);
  }

}
