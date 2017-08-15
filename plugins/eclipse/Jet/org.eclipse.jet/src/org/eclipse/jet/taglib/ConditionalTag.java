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
 * Interface to a conditional tag. A conditional custom tag:
 * <bl>
 * <li>requires a body (i.e., the form &lt;myConditionalTag/&gt; is an error).</li>
 * <li>includes or excludes its body content depending on the evaluation of some condition</li>
 * </bl>
 * Methods are called in the following order:
 * <bl>
 * <li>The set*() methods as defined by {@link CustomTag}.</li>
 * <li>{@link #doEvalCondition(TagInfo, JET2Context)}.<li>
 * <li>If {@link #doEvalCondition(TagInfo, JET2Context)} returns <code>true</code>, then the body processing
 * methods are called as described in {@link ContainerTag}.</li>
 * </bl>
 *
 */
public interface ConditionalTag extends ContainerTag
{

  /**
   * Evalutate the condition that determines whether the tag's body is to be written to the tags 
   * output writer. 
   * @param td the tag information (i.e. attribute values)
   * @param context the JET2 execution context
   * @return <code>true</code> if the body should be included, <code>false</code> otherwise.
   * @throws JET2TagException if the method cannot execute properly.
   */
  public abstract boolean doEvalCondition(TagInfo td, JET2Context context) throws JET2TagException;

}
