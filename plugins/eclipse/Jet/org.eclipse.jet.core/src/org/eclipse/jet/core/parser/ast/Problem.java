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
 * $Id: Problem.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

import java.net.URI;
import java.text.MessageFormat;

import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ProblemSeverity;

/**
 * Represent a compilation problem on a JET2 tempalte.
 * <p>
 * This class is not intended to be extended by clients
 * </p>
 * @since 0.8.0
 */
public final class Problem {

	/**
	 * Error Id for an XML end tag that has no corresponding start tag.
	 * @see #getId()
	 * @deprecated Use {@link IProblem#MissingXmlStartTag}
	 */
	public static final int MissingXmlStartTag = IProblem.MissingXmlStartTag;

	/**
	 * Error Id for an XML start tag that has no corresponding end tag.
	 * @see #getId()
	 * @deprecated Use {@link IProblem#MissingXmlEndTag}
	 */
	public static final int MissingXmlEndTag = IProblem.MissingXmlEndTag;

	/**
	 * Error Id for an XML tag or JET directive that is missing a required attribute
	 * @see #getId()
	 * @deprecated Use {@link IProblem#MissingRequiredAttribute}
	 */
	public static final int MissingRequiredAttribute = IProblem.MissingRequiredAttribute;

	/**
	 * Error Id for taglib directive that defines a prefix defined by a preceding taglib directive
	 * @see #getId()
	 * @deprecated Use {@link IProblem#DuplicateXMLNamespacePrefix}
	 */
	public static final int DuplicateXMLNamespacePrefix = IProblem.DuplicateXMLNamespacePrefix;

	/**
	 * Error Id for taglib directive that defines references an unknown tag library id
	 * @see #getId()
	 * @deprecated Use {@link IProblem#UnknownTagLibrary}
	 */
	public static final int UnknownTagLibrary = IProblem.UnknownTagLibrary;

	/**
	 * Error Id for an attribute that is not defined in the tag definition
	 * @deprecated Use {@link IProblem#UnknownAttributeInTag}
	 */
	public static final int UnknownAttributeInTag = IProblem.UnknownAttributeInTag;

	/**
	 * Represent an unterminated XML Tag
	 * @deprecated Use {@link IProblem#UnterminatedXMLTag}
	 */
	public static final int UnterminatedXMLTag = IProblem.UnterminatedXMLTag;

	/**
	 * Represent a duplicate attribute in an XML Tag;
	 * @deprecated Use {@link IProblem#DuplicateAttribute}
	 */
	public static final int DuplicateAttribute = IProblem.DuplicateAttribute;

	/**
	 * An underlying JETException was thrown by the JET parser
	 * @deprecated Use {@link IProblem#JETException}
	 */
	public static final int JETException = IProblem.JETException;

	/**
	 * Two templates specify that they compile to the same Java Class
	 * @deprecated Use {@link IProblem#MultipleTemplatesWithSameJavaClass}
	 */
	public static final int MultipleTemplatesWithSameJavaClass = IProblem.MultipleTemplatesWithSameJavaClass;

	/**
	 * Use of an attribute that has been deprecated.
	 * @deprecated Use {@link IProblem#DeprecatedAttribute}
	 */
	public static final int DeprecatedAttribute = IProblem.DeprecatedAttribute;

	/**
	 * Tag may not have a body - the tag must be of the form &lt;tagName/&gt;.
	 * @deprecated Use {@link IProblem#TagCannotHaveContent}
	 */
	public static final int TagCannotHaveContent = IProblem.TagCannotHaveContent;

	/**
	 * Tag must have content - the tag must be of the form &lt;tagName&gt;xxx&lt;/tagName&gt;.
	 * @deprecated Use {@link IProblem#TagCannotBeEmpty}
	 */
	public static final int TagCannotBeEmpty = IProblem.TagCannotBeEmpty;

	/**
	 * Use of the tag has been deprecated.
	 * @deprecated Use {@link IProblem#DeprecatedTag}
	 */
	public static final int DeprecatedTag = IProblem.DeprecatedTag;

	/**
	 * Unsupported Directive.
	 * @deprecated Use {@link IProblem#UnsupportedDirective}
	 */
	public static final int UnsupportedDirective = IProblem.UnsupportedDirective;

	/**
	 * A tag that has a known tag library prefix, but is not a recognized name. Usually
	 * indicates a typographical error.
	 * @deprecated Use {@link IProblem#UnknownXMLTag}
	 */
	public static final int UnknownXMLTag = IProblem.UnknownXMLTag;

	/**
	 * A tag this is declared as an 'emptyTag' occured as &lt;tag ...&gt;, and has been
	 * interpreted as the equivalent empty tag &lt;tag .../&gt;.
	 * @deprecated Use {@link IProblem#TagInterpretedAsEmptyTag}
	 */
	public static final int TagInterpretedAsEmptyTag = IProblem.TagInterpretedAsEmptyTag;

	private final String originatingFileName;

	private final int id;

	private final String message;

	private final Object[] messageArgs;

	private final int start;

	private final int end;

	private final int lineNumber;

	private final ProblemSeverity problemSeverity;

	private final int colOffset;

	/**
	 * Create an new instance
	 * @param baseLocation the base location from which the template path is resolved.
	 * @param templatePath the file name from which the problem originates
	 * @param severity the problem severity
	 * @param id the problem id. See static constants declared on this class
	 * @param message the error message in {@link MessageFormat} style
	 * @param messageArgs the error message arguments
	 * @param start the start offset of the problem (doc relative)
	 * @param end the end offset of the problem (doc relative)
	 * @param lineNumber the start line of the problem
	 * @param colOffset the column position (one based) of the problem start
	 */
	public Problem(URI baseLocation, String templatePath,
			ProblemSeverity severity, int id, String message,
			Object[] messageArgs, int start, int end, int lineNumber,
			int colOffset) {
		super();
		this.originatingFileName = templatePath;
		this.problemSeverity = severity;
		this.id = id;
		this.colOffset = colOffset;
		this.message = messageArgs != null && messageArgs.length > 0 ? MessageFormat
				.format(message, messageArgs)
				: message;
		this.messageArgs = messageArgs;
		this.start = start;
		this.end = end;
		this.lineNumber = lineNumber;
	}

	/**
	 * @return Returns the end.
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Returns the lineNumber.
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return Returns the messageArgs.
	 */
	public Object[] getMessageArgs() {
		return messageArgs;
	}

	/**
	 * @return Returns the originatingFileName.
	 */
	public String getOriginatingFileName() {
		return originatingFileName;
	}

	/**
	 * @return Returns the start.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return Returns the colOffset.
	 */
	public final int getColOffset() {
		return colOffset;
	}

	/**
	 * Return the problem severity
	 * @return the problem severity
	 * @since 0.8.0
	 */
	public ProblemSeverity getProblemSeverity() {
		return problemSeverity;
	}

}
