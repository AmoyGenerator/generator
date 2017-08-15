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

package org.eclipse.jet.internal.taglib.control;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the JET2 standard tag 'otherwise', which is a child tag of 'choose'
 *
 */
public class OtherwiseTag extends AbstractChooseAlternative
{

  /**
   * 
   */
  public OtherwiseTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ConditionalTag#doEvalCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public boolean doEvalCondition(TagInfo td, JET2Context context) throws JET2TagException
  {
    boolean processContents = false;

    ChooseTag chooseTag = getChooseTag();
    if (!chooseTag.isSatisfied())
    {
      processContents = true;
      chooseTag.setSatisfied(true);
    }

    return processContents;
  }

}
