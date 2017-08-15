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
 * $Id: TextElement.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

import org.eclipse.jet.internal.core.parser.LineInfo;

/**
 * Define a Text Element in the JET AST
 * 
 * @since 0.8.0
 *
 */
public final class TextElement extends BodyElement {

	private static final String ESCAPED_JET_ELEMENT_START = "<\\%"; //$NON-NLS-1$

	private char[] text;

	private boolean trimLastLine = false;

	private boolean trimFirstLine = false;

	private final LineInfo[] lines;

	/**
	 * Create an instance
	 * @param jetast the AST
	 * @param text the text
	 */
	TextElement(JETAST jetast, char[] text) {
		super(jetast, -1, -1, 0, 0);
		this.text = handleEscapes(text);
		this.lines = LineInfo.calculateLines(this.text);
	}

	/**
	 * Remove any escape sequences in the raw text.
	 * The only escaped sequences handled are:
	 * <bl>
	 * <li>&lt;\% to &lt;%</li>
	 * </bl>
	 * @param originalText
	 * @return
	 */
	private char[] handleEscapes(char[] originalText) {
		StringBuffer buffer = new StringBuffer(originalText.length);
		buffer.append(originalText);
		for (int i = buffer.indexOf(ESCAPED_JET_ELEMENT_START); i != -1; i = buffer
				.indexOf(ESCAPED_JET_ELEMENT_START, i)) {
			buffer.replace(i, i + ESCAPED_JET_ELEMENT_START.length(), "<%"); //$NON-NLS-1$
		}
		return buffer.toString().toCharArray();
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected void accept0(JETASTVisitor visitor) {
	    visitor.visit(this);
	    visitor.endVisit(this);
	}

	/**
	 * Return the text content
	 * @return the text
	 */
	public char[] getText() {
		char[] result = text;
	    if (lines.length > 0) {
				int start = isTrimFirstLine() && lines[0].hasDelimiter()? lines[0]
						.getEnd()
						+ lines[0].getDelimiter().length() : 0;
				int end = isTrimLastLine()
						&& !lines[lines.length - 1].hasDelimiter() ? lines[lines.length - 1]
						.getStart()
						: text.length;
				result = new String(text, start, end - start).toCharArray();
		}
		
		return result;
	}

	public char[] getRawText() {
		return text;
	}

	/**
	 * 
	 * @param newText
	 * @deprecated
	 */
	void setText(char[] newText) {
		text = newText;
	}

	public boolean removeLineWhenOtherwiseEmpty() {
		return false;
	}

	public void setTrimLastLine(boolean trim) {
		this.trimLastLine = trim;
	}

	/**
	 * @return Returns the trimLastLine.
	 */
	public final boolean isTrimLastLine() {
		return trimLastLine;
	}

	public void setTrimFirstLine(boolean trim) {
		this.trimFirstLine = trim;
	}

	/**
	 * @return Returns the trimFirstLine.
	 */
	public final boolean isTrimFirstLine() {
		return trimFirstLine;
	}

	/**
	 * @return Returns the lines.
	 */
	public final LineInfo[] getLines() {
		return lines;
	}

}
