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
package org.eclipse.jet.taglib;


import org.eclipse.jet.JET2Context;


/**
 * Define the behavior of a JET2 iterating tag. An iterating tag:
 * <bl>
 * <li>Must be of the form an open-tag (&lt;myiterate&gt;) and a
 * close-tag (&lt;/myiterate&gt;).</li>
 * <li>Control the number of times the body content is written via the
 * methods {@link #doInitializeLoop(TagInfo, JET2Context)} and
 * {@link #doEvalLoopCondition(TagInfo, JET2Context)}.</li>
 * </bl>
 * Methods are called in the following sequence:
 * <bl>
 * <li>The set*() methods as defined by {@link CustomTag} to initialize the tag</li>
 * <li>{@link #doInitializeLoop(TagInfo, JET2Context)} is called to allow the tag to determine the loop data.<li>
 * <li>{@link #doEvalLoopCondition(TagInfo, JET2Context)} is called multiple times to traverse the loop data.<li>
 * <li>Each time  {@link #doEvalLoopCondition(TagInfo, JET2Context)} returns <code>true</code>, then the body processing
 * methods are called as described in {@link ContainerTag}.</li>
 * </bl>
 *
 */
public interface IteratingTag extends ContainerTag
{

  /**
   * Determine whether the tag should do another iteration, and, if so,
   * setup any data for the iteration. This method is called one more more times
   * after the call to {@link #doInitializeLoop(TagInfo, JET2Context)}.
   * @param td the tag information (attribute values, etc)
   * @param context the JET2 execution context.
   * @return <code>true</code> if another iteration is to be performed,
   * <code>false</code> otherwise.
   * @throws JET2TagException if the method cannot complete successfully. Note
   * that this will terminate the tag loop.
   */
  public abstract boolean doEvalLoopCondition(TagInfo td, JET2Context context) throws JET2TagException;

  /**
   * Initialize any data required to determine how many times the
   * tag should iterate.
   * This method is called once, and is called prior to any calls to {@link #doEvalLoopCondition(TagInfo, JET2Context)}.
   * @param td the tag data (attribute values, etc)
   * @param context the JET2 exectuion context
   * @throws JET2TagException if the method cannot execute successfully.
   */
  public abstract void doInitializeLoop(TagInfo td, JET2Context context) throws JET2TagException;

}
