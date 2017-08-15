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
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.exception.JmrException;


/**
 * Define characteristics of a JET2 emptyTag. An emptyTag:
 * <bl>
 * <li>Has no content (i.e. &lt;mytag&gt;...&lt;/mytag&gt; is an error)</li>
 * <li>Has a single method {@link #doAction(TagInfo, JET2Context, JET2Writer)} 
 * which performs the tag action</li>
 * </bl>
 *
 */
public interface EmptyTag extends CustomTag
{

  /**
   * Perform the action for the empty tag.
   * @param td the tag information (attribute values, etc)
   * @param context the JET2 execution context
   * @param out the current output writer.
   * @throws JmrException 
   * @throws JET2TagException if an error occurs
   *
   */
  public abstract void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JmrException;

}
