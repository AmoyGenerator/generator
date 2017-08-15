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
 * $Id: InternalJET1Parser.java,v 1.4 2009/04/06 17:55:06 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.core.parser;

import java.io.CharArrayReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jet.core.parser.IJETParser;
import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.core.parser.RecursiveIncludeException;
import org.eclipse.jet.core.parser.TemplateInputException;
import org.eclipse.jet.core.parser.ast.BodyElement;
import org.eclipse.jet.core.parser.ast.Comment;
import org.eclipse.jet.core.parser.ast.IncludedContent;
import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.core.parser.ast.JETDirective;
import org.eclipse.jet.core.parser.ast.JavaDeclaration;
import org.eclipse.jet.core.parser.ast.JavaExpression;
import org.eclipse.jet.core.parser.ast.JavaScriptlet;
import org.eclipse.jet.core.parser.ast.TextElement;
import org.eclipse.jet.internal.core.parser.jasper.CommentElementDelegate;
import org.eclipse.jet.internal.core.parser.jasper.DeclarationElementDelegate;
import org.eclipse.jet.internal.core.parser.jasper.ErrorRedirectingCoreElementDelegate;
import org.eclipse.jet.internal.core.parser.jasper.JETCoreElement;
import org.eclipse.jet.internal.core.parser.jasper.JETException;
import org.eclipse.jet.internal.core.parser.jasper.JETMark;
import org.eclipse.jet.internal.core.parser.jasper.JETParseEventListener2;
import org.eclipse.jet.internal.core.parser.jasper.JETParser;
import org.eclipse.jet.internal.core.parser.jasper.JETReader;
import org.eclipse.jet.internal.l10n.JET2Messages;

/**
 * JET Parser Listener used by the JET1 Syntax
 *
 */
public class InternalJET1Parser implements JETParseEventListener2, IJETParser, IJETParser2 {

	/**
	 * Enumeratin of IncludeFailActions
	 *
	 */
	public final static class IncludeFailAction {

		public static final IncludeFailAction ERROR = new IncludeFailAction(
				"error"); //$NON-NLS-1$

		public static final IncludeFailAction SILENT = new IncludeFailAction(
				"silent"); //$NON-NLS-1$

		public static final IncludeFailAction ALTERNATIVE = new IncludeFailAction(
				"alternative"); //$NON-NLS-1$

		private final String displayValue;

		private IncludeFailAction(String displayValue) {
			this.displayValue = displayValue;
		}

		public String toString() {
			return displayValue;
		}

		public static IncludeFailAction getAction(String action) {
			if ("alternative".equalsIgnoreCase(action)) { //$NON-NLS-1$
				return ALTERNATIVE;
			} else if ("silent".equalsIgnoreCase(action)) { //$NON-NLS-1$
				return SILENT;
			} else {
				return ERROR;
			}
		}
	}

	private static final String JET__DIRECTIVE = "jet"; //$NON-NLS-1$

	private static final String INCLUDE__DIRECTIVE = "include"; //$NON-NLS-1$

	private static final String FILE__ATTR = "file"; //$NON-NLS-1$

	private static final String FAIL__ATTR = "fail"; //$NON-NLS-1$

	private static final String START__DIRECTIVE = "start"; //$NON-NLS-1$

	private static final String END__DIRECTIVE = "end"; //$NON-NLS-1$

	private final IncludeAlternativesTracker includeAlternativesTracker = new IncludeAlternativesTracker();

	/**
	 * Stack of open includedContent elements
	 */
	private final Stack includedContentStack = new Stack();
	
	/**
	 * Stack of ITemplateInput objects.
	 */
	private final Stack templateInputs = new Stack();

	private JETCompilationUnit compilationUnit;

	private JETAST ast;

	private JETReader reader;

	private final ITemplateResolver templateResolver;

	public InternalJET1Parser(ITemplateResolver templateResolver) {
		this.templateResolver = templateResolver;
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener#beginPageProcessing()
	 */
	public void beginPageProcessing() {
		// nothing to do
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleDirective(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
	 */
	public void handleDirective(String directive, JETMark start, JETMark stop,
			Map attributes) {
		JETDirective directiveElement = ast.newJETDirective(start.getLine(), start
				.getCol(), start.getCursor(), stop.getCursor() + 1, directive,
				attributes);

	    boolean compileEnabled = includeAlternativesTracker.isCompileEnabled();
		// although a directive may appear nested, it really isn't. Add it to the compilation unit.

		if (JET__DIRECTIVE.equalsIgnoreCase(directive)) {
		    if(compileEnabled) {
		    	addBodyElement(directiveElement);
				handleJetDirective(start, stop, attributes);
		    }
		} else if (INCLUDE__DIRECTIVE.equalsIgnoreCase(directive)) {
			// add the include to the AST only if the section is enabled...
		    if(compileEnabled) {
		    	addBodyElement(directiveElement);
		    }
			handleIncludeDirective(directiveElement, start, stop, attributes);
		} else if (START__DIRECTIVE.equalsIgnoreCase(directive)) {
			try {
				includeAlternativesTracker.startAlternative(directiveElement);
				// add the start to the AST only if, AFTER processing the @start,
				// the section is enabled...
			    if(includeAlternativesTracker.isCompileEnabled()) {
			    	addBodyElement(directiveElement);
			    }
			} catch (IllegalStateException e) {
				recordProblem(ProblemSeverity.ERROR,
						IProblem.StartDirectiveOutOfContext,
						JET2Messages.JET2Compiler_StartDirectiveOutOfContext,
						new Object[] { directive }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
		} else if (END__DIRECTIVE.equalsIgnoreCase(directive)) {
			try {
				// add the end to the AST only if the secion is enabled...
			    if(compileEnabled) {
			    	addBodyElement(directiveElement);
			    }
				includeAlternativesTracker.endAlternative(directiveElement);
			} catch (IllegalStateException e) {
				recordProblem(ProblemSeverity.ERROR,
						IProblem.EndDirectiveOutOfContext,
						JET2Messages.JET2Compiler_EndDirectiveOutOfContext,
						new Object[] { directive }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
		} else if (compileEnabled) {
			recordProblem(ProblemSeverity.WARNING,
					IProblem.UnsupportedDirective,
					JET2Messages.ASTCompilerParseListener_UnsupportedDirective,
					new Object[] { directive }, start.getCursor(), stop
							.getCursor(), start.getLine(), start.getCol());
		}
	}

	private static Set knownIncludeAttributes = new LinkedHashSet(Arrays
			.asList(new String[] { FILE__ATTR, FAIL__ATTR, }));

	private void handleIncludeDirective(JETDirective directive, JETMark start,
			JETMark stop, Map attributes) {
		validateAttributes(start, stop, attributes, knownIncludeAttributes,
				Collections.EMPTY_SET);

		String file = (String) attributes.get(FILE__ATTR);
		if (file == null) {
			missingRequiredAttribute(start, stop, INCLUDE__DIRECTIVE,
					FILE__ATTR);
			return;
		}

		final IncludeFailAction failAction = IncludeFailAction
				.getAction((String) attributes.get(FAIL__ATTR));

		final boolean includePushed = includeAlternativesTracker.isCompileEnabled()
				&& doPushInclude(file);

		if(includePushed) {
			ITemplateInput templateInput = (ITemplateInput) templateInputs.peek();
			final IncludedContent includedContent = ast.newIncludedContent(start.getLine(), start.getCol(), start.getCursor(), stop.getCursor(),
					templateInput.getBaseLocation(), templateInput.getTemplatePath());
			addBodyElement(includedContent);
			includedContentStack.push(includedContent);
		} else if (includeAlternativesTracker.isCompileEnabled()) {
			if (failAction == IncludeFailAction.ERROR) {
				recordProblem(ProblemSeverity.ERROR, IProblem.MissingFile,
						JET2Messages.JET2Compiler_MissingFile,
						new Object[] { file }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
		}
		if (failAction == IncludeFailAction.ALTERNATIVE) {
			boolean processAlternative = includeAlternativesTracker
					.isCompileEnabled()
					&& !includePushed;
			includeAlternativesTracker.addIncludeWithAlternative(directive,
					processAlternative);
		}
	}

	private boolean doPushInclude(String relativePath) {
		ITemplateInput[] activeInputs = (ITemplateInput[]) templateInputs.toArray(new ITemplateInput[templateInputs.size()]);

		if(activeInputs.length > 0) {
			try {
				final ITemplateInput includedInput = templateResolver.getIncludedInput(relativePath, activeInputs);
				
				if(includedInput != null) {
					reader.stackStream(includedInput.getBaseLocation().toString(), includedInput.getTemplatePath(), includedInput.getReader());
		
					templateInputs.push(includedInput);
					
					return true;
				}
			} catch (JETException e) {
				// fall through
			} catch (TemplateInputException e) {
				// fall through
			} catch (RecursiveIncludeException e) {
				// fall through
			}
		}

		return false;
	}

	private void validateAttributes(JETMark start, JETMark stop,
			Map attributes, Set knownAttributes, Set deprecatedAttributes) {
		for (Iterator i = attributes.keySet().iterator(); i.hasNext();) {
			String attrName = (String) i.next();
			if (!knownAttributes.contains(attrName)) {
				recordProblem(ProblemSeverity.ERROR,
						IProblem.UnknownAttributeInTag,
						JET2Messages.JET2Compiler_UnknownAttribute,
						new Object[] { attrName }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
			if (deprecatedAttributes.contains(attrName)) {
				recordProblem(ProblemSeverity.WARNING,
						IProblem.DeprecatedAttribute,
						JET2Messages.JET2Compiler_DeprecatedAttribute,
						new Object[] { attrName }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
		}
	}

	private void missingRequiredAttribute(JETMark start, JETMark stop,
			String directive, String attribute) {
		compilationUnit.createProblem(ProblemSeverity.ERROR,
				IProblem.MissingRequiredAttribute,
				JET2Messages.JET2Compiler_MissingDirectiveAttribute,
				new Object[] { directive, attribute },
				//	      start.getLocalFile(),
				start.getCursor(), stop.getCursor(), start.getLine(), start
						.getCol());
	}

	private static Set knownJETAttributes = new LinkedHashSet(Arrays
			.asList(new String[] { "skeleton", //$NON-NLS-1$
					"package", //$NON-NLS-1$
					"imports", //$NON-NLS-1$
					"class", //$NON-NLS-1$
					"nlString", //$NON-NLS-1$
					"startTag", //$NON-NLS-1$
					"endTag", //$NON-NLS-1$
					"version", //$NON-NLS-1$
			}));

	private static Set deprecatedJETAttributes = new LinkedHashSet(Arrays
			.asList(new String[] { "skeleton", //$NON-NLS-1$
					"nlString", //$NON-NLS-1$
			}));

	private JETParser parser;

	private void handleJetDirective(JETMark start, JETMark stop, Map attributes) {
		for (Iterator i = attributes.keySet().iterator(); i.hasNext();) {
			String attrName = (String) i.next();
			if (!knownJETAttributes.contains(attrName)) {
				recordProblem(ProblemSeverity.ERROR,
						IProblem.UnknownAttributeInTag,
						JET2Messages.JET2Compiler_UnknownAttribute,
						new Object[] { attrName }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
			if (deprecatedJETAttributes.contains(attrName)) {
				recordProblem(ProblemSeverity.WARNING,
						IProblem.DeprecatedAttribute,
						JET2Messages.JET2Compiler_DeprecatedAttribute,
						new Object[] { attrName }, start.getCursor(), stop
								.getCursor(), start.getLine(), start.getCol());
			}
		}

		String pkg = (String) attributes.get("package"); //$NON-NLS-1$
		String cls = (String) attributes.get("class"); //$NON-NLS-1$
		String importStr = (String) attributes.get("imports"); //$NON-NLS-1$
		String startTag = (String) attributes.get("startTag"); //$NON-NLS-1$
		String endTag = (String) attributes.get("endTag"); //$NON-NLS-1$

		if (pkg != null) {
			compilationUnit.setOutputJavaPackage(pkg);
		}
		if (cls != null) {
			compilationUnit.setOutputJavaClassName(cls);
		}
		if (importStr != null) {
			String[] imports = importStr.split("\\s+"); //$NON-NLS-1$
			compilationUnit.addImports(Arrays.asList(imports));
		}

		if (startTag != null) {
			parser.setStartTag(startTag);
		}
		if (endTag != null) {
			parser.setEndTag(endTag);
		}
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleExpression(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
	 */
	public void handleExpression(JETMark start, JETMark stop, Map attributes) {
		if(!includeAlternativesTracker.isCompileEnabled()) {
			return;
		}
		JavaExpression expression = ast.newJavaExpression(start.getLine(),
				start.getCol(), start.getCursor() - 3, stop.getCursor() + 2,
				start.getCursor(), stop.getCursor(), reader.getChars(start,
						stop));

		addBodyElement(expression);
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleCharData(char[])
	 */
	public void handleCharData(char[] chars) {
		if(!includeAlternativesTracker.isCompileEnabled()) {
			return;
		}
		TextElement text = ast.newTextElement(chars);

		addBodyElement(text);
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener#endPageProcessing()
	 */
	public void endPageProcessing() {
		if(templateInputs.size() > 0) {
			templateInputs.pop();
		}
		if(includedContentStack.size() > 0) {
			includedContentStack.pop();
		}
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleScriptlet(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
	 */
	public void handleScriptlet(JETMark start, JETMark stop, Map attributes) {
		if(!includeAlternativesTracker.isCompileEnabled()) {
			return;
		}
		JavaScriptlet scriplet = ast.newJavaScriptlet(start.getLine(), start
				.getCol(), start.getCursor() - 3, stop.getCursor() + 2, start
				.getCursor(), stop.getCursor(), reader.getChars(start, stop));

		addBodyElement(scriplet);

	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleComment(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark)
	 */
	public void handleComment(JETMark start, JETMark stop) {
		if(!includeAlternativesTracker.isCompileEnabled()) {
			return;
		}
		Comment comment = ast.newComment(start.getLine(), start.getCol(), start
				.getCursor() - 4, stop.getCursor() + 4, start.getCursor(), stop
				.getCursor(), reader.getChars(start, stop));

		addBodyElement(comment);
	}

	/**
	 * @param bodyElement
	 */
	private void addBodyElement(BodyElement bodyElement) {
		if (includedContentStack.isEmpty())
	    {
	      compilationUnit.addBodyElement(bodyElement);
	    }
	    else
	    {
	      IncludedContent topElement = (IncludedContent)includedContentStack.peek();

	      topElement.addBodyElement(bodyElement);
	    }
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleDeclaration(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark)
	 */
	public void handleDeclaration(JETMark start, JETMark stop) {
		if(!includeAlternativesTracker.isCompileEnabled()) {
			return;
		}
		JavaDeclaration decl = ast.newJavaDeclaration(start.getLine(), start
				.getCol(), start.getCursor() - 3, stop.getCursor() + 2, start
				.getCursor(), stop.getCursor(), reader.getChars(start, stop));

		addBodyElement(decl);
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleXMLEndTag(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark)
	 */
	public void handleXMLEndTag(String tagName, JETMark start, JETMark stop) {
		throw new IllegalStateException();
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleXMLEmptyTag(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
	 */
	public void handleXMLEmptyTag(String tagName, JETMark start, JETMark stop,
			Map attributeMap) {
		throw new IllegalStateException();
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleXMLStartTag(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
	 */
	public void handleXMLStartTag(String tagName, JETMark start, JETMark stop,
			Map attributeMap) {

		throw new IllegalStateException();
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#isKnownTag(java.lang.String)
	 */
	public boolean isKnownTag(String tagName) {
		return false;
	}

	/**
	 * Return the compilation unit created as a result of handling the JET2 parser events.
	 * @return compilation unit
	 */
	public JETCompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	/**
	 * @see org.eclipse.jet.internal.parser.JETParseEventListener2#recordProblem(org.eclipse.jet.compiler.Problem.ProblemSeverity, int, java.lang.String, java.lang.Object[], int, int, int, int)
	 */
	public void recordProblem(ProblemSeverity severity, int problemId,
			String message, Object[] msgArgs, int start, int end, int line,
			int colOffset) {
		compilationUnit.createProblem(severity, problemId, message, msgArgs,
				start, end, line, colOffset);
	}

	/**
	 * @param reader
	 * @return 
	 */
	private JETParser configureParser(JETReader reader) {
		JETParser.Directive directive = new JETParser.Directive();
		directive.getDirectives().add("jet"); //$NON-NLS-1$
		directive.getDirectives().add("include"); //$NON-NLS-1$
		directive.getDirectives().add("start"); //$NON-NLS-1$
		directive.getDirectives().add("end"); //$NON-NLS-1$
		JETCoreElement[] coreElements = new JETCoreElement[] {
				new ErrorRedirectingCoreElementDelegate(directive),
				new ErrorRedirectingCoreElementDelegate(
						new JETParser.Expression()),
				new ErrorRedirectingCoreElementDelegate(
						new CommentElementDelegate()),
				new ErrorRedirectingCoreElementDelegate(
						new DeclarationElementDelegate()),
				new ErrorRedirectingCoreElementDelegate(
						new JETParser.Scriptlet()), };

		return new JETParser(reader, this, coreElements);
	}

	public boolean isKnownInvalidTagName(String tagName) {
		return false;
	}

	public Object parse(String templatePath) {
		final ITemplateInput templateInput = templateResolver
				.getInput(templatePath);
		templateInputs.push(templateInput);
		final URI baseLocation = templateInput.getBaseLocation();
		try {
			if (compilationUnit == null) {
				compilationUnit = new JETAST()
						.newJETCompilationUnit(baseLocation, templatePath,
								templateInput.getEncoding());
				ast = compilationUnit.getAst();
			}
			reader = new JETReader(baseLocation == null ? null : baseLocation
					.toString(), templatePath, templateInput.getReader());
			intermalParse();
		} catch (org.eclipse.jet.internal.core.parser.jasper.JETException e) {
			// create a minimal compilation unit with the exeception recorded as the error.
			recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e
					.getLocalizedMessage(), null, 0, 0, 1, 1);
		} catch (TemplateInputException e) {
			// create a minimal compilation unit with the exeception recorded as the error.
			recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e
					.getLocalizedMessage(), null, 0, 0, 1, 1);
		}
		return compilationUnit;
	}

	public Object parse(char[] template) {
		return parse(template, "");
	}

	public Object parse(char[] template, String templatePath) {
		final ITemplateInput templateInput;
		final URI baseLocation;
		if(templatePath == null) {
			templatePath = "";
		}
		if(templatePath.length() > 0) {
			templateInput = templateResolver.getInput(templatePath);
			templateInputs.push(templateInput);
			baseLocation = templateInput.getBaseLocation();
		} else {
			templateInput = null;
			baseLocation = null;
		}
		if (compilationUnit == null) {
			compilationUnit = new JETAST()
					.newJETCompilationUnit(baseLocation, templatePath, null);
			ast = compilationUnit.getAst();
		}
		try {
			reader = new JETReader(baseLocation == null ? null : baseLocation
					.toString(), templatePath, new CharArrayReader(template)); //$NON-NLS-1$
			intermalParse();
		} catch (JETException e) {
			// create a minimal compilation unit with the exeception recorded as the error.
			recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e
					.getLocalizedMessage(), null, 0, 0, 1, 1);
		}
		return compilationUnit;

	}
	
	/**
	 * @throws JETException
	 */
	private void intermalParse() throws JETException {
		parser = configureParser(reader);
		this.beginPageProcessing();
		parser.parse();
		this.endPageProcessing();
		compilationUnit.accept(new TextTrimmingVisitor());
	}

	public void handleEmbeddedExpression(String language, JETMark start, JETMark stop)
			throws JETException {
		throw new IllegalStateException();
	}

}
