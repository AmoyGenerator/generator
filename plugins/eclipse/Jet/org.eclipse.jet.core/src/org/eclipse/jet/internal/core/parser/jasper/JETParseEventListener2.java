/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;


import java.util.Map;

import org.eclipse.jet.core.parser.ProblemSeverity;


public interface JETParseEventListener2 extends JETParseEventListener
{

  void handleComment(JETMark start, JETMark stop) throws JETException;

  void handleDeclaration(JETMark start, JETMark stop) throws JETException;

  void handleXMLEndTag(String tagName, JETMark start, JETMark stop) throws JETException;

  void handleXMLEmptyTag(String tagName, JETMark start, JETMark stop, Map attributeMap) throws JETException;

  void handleXMLStartTag(String tagName, JETMark start, JETMark stop, Map attributeMap) throws JETException;
  
  void handleEmbeddedExpression(String language, JETMark start, JETMark stop) throws JETException;

  //	void handleText(JETMark start, JETMark end);

  boolean isKnownTag(String tagName);

  boolean isKnownInvalidTagName(String tagName);
  /**
   * Record a parsing problem.
   * @param severity
   * @param problemId
   * @param message
   * @param msgArgs
   * @param start
   * @param end
   * @param line
   * @param colOffset TODO
   */
  void recordProblem(ProblemSeverity severity, int problemId, String message, Object[] msgArgs, int start, int end, int line, int colOffset);

}
