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
 * Interface to a container tag. A container has the following characteristics:
 * <bl>
 * <li>The tag must be of the form &lt;mycontainer&gt>...<&lt;/mycontiner&gt;.</li>
 * <li>The tag may perform an action before its content is processed (via {@link #doBeforeBody(TagInfo, JET2Context, JET2Writer)})</li>
 * <li>If the tag is a content re-writing tag (as declared in the tag extension point), then
 * {@link #setBodyContent(JET2Writer)} is called after {@link #doBeforeBody(TagInfo, JET2Context, JET2Writer)}.</li>
 * <li>The tag may perform an action after its content is processed (via {@link #doAfterBody(TagInfo, JET2Context, JET2Writer)}).</li>
 * </bl>
 * Tag processing proceeds as follows:
 * <bl>
 * <li>Tag set*() functions are called, as described by {@link CustomTag}.</li>
 * <li>The method {@link #doBeforeBody(TagInfo, JET2Context, JET2Writer)} is called immediately before starting processing
 * of the tag's content</li>
 * <li>The tags content's are processed.</li>
 * <li>If the tag is declared as a content rewriter in the tagLibaries extension point, then
 * {@link #setBodyContent(JET2Writer)} is called.</li>
 * <li>The method {@link #doAfterBody(TagInfo, JET2Context, JET2Writer)} is called.
 * </bl>
 *
 */
public interface ContainerTag extends CustomTag, EmptyTag
{

  /**
   * Perform processing before the tags body is processed.
   * @param td
   * @param context
   * @param out
   * @throws JET2TagException
   */
  public abstract void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException;

  /**
   * Perform processing after the tag body is processed.
   * @param td
   * @param context
   * @param out
   * @throws JET2TagException
   */
  public abstract void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException;

  /**
   * Passes the tag handler a writer containing the processed contents of the tag body.
   * Called if and only the tag is declared as a content re-writer.
   * @param bodyContent
   */
  public abstract void setBodyContent(JET2Writer bodyContent);
  
  /**
   * Perform processing end.
   * @param td
   * @param context
   * @param out
   * @throws JET2TagException
   */
  public abstract void doEnd(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException;


}
