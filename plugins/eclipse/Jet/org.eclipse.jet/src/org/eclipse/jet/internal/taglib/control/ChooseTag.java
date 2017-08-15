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


import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the JET2 standard tag 'choose'.
 *
 */
public class ChooseTag extends AbstractContainerTag
{

  private boolean satisfied;
  private JET2Writer selectionWriter;
  private Object selectObject = null;

  /**
   * 
   */
  public ChooseTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doStart(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String select = getAttribute("select"); //$NON-NLS-1$
    if(select != null) {
      final XPathContextExtender xce = XPathContextExtender.getInstance(context);
      selectObject = xce.resolveAsObject(xce.currentXPathContextObject(), select);
    }
    
    satisfied = false;
    this.selectionWriter = new BodyContentWriter();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doEnd(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // nothing to do
  }

  /**
   * Test whether one of the 'choose' conditions has been satisfied.
   * @return <code>true</code> if the tag has been satisfied.
   */
  public final boolean isSatisfied()
  {
    return satisfied;
  }

  /**
   * Indicate whether a 'choose' condition has been statisfied.
   * @param satisfied <code>true</code> to indicate a condition has been statisfied.
   */
  public final void setSatisfied(boolean satisfied)
  {
    this.satisfied = satisfied;
  }

  public void setBodyContent(JET2Writer bodyContent)
  {
    // ignore the direct body content of the tag, and instead, write what is in the selectionWriter,
    // which has been written to by one of the child 'when' or 'otherwise' tags.
    getOut().write(selectionWriter);
  }

  public void writeSelection(JET2Writer bodyContent)
  {
    selectionWriter.write(bodyContent);
  }

  /**
   * @return Returns the selectObject.
   */
  protected final Object getSelectObject()
  {
    return selectObject;
  }
}
