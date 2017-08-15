/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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
package org.eclipse.jet.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class for Java related code generation.
 */
public class JavaUtil
{

  private static final String NL_PATTERN = "([\\n][\\r]?|[\\r][\\n]?)"; //$NON-NLS-1$

  /**
   * 
   */
  private JavaUtil()
  {
    super();
  }

  public static String escapeJavaString(char[] text)
  {
    StringBuffer buffer = new StringBuffer(text.length);
    JavaUtil.escapeJavaString(text, buffer);
    return buffer.toString();
  }

  /**
   * @param originalText
   * @param escapedText
   */
  public static void escapeJavaString(char[] originalText, StringBuffer escapedText)
  {
    for (int i = 0; i < originalText.length; i++)
    {
      switch (originalText[i])
      {
        case '\b':
          escapedText.append("\\b"); //$NON-NLS-1$
          break;
        case '\f':
          escapedText.append("\\f"); //$NON-NLS-1$
          break;
        case '\t':
          escapedText.append("\\t"); //$NON-NLS-1$
          break;
        case '\r':
          escapedText.append("\\r"); //$NON-NLS-1$
          break;
        case '\n':
          escapedText.append("\\n"); //$NON-NLS-1$
          break;
        case '"':
          escapedText.append("\\\""); //$NON-NLS-1$
          break;
        case '\\':
          escapedText.append("\\\\"); //$NON-NLS-1$
          break;
        default:
          escapedText.append(originalText[i]);
      }
    }
  }

  /**
   * Return a Java string, including initial and final double quotes, and inserting
   * any required Java escape sequences.
   * @param text the text to quote
   * @return the quoted Java string
   */
  public static String asJavaQuotedString(char[] text)
  {
    StringBuffer encoded = new StringBuffer(text.length + 2);
    encoded.append('"');
  
    escapeJavaString(text, encoded);
  
    encoded.append('"');
    return encoded.toString();
  }

  /**
   * Return a Java string, including initial and final double quotes, and inserting
   * any required Java escape sequences.
   * @param content the text to quote
   * @return the quoted Java string
   */
  public static String asJavaQuotedString(String content) {
    return asJavaQuotedString(content.toCharArray());
  }
  
  /**
   * Return a Java String expression with + NL + in place of new line data
   * @param text
   * @return
   */
  public static String asJavaQuoteStringWithNLRemoved(char[] text)
  {
    String rawString = new String(text);
    Matcher m = Pattern.compile(NL_PATTERN, Pattern.MULTILINE).matcher(rawString);
    int nextStringStart = 0;
    boolean nlFound = m.find();
    if(nlFound) {
      StringBuffer result = new StringBuffer(text.length);
      boolean firstExprComponent = true;
      for( ;nlFound; nlFound = m.find()) {
        if(firstExprComponent) {
          firstExprComponent = false;
        } else {
          result.append(" + "); //$NON-NLS-1$
        }
        if(m.start() > nextStringStart) {
          result.append(asJavaQuotedString(rawString.substring(nextStringStart, m.start()).toCharArray()));
          result.append(" + "); //$NON-NLS-1$
        }
        result.append("NL"); //$NON-NLS-1$
        nextStringStart = m.end();
      }
      if(nextStringStart < rawString.length()) {
        if(!firstExprComponent) {
          result.append(" + "); //$NON-NLS-1$
          result.append(asJavaQuotedString(rawString.substring(nextStringStart).toCharArray()));
        }
      }
      return result.toString();
    } else {
      return asJavaQuotedString(text);
    }
  }

  /**
   * Return a Java String expression with + NL + in place of new line data
   * @param text
   * @return
   */
  public static String[] asJavaQuotedStrings(char[] text, String nlConstantName)
  {
    
    String rawString = new String(text);
    Matcher m = Pattern.compile(NL_PATTERN, Pattern.MULTILINE).matcher(rawString);
    int nextStringStart = 0;
    boolean nlFound = m.find();
    if (nlFound)
    {
      final List constants = new ArrayList();

      for (; nlFound; nlFound = m.find())
      {
        if (m.start() > nextStringStart)
        {
          constants.add(asJavaQuotedString(rawString.substring(nextStringStart, m.start()).toCharArray()));
        }
        constants.add(nlConstantName != null ? nlConstantName : asJavaQuotedString(m.group().toCharArray()));
        nextStringStart = m.end();
      }
      if (nextStringStart < rawString.length())
      {
        constants.add(asJavaQuotedString(rawString.substring(nextStringStart).toCharArray()));
      }
      return (String[])constants.toArray(new String [constants.size()]);
    }
    else
    {
      return new String []{ asJavaQuotedString(text) };
    }
  }

  /**
   * Return a Java String expression with + NL + in place of new line data
   * @param text
   * @return
   */
  public static String nlsCommentsForJavaQuoteStringWithNLRemoved(char[] text)
  {
    String rawString = new String(text);
    Matcher nlMatch = Pattern.compile(NL_PATTERN, Pattern.MULTILINE).matcher(rawString);
    int nextLiteralStart = 0;
    int i = 0;
    
    StringBuffer result = new StringBuffer(text.length);
    for (boolean nlFound = nlMatch.find(); nlFound; nlFound = nlMatch.find())
    {
      if (nlMatch.start() > nextLiteralStart)
      {
        nlComment(result, ++i);
      }
      nextLiteralStart = nlMatch.end();
    }
    if (nextLiteralStart < rawString.length() || rawString.length() == 0)
    {
      nlComment(result, ++i);
    }
    return result.toString();
  }

  /**
   * @param result
   * @param i
   * @return
   */
  private static StringBuffer nlComment(StringBuffer result, int i)
  {
    return result.append(" //$NON-NLS-").append(i).append("$"); //$NON-NLS-1$ //$NON-NLS-2$
  }
}
