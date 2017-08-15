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


import java.util.Iterator;

import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.AbstractIteratingTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the JET Standard Control Tag 'visitor'.
 */
public class VisitorTag extends AbstractIteratingTag
{

  private String varName;

  private Iterator loopIterator;

  private Object next;

  /**
   * 
   */
  public VisitorTag()
  {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.taglib.IteratingTag#doEvalLoopCondition(org.eclipse.jet.taglib.TagInfo,
   *      org.eclipse.jet.JET2Context)
   */
  public boolean doEvalLoopCondition(TagInfo td, JET2Context context) throws JET2TagException
  {
    context.removeVariable(varName);

    boolean doAgain = loopIterator.hasNext();
    if (doAgain)
    {
      next = loopIterator.next();
      context.setVariable(varName, next);
    }

    return doAgain;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.taglib.IteratingTag#doInitializeLoop(org.eclipse.jet.taglib.TagInfo,
   *      org.eclipse.jet.JET2Context)
   */
  public void doInitializeLoop(TagInfo td, JET2Context context) throws JET2TagException
  {

    String xpathSelect = getAttribute("select"); //$NON-NLS-1$
    varName = getAttribute("var"); //$NON-NLS-1$

    XPathContextExtender xpathExtender = XPathContextExtender.getInstance(context);
    Object root = xpathExtender.resolveSingle(xpathExtender.currentXPathContextObject(), xpathSelect);

    // hack alert...
    // Should be xpathExtender.resolve(root, "descendant-or-self::*")
    if (root instanceof EObject)
    {
      loopIterator = new AbstractTreeIterator(root, true)
        {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;

          protected Iterator getChildren(Object obj)
          {
            return ((EObject)obj).eContents().iterator();
          }
        };
    }

  }

  public final Object getNext()
  {
    return next;
  }

}
