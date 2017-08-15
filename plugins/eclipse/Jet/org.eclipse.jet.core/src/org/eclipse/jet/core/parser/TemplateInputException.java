/*
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.core.parser;

/**
 * Wrap an exception from a ITemplateInput
 *
 */
public class TemplateInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4844450071476080161L;

	/**
	 * Construct a new exception with the specified message and cause.
	 * 
	 * Delegates to {@link Exception#Exception(String, Throwable)}
	 * @param message the message
	 * @param cause the cause
	 */
	public TemplateInputException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construct a new exception with the specified message.
	 * Delegates to {@link Exception#Exception(String)}.
	 * @param message the detailed message
	 */
	public TemplateInputException(String message) {
		super(message);
	}

	/**
	 * Construct a new exception with the specified cause.
	 * Delegates to {@link Exception#Exception(Throwable)}.
	 * @param cause the cause
	 */
	public TemplateInputException(Throwable cause) {
		super(cause);
	}

	
}
