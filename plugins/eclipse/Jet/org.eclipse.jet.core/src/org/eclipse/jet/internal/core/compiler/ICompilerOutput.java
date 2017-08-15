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
package org.eclipse.jet.internal.core.compiler;

import java.util.List;

/**
 * Protocol for compiler output. 
 * @see IJETCompiler
 */
public interface ICompilerOutput {

	/**
	 * Notification that templatePath is about to be compiled
	 * @param templatePath a templatePath
	 */
	void preCompile(String templatePath);
	
	/**
	 * Write generated ouptut with the given encoding
	 * @param outputFilePath a compiler output base location relative path of the file to write
	 * @param contents the file contents
	 * @param encoding the encoding for the generated class. A value of <code>null</code> indicates
	 * the default encoding
	 */
	void writeOutput(String outputFilePath, String contents, String encoding);

	/**
	 * Remove a generated ouptput file
	 * @param outputFilePath a compiler output base location relative path
	 */
	void removeOutput(String outputFilePath);

	/**
	 * Record problems found in the given templatePath 
	 * @param templatePath the templatePath being compiled
	 * @param problems a list of problems
	 */
	void recordProblems(String templatePath, List problems);

}
