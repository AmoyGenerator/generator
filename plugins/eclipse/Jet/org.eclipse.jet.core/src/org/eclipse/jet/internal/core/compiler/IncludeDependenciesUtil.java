/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: IncludeDependenciesUtil.java,v 1.1 2007/04/04 14:53:54 pelder Exp $
 */
package org.eclipse.jet.internal.core.compiler;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jet.core.parser.ast.IncludedContent;
import org.eclipse.jet.core.parser.ast.JETASTVisitor;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;

/**
 * Utility for calculating include dependencies of a compilation unit
 */
public final class IncludeDependenciesUtil extends JETASTVisitor {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * Return the include dependencies for a compilation unit.
	 * @param cu a compilation unit
	 * @return a possibly empty array of template paths
	 */
	public static String[] getDependencies(JETCompilationUnit cu) {
		final IncludeDependenciesUtil util = new IncludeDependenciesUtil();
		cu.accept(util);

		return (String[]) util.dependencies.toArray(EMPTY_STRING_ARRAY);
	}

	private final Set dependencies = new HashSet();

	private IncludeDependenciesUtil() {
      // prevent instantiation
	}

	public boolean visit(IncludedContent content) {
		dependencies.add(content.getTemplatePath());
		return true;
	}
}
