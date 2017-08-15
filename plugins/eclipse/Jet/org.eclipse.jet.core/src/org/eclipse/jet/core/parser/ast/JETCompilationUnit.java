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
 * $Id: JETCompilationUnit.java,v 1.5 2007/06/01 20:26:19 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.core.parser.ast;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.taglib.TagLibraryReference;

/**
 * Represent a compilation unit (a template) in the JET AST.
 * 
 */
public final class JETCompilationUnit extends JETASTElement {

	private BodyElements bodyElements = null;

	private final List problems = new ArrayList();

	private String outputJavaPackage = null;

	private String outputJavaClassName;

	private boolean errors = false;

	private boolean warnings = false;

	private TagLibraryReference[] tagLibraryReferences;

	private Set imports = new LinkedHashSet();

	private final URI baseLocation;

	private final String templatePath;

	private final String encoding;

	/**
	 * Create a JET2Compilation Unit
	 * 
	 * @param ast
	 *            the parent AST
	 * @param baseLocation
	 *            the base location URI or <code>null</code>
	 * @param templatePath
	 *            the template path or the empty string
	 * @param encoding
	 *            the template encoding or <code>null</code>
	 */
	JETCompilationUnit(JETAST ast, URI baseLocation, String templatePath,
			String encoding) {
		super(ast, -1, -1, -1, -1);
		this.baseLocation = baseLocation;
		this.templatePath = templatePath;
		this.encoding = encoding;
	}

	/**
	 * Define tag library prefixes (and associated tag library ids) that are
	 * automatically available to the transform.
	 * 
	 * @param predefinedLibraryMap
	 *            a map from prefix to tag library id.
	 */
	public void setPredefinedTagLibraries(Map predefinedLibraryMap) {
	}

	/**
	 * Return the internal BodyElements object that gives access to an
	 * updateable list of AST elements the the compilation unit contains
	 * 
	 * @return a BodyElements object
	 */
	BodyElements getInternalBodyElements() {
		if (bodyElements == null) {
			bodyElements = new BodyElements(this);
		}
		return bodyElements;
	}

	/**
	 * Return a {@link List} of JET2 AST element (@link JETASTElement}
	 * instances.
	 * 
	 * @return a List. The empty list of there are no body elements.
	 */
	public final List getBodyElements() {
		if (bodyElements == null) {
			return Collections.EMPTY_LIST;
		} else {
			return bodyElements.getBodyElements();
		}
	}

	public final void addBodyElement(BodyElement bodyElement) {
		getInternalBodyElements().addBodyElement(bodyElement);
	}

	/**
	 * @see org.eclipse.jet.core.parser.ast.JETASTElement#accept0(JETASTVisitor)
	 */
	protected final void accept0(JETASTVisitor visitor) {
		final boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			for (Iterator i = getBodyElements().iterator(); i.hasNext();) {
				JETASTElement element = (JETASTElement) i.next();
				element.accept(visitor);
			}
		}
		visitor.endVisit(this);
	}

	/**
	 * Return a list of problems discovered in the compilation unit
	 * 
	 * @return a List of {@link Problem} objects. The empty list is returned if
	 *         no problems were found.
	 */
	public List getProblems() {
		return Collections.unmodifiableList(problems);
	}

	/**
	 * Test if the compilation unit has any errors
	 * 
	 * @return <code>true</code> if the compilation unit had errors,
	 *         <code>false</code> otherwise.
	 */
	public boolean hasErrors() {
		return errors;
	}

	/**
	 * Test if the compilation unit has any warnings
	 * 
	 * @return <code>true</code> if the compilation unit had warnings,
	 *         <code>false</code> otherwise.
	 */
	public boolean hasWarnings() {
		return warnings;
	}

	/**
	 * Create a new problem on the compilation unit
	 * 
	 * @param error
	 *            the severity of the problem
	 * @param problemId
	 *            the problem id. A value from {@link Problem} static files
	 * @param message
	 *            an error message, with optional replacement tokens
	 * @param messageArgs
	 *            the error message arguments
	 * @param start
	 *            the start offset of the problem (doc relative)
	 * @param end
	 *            the end offset of the problem (doc relative)
	 * @param line
	 *            the line number of the problem (1 based)
	 * @param colOffset
	 *            TODO
	 */
	public void createProblem(ProblemSeverity error, int problemId,
			String message, Object[] messageArgs, int start, int end, int line,
			int colOffset) {
		if (error == ProblemSeverity.ERROR) {
			errors = true;
		} else if (error == ProblemSeverity.WARNING) {
			warnings = true;
		}
		problems.add(new Problem(baseLocation, templatePath, error, problemId,
				message, messageArgs, start, end, line, colOffset));
	}

	/**
	 * Return the name of the Java package to which the compilation unit will be
	 * compiled.
	 * 
	 * @return a string
	 */
	public String getOutputJavaPackage() {
		return outputJavaPackage;
	}

	/**
	 * Return the unqualified name of the Java class into which the compilation
	 * unit will be compiled.
	 * 
	 * @return Returns the outputJavaClassName.
	 */
	public String getOutputJavaClassName() {
		return outputJavaClassName;
	}

	/**
	 * Set the unqualifeid name of the Java class into which the compilation
	 * unit will be compiled.
	 * 
	 * @param outputJavaClassName
	 *            The outputJavaClassName to set.
	 */
	public void setOutputJavaClassName(String outputJavaClassName) {
		this.outputJavaClassName = outputJavaClassName;
	}

	/**
	 * Set the Java package into which the compilation unit will be compiled.
	 * 
	 * @param outputJavaPackage
	 *            The outputJavaPackage to set.
	 */
	public void setOutputJavaPackage(String outputJavaPackage) {
		this.outputJavaPackage = outputJavaPackage;
	}

	public boolean removeLineWhenOtherwiseEmpty() {
		return false;
	}

	/**
	 * Return an array of tag libraries referenced by this template.
	 * 
	 * @return a possibly empty array of tag library references.
	 */
	public TagLibraryReference[] getTagLibraryReferences() {
		return tagLibraryReferences == null ? new TagLibraryReference[0]
				: tagLibraryReferences;
	}

	public void addImports(List list) {
		imports.addAll(list);
	}

	public Set getImports() {
		return Collections.unmodifiableSet(imports);
	}

	/**
	 * Set the tag libraries referenced by this template
	 * 
	 * @param tagLibraryReferences
	 *            the tag library references
	 * @since 0.8.0
	 */
	public void setTagLibraryReferences(
			TagLibraryReference[] tagLibraryReferences) {
		this.tagLibraryReferences = tagLibraryReferences;
	}

	/**
	 * Return the output encoding for the template
	 * 
	 * @return the output encoding
	 * @since 0.8.0
	 */
	public String getOutputEncoding() {
		return encoding;
	}

	public JETASTElement elementAfter(JETASTElement element) {
		return bodyElements.elementAfter(element);
	}

	public BodyElement elementBefore(JETASTElement element) {
		return bodyElements.elementBefore(element);
	}

	public JETASTElement getElementByOffset(List elements, int offset){
		for(Object obj : elements){
			if(obj instanceof XMLBodyElement){
				XMLBodyElement xmlBodyElement = (XMLBodyElement) obj;
				int start = xmlBodyElement.getStart();
				int end = xmlBodyElement.getEnd();
				if(offset >= start && offset <= end){
					return xmlBodyElement;
				}
				JETASTElement jetastElement = getElementByOffset(xmlBodyElement.getBodyElements(), offset);
				if(jetastElement != null){
					return jetastElement;
				}
			}else if(obj instanceof JETASTElement){
				JETASTElement jetastElement = (JETASTElement) obj;
				int start = jetastElement.getStart();
				int end = jetastElement.getEnd();
				if(offset >= start && offset <= end){
					return jetastElement;
				}
			}
		}
		return null;
	}
	
	public JETASTElement getElementByOffset(XMLBodyElement bodyElement, int offset){
		List elements = bodyElement.getBodyElements();
		for(Object obj : elements){
			if(obj instanceof JETASTElement){
				JETASTElement jetastElement = (JETASTElement) obj;
				int start = jetastElement.getStart();
				int end = jetastElement.getEnd();
				if(offset >= start && offset <= end){
					return jetastElement;
				}
			}
		}
		return null;
	}
	
}
