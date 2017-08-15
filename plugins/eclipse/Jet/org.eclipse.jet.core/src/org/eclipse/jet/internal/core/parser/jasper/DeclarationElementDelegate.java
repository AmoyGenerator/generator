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
 * $Id: DeclarationElementDelegate.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;


/**
 * Parser delegate for parsing a JET2 declaration &lt;%! ... %&gt;
 */
public class DeclarationElementDelegate implements JETCoreElement
{

  private static final String STD_DECL_CHARS = "!"; //$NON-NLS-1$

  public DeclarationElementDelegate()
  {
    super();
  }

  public boolean accept(JETParseEventListener listener, JETReader reader, JETParser parser) throws JETException
  {
    if (!(listener instanceof JETParseEventListener2))
    {
      return false;
    }
    JETParseEventListener2 jet2Listener = (JETParseEventListener2)listener;

    String declOpen = parser.getOpenScriptlet() + STD_DECL_CHARS;
    String declClose = parser.getCloseScriptlet();
    if (!reader.matches(declOpen))
    {
      return false;
    }
    JETMark elementStart = reader.mark();
    reader.advance(declOpen.length());

    JETMark start = reader.mark();
    JETMark stop = reader.skipUntil(declClose);
    if (stop == null)
    {
      MessagesUtil.recordUnterminatedElement(jet2Listener, declClose, elementStart, reader);
    }
    else
    {
      jet2Listener.handleDeclaration(start, stop);
    }
    return true;
  }

}
