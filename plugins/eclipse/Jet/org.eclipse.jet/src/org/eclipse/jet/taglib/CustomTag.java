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

import java.util.List;
import java.util.Map;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
//import org.eclipse.jet.model.Group;
//import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.api.Group;
import org.eclipse.jet.model.util.ProposalType;


/**
 * Interface representing common characteristics of JET2 custom tags.
 * <p>
 * All custom tags have the same setup sequence. The following methods are always called to initialize a tag:
 * <bl>
 * <li>{@link #setParent(CustomTag)} specifying the containing tag, if any.</li>
 * <li>{@link #setTagInfo(TagInfo)} specifying the tag attribute values.</li>
 * <li>{@link #setContext(JET2Context)} specifying the JET execution context.</li>
 * <li>{@link #setOut(JET2Writer)} specifying the writer to which the tag should write.</li>
 * </bl>
 * <p>
 * <b>This interface is not intended to be directly implemented by clients</b>
 * </p>
 *
 */
public interface CustomTag
{

  /**
   * Set the custom tag representing the parent of this tag.
   * @param parent the parent tag, or <code>null</code> if the tag has none.
   */
  public abstract void setParent(CustomTag parent);

  /**
   * Return the parent tag.
   * @return the parent tag, or <code>null</code> if the tag has none.
   */
  public abstract CustomTag getParent();

  /**
   * Return the kind of the custom tag
   * @return the tag kind
   * @see CustomTagKind
   */
  public abstract CustomTagKind getKind();

  /**
   * Set the tag info for the tag. This is done by the compiler, and is not intended
   * to be called by clients.
   * @param td the tag info.
   */
  public abstract void setTagInfo(TagInfo td);

  /**
   * Set the context of the tag. This is done by the compiler, and is not intended
   * to be called by clients.
   * @param context the context.
   */
  public abstract void setContext(JET2Context context);

  /**
   * Return the 'raw' value of the named tag attribute.
   * @param name the tag attribute name.
   * @return the raw value or <code>null</code> if the attribute is not defined.
   */
  public abstract String getRawAttribute(String name);

  /**
   * Return the processed value of the tag attribute (with dynamic XPath expressions
   * already resolved.
   * @param name the tag attribute name.
   * @return the processed value of <code>null</code> if the attribute is not defined.
   * @throws JET2TagException if an error occurs while processing a dynmaic XPath expression.
   */
  public abstract String getAttribute(String name) throws JET2TagException;

  /**
   * Set the writer to which the tag will write.
   * @param out a non-null instance of JET2Writer.
   */
  public abstract void setOut(JET2Writer out);

  /**
   * Return the writer to which the tag will write.
   * @return the tag's writer.
   */
  public abstract JET2Writer getOut();

  /**
   * Return the proposal list
   * @param contextMap 
   * @param objectName 
   * @return
   */
  public abstract List<ProposalType> getProposals(Group group, Map<String, Object> contextMap, String attrName, String objectName);
  
  public abstract Map<String, Object> getContext(Map<String, Object> contextMap, Group group);
  
}
