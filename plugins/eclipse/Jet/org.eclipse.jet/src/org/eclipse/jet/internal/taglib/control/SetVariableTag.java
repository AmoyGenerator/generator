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
package org.eclipse.jet.internal.taglib.control;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the JET2 Standard tag 'setVariable'.
 *
 */
public class SetVariableTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public SetVariableTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String select = getAttribute("select"); //$NON-NLS-1$
    String var = getAttribute("var"); //$NON-NLS-1$

    XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);
    Object value = xpathExtender.resolveAsObject(xpathExtender.currentXPathContextObject(), select);

    context.setVariable(var, value);
  }

}
