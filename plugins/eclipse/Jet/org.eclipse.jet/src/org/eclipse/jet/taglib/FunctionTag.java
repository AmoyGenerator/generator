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
 * Define a JET2 function tag. A function tag has the following characteristics:
 * <bl>
 * <li>The tag must be of the form open tag (&lt;myfunc&gt;) 
 * and close tag(&lt;/myfunc&gt;).</li>
 * <li>The tag rewrites its contents by evalating a 
 * function (via {@link #doFunction(TagInfo, JET2Context, String)}).</li>
 * </bl>
 *
 */
public interface FunctionTag extends CustomTag
{

  /**
   * Calculate the re-written tag content.
   * @param td the tag data (attribute values, ...)
   * @param context the JET2 execution context.
   * @param bodyContent the body content to be re-written.
   * @return the re-written body content
   * @throws JET2TagException if the method cannot complete successfully
   */
  public abstract String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException;

}
