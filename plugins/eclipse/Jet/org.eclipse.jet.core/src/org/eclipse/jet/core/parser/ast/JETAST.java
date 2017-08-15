/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * $Id: JETAST.java,v 1.5 2009/04/06 17:55:06 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.core.parser.ast;

import java.net.URI;
import java.util.Map;

import org.eclipse.jet.taglib.TagDefinition;

/**
 * The root object of JET AST trees and a factory for JET nodes in that tree.
 * @since 0.8.0
 *
 */
public final class JETAST {

	/**
	 * Compile to the original JET specification (org.eclipse.emf.codegen).
	 * @see JETASTParser
	 */
	public static final int JET_SPEC_V1 = 1;

	/**
	 * Compile according to the JET2 specification (org.eclipse.jet).
	 * @see JETASTParser
	 */
	public static final int JET_SPEC_V2 = 2;

	/**
	 * Create an instance
	 */
	public JETAST() {
		// do nothing
	}

	/**
	 * Create a new, unparented JET2Compilation unit that is not based on a template reference
	 * @return the compilation unit node
	 * @since 0.8.0
	 */
	public JETCompilationUnit newJETCompilationUnit() {
		return new JETCompilationUnit(this, null, "", null); //$NON-NLS-1$
	}

	/**
	 * Create a new unparented JET2Compilation unit for the given template in the given base location
	 * @param baseLocation
	 * @param templatePath
	 * @param encoding the template encoding
	 * @return the compilation unit node
	 * @since 0.8.0
	 */
	public JETCompilationUnit newJETCompilationUnit(URI baseLocation,
			String templatePath, String encoding) {
		return new JETCompilationUnit(this, baseLocation, templatePath,
				encoding);
	}

	/**
	 * Create a text element in the AST
	 * @param chars the text
	 * @return the new element
	 */
	public TextElement newTextElement(char[] chars) {
		return new TextElement(this, chars);
	}

	/**
	 * Create a new Directive element in the AST
	 * @param line the start line
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset (doc relative)
	 * @param end the end offset (doc relative)
	 * @param directiveName the directive name
	 * @param attributes a map off attribute names and values
	 * @return the new element
	 */
	public JETDirective newJETDirective(int line, int colOffset, int start,
			int end, String directiveName, Map attributes) {
		return new JETDirective(this, line, colOffset, start, end,
				directiveName, attributes);
	}

	/**
	 * Create a new Java Expression element in the AST
	 * @param line the start line
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the offset of the expression element (doc relative)
	 * @param end the end offset of the expression element (doc relative)
	 * @param javaStart the offset of the Java code (doc relative)
	 * @param javaEnd the end offset of the Java code
	 * @param javaContent the Java content
	 * @return the new element
	 */
	public JavaExpression newJavaExpression(int line, int colOffset, int start,
			int end, int javaStart, int javaEnd, char[] javaContent) {
		return new JavaExpression(this, line, colOffset, start, end, javaStart,
				javaEnd, javaContent);
	}

	/**
	 * Create a new Java Scriptlet element in the AST
	 * @param line the start line
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the offset of the scriptlet element (doc relative)
	 * @param end the end offset of the scriplet element (doc relative)
	 * @param javaStart the offset of the Java code (doc relative)
	 * @param javaEnd the end offset of the Java code
	 * @param javaContent the Java content
	 * @return the new element
	 */
	public JavaScriptlet newJavaScriptlet(int line, int colOffset, int start,
			int end, int javaStart, int javaEnd, char[] javaContent) {
		return new JavaScriptlet(this, line, colOffset, start, end, javaStart,
				javaEnd, javaContent);
	}

	/**
	 * Create a new Java Declaration element in the AST
	 * @param line the start line
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the offset of the declaration element (doc relative)
	 * @param end the end offset of the declaration element (doc relative)
	 * @param javaStart the offset of the Java code (doc relative)
	 * @param javaEnd the end offset of the Java code
	 * @param javaContent the Java content
	 * @return the new element
	 */
	public JavaDeclaration newJavaDeclaration(int line, int colOffset,
			int start, int end, int javaStart, int javaEnd, char[] javaContent) {
		return new JavaDeclaration(this, line, colOffset, start, end,
				javaStart, javaEnd, javaContent);
	}

	/**
	 * Create a new empty XML element in the AST
	 * @param line the start line of the element
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param tagName the QName of the element
	 * @param attributeMap a Map off element attribute names and values
	 * @param td the TagDefinition of the element
	 * @return the new element
	 */
	public XMLEmptyElement newXMLEmptyElement(int line, int colOffset,
			int start, int end, String tagName, Map attributeMap,
			TagDefinition td) {
		return new XMLEmptyElement(this, line, colOffset, start, end, tagName,
				attributeMap, td);
	}

	/**
	 * Create a new XML element with body in the AST
	 * @param line the start line of the element
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param tagName the QName of the element
	 * @param attributeMap a Map off element attribute names and values
	 * @param td the TagDefinition of the tag
	 * @return the new element
	 */
	public XMLBodyElement newXMLBodyElement(int line, int colOffset,
			int start, int end, String tagName, Map attributeMap,
			TagDefinition td) {
		return new XMLBodyElement(this, line, colOffset, start, end, tagName,
				attributeMap, td);
	}

	/**
	 * Create a new Comment element in the AST
	 * @param line the start line of the element
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param commentStart the start offset of the comment text (doc relative)
	 * @param commentEnd the end offset of the comment text (doc relative)
	 * @param comment the comment text
	 * @return the new element
	 */
	public Comment newComment(int line, int colOffset, int start, int end,
			int commentStart, int commentEnd, char[] comment) {
		return new Comment(this, line, colOffset, start, end, commentStart,
				commentEnd, comment);
	}

	/**
	 * Create a new InludedContent element in the AST
	 * @param line the start line of the element
	 * @param colOffset the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param baseLocation the base Location URI of the resolved include
	 * @param templatePath the templatePath (relative to the base location
	 * @return the IncludedContent element
	 */
	public IncludedContent newIncludedContent(int line, int colOffset, int start, int end, URI baseLocation, String templatePath) {
		return new IncludedContent(this, templatePath, baseLocation, line, colOffset, start, end);
	}
	
	/**
	 * Create a new XML end element corresponding to the end of a XMLBodyElement.
	 * @param line the start line of the element
	 * @param col the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param tagName the fully qualified tag name
	 * @return the new XML BodyElementEnd
	 */
	public XMLBodyElementEnd newXMLBodyElementEnd(int line, int col,
			int start, int end, String tagName) {
		return new XMLBodyElementEnd(this, line, col, start, end);
	}

	/**
	 * Create a new Embedded Expression element
	 * @param line the start line of the element
	 * @param col the offset within the line of the element's start.
	 * @param start the start offset of the element (doc relative)
	 * @param end the end offset of the element (doc relative)
	 * @param language the expression language
	 * @param content the expression including ${ and } delimiters
	 * @return the AST element
	 */
	public EmbeddedExpression newEmbeddedExpression(int line, int col,
			int start, int end, String language, char[] content) {
		return new EmbeddedExpression(this, line, col, start, end, language, content);
	}

}
