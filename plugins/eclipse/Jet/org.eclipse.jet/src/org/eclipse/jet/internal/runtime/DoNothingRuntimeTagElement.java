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

package org.eclipse.jet.internal.runtime;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.RuntimeTagElement;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement a "Do Nothing" tag that is used as a serrogate for a misbehaving or missing tag element.
 * The result of evaluating this tag is the same as if the open/close tags had been omitted from
 * the template entirely. That is, this tag element behaves in the following fashion
 * <bl>
 * <li>{@link #doStart(JET2Context, JET2Writer)} creates no content.</li>
 * <li>{@link #okToProcessBody()} returns <code>true</code> exactly once.</li>
 * <li>{@link #handleBodyContent(JET2Writer)} (which is called only once if there is body content) 
 * rewrites its body context verbatim.</li>
 * <li>{@link #doEnd()} creates no content.</li>  
 * </bl>
 * 
 */
public final class DoNothingRuntimeTagElement implements RuntimeTagElement
{

  private boolean executedOnce;

  private TagInfo tagInfo;

  private JET2Context context;

  private JET2Writer out;

  /**
   * 
   */
  public DoNothingRuntimeTagElement()
  {
    super();
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#doStart(org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doStart(JET2Context initContext, JET2Writer initOut)
  {
    this.context = initContext;
    this.out = initOut;
    this.executedOnce = false;
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#doEnd()
   */
  public void doEnd()
  {
    // nothing to do
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    boolean okToProcess = !executedOnce;
    executedOnce = true;
    return okToProcess;
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#handleBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void handleBodyContent(JET2Writer bodyContent)
  {
    getWriter().write(bodyContent);
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#setRuntimeParent(org.eclipse.jet.taglib.RuntimeTagElement)
   */
  public void setRuntimeParent(RuntimeTagElement parentTag)
  {
    // do nothing
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#setTagInfo(org.eclipse.jet.taglib.TagInfo)
   */
  public void setTagInfo(TagInfo tagInfo)
  {
    this.tagInfo = tagInfo;
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getTagInfo()
   */
  public TagInfo getTagInfo()
  {
    return tagInfo;
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getContext()
   */
  public JET2Context getContext()
  {
    return context;
  }

  /**
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getWriter()
   */
  public JET2Writer getWriter()
  {
    return out;
  }

}
