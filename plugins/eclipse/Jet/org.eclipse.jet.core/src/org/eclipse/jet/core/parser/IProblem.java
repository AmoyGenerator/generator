/*
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.core.parser;

/** 
 * Describe a parsing problem
 * 
 * This interface is not intended to be implemented by clients
 */
public interface IProblem {
	  /**
	   * Error Id for an XML end tag that has no corresponding start tag.
	   */
	  public static final int MissingXmlStartTag = 1;

	  /**
	   * Error Id for an XML start tag that has no corresponding end tag.
	   */
	  public static final int MissingXmlEndTag = 2;

	  /**
	   * Error Id for an XML tag or JET directive that is missing a required attribute
	   */
	  public static final int MissingRequiredAttribute = 3;

	  /**
	   * Error Id for taglib directive that defines a prefix defined by a preceding taglib directive
	   */
	  public static final int DuplicateXMLNamespacePrefix = 4;

	  /**
	   * Error Id for taglib directive that defines references an unknown tag library id
	   */
	  public static final int UnknownTagLibrary = 5;

	  /**
	   * Error Id for an attribute that is not defined in the tag definition
	   */
	  public static final int UnknownAttributeInTag = 6;

	  /**
	   * Represent an unterminated XML Tag
	   */
	  public static final int UnterminatedXMLTag = 7;

	  /**
	   * Represent a duplicate attribute in an XML Tag;
	   */
	  public static final int DuplicateAttribute = 8;

	  /**
	   * An underlying JETException was thrown by the JET parser
	   */
	  public static final int JETException = 9;

	  /**
	   * Two templates specify that they compile to the same Java Class
	   */
	  public static final int MultipleTemplatesWithSameJavaClass = 10;

	  /**
	   * Use of an attribute that has been deprecated.
	   */
	  public static final int DeprecatedAttribute = 11;

	  /**
	   * Tag may not have a body - the tag must be of the form &lt;tagName/&gt;.
	   */
	  public static final int TagCannotHaveContent = 12;

	  /**
	   * Tag must have content - the tag must be of the form &lt;tagName&gt;xxx&lt;/tagName&gt;.
	   */
	  public static final int TagCannotBeEmpty = 13;

	  /**
	   * Use of the tag has been deprecated.
	   */
	  public static final int DeprecatedTag = 14;

	  /**
	   * Unsupported Directive.
	   */
	  public static final int UnsupportedDirective = 15;

	  /**
	   * A tag that has a known tag library prefix, but is not a recognized name. Usually
	   * indicates a typographical error.
	   */
	  public static final int UnknownXMLTag = 16;

	  /**
	   * A tag this is declared as an 'emptyTag' occured as &lt;tag ...&gt;, and has been
	   * interpreted as the equivalent empty tag &lt;tag .../&gt;.
	   */
	  public static final int TagInterpretedAsEmptyTag = 17;

	  /**
	   * An %&#064;include directive could not resolve a referenced file.
	   */
	  public static final int MissingFile = 18;

	  /**
	   * A &lt;%%&#064; start %&gt; directive was found other than after a &lt;%%&#064; include fail="alternative" %&gt; directive.
	   */
	  public static final int StartDirectiveOutOfContext = 19;

	  /**
	   * A &lt;%%&#064; end %&gt; directive was found other than after a &lt;%%&#064; include fail="alternative" %&gt; and &lt;%%&#064; start %&gt; directive.
	   */
	  public static final int EndDirectiveOutOfContext = 20;

	  /**
	   * An &lt;%%&#064; include fail="alternative" %&gt; directive has a &lt;%%&#064; start %&gt; directive, but no &lt;%%&#064; end %&gt; directive.
	   */
	  public static final int MissingEndDirective = 21;

	  /**
	   * An &lt;%%&#064; include fail="alternative" %&gt; directive has not &lt;%%&#064; start %&gt; and &lt;%%&#064; end %&gt; directives.
	   */
	  public static final int MissingIncludeAlternative = 22;

}