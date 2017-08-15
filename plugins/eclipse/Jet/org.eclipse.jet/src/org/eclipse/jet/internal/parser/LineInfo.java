/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.internal.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Represent line information. Each line has a buffer relative offset of its first character (start), and a buffer
 * relative offset of its line separator (end). 
 */
public final class LineInfo {
  private final int start;
  private final int end;
  private final String delimiter;

  public LineInfo(int start, int end, String delimiter)
  {
    this.start = start;
    this.end = end;
    this.delimiter = delimiter;
    
  }

  /**
   * @return Returns the delimiter.
   */
  public final String getDelimiter()
  {
    return delimiter;
  }

  /**
   * @return Returns the end.
   */
  public final int getEnd()
  {
    return end;
  }

  /**
   * @return Returns the start.
   */
  public final int getStart()
  {
    return start;
  }
  
  public String toString()
  {
    return "[" + start + ", " + end + ") ";    //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
  }
  
  /**
   * Return an array of Line info objects describing the lines of the passed buffer. The
   * code recognizes the following line separators:
   * <bl>
   * <li>\r - MAC/OS</li>
   * <li>\n - Unix/Linux</li>
   * <li>\r\n - Windows</li>
   * </bl>
   * If the last line lacks a separater, then an empty separator is assumed.
   * @param buffer the buffer to analyse.
   * @return an array of LineInfo objects. The array will be empty if <code>buffer</code> is empty.
   */
  public static LineInfo[] calculateLines(char[] buffer)
  {
    List list = new ArrayList();
    

    int start = 0;
    String separator;
    for (int i = 0; i < buffer.length; i++)
    {
      switch(buffer[i])
      {
        case '\r':
          if(i + 1 < buffer.length && buffer[i+1] == '\n') {
            separator = "\r\n"; //$NON-NLS-1$
          } else {
            separator = "\r"; //$NON-NLS-1$
          }
          break;
        case '\n':
          separator = "\n"; //$NON-NLS-1$
          break;
        default:
          continue;
      }
      LineInfo lineInfo = new LineInfo(start, i, separator);
      list.add(lineInfo);
      if(separator.length() == 2) {
        i++;
      }
      start = i + 1;
    }
    if(start < buffer.length) {
      LineInfo lineInfo = new LineInfo(start, buffer.length, ""); //$NON-NLS-1$
      list.add(lineInfo);
    }
    
    return (LineInfo[])list.toArray(new LineInfo[list.size()]);
  }
  
  /**
   * Return the LineInfo for the given offset
   * @param lineInfo an array of LineInfo objects, as returned by {@link #calculateLines(char[])}.
   * @param offset an offset in the file.
   * @return the line index.
   */
  public static LineInfo getLineInfo(LineInfo[] lineInfo, final int offset)
  {
    final int index = getLineNo(lineInfo, offset) - 1;
    return index < lineInfo.length ? lineInfo[index] : null;
  }

  /**
   * Return the line number of a given offset.
   * @param lineInfo
   * @param offset
   * @return the one based line number
   */
  public static int getLineNo(LineInfo[] lineInfo, final int offset)
  {
    if(offset < 0) { 
      throw new IllegalArgumentException("offset = " + offset); //$NON-NLS-1$
    }
    final int index = Arrays.binarySearch(lineInfo, null, new Comparator() {

      public int compare(Object arg0, Object arg1)
      {
        LineInfo li = (LineInfo)arg0;
        if( offset < li.start) {
          return 1;
        }
        if(li.start <= offset && offset < li.end + li.delimiter.length()) {
          return 0;
        }
        return -1;
      }});
    return (index >= 0 ? index : -index - 1) + 1;
  }

  public boolean hasDelimiter()
  {
    return getDelimiter().length() > 0;
  }
 

}