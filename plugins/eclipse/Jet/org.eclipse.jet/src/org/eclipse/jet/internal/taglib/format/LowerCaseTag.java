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
 * Implement the Standard JET Format Tag 'lc' (lowercase).
 *
 */
public class LowerCaseTag extends AbstractCaseShiftTag
{

  /**
   * 
   */
  public LowerCaseTag()
  {
    super();
  }

  protected String doCaseShift(String textToCaseShift)
  {
    return textToCaseShift.toLowerCase();
  }


}
