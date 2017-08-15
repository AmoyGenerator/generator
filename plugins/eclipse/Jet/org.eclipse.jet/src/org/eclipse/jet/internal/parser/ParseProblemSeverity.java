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
package org.eclipse.jet.internal.parser;



/**
 * Enumeration of problem Severities
 * @deprecated
 */
public final class ParseProblemSeverity
{

  private final String display;

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return display;
  }

  private ParseProblemSeverity(String display)
  {
    this.display = display;
  }

  /**
   * The compilation unit has a problem that will prevent execution.
   */
  public static final ParseProblemSeverity ERROR = new ParseProblemSeverity("ERROR"); //$NON-NLS-1$
  /**
   * The compilation unit has a problem, but it is will not prevent execution.
   */
  public static final ParseProblemSeverity WARNING = new ParseProblemSeverity("WARNING"); //$NON-NLS-1$

}
