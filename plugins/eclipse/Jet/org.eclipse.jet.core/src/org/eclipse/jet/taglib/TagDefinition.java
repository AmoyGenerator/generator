/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
 * $Id: TagDefinition.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;


import java.util.List;


/**
 * Expose the definition of a tag, as declared in a 'org.eclipse.jet.tagLibraries' extension.
 * <P>
 * This interface is not intended to be implemented by clients.
 * </P> 
 */
public interface TagDefinition
{

  /**
   * Return the name of the tag as it is registered in the tag library
   * @return the tag's name
   */
  public abstract String getName();

  public abstract String getDescription();

  /**
   * Return the tag kind ({@link CustomTagKind}).
   * @return the tag kind.
   */
  public abstract CustomTagKind getKind();

  /**
   * Return the definition of the named attribute
   * @param name the attribute name
   * @return the attribute definition, or <code>null</code> if the <code>name</code> is not
   * an attribute of the named tag.
   */
  public abstract TagAttributeDefinition getAttributeDefinition(String name);

  /**
   * Return a list of attribute definitions for this tag
   * @return a List of {@link TagAttributeDefinition} objects. The empty list is returned if there
   * are no attribute definitions for this tag definition.
   */
  public abstract List getAttributeDefinitions();

  /**
   * Test if the tag is declared to be deprecated.
   * @return <code>true</code> if the tag is deprecated.
   */
  public abstract boolean isDeprecated();

  /**
   * Test whether the tag requires a new writer for its contents.
   * The following tag declaration will have the value set to <code>true</code>:
   * <bl>
   * <li><code>functionTag</code></li>
   * <li><code>containerTag</code> with <code>processContents</code> set to <code>custom</code>.</li>
   * <li><code>conditionalTag</code> with <code>processContents</code> set to <code>custom</code>.</li>
   * <li><code>iteratingTag</code> with <code>processContents</code> set to <code>custom</code>.</li>
   * </bl>
   * @return <code>true</code> if a new writer is required, <code>false</code> otherwise.
   */
  public abstract boolean requiresNewWriter();

  /**
   * Test whether the tag is allowed to be specified in the empty tag form: &lt;tagName/&gt;.
   * The following tag declarations will have the value set to <code>true</code>.
   * <bl>
   * <li><code>emptyTag</code></li>
   * <li><code>containerTag</code> with <code>allowAsEmpty</code> set to <code>true</code>.</li>
   * </bl>
   * @return <code>true</code> if the tag may be expressed as a empty tag.
   */
  public abstract boolean isEmptyTagAllowed();

  /**
   * Test whether the tag is allowed to have content (even empty content). That is, this
   * method tests whether a tag of the form: 
   * <blockquote> 
   * &lt;tagName&gt; ... &lt;/tagName&gt;
   * </blockquote>
   * The following tag declarations will have the value set to <code>true</code>:
   * <bl>
   * <li><code>functionTag</code></li>
   * <li><code>containerTag</code></li>
   * <li><code>conditionalTag</code></li>
   * <li><code>iteratingTag</code></li>
   * </bl>
   * @return <code>true</code> if the tag may be expressed as a content tag.
   */
  public abstract boolean isContentAllowed();

  /**
   * Return the {@link TagLibrary} that contains this tag definition.
   * @return a TagLibrary instance.
   */
  public abstract TagLibrary getTagLibrary();
  
  /**
   * Indicate whether the compiler should remove whitespace including the trailing
   * new line from tags that occur on an otherwise empty line.
   * @return <code>true</code> if such whitespace should be removed.
   */
  public abstract boolean removeWhenContainingLineIsEmpty();
}
