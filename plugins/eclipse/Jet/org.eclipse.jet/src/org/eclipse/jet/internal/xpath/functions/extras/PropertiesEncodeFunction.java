/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: PropertiesEncodeFunction.java,v 1.1 2008/04/28 16:45:46 pelder Exp $
 */
package org.eclipse.jet.internal.xpath.functions.extras;

import java.util.List;

import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathUtil;

/**
 * Implement 'String propertiesEncode(String input[, boolean leadingSpacesOnly])'.
 */
public class PropertiesEncodeFunction implements XPathFunction
{

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    final String input = XPathUtil.xpathString(args.get(0));
    final boolean leadingSpacesOnly = args.size() == 1 ? false : XPathUtil.xpathBoolean(args.get(1));
    return PropertiesEncodeFunction.encode(input, leadingSpacesOnly);
  }

  public static String encode(final String input, final boolean leadingSpacesOnly)
  {
    final StringBuffer buffer = new StringBuffer(input.length());
    // encode all spaces initiallly
    boolean encodeSpaces = true;
    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      switch (c)
      {
        case '\\':
        case '\t':
        case '\f':
        case '\n':
        case '\r':
        case '#':
        case '!':
        case '=':
        case ':':
        if (leadingSpacesOnly)
          encodeSpaces = false;
        buffer.append('\\');
        buffer.append(c);
          break;
        case ' ':
        if (encodeSpaces)
          buffer.append('\\');
        buffer.append(c);
          break;
        default:
        if (leadingSpacesOnly)
          encodeSpaces = false;
        if (0x0020 <= c && c <= 0x007e)
        {
          buffer.append(c);
        }
        else
        {
          buffer.append('\\').append('u').append(Character.forDigit((c >> 12) & 0xF, 16)).append(Character.forDigit((c >> 8) & 0xF, 16)).append(
            Character.forDigit((c >> 4) & 0xF, 16)).append(Character.forDigit(c & 0xF, 16));
        }
          break;
      }
    }
    return buffer.toString();
  }

}
