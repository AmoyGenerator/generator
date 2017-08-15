/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.taglib.format;


/**
 * Implement the Standard JET Format Tag 'uc' (uppercase).
 *
 */
public class UpperCaseTag extends AbstractCaseShiftTag
{

  /**
   * 
   */
  public UpperCaseTag()
  {
    super();
  }

 
  /**
   * @param textToCaseShift
   * @return
   */
  protected String doCaseShift(final String textToCaseShift)
  {
    return textToCaseShift.toUpperCase();
  }

}
