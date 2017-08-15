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
 * $Id: Comment.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

/**
 * An JET AST element representing a comment
 *
 */
public final class Comment extends BodyElement {

	private final int commentStart;

	private final int commentEnd;

	private final char[] comment;

	/**
	 * Create a comment element
	 * @param ast the root AST object to to which the comment will be long
	 * @param line the start line of the comment
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset (doc relative) of the comment
	 * @param end the end offset of the comment
	 * @param commentStart the start offset of the comment text (doc relative)
	 * @param commentEnd the end offset of the comment text (doc relative)
	 * @param comment the comment text
	 */
	Comment(JETAST ast, int line, int colOffset, int start, int end,
			int commentStart, int commentEnd, char[] comment) {
		super(ast, line, colOffset, start, end);
		this.commentStart = commentStart;
		this.commentEnd = commentEnd;
		this.comment = comment;
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected final void accept0(JETASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);

	}

	/**
	 * @return Returns the commentEnd.
	 */
	public int getCommentEnd() {
		return commentEnd;
	}

	/**
	 * @return Returns the commentStart.
	 */
	public int getCommentStart() {
		return commentStart;
	}

	/**
	 * Return the comment text
	 * @return the comment text
	 */
	public String getCommentText() {
		return new String(comment);
	}

	public boolean removeLineWhenOtherwiseEmpty() {
		return true;
	}

}
