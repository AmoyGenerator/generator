/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractIteratingTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathUtil;


/**
 * Implement the standard JET2 control tag 'iterate'.
 *
 */
public class IterateTag extends AbstractIteratingTag
{

  private static final String UPDATE_CONTEXT_VAR = "org.eclipse.jet.taglib.control.iterateSetsContext"; //$NON-NLS-1$
  
  private Boolean updateContext;
  
  private Object[] selectedObjects;

  private int currentIndex;

  private String varName;

  private Object savedVarValue = null;
  
  private boolean nodeSetIteration;

  private long maxIterations = 0;

  /**
   * 
   */
  public IterateTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.IteratingTag#doEvalLoopCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public boolean doEvalLoopCondition(TagInfo tc, JET2Context context) throws JET2TagException
  {
    boolean doAnotherIteration = ++currentIndex < maxIterations;

    if (!doAnotherIteration && varName != null)
    {
      // clean up variables...
      if (savedVarValue != null)
      {
        context.setVariable(varName, savedVarValue);
      }
      else
      {
        context.removeVariable(varName);
      }
    }

    return doAnotherIteration;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.AbstractIteratingTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    super.doBeforeBody(td, context, out);
    
    // set variables and context objects for this interation
    if (nodeSetIteration)
    {
      final Object currentObject = selectedObjects[currentIndex];
      if(varName != null) {
        context.setVariable(varName, currentObject);
      }
      if(updateContext(context)) {
        XPathContextExtender.getInstance(context).pushXPathContextObject(currentObject);
      }
    }
    else
    {
      // numeric based looping is 1-based, add 1 to the current index.
      if(varName != null) {
        context.setVariable(varName, new Long(currentIndex + 1));
      }
    }

  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.AbstractIteratingTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // restore variables, context objects from previous interation up variables
    if (updateContext(context) && nodeSetIteration)
    {
      XPathContextExtender.getInstance(context).popXPathContextObject();
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.IteratingTag#doInitializeLoop(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public void doInitializeLoop(TagInfo td, JET2Context context) throws JET2TagException
  {
    String selectXPath = getAttribute("select"); //$NON-NLS-1$
    varName = getAttribute("var"); //$NON-NLS-1$

    setDelimiter(getAttribute("delimiter")); //$NON-NLS-1$

    XPathContextExtender xpathContext = XPathContextExtender.getInstance(context);

    if (varName != null && context.hasVariable(varName))
    {
      savedVarValue = context.getVariable(varName);
    }

    final Object resultObject = xpathContext.resolveAsObject(xpathContext.currentXPathContextObject(), selectXPath);
    if (resultObject instanceof Number)
    {
      nodeSetIteration = false;
      maxIterations = ((Number)resultObject).longValue();
    }
    else if (resultObject instanceof NodeSet)
    {
      nodeSetIteration = true;
      selectedObjects = ((NodeSet)resultObject).toArray();
      maxIterations = selectedObjects.length;
    }
    else
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.IterateTag_CannotIterateOnResult, new Object []{ selectXPath }));
    }
    currentIndex = -1;
  }

  private boolean updateContext(JET2Context context)
  {
    if(updateContext == null) {
      if(!context.hasVariable(UPDATE_CONTEXT_VAR)) {
        updateContext = Boolean.FALSE;
      } else {
        updateContext = Boolean.valueOf(XPathUtil.xpathBoolean(context.getVariable(UPDATE_CONTEXT_VAR)));
      }
    }
    return updateContext.booleanValue();
  }
}
