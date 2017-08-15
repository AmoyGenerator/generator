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

import java.io.Reader;
import java.net.URI;

/** 
 * Encapsulate a JET Templates input
 * @since 1.0
 */
public interface ITemplateInput {
	/**
	 * Return the template Path of the input.
	 * @return the template Path
	 */
	public String getTemplatePath();

	/**
	 * Return the base URL of the input
	 * @return the template base URL
	 */
	public URI getBaseLocation();

	/**
	 * Return a reader for the template input.
	 * It is the responsibility of the caller to close the reader
	 * @return a reader
	 */
	public Reader getReader() throws TemplateInputException;

	/**
	 * Return the encoding of the template input
	 * @return the encoding or <code>null</code> if not known
	 * @throws TemplateInputException if an error occurs while determing the encoding
	 */
	public String getEncoding() throws TemplateInputException;
}