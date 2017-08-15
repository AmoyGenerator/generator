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

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.JET2TagException;

/**
 * Protocol for embedded expression evaluators
 *
 */
public interface IEmbeddedExpression {
	
	/**
	 * Return the expression value as a string
	 * @param context the JET context in which from which variables are to be resolved
	 * @return a string value
	 * @throws JET2TagException if the expression cannot be evaluated
	 */
	public abstract String evalAsString(JET2Context context) throws JET2TagException;
	
	/**
	 * Test whether the expression contains nothing but text.
	 * @return <code>true</code> if the expression is text only
	 */
	public abstract boolean isText();

}
