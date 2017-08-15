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
 * $Id: ProblemSeverity.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser;


/**
 * Enumeration of problem Severities
 * <p>
 * This class is not intended to be subclassed by clients.
 * </p>
 *
 * @since 1.0
 */
public final class ProblemSeverity
{

  /**
   * The compilation unit has a problem, but it is will not prevent execution.
   */
  public static final ProblemSeverity WARNING = new ProblemSeverity("WARNING"); //$NON-NLS-1$

  /**
   * The compilation unit has a problem that will prevent execution.
   */
  public static final ProblemSeverity ERROR = new ProblemSeverity("ERROR"); //$NON-NLS-1$

  private final String display;

  protected ProblemSeverity(String display)
  {
    this.display = display;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return display;
  }
}
