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


/**
 * Represent a fully generate tag implementation. Clients are urged to implement one of the
 * specific tag implementations instead.
 *
 */
public interface OtherTag extends CustomTag
{

  /**
   * Perform actions required after the tag body has been evaluted.
   * @param tc
   * @param context
   * @param out
   * @throws JET2TagException
   */
  public abstract void doEnd(TagInfo tc, JET2Context context, JET2Writer out) throws JET2TagException;

  /**
   * Perform actions required prior to the tag body evaluation.
   * @param tc
   * @param context
   * @param out
   * @throws JET2TagException
   */
  public abstract void doStart(TagInfo tc, JET2Context context, JET2Writer out) throws JET2TagException;

  /**
   * Re-write the tag body.
   * @param tc
   * @param context
   * @param out
   * @param bodyContent
   * @throws JET2TagException
   */
  public abstract void handleBodyContent(TagInfo tc, JET2Context context, JET2Writer out, JET2Writer bodyContent) throws JET2TagException;

  /**
   * Test whether the tags body should be processed.
   * @param tc
   * @param context
   * @return <code>true</code> if the body is to be processed
   * @throws JET2TagException
   */
  public abstract boolean okToProcessBody(TagInfo tc, JET2Context context) throws JET2TagException;

}
