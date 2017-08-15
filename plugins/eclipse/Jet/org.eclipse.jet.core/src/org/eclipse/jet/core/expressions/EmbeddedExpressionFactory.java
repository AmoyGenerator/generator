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
package org.eclipse.jet.core.expressions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.core.expressions.EmbeddedExpressionLanguageManager;
import org.eclipse.jet.internal.core.expressions.IEmbeddedExpressionScanner;
import org.eclipse.jet.internal.core.expressions.IEmbeddedLanguage;

/**
 * A factory for creating embedded expressions 
 *
 */
public final class EmbeddedExpressionFactory {
	
	/**
	 * Implementation of an expression that concatenates other expression results to gether as strings.
	 *
	 */
	private static final class ConcatExpression implements IEmbeddedExpression {

		private final List expressions;

		public ConcatExpression(List expressions) {
			this.expressions = expressions;
		}

		public String evalAsString(JET2Context context) {
			StringBuffer result = new StringBuffer();
			for (Iterator i = expressions.iterator(); i.hasNext();) {
				IEmbeddedExpression expr = (IEmbeddedExpression) i.next();
				result.append(expr.evalAsString(context));
			}
			return result.toString();
		}
		
		public String toString() {
			StringBuffer result = new StringBuffer();
			for (Iterator i = expressions.iterator(); i.hasNext();) {
				IEmbeddedExpression expr = (IEmbeddedExpression) i.next();
				result.append(expr.toString());
			}
			return result.toString();
		}

		public boolean isText() {
			return false;
		}
		
	}
	/**
	 * Implementation of an expression that wraps text
	 *
	 */
	private static final class TextExpression implements IEmbeddedExpression {

		private final String text;
		
		public TextExpression(String text) {
			this.text = text;
			
		}
		public String evalAsString(JET2Context context) {
			return text;
		}
		
		public String toString() {
			return text;
		}

		public boolean isText() {
			return true;
		}
	}
	/**
	 * The default expression language. Value: jet.xpath
	 */
	public static final String DEFAULT_EXPRESSION_LANGUAGE = "jet.xpath";

	public static final String EXPRESSION_OPEN = "${";
	
	public static final char EXPRESSION_CLOSE = '}';
	
	/**
	 * Factory method for an embedded expression factory.
	 * @return a embedded expression factory
	 */
	public static EmbeddedExpressionFactory newInstance() {
		return new EmbeddedExpressionFactory(DEFAULT_EXPRESSION_LANGUAGE);
	}

	private final Map languages = EmbeddedExpressionLanguageManager.getInstance().getLanguages();

	private final String defaultExpressionLanguage;

	/**
	 * @param defaultExpressionLanguage
	 */
	private EmbeddedExpressionFactory(String defaultExpressionLanguage) {
		this.defaultExpressionLanguage = defaultExpressionLanguage;
	}

	/**
	 * Create an expression using the default expression language
	 * @param expression the expression
	 * @return the expression implementation
	 * @throws IllegalArgumentException if the expression is malformed
	 */
	public IEmbeddedExpression createExpression(String expression) {
		return createExpression(defaultExpressionLanguage, expression);
	}

	public IEmbeddedExpression createExpression(String language,
			String expression) {
		final IEmbeddedLanguage lang = (IEmbeddedLanguage) languages.get(language);
		final List expressions = new LinkedList();
		
		for(int i = 0; i < expression.length();) {
			final int elStart = expression.indexOf(EXPRESSION_OPEN, i);;
			if(elStart == -1) {
				// no more expressions
				expressions.add(new TextExpression(expression.substring(i)));
				break;
			} else {
				final int elEnd = scanExpression(lang, expression, elStart);
				if(elStart > i) {
					expressions.add(new TextExpression(expression.substring(i, elStart)));
				}
				expressions.add(lang.getExpression(expression.substring(elStart + EXPRESSION_OPEN.length(), elEnd - 1)));
				i = elEnd;
			}
		}
		
		return expressions.size() == 1 ? (IEmbeddedExpression)expressions.get(0) : new ConcatExpression(expressions);
	}

	/**
	 * @param lang
	 * @param expression
	 * @param elStart
	 * @return
	 */
	private int scanExpression(final IEmbeddedLanguage lang, String expression,
			final int elStart) {
		final IEmbeddedExpressionScanner scanner = lang.getScanner();
		for(int j = elStart + EXPRESSION_OPEN.length(); j < expression.length() ; j++) {
			int ch = expression.charAt(j);
			scanner.setNextChar(ch);
			if(EXPRESSION_CLOSE == ch && !scanner.ignoreChar()) {
				return j + 1;
			}
		}
		throw new IllegalArgumentException("Unterminated embedded expresion");
	}
}
