/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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
 * $Id: JETMark.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;


import java.util.Stack;

import org.eclipse.jet.internal.core.parser.LineInfo;


/**
 * A mark represents a point in the JET input.
 */
public final class JETMark
{
 
  /**
   * This is the character offset.
   */
  protected int cursor;

  /**
   * This is the id of the file.
   */
  protected int fileid;

  /**
   * This is the base URI for relative paths.
   */
  protected String baseDir;

  /**
   * This is the stream of characters.
   */
  protected char[] stream = null;

  /**
   * This is an array of lines descriptors.
   */
  protected LineInfo[] lineInfo = null;
  
  /**
   * This is the stack of inclusions.
   */
  protected Stack includeStack = null;

  /**
   * This is the reader that owns this mark.
   */
  protected JETReader reader;

  /**
   * Keep track of parser before parsing an included file.
   * This class keeps track of the parser before we switch to parsing an
   * included file. In other words, it's the parser's continuation to be
   * reinstalled after the included file parsing is done.
   */
  class IncludeState
  {
    int cursor;

     int fileid;

    String baseDir;

    String encoding;

    char[] stream = null;

    private final LineInfo[] lineInfo;

    IncludeState(int inCursor, LineInfo[] inLineInfo, int inFileid, String inBaseDir, char[] inStream)
    {
      cursor = inCursor;
      lineInfo = inLineInfo;
      fileid = inFileid;
      baseDir = inBaseDir;
      stream = inStream;
    }
  }

  /**
   * Creates a new mark
   * @param reader JETReader this mark belongs to
 * @param inStream current stream for this mark
 * @param fileid id of requested jet file
 * @param inBaseDir base directory of requested jet file
   */
  JETMark(JETReader reader, char[] inStream, int fileid, String inBaseDir)
  {
    this.reader = reader;
    this.stream = inStream;
    this.cursor = 0;
    this.lineInfo = LineInfo.calculateLines(inStream);
    this.fileid = fileid;
    this.baseDir = inBaseDir;
    this.includeStack = new Stack();
  }

  JETMark(JETMark other)
  {
    this.reader = other.reader;
    this.stream = other.stream;
    this.fileid = other.fileid;
    this.cursor = other.cursor;
    this.lineInfo = other.lineInfo;
    this.baseDir = other.baseDir;

    // clone includeStack without cloning contents
    //
    includeStack = new Stack();
    for (int i = 0; i < other.includeStack.size(); ++i)
    {
      includeStack.addElement(other.includeStack.elementAt(i));
    }
  }

  /** 
   * Sets this mark's state to a new stream.
   * It will store the current stream in it's includeStack.
   * @param inStream new stream for mark
   * @param inFileid id of new file from which stream comes from
   * @param inBaseDir directory of file
   * @param inEncoding encoding of new file
   */
  public void pushStream(char[] inStream, int inFileid, String inBaseDir)
  {
    // Store the current state in stack.
    //
    includeStack.push(new IncludeState(cursor, lineInfo, fileid, baseDir, stream));

    // Set the new variables.
    //
    cursor = 0;
    fileid = inFileid;
    baseDir = inBaseDir;
    stream = inStream;
    lineInfo = LineInfo.calculateLines(inStream);
  }

  /** 
   * Restores this mark's state to a previously stored stream.
   */
  public boolean popStream()
  {
    // Make sure we have something to pop.
    //
    if (includeStack.size() <= 0)
    {
      return false;
    }

    // Get previous state in stack.
    //
    IncludeState state = (IncludeState)includeStack.pop();

    // Set the new variables.
    //
    cursor = state.cursor;
    lineInfo = state.lineInfo;
    fileid = state.fileid;
    baseDir = state.baseDir;
    stream = state.stream;
    return true;
  }

  public String getFile()
  {
    return reader.getFile(fileid);
  }

  public String getBaseURI()
  {
    return reader.getBaseURI(fileid);
  }

  public String getLocalFile()
  {
    String file = reader.getFile(fileid);
    if(file.startsWith("platform:/resource/")) { //$NON-NLS-1$
    	file = file.substring("platform:/resource".length()); //$NON-NLS-1$
    }
    else if (file.startsWith("file:/")) //$NON-NLS-1$
    {
    	// FIXME delegate this to an ITemplateResolver
//      IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
//      IFile iFile = workspaceRoot.getFileForLocation(new Path(file.substring(6)));
//      file = iFile.getFullPath().toString();
    }

    return file;
  }

  public int getFileId()
  {
    return fileid;
  }

  public int getCursor()
  {
    return cursor;
  }

  public String toShortString()
  {
    return "(" + getLine() + "," + getCol() + ")";  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
  }

  public String toString()
  {
    return getLocalFile() + "(" + getLine() + "," + getCol() + ")";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
  }

  public String format(String key)
  {
    return MessagesUtil.getString(
      key,
      new Object []{ getLocalFile(), new Integer(getLine() + 1), new Integer(getCol() + 1), new Integer(cursor) });
  }

  public boolean equals(Object other)
  {
    if (other instanceof JETMark)
    {
      JETMark m = (JETMark)other;
      return this.reader == m.reader && this.fileid == m.fileid && this.cursor == m.cursor && this.getLine() == m.getLine()
        && this.getCol() == m.getCol();
    }
    return false;
  }

  /**
   * Return the one-based line number of the cursor.
   * @return Returns the line.
   */
  public int getLine()
  {
    return LineInfo.getLineNo(lineInfo, cursor);
  }

  /**
   * Return the one-based column number of the cursor.
   * @return the one based column number of the cursor.
   */
  public int getCol()
  {
    int lineIndex = getLine() - 1;
    if(lineIndex < lineInfo.length) {
      return cursor - lineInfo[lineIndex].getStart() + 1;
    } else {
      return cursor - (lineInfo[lineInfo.length - 1].getEnd() + lineInfo[lineInfo.length - 1].getDelimiter().length()) + 1;
    }
  }
}
