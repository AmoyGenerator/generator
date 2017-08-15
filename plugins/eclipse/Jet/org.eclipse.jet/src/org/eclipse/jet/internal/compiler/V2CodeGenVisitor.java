/**
 * <copyright>
 *
 * Copyright (c) 2007, 2009 IBM Corporation and others.
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
 * $Id: V2CodeGenVisitor.java,v 1.4 2009/04/13 17:22:07 pelder Exp $
 */
package org.eclipse.jet.internal.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.core.parser.ast.JETASTElement;
import org.eclipse.jet.core.parser.ast.TextElement;
import org.eclipse.jet.core.parser.ast.XMLElement;
import org.eclipse.jet.internal.JavaUtil;

/**
 * AST Visitor used by code generation
 */
public class V2CodeGenVisitor extends SafeJETASTVisitor
{
  private static final String UNDERSCORE = "_"; //$NON-NLS-1$

  private final String textPrefix = "TEXT_"; //$NON-NLS-1$
  private final String tagVarPrefix = "_jettag_"; //$NON-NLS-1$

  /**
   * @param context
   * @param out
   * @param initialIndent
   */
  public V2CodeGenVisitor(JET2Context context, JET2Writer out, String stdIndent)
  {
    super(context, out, stdIndent);
  }

  public V2CodeGenVisitor(JET2Context context, JET2Writer out)
  {
    this(context, out, "    "); //$NON-NLS-1$
  }

  public String tagVariableName(XMLElement element)
  {
    return tagVar(element);
  }

  public String tagVar(XMLElement element)
  {
    return tagVarPrefix + tagVariableSuffix(element);
  }

  /**
   * Return the name of a unique variable to refer to the static information about the passed element.
   * @param element
   * @return the variable name
   */
  public String tagInfoVariableName(XMLElement element)
  {
    return tagInfoVar(element);
  }

  /**
   * Return the name of a unique variable to refer to the static information about the passed element.
   * @param element
   * @return the variable name
   */
  public String tagInfoVar(XMLElement element)
  {
    return tagVariableSuffix(element);
  }

  /**
   * Create a unique suffix of all variables that refer to the passed element
   * @param element an XML Element
   * @return the tag variable name
   */
  private String tagVariableSuffix(XMLElement element)
  {
    return element.getNSPrefix() + UNDERSCORE + element.getTagNCName() + UNDERSCORE + element.getLine() + UNDERSCORE
    + element.getColumn();
  }

  public String textVar(int i) {
    return textPrefix + i;
  }

  public boolean requiresOwnWriter(XMLElement element) {
    return element.getTagDefinition().requiresNewWriter();
  }

  public String parentTagVar(XMLElement element) {
    XMLElement parentXMLElement = null;
    for(JETASTElement astParent = element.getParent(); astParent != null; astParent = astParent.getParent()) 
    {
      if(astParent instanceof XMLElement) {
        parentXMLElement = (XMLElement)astParent;
        break;
      }
    }
    return parentXMLElement == null ? "null" : tagVar(parentXMLElement); //$NON-NLS-1$
  }

  public String textExpr(TextElement text) {
    return JavaUtil.asJavaQuoteStringWithNLRemoved(text.getText());
  }

  /**
   * Return the text element as an array of Java text constants.
   * @param text the text element
   * @param nlConstantName the name of the constant to use for NL characters
   * @return an array properly formatted Java quoted strings (including quotes) and NL constants
   */
  public String[] textConstants(TextElement text, String nlConstantName) {
    return JavaUtil.asJavaQuotedStrings(text.getText(), nlConstantName);
  }

//update start
  public boolean isFirstLineTrim(TextElement text, String nlConstantName){
    String[] strings = textConstants(text, nlConstantName);
    
    if(strings.length > 0){
      String str = strings[0];
      if("NL".equals(str)){
        return true;
      }
      if(str.length() > 2){
        str = str.substring(1, str.length() - 2);
        if(str.trim().isEmpty()){
          return true;
        }
      }
    }
    return false;
  }

  public boolean isLastLineTrim(TextElement text, String nlConstantName){
    String[] strings = textConstants(text, nlConstantName);
    if(strings.length > 0){
      String str = strings[strings.length - 1];
      if(NL.equals(str)){
        return true;
      }
      if(str.length() > 2){
        str = str.substring(1, str.length() - 2);
        if(str.trim().isEmpty()){
          return true;
        }
      }
    }
    return false;
  }
//update end

  public String nlsComment(TextElement text) {
    return JavaUtil.nlsCommentsForJavaQuoteStringWithNLRemoved(text.getText());
  }

  private static final String NL_REGEX = "([\\n][\\r]?|[\\r][\\n]?)"; //$NON-NLS-1$
  private static final Pattern NL_PATTERN = Pattern.compile(NL_REGEX, Pattern.MULTILINE);
  private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

  protected boolean isClassComment(String comment) {
    final String text = getFirstCommentLine(comment);
    return text.equals("@class"); //$NON-NLS-1$
  }

  protected boolean isHeaderComment(String comment) {
    final String text = getFirstCommentLine(comment);
    return text.equals("@header"); //$NON-NLS-1$
  }

  protected boolean isNormalComment(String comment) {
    final String text = getFirstCommentLine(comment);
    return !text.equals("@header") && !text.equals("@class"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  protected String formatComment(String text, String leadIn) {
    if(!isNormalComment(text)) {
      text = text.replaceFirst("(?ms)^.*?$.*?^", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return indent(text, leadIn, 1);
  }

  private String getFirstCommentLine(String text)
  {
    text = text.trim(); // remove any leading or trailing whitespace
    final Matcher matcher = NL_PATTERN.matcher(text);
    final String firstLine;
    if(matcher.find()) {
      firstLine = text.substring(0, matcher.start()).trim();
    } else {
      firstLine = text;
    }
    return firstLine;
  }

  public static String indent(String content, String indent, int depth) {
    final String totalIndent = computeTotalIndent(indent, depth);
    if(totalIndent.length() == 0) {
      return content;
    }
    boolean indentIsWhitespace = totalIndent.matches("\\s*"); //$NON-NLS-1$

    final Matcher matcher = NL_PATTERN.matcher(content);
    final StringBuffer result = new StringBuffer(content.length() * 2); // a guess
    int lineStart;
    for(lineStart = 0; matcher.find(lineStart); lineStart = matcher.end()) {
      String line = content.substring(lineStart, matcher.start());
      if(line.length() > 0 || !indentIsWhitespace) {
        result.append(totalIndent);
      }
      result.append(line);
      result.append(NL);
      lineStart = matcher.end();
    }

    // look for final line without a NL character...
    if(lineStart < content.length()) {
      String line = content.substring(lineStart);
      result.append(totalIndent).append(line).append(NL);
    }

    return result.toString();
  }

  /**
   * @param indent
   * @param depth
   * @return
   */
  private static String computeTotalIndent(String indent, int depth)
  {
    final StringBuffer buffer = new StringBuffer(
      depth * indent.length() 
    );
    if(indent != null) {
      for(int i = 0; i < depth; i++) {
        buffer.append(indent);
      }
    }
    return buffer.toString();
  }
}
