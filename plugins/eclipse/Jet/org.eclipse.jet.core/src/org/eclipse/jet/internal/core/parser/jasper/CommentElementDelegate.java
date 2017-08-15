/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
 * $Id: CommentElementDelegate.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.core.parser.jasper;

/**
 * A JET Compiler delegate for handling JET2 comments (&lt;%-- ... --%&gt;).
 *
 */
public class CommentElementDelegate implements JETCoreElement
{

  private static final String STD_COMMENT_CHARS = "--"; //$NON-NLS-1$

  /**
   * Create a new CommentElementDelegate
   */
  public CommentElementDelegate()
  {
    super();
  }

  /**
   * Given the parser state (reader, parser), determine if the parser is currently located at
   * a JET2 comment. If so, parse it, calling appropriate methods on <code>listener</code>.
   * @param listener the JET parser event listener, to which comment events are dispatched if a comment is recognized
   * @param reader the JET reader (lexer)
   * @param parser the JET parser instance
   * @return <code>true</code> a JET2 comment element was recognized, <code>false</code> otherwise
   * @throws JETException if an error occurs
   * 
   */
  public boolean accept(JETParseEventListener listener, JETReader reader, JETParser parser) throws JETException
  {
    if (!(listener instanceof JETParseEventListener2))
    {
      return false;
    }
    JETParseEventListener2 jet2Listener = (JETParseEventListener2)listener;

    String elementOpen = parser.getOpenScriptlet() + STD_COMMENT_CHARS;
    String elementClose = STD_COMMENT_CHARS + parser.getCloseScriptlet();
    if (!reader.matches(elementOpen))
    {
      return false;
    }
    JETMark elementStart = reader.mark();
    reader.advance(elementOpen.length());

    JETMark start = reader.mark();
    JETMark stop = reader.skipUntil(elementClose);
    if (stop == null)
    {
      MessagesUtil.recordUnterminatedElement(jet2Listener, elementClose, elementStart, reader);
    }
    else
    {
      jet2Listener.handleComment(start, stop);
    }
    return true;
  }

}
