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

import java.io.Serializable;


/**
 * Protocol for an incremental JET Compiler. Typically, the compiler methods are called in the following order
 * <ol>
 * <li>{@link #clean()} - optional - remove any previously created compiler output</li>
 * <li>{@link #compile(String)} or {@link #removeOutput(String)} - once for each tempalte to compiler or template removed, respectively</li>
 * <li>{@link #finish()} - once, after all templates are compiled</li>
 * <li>{@link #getTagLibaryDependencies()} - once, in order to track tag library dependiencies
 * <li>{@link #getMemento()} - optional - obtain the compiler state for potential re-use later
 * </ol>
 *
 */
public interface IJETCompiler {
	
	/**
	 * Enumeration defining results for {@link IJETCompiler#compile(String)}
	 *
	 */
	public static final class CompileResult {

		/**
		 * Compile was successful with no errors or warnings
		 */
		public static final CompileResult OK = new CompileResult("OK"); //$NON-NLS-1$
		
		/**
		 * Compile was successful with warnings
		 */
		public static final CompileResult WARNINGS = new CompileResult("WARNINGS"); //$NON-NLS-1$
		
		/**
		 * Compile failed with errors or errors and warnings
		 */
		public static final CompileResult ERRORS = new CompileResult("ERRORS"); //$NON-NLS-1$
		
		/**
		 * Compile was not attempted - file did not have a recognized file extension
		 */
		public static final CompileResult IGNORED = new CompileResult("IGNORED"); //$NON-NLS-1$
		
		private final String display;

		private CompileResult(String display) {
			this.display = display;
		}
		
		public String toString() {
			return display;
		}
	}

	/**
	 * Compile the specified template.
	 * Any compile problems are included in the compilation unit returned.
	 * @param templatePath the template path of the JET file
	 * @return a {@link CompileResult} indicating the result of the compilation.
	 * @throws IllegalStateException if called after a call to {@link #finish()}
	 */
	public abstract CompileResult compile(String templatePath);
	
	
	/**
	 * Retrieve the current compiler state.
	 */
	public abstract Serializable getMemento();

	/**
	 * Finish compilation by generating cross template information such as template loaders or maps.
	 */
	public abstract void finish();
	
	/**
	 * Remove any compiler created artifacts.
	 * @throws IllegalStateException if {@link #compile(String)} has already been called
	 */
	public abstract void clean();
	
	/**
	 * Remove the previously generated output for a template, corresponding to the removal
	 * of a tempalte
	 * @param templatePath a tempate path
	 */
	public abstract void removeTemplate(String templatePath);

	/**
	 * Return the tag libaries referenced by the compilation
	 * @return a non-null array of tag library ids.
	 */
	public abstract String[] getTagLibaryDependencies();
	
	/**
	 * Test whether the template path has a recognized JET extension according to this compiler.
	 * @param templatePath a tempate path
	 * @return <code>true</code> if the tempalte has a recognized template extension
	 */
	public abstract boolean isTemplate(String templatePath);
	
	/**
	 * Return the list of files needing recompilation if the file represented by
	 * changedFilePath has changed.
	 * Note that if changeFilePath is a a template path, the the template path
	 * is not returned in the list.
	 * Note also that this method depends on the compiler maintaining incremental
	 * compilation state and is useful to an incremental builder.
	 * @param changedFilePath a template or included file path
	 * @return a possibly empty list of templates paths to recompile.
	 */
	public abstract String[] getAffectedTemplatePaths(String changedFilePath);
}
