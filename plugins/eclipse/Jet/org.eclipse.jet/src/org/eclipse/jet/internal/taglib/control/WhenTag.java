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
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the JET2 standard tag 'when', which is a child tag of 'choose'.
 * @see org.eclipse.jet.internal.taglib.control.ChooseTag
 *
 */
public class WhenTag extends AbstractChooseAlternative
{

  /**
   * A variable name that hopefully nobody will every use.
   * We use it to handle the chase where the choose tag had a select expression.
   */
  private static final String CHOOSE_SELECT_OBJECT_TMP_VAR = "_org.eclipse.jet.tags.choose"; //$NON-NLS-1$

  /**
   * 
   */
  public WhenTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ConditionalTag#doEvalCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public boolean doEvalCondition(TagInfo td, JET2Context context) throws JET2TagException
  {
    String testXPath = getAttribute("test"); //$NON-NLS-1$

    boolean processContents = false;

    ChooseTag chooseTag = getChooseTag();
    if (!chooseTag.isSatisfied())
    {
      XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);
      final Object chooseSelectObject = chooseTag.getSelectObject();
      if (chooseSelectObject != null)
      {
        // handle the case where choose had a select attribute by converting the test XPATH expression
        // into a comparison with the result of the choose select expression.
        try
        {
          context.setVariable(CHOOSE_SELECT_OBJECT_TMP_VAR, chooseSelectObject);
          testXPath = "$" + CHOOSE_SELECT_OBJECT_TMP_VAR + " = (" + testXPath + ")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
          processContents = xpathExtender.resolveTest(xpathExtender.currentXPathContextObject(), testXPath);
        }
        finally
        {
          // make sure we undefine the variable
          context.removeVariable(CHOOSE_SELECT_OBJECT_TMP_VAR);
        }
      }
      else
      {
        processContents = xpathExtender.resolveTest(xpathExtender.currentXPathContextObject(), testXPath);
      }
      if (processContents)
      {
        chooseTag.setSatisfied(true);
      }
    }
    return processContents;
  }
}
