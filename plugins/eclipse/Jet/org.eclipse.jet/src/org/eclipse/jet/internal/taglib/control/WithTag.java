/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: WithTag.java,v 1.2 2009/04/08 17:06:21 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.AbstractConditionalTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Implement the &lt;c:with&gt; tag.
 */
public class WithTag extends AbstractConditionalTag
{

  private String varName = null;
  private Object savedVarValue = null;
  private Object contextObject;
  
  public boolean doEvalCondition(TagInfo td, JET2Context context) throws JET2TagException
  {
    final String selectXPath = getAttribute("select"); //$NON-NLS-1$
    varName = getAttribute("var"); //$NON-NLS-1$
    final XPathContextExtender xpc = XPathContextExtender.getInstance(context);
    
    contextObject = xpc.resolveSingle(selectXPath);
    
    return contextObject != null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    final XPathContextExtender xpc = XPathContextExtender.getInstance(context);
    xpc.pushXPathContextObject(contextObject);
    if(varName != null) {
      savedVarValue = context.hasVariable(varName) ? context.getVariable(varName) : null;
      
      context.setVariable(varName, contextObject);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    final XPathContextExtender xpc = XPathContextExtender.getInstance(context);
    
    xpc.popXPathContextObject();
    if(varName != null) {
      if(savedVarValue != null) {
        context.setVariable(varName, savedVarValue);
      } else {
        context.removeVariable(varName);
      }
    }
  }


}
