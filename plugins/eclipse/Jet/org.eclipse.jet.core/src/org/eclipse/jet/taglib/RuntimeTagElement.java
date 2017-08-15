/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.taglib;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;


/**
 * Defines the execution behavior an XML Element in a JET2 compiled template.
 * <P>
 * Clients do not typically use the class directly.
 * </P>
 * <P>
 * The tag has the following life cycle:
 * <BL>
 * <LI>Tag constructor is called.</LI>
 * <LI>Parent tag is set via {@link #setRuntimeParent(RuntimeTagElement) setParent()}. If the tag has no parent, <code>null</code> is passed.</LI>
 * <li>The tag context is set via {@link #setTagInfo(TagInfo)}.</li>
 * <LI>The tag is readied for operation by a call to {@link #doStart(JET2Context, JET2Writer) doStart()}.</LI>
 * <LI>If the element has body content (that is, it is not an empty tag, but a begin-tag/end-tag pair), then
 * {@link #okToProcessBody() okToProcessBody()} is called.</LI>
 * <LI>If {@link #okToProcessBody() okToProcessBody()} returns <code>true</code>, then {@link #handleBodyContent(JET2Writer) handleBodyContent()} is called.</LI>
 * <LI>{@link #okToProcessBody() okToProcessBody()} and {@link #handleBodyContent(JET2Writer) handleBodyContent()} are called 
 * repeatedly until {@link #okToProcessBody() okToProcessBody()} returns <code>false</code>.</LI>
 * <LI>Once body processing is complete, {@link #doEnd() doEnd()} is called.</LI>
 * </BL>
 * </P>
 * <P>
 * This interface is not intended to be implemented by clients.
 * </P> 
 */
public interface RuntimeTagElement
{

  /**
   * Set the parent tag of the element. This method always called once, after construction, 
   * and before {@link #doStart(JET2Context, JET2Writer) ready()} is called.
   * @param parentTag the parent tag, or <code>null</code> if the tag has no parent in the template.
   */
  public abstract void setRuntimeParent(RuntimeTagElement parentTag);

  /**
   * Provide the tag with information on its context within the template, include attribute values
   * and position.
   * @param tagInfo the tag information. Will never by <code>null</code>.
   * @see TagInfo
   */
  public abstract void setTagInfo(TagInfo tagInfo);

  /**
   * Return the tag context set by {@link #setTagInfo(TagInfo)}.
   * @return the tag context, or <code>null</code> if not yet set.
   */
  public abstract TagInfo getTagInfo();

  /**
   * Perform any actions associated with the start of the tag element.
   * @param context
   * @param out
   */
  public abstract void doStart(JET2Context context, JET2Writer out);

  /**
   * Perform any actions associated with the end of the tag element.
   *
   */
  public abstract void doEnd();

  /**
   * Determine if the tag element's body should be processed.
   * If the tag element has a body (even an empty body), then this method is called repeatedly until <code>false</code>
   * is returned. Each time <code>true</code> is returned, {@link #handleBodyContent(JET2Writer)} is called.
   * 
   * @return <code>true</code> if the body should be processed, <code>false</code> otherwise.
   */
  public abstract boolean okToProcessBody();

  /**
   * Handle the body content of the tag element.
   * This method is called once everytime a call to {@link #okToProcessBody() okToProcessBody()} returns <code>true</code>.
   * @param bodyContent the writer containing the body content. Will never by <code>null</code>.
   */
  public abstract void handleBodyContent(JET2Writer bodyContent);

  /**
   * Return the context passed to {@link #doStart(JET2Context, JET2Writer)}.
   * @return the context
   */
  public abstract JET2Context getContext();

  /**
   * Return the writer passed to {@link #doStart(JET2Context, JET2Writer)}.
   * @return the current output writer
   */
  public abstract JET2Writer getWriter();
}
