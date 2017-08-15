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


/**
 * An instance of an token found in an input stream.
 *
 */
public class TokenInstance
{

  public final Token token;

  public final int start;

  public final int end;

  /**
   * 
   */
  public TokenInstance(Token token, int start, int end)
  {
    super();
    this.token = token;
    this.start = start;
    this.end = end;
  }

  public String getTokenText(String input)
  {
    return input.substring(start, end);
  }

  public String toString()
  {
    return token.toString() + "[" + start + "," + end + ")"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
  }
}
