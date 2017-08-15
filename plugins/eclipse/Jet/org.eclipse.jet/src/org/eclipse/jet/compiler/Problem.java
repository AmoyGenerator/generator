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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet.compiler;


import java.net.URI;

import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.internal.parser.ParseProblemSeverity;

/**
 * Represent a compilation problem on a JET2 tempalte.
 * <p>
 * This class is not intended to be extended by clients
 * </p>
 * @since 0.7.0
 * @deprecated Since 0.8.0, use {@link org.eclipse.jet.core.parser.ast.Problem}
 */
public final class Problem
{

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

  private final org.eclipse.jet.core.parser.ast.Problem delegate;

  /**
   * Create an new instance
   * @param originatingFileName the file name from which the problem originates
   * @param severity the severity
   * @param id the problem id. See static constants declared on this class
   * @param message the error message in {@link java.text.MessageFormat} style
   * @param messageArgs the error message arguments
   * @param start the start offset of the problem (doc relative)
   * @param end the end offset of the problem (doc relative)
   * @param lineNumber the start line of the problem
   * @param colOffset TODO
   * @deprecated Use {@link #Problem(URI,String,ProblemSeverity,int,String,Object[],int,int,int,int)} instead
   */
  public Problem(
    String originatingFileName,
    ParseProblemSeverity severity,
    int id,
    String message,
    Object[] messageArgs,
    int start,
    int end,
    int lineNumber, 
    int colOffset)
  {
    this(
      TemplatePathUtil.baseLocationURI(originatingFileName),
      TemplatePathUtil.templatePath(originatingFileName),
      severity == ParseProblemSeverity.ERROR ? ProblemSeverity.ERROR : ProblemSeverity.WARNING,
      id,
      message,
      messageArgs,
      start,
      end,
      lineNumber,
      colOffset);
  }

  /**
   * Create an new instance
   * @param baseLocation the base location from which the template path is resolved.
   * @param templatePath the file name from which the problem originates
   * @param severity the problemSeverity
   * @param id the problem id. See static constants declared on this class
   * @param message the error message in {@link java.text.MessageFormat} style
   * @param messageArgs the error message arguments
   * @param start the start offset of the problem (doc relative)
   * @param end the end offset of the problem (doc relative)
   * @param lineNumber the start line of the problem
   * @param colOffset TODO
   */
  public Problem(
    URI baseLocation,
    String templatePath,
    ProblemSeverity severity,
    int id,
    String message,
    Object[] messageArgs,
    int start,
    int end, 
    int lineNumber, int colOffset)
  {
    this(new org.eclipse.jet.core.parser.ast.Problem(baseLocation, templatePath, severity, id, message, messageArgs, start, end, lineNumber, colOffset));
  }

  Problem(org.eclipse.jet.core.parser.ast.Problem problem)
  {
    this.delegate = problem;
    
  }

  /**
   * @return Returns the end.
   */
  public int getEnd()
  {
    return delegate.getEnd();
  }

  /**
   * @return Returns the id.
   */
  public int getId()
  {
    return delegate.getId();
  }

  /**
   * @return Returns the lineNumber.
   */
  public int getLineNumber()
  {
    return delegate.getLineNumber();
  }

  /**
   * @return Returns the message.
   */
  public String getMessage()
  {
    return delegate.getMessage();
  }

  /**
   * @return Returns the messageArgs.
   */
  public Object[] getMessageArgs()
  {
    return delegate.getMessageArgs();
  }

  /**
   * @return Returns the originatingFileName.
   */
  public String getOriginatingFileName()
  {
    return delegate.getOriginatingFileName();
  }

  /**
   * @return Returns the problemSeverity.
   * @deprecated Use {@link #getProblemSeverity()} instead.
   */
  public ParseProblemSeverity getSeverity()
  {
    ProblemSeverity problemSeverity = delegate.getProblemSeverity();
    if(problemSeverity == ProblemSeverity.ERROR) 
    {
      return ParseProblemSeverity.ERROR;
    }
    else if(problemSeverity == ProblemSeverity.WARNING) 
    {
      return ParseProblemSeverity.WARNING;
    }
    else
    {
      throw new IllegalStateException();
    }
  }
  
  

  /**
   * @return Returns the start.
   */
  public int getStart()
  {
    return delegate.getStart();
  }

  /**
   * @return Returns the colOffset.
   */
  public final int getColOffset()
  {
    return delegate.getColOffset();
  }


  /**
   * Return the problem severity
   * @return the problem severity
   * @since 0.8.0
   */
  public ProblemSeverity getProblemSeverity()
  {
    return delegate.getProblemSeverity();
  }

}
