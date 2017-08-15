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
 * $Id: JavaScriptlet.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

/**
 * Define a Java Scriptlet Element in the JET AST
 * 
 */
public final class JavaScriptlet extends JavaElement {

	private static final String OUT_WRITE_MATCH = "([\\w\\W]*)out\\s*.\\s*write\\s*(([\\s\\S]*))\\s*;([\\w\\W]*)";
	
	/**
	 * Create an instance
	 * 
	 * @param ast
	 *            the owning AST
	 * @param line
	 *            the start line of the element
	 * @param colOffset
	 *            the offset within the line of the element's start.
	 * @param start
	 *            the start offset of the element (doc relative)
	 * @param end
	 *            the end offset of the element (doc relative)
	 * @param javaStart
	 *            the start offset of the Java code (doc relative)
	 * @param javaEnd
	 *            the end offset of the Java code (doc relative)
	 * @param javaContent
	 *            the Java code
	 */
	JavaScriptlet(JETAST ast, int line, int colOffset, int start, int end,
			int javaStart, int javaEnd, char[] javaContent) {
		super(ast, line, colOffset, start, end, javaStart, javaEnd, javaContent);
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected void accept0(JETASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);
	}

	public boolean removeLineWhenOtherwiseEmpty() {
		//return true;
		//有out write的时候不能删除空白行
		if(hasOutWrite()){
			return false;
		}
		return true;
	}

	public boolean hasOutWrite(){
		String content = getJavaContent();
		if(content.matches(OUT_WRITE_MATCH)){
			return true;
		}
		return false;
	}
	
}
