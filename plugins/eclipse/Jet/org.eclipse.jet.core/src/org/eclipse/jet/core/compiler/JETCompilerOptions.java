/**
 * <copyright>
 *
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: JETCompilerOptions.java,v 1.5 2009/04/07 17:44:50 pelder Exp $
 */
package org.eclipse.jet.core.compiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jet.core.parser.ast.JETAST;

/**
 * Utility class representing JET compiler Options
 */
public final class JETCompilerOptions {

	/**
	 * The default value of the  {@link #OPTION_COMPILED_TEMPLATE_PACKAGE}  option; value: "org.eclipse.jet2.internal.compiled".
	 */
	public static final String DEFAULT_COMPILED_TEMPLATE_PACKAGE = "org.eclipse.jet.compiled"; //$NON-NLS-1$

	/**
	 * The default value of the  {@link #OPTION_COMPILED_TEMPLATE_SRC_DIR}  option; value: "jet2java".
	 */
	public static final String DEFAULT_COMPILED_TEMPLATE_SRC_DIR = "jet2java"; //$NON-NLS-1$

	/**
	 * The default value of the  {@link #OPTION_TEMPLATE_EXT}  option; value: "jet".
	 */
	public static final String DEFAULT_TEMPLATE_EXT = "jet,jet2"; //$NON-NLS-1$

	/**
	 * The default value of the {@link #OPTION_SET_JAVA_FILES_AS_DERIVED} option (Boolean.TRUE).
	 */
	public static final Boolean DEFAULT_SET_JAVA_FILES_AS_DERIVED = Boolean.TRUE;

	/**
	 * Common prefix for compile option names
	 */
	private static final String NS = "org.eclipse.jet."; //$NON-NLS-1$

	/**
	 * Compiler option specifying the package to which compiled templates are written; value: "compiledTemplatePackage".
	 */
	public static final String OPTION_COMPILED_TEMPLATE_PACKAGE = NS
			+ "compiledTemplatePackage"; //$NON-NLS-1$

	/**
	 * Compiler option specifying the Java project source directory to which compiled templates are written; value: "compiledTemplateSrcDir".
	 */
	public static final String OPTION_COMPILED_TEMPLATE_SRC_DIR = NS
			+ "compiledTemplateSrcDir"; //$NON-NLS-1$

	/**
	 * Compiler option specifying which extensions are recognized as extensions; value: "templateExt".
	 */
	public static final String OPTION_TEMPLATE_EXT = NS + "templateExt"; //$NON-NLS-1$

	/**
	 * Compiler option specifying whether generated Java source should be marked as 'derived'.
	 */
	public static final String OPTION_SET_JAVA_FILES_AS_DERIVED = NS
			+ "setJavaDerived"; //$NON-NLS-1$

	/**
	 * Compiler option specifying which JET specification version to compile; value "jetSpecificationVersion".
	 */
	public static final String OPTION_JET_SPECIFICATION_VERSION = NS
			+ "jetSpecificationVersion"; //$NON-NLS-1$

	/**
	 * Default value for {@link #OPTION_JET_SPECIFICATION_VERSION}.
	 */
	public static final Integer DEFAULT_JET_SPECIFICATION_VERSION = new Integer(
			JETAST.JET_SPEC_V2);

	/**
	 * Compiler option specifying the base locations for JET1 templates to override; value "{@value #OPTION_V1_BASE_TRANSFORMATION}".
	 * Use of this option is discouraged, along with the &lt;%&#064; include %&gt; directive.
	 * <p>
	 * This option is not set globally. It is only set at the project level.
	 * </p>
	 */
	public static final String OPTION_V1_BASE_TRANSFORMATION = NS
			+ "v1BaseTransformationID"; //$NON-NLS-1$

	/**
	 * Default value for {@link #OPTION_V1_BASE_TRANSFORMATION}.
	 */
	public static final String DEFAULT_V1_BASE_TRANSFORMATION = ""; //$NON-NLS-1$

	/**
	 * Compiler option specifying whether all base templates are recompiled into the current project; value "{@value #OPTION_V1_COMPILE_BASE_TEMPLATES}".
	 * Use this option when {@link #OPTION_V1_BASE_TRANSFORMATION} is not empty to cause all base templates to be overriden. This will
	 * be necessary if base templates use &lt;%&#064; include %&gt; directives that the current project overrides.
	 */
	public static final String OPTION_V1_COMPILE_BASE_TEMPLATES = NS
			+ "v1CompileBaseTemplates"; //$NON-NLS-1$

	/**
	 * Default value for {@link #OPTION_V1_COMPILE_BASE_TEMPLATES}; value: {@link Boolean#FALSE}.
	 */
	public static final Boolean DEFAULT_V1_COMPILE_BASE_TEMPLATES = Boolean.FALSE;

	/**
	 * Compiler option specifying the project relative location of JET V1 templates.
	 */
	public static final String OPTION_V1_TEMPLATES_DIR = NS + "v1TemplatesDir"; //$NON-NLS-1$
	
	/**
	 * Default value for {@link #OPTION_V1_TEMPLATES_DIR}; value "{@value #DEFAULT_V1_TEMPLATES_DIR}".
	 */
	public static final String DEFAULT_V1_TEMPLATES_DIR = "templates"; //$NON-NLS-1$
	
	
	/**
	 * Folder name into which Java compiler will place its output of compiled JET templates
	 * @since 0.9.0
	 */
	public static final String OPTION_JAVA_OUTPUT_FOLDER = NS + "javaOutputFolder"; //$NON-NLS-1$
	
	/**
	 * Default value for {@link #DEFAULT_JAVA_OUTPUT_FOLDER}; value "{@value #DEFAULT_JAVA_OUTPUT_FOLDER}".
	 * @since 0.9.0
	 */
	public static final String DEFAULT_JAVA_OUTPUT_FOLDER = "bin"; //$NON-NLS-1$
	
	/**
	 * Indicates whether the JET Compiler will emit code requiring Java 5 source compliance.
	 * @since 0.9.0 
	 */
	public static final String OPTION_USE_JAVA5 = NS + "useJava5"; //$NON-NLS-1$
	
	/**
	 * Default value for {@link #OPTION_USE_JAVA5}; value: {@value #DEFAULT_USE_JAVA5}.
	 */
	public static final String DEFAULT_USE_JAVA5 = Boolean.FALSE.toString();
	
	/**
	 * Lazily initialized Map containing the default compiler options
	 */
	static Map defaultCompileOptions = null;

	/**
	 * Lock option used for compiler options initialization
	 */
	static final Object defaultCompileOptionsLock = new Object();

	/**
	 * Utility class - prevent instantiation
	 *
	 */
	private JETCompilerOptions() {

	}

	/**
	 * Return the default compiler options
	 * @return an unmodifiable map containing the default compiler options (keys) and their default values
	 * @see #OPTION_COMPILED_TEMPLATE_PACKAGE
	 * @see #OPTION_COMPILED_TEMPLATE_SRC_DIR
	 * @see #OPTION_SET_JAVA_FILES_AS_DERIVED
	 * @see #OPTION_TEMPLATE_EXT
	 */
	public static Map getDefaultCompilerOptions() {
		if (defaultCompileOptions == null) {
			Map map = new HashMap();
			map.put(OPTION_COMPILED_TEMPLATE_PACKAGE,
					DEFAULT_COMPILED_TEMPLATE_PACKAGE);
			map.put(OPTION_COMPILED_TEMPLATE_SRC_DIR,
					DEFAULT_COMPILED_TEMPLATE_SRC_DIR);
			map.put(OPTION_TEMPLATE_EXT, DEFAULT_TEMPLATE_EXT);
			map.put(OPTION_SET_JAVA_FILES_AS_DERIVED,
					DEFAULT_SET_JAVA_FILES_AS_DERIVED);
			map.put(OPTION_JET_SPECIFICATION_VERSION,
					DEFAULT_JET_SPECIFICATION_VERSION);
			map.put(OPTION_V1_COMPILE_BASE_TEMPLATES,
					DEFAULT_V1_COMPILE_BASE_TEMPLATES);
			map.put(OPTION_V1_BASE_TRANSFORMATION,
					DEFAULT_V1_BASE_TRANSFORMATION);
			map.put(OPTION_V1_TEMPLATES_DIR, DEFAULT_V1_TEMPLATES_DIR);
			map.put(OPTION_JAVA_OUTPUT_FOLDER, DEFAULT_JAVA_OUTPUT_FOLDER);
			map.put(OPTION_USE_JAVA5, DEFAULT_USE_JAVA5);

			synchronized (defaultCompileOptionsLock) {
				defaultCompileOptions = Collections.unmodifiableMap(map);
			}
		}
		return defaultCompileOptions;
	}

	/**
	 * Return the value of a string option, or the default value of the option if not specified in the pass options map
	 * @param options a compiler options map
	 * @param key a compiler option key
	 * @return the option value or default value
	 * @throws NullPointerException if <code>options</code> or <code>key</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>key</code> is not a know compiler option
	 */
	public static String getStringOption(Map options, String key) {
		if (options == null || key == null) {
			throw new NullPointerException();
		}
		if (!getDefaultCompilerOptions().containsKey(key)) {
			throw new IllegalArgumentException(key);
		}

		Object value = options.get(key);
		if (value == null) {
			value = getDefaultCompilerOptions().get(key);
		}

		return value == null ? null : value.toString();
	}

	/**
	 * Return the value of a boolean option, or the default value of the option if not specified in the pass options map
	 * @param options a compiler options map
	 * @param key a compiler option key
	 * @return the option value or default value
	 * @throws NullPointerException if <code>options</code> or <code>key</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>key</code> is not a know compiler option
	 */
	public static boolean getBooleanOption(Map options, String key) {
		if (options == null || key == null) {
			throw new NullPointerException();
		}
		if (!getDefaultCompilerOptions().containsKey(key)) {
			throw new IllegalArgumentException(key);
		}

		Object value = options.get(key);
		if (value == null) {
			value = getDefaultCompilerOptions().get(key);
		}
		return value == null ? false
				: value instanceof Boolean ? ((Boolean) value).booleanValue()
						: Boolean.valueOf(value.toString()).booleanValue();
	}

	/**
	 * Return the value of an integer option, or the default value of the option if not specified in the pass options map
	 * @param options a compiler options map
	 * @param key a compiler option key
	 * @return the option value or default value
	 * @throws NullPointerException if <code>options</code> or <code>key</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>key</code> is not a know compiler option
	 */
	public static int getIntOption(Map options,
			String key) {
		if (options == null || key == null) {
			throw new NullPointerException();
		}
		if (!getDefaultCompilerOptions().containsKey(key)) {
			throw new IllegalArgumentException(key);
		}

		Object value = options.get(key);
		if (value == null) {
			value = getDefaultCompilerOptions().get(key);
		}
		return value == null ? -1
				: value instanceof Integer ? ((Integer) value).intValue()
						: Integer.valueOf(value.toString()).intValue();
	}
}
