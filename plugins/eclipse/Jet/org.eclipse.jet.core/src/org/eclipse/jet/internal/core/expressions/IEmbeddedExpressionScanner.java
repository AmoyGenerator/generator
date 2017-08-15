/*
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.internal.core.expressions;

/**
 * Defines the protocal to a scanner employed by the JET template parser to determine
 * the end of an embedded expression. The scanner's primary roll is to tell the XPath parser
 * whether each given character should be considered as the terminating character of the embedded expression, of
 * whether it should be ignored. The prime reason for ignoring a character is because it is contained in a literal
 * string in the expression language. The scanner interface, however, allows for other reasons to ignore the character.
 * <p>
 * The scanner is informed of every character from the beginning of the embedded expresion, excluding the JET language's lead-in characters.
 * After each character, the scanner may be asked whether the JET parser should ignore that character when considering whether
 * to terminate the embedded expression. The scanner is never asked to backtrack.
 * </p>
 * <p>
 * Note that scanners typically do not need to parse the embedded expression. Typically, they only need to recognized the embedded
 * language's string literals.
 * </p>
 *
 */
public interface IEmbeddedExpressionScanner {

	/**
	 * Informs the scanner of the text character in the embedded expression
	 * @param ch the next character
	 */
	void setNextChar(int ch);

	/**
	 * Indicate whether the character passed by the last call to {@link #setNextChar(int)} should be
	 * ignored when searching for JET's embedded expression terminating character.
	 * @return <code>true</code> if the character should not be considered as the embedded expression terminator
	 */
	boolean ignoreChar();

}
