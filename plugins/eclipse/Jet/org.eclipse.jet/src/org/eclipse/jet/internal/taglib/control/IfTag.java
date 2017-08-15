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
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.AbstractConditionalTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.xpath.XPathUtil;


/**
 * Implement the JET2 Control tag 'if'.
 *
 */
public class IfTag extends AbstractConditionalTag
{

  private String var = null;
  private boolean varAlreadySet = false; 
  private Object savedValue;

  /**
   * 
   */
  public IfTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ConditionalTag#doEvalCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public boolean doEvalCondition(TagInfo td, JET2Context context) throws JET2TagException
  {

    String testXPath = getAttribute("test"); //$NON-NLS-1$
    var = getAttribute("var"); //$NON-NLS-1$
    
    XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);
    
    // get the result of the xpath expression prior to casting to boolean, in case we define a 'var'.
    Object rawObject = xpathExtender.resolveAsObject(xpathExtender.currentXPathContextObject(), testXPath);
    
    boolean processContents = XPathUtil.xpathBoolean(rawObject);
    if(processContents && var != null)
    {
      varAlreadySet = context.hasVariable(var);
      if(varAlreadySet)
      {
        savedValue = context.getVariable(var);
      }
      context.setVariable(var, rawObject);
    }

    return processContents;
  }

  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if(var != null)
    {
      if(varAlreadySet)
      {
        context.setVariable(var, savedValue);
      }
      else
      {
        context.removeVariable(var);
      }
    }
  }
}
