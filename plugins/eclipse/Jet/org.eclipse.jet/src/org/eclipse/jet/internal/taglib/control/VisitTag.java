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


import java.text.MessageFormat;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractConditionalTag;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the subtag 'visit' of the JET Standard Control Tag 'visitor'.
 */
public class VisitTag extends AbstractConditionalTag
{

  /**
   * 
   */
  public VisitTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ConditionalTag#doEvalCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public boolean doEvalCondition(TagInfo td, JET2Context context) throws JET2TagException
  {

    CustomTag parentTag = getParent();
    if (!(parentTag instanceof VisitorTag))
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_IllegalParent, new Object []{ "visitor" })); //$NON-NLS-1$
    }

    Object next = ((VisitorTag)parentTag).getNext();

    String testXPath = getAttribute("test"); //$NON-NLS-1$

    XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);

    return xpathExtender.resolveTest(next, testXPath);
  }

}
