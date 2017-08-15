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
package org.eclipse.jet.internal.core.parser.jasper;

import java.text.MessageFormat;

import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ProblemSeverity;

/**
 * Utility class for formatting messages.
 *
 */
public class MessagesUtil {
	
	private MessagesUtil() {
		// do nothing
	}

	public static String getString(String key, Object[] args) {
		return MessageFormat.format(Messages.getString(key), args);
	}

	/**
	 * @param reader
	 * @param close
	 * @param open
	 */
	public static String getUnterminatedMessage(JETReader reader, String close, String open) {
	    final String msg = MessagesUtil.getString(
				"jet.error.unterminated", //$NON-NLS-1$
				new Object[] { open, reader.mark().toString() });
		return msg;
	}

	  public static void recordUnterminatedElement(
			    JETParseEventListener2 listener,
			    String expectedCloseSequence,
			    JETMark elementStart,
			    JETReader reader)
			  {
			    listener.recordProblem(
			      ProblemSeverity.ERROR,
			      IProblem.UnterminatedXMLTag,
			      Messages.getString("jet.error.unterminated"), //$NON-NLS-1$
			      new Object []{ expectedCloseSequence },
			      elementStart.getCursor(),
			      reader.mark().getCursor(),
			      elementStart.getLine(),
			      elementStart.getCol());

			  }

}
