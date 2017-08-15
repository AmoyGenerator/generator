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

import java.net.URI;



/** 
 * Protocol for resolving JET template paths into actual input - used by the JET parser and compiler.
 */
public interface ITemplateResolver {
	/** 
	 * Return the template input given a template path.
	 * @param templatePath the JET template path
	 * @return the template input, or <code>null</code>
	 */
	public ITemplateInput getInput(String templatePath);

	/** 
	 * Return the appropriate template input, given a template path, and the current stack of 
	 * template inputs.
	 * The activeInputs argument is order from initial input to most recently included input.
	 * @param templatePath the JET template path
	 * @param activeInputs the input stack.
	 * @return the template input, or <code>null</code>
	 * @throws RecursiveIncludeException if including templatePath would result in a recursive loop
	 * @throws NullPointerException if either argument is <code>null</code>
	 * @throws IllegalArgumentException if <code>inputStack</code> does not contain at least one element.
	 */
	public ITemplateInput getIncludedInput(String templatePath, ITemplateInput[] activeInputs) throws RecursiveIncludeException;

	/**
	 * Return the base locations from which the template resolver will load templates
	 * @return a non-empty array of base location URIs.
	 */
	public URI[] getBaseLocations();
}