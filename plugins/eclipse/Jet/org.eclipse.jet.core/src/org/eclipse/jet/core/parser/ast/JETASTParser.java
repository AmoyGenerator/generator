/**
 * <copyright>
 *
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: JETASTParser.java,v 1.3 2009/04/06 17:55:06 pelder Exp $
 */
package org.eclipse.jet.core.parser.ast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.jet.core.parser.IJETParser;
import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.RecursiveIncludeException;
import org.eclipse.jet.internal.core.parser.IJETParser2;
import org.eclipse.jet.internal.core.parser.InternalJET1Parser;
import org.eclipse.jet.internal.core.parser.InternalJET2Parser;
import org.eclipse.jet.taglib.TagLibrary;

/**
 * Parser for creating JET ASTs
 * @since 0.8.0
 */
public final class JETASTParser implements IJETParser, IJETParser2 {

	private static final class NullTagLibraryResolver implements
			ITagLibraryResolver {

		public TagLibrary getLibrary(String tagLibraryID) {
			return null;
		}
	}

	/**
	 * Builder for JETAST Parser
	 */
	public static final class Builder {

		ITemplateResolver templateResolver = null;

		Map predefinedTagLibraries;

		ITagLibraryResolver tagLibraryResolver;

		final int jetSpec;

		boolean enableEmbeddedExpressions = false;

		/**
		 * Create a JETASTParser builder for a parser using the specified JET language specification
		 * @param jetSpec either {@link JETAST#JET_SPEC_V1} or {@link JETAST#JET_SPEC_V2}.
		 * @throws IllegalArgumentException if <code>jetSpec</code> is not one of the specified values.
		 */
		public Builder(int jetSpec) {
			if(jetSpec != JETAST.JET_SPEC_V1 && jetSpec != JETAST.JET_SPEC_V2) {
				throw new IllegalArgumentException();
			}
			this.jetSpec = jetSpec;
		}

		/**
		 * Specify a template resolver for the parser. If not specified,
		 * then not template paths are resolvable, and all templatePath references
		 * in parser APIs or in JET directives will result in compilation errors.
		 * @param templateResolver a templateResolver. Cannot be <code>null</code>
		 * @return the builder
		 * @throws NullPointerException if <code>templateResolver</code> is <code>null</code>
		 */
		public Builder templateResolver(ITemplateResolver templateResolver) {
			if (templateResolver == null) {
				throw new NullPointerException();
			}
			this.templateResolver = templateResolver;
			return this;
		}

		/**
		 * Specify a map of predefined JET tag library prefixes to their corresponding JET
		 * tag libary ids. If not specified, then not tag libraries are predefined.
		 * @param predefinedTagLibraries a non-null map of predefined JET tag libraries.
		 * @return the builder.
		 * @throws NullPointerException if <code>predefinedTagLibraries</code> is <code>null</code>
		 */
		public Builder predefinedTagLibraries(Map predefinedTagLibraries) {
			if (predefinedTagLibraries == null) {
				throw new NullPointerException();
			}
			this.predefinedTagLibraries = predefinedTagLibraries;
			return this;
		}

		/**
		 * Specify a tag library resolver for the parser
		 * @param tagLibraryResolver a tag library resolver instance
		 * @return the tag builder
		 */
		public Builder tagLibraryResolver(ITagLibraryResolver tagLibraryResolver) {
			this.tagLibraryResolver = tagLibraryResolver;
			return this;
		}

		public Builder enableEmbeddedExpressions(boolean enableEmbeddedExpressions) {
			this.enableEmbeddedExpressions = enableEmbeddedExpressions;
			return this;
		}
		/**
		 * Build the JETASTParser
		 * @return the new parser.
		 */
		public JETASTParser build() {
			return new JETASTParser(this);
		}
	}

	private static final ITemplateResolver nullTemplateResolver = new ITemplateResolver() {

		public URI[] getBaseLocations() {
			URI nullURI;
			try {
				nullURI = new URI(""); //$NON-NLS-1$
				return new URI[] { nullURI };
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		public ITemplateInput getIncludedInput(String templatePath,
				ITemplateInput[] activeInputs) throws RecursiveIncludeException {
			return null;
		}

		public ITemplateInput getInput(String templatePath) {
			return null;
		}

	};

	private final IJETParser parser;

	/**
	 * Private constructor based on Builder
	 * @param builder
	 */
	private JETASTParser(Builder builder) {
		ITemplateResolver templateResolver = builder.templateResolver == null ? nullTemplateResolver
				: builder.templateResolver;
		Map predefinedTagLibraries = builder.predefinedTagLibraries == null ? Collections.EMPTY_MAP
				: builder.predefinedTagLibraries;

		
		if(builder.jetSpec == JETAST.JET_SPEC_V2) {
			parser = new InternalJET2Parser(templateResolver,
					builder.tagLibraryResolver == null ? 
							new NullTagLibraryResolver() : builder.tagLibraryResolver,
					predefinedTagLibraries, builder.enableEmbeddedExpressions);
		} else {
			parser = new InternalJET1Parser(templateResolver);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.IJETParser#parse(java.lang.String)
	 */
	public Object parse(String templatePath) {
		return parser.parse(templatePath);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.IJETParser#parse(char[])
	 */
	public Object parse(char[] template) {
		return parser.parse(template);
	}

	public Object parse(char[] template, String templatePath) {
		if(parser instanceof IJETParser2) {
			return ((IJETParser2) parser).parse(template, templatePath);
		} else {
			return parser.parse(template);
		}
	}

}
