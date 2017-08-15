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


/**
 * An enumeration of the deferent kinds of tags.
 *
 */
public final class CustomTagKind
{

  /**
   * A tag declared in the tagLibrary extension using the 'tag' element.
   */
  public static final CustomTagKind OTHER = new CustomTagKind("OTHER"); //$NON-NLS-1$

  /**
   * A tag declared in the tagLibrary extension using the 'emptyTag' element. 
   * Empty tags must take the form of an XML empty tag (&lt;<i>tagName</i> ... /&gt;), and
   * create any content or perform any actions based solely on the elements context and attribute values.
   */
  public static final CustomTagKind EMPTY = new CustomTagKind("EMPTY"); //$NON-NLS-1$

  /**
   * A tag declared in the tagLibrary extension using the 'functionTag' element.
   * Function tags must take the form of an XML begin/end tag pair (&lt;tagName ... &;gt><i>content</i>&lt;/tagName&gt;).
   * A function tag creates output by evaluating a function upon the elements content, its context and attribute values.
   */
  public static final CustomTagKind FUNCTION = new CustomTagKind("FUNCTION"); //$NON-NLS-1$

  /**
   * A tag declared in the tagLibrary extension using the 'iteratingTag' element.
   * Iterating tags must take the form of an XML begin/end tag pair.
   * An iterating tag creates output by repeating evaluating a condition based on the element's context and attributes.
   * Each time the condition evaluates to <code>true</code>, the elements contents are written to the output. 
   */
  public static final CustomTagKind ITERATING = new CustomTagKind("ITERATING"); //$NON-NLS-1$

  /**
   * A tag declared in the tagLibrary extension using the 'conditionalTag' element.
   * Conditional tags must take the form of an XML begin/end tag pair.
   * A conditional tag creates output by evaluating a condition based on the element's context and attributes.
   * If the condition evaluates to <code>true</code>, the elements contents are written to the output. 
   */
  public static final CustomTagKind CONDITIONAL = new CustomTagKind("CONDITIONAL"); //$NON-NLS-1$

  /**
   * A tag declared in the tagLibrary extension using the 'containerTag' element.
   * Container tags must tag the form of an XML begin/end tag pari.
   * A container tag preforms actions prior to its content being processed and
   * after its content has been processed.
   */
  public static final CustomTagKind CONTAINER = new CustomTagKind("CONTAINER"); //$NON-NLS-1$

  private final String display;

  private CustomTagKind(String display)
  {
    this.display = display;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return display;
  }

}