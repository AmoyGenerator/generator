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
 * $Id: PreferenceValueUtil.java,v 1.1 2007/05/16 16:44:13 pelder Exp $
 */
package org.eclipse.jet.internal.compiler;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Utility class for propertly escaping Java preference values.
 */
public class PreferenceValueUtil
{

  public static String encode(String value)
  {
    StringBuffer buffer = new StringBuffer(value.length());
    
    CharacterIterator ci = new StringCharacterIterator(value);
    for(char c = ci.first(); c != CharacterIterator.DONE; c = ci.next())
    {
      switch(c) {
        case '\\':
        case '\t':
        case '\r':
        case '\n':
        case '\f':
        case ' ':
        case '#':
        case '!':
        case '=':
          buffer.append('\\').append(c);
          break;
        default:
          if(0x0020 <= c && c <= 0x007e)
          {
            buffer.append(c);
          }
          else
          {
            buffer.append("\\u").append(hexValue(c)); //$NON-NLS-1$
          }
      }
    }
    
    
    return buffer.toString();
  }

  private static final char[] hexDigits = "0123456789abcdef".toCharArray(); //$NON-NLS-1$
  private static char[] hexValue(char c)
  {
    char[] result = new char[4];
    result[0] = hexDigits[(c >> 12) & 0xf];
    result[1] = hexDigits[(c >> 8) & 0xf ];
    result[2] = hexDigits[(c >> 4) & 0xf ];
    result[3] = hexDigits[c & 0xf ];
    return result;
  }
}
