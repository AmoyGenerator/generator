/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;

import org.eclipse.jet.internal.core.expressions.IEmbeddedExpressionScanner;

/**
 * A {@link JETCoreElement} for the {@link JETParser} that recognizes embedded expressions 
 * in the template body 
 * 
 */
public class EmbeddedExpressionElement implements JETCoreElement {

	public boolean accept(JETParseEventListener listener, JETReader reader,
			JETParser parser) throws JETException {
		final JETMark start = reader.mark();
		if (reader.matches("${") && parser.embeddedLanguage != null) {
			final IEmbeddedExpressionScanner scanner = parser.embeddedLanguage
					.getScanner();
			JETMark stop = reader.scanEmbeddedExpression(scanner);
			if (stop != null) {
				((JETParseEventListener2) listener).handleEmbeddedExpression(
						parser.embeddedLanguageID, start, stop);
				return true;
			} else {
				MessagesUtil.recordUnterminatedElement(
						(JETParseEventListener2) listener, "}", start, reader);
			}
		}
		return false;
	}

}