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
package org.eclipse.jet.internal.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for setting new line characters in strings 
 */
public class NewLineUtil {

	/**
	 * This utility knows about the following line delimiters:
	 * \r\n - windows
	 * \n - linux/unix
	 * \r - Mac OS 9
	 */
	public static final Pattern NEW_LINE_PATTERN = Pattern.compile("(\\r\\n|\\n|\\r)", Pattern.MULTILINE); //$NON-NLS-1$

	private NewLineUtil() {
		// do nothing
	}

	/**
	 * Set the new line in sourceText to the terminator supplied
	 * @param sourceText the source text
	 * @param lineTerminator the terminator to use
	 * @return the text with the supplied line terminator
	 */
	public static String setLineTerminator(CharSequence sourceText, String lineTerminator) {
		final Matcher m = NEW_LINE_PATTERN.matcher(sourceText);
		return m.replaceAll(lineTerminator);
	}

	/**
	 * Find the line terminator used by the passed text. The line terminator
	 * is the first valid line terminator found. If no line terminator is found then null is return.
	 * @param sourceText the source text
	 * @return the line terminator or <code>null</code> if no line terminator was found.
	 */
	public static String getLineTerminator(CharSequence sourceText) {
		final Matcher m = NEW_LINE_PATTERN.matcher(sourceText);
		return m.find() ? sourceText.subSequence(m.start(), m.end()).toString() : null;
	}
}
