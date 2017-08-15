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
 * Define the  behavior of a JET Parser
 */
public interface IJETParser {
	/** 
	 * Parse the named input template
	 * @param templatePath the template Path
	 * @return the root of the AST
	 */
	public Object parse(String templatePath);
	
	/**
	 * Parse contents as a JET template
	 * @param template the template
	 * @return the root of the AST
	 */
	public Object parse(char[] template);
}