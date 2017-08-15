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

import org.eclipse.jet.core.expressions.IEmbeddedExpression;


/**
 * Define the protocol for a JET embedded language implementation.
 * Embedded language implementations may be registered via {@link EmbeddedExpressionLanguageManager#addLanguage(String, IEmbeddedLanguage)}.
 *
 */
public interface IEmbeddedLanguage {

	/**
	 * Return an embedded expression implementation given the a JET execution context and an language expression.
	 * Note that any errors in evaluating the expression should be logged via the JET contexts logging functions.
	 * @param expression a 
	 * @return the expression implementation - this value must not be <code>null</code>
	 */
	IEmbeddedExpression getExpression(String expression);
	
	/**
	 * Return an scanner that enables the JET template parser to identify the JET language's embedded expression terminating character.
	 * This
	 * @return an implementation of {@link IEmbeddedExpressionScanner} - this value must not be <code>null</code>
	 */
	IEmbeddedExpressionScanner getScanner();
}
