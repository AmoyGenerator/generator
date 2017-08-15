/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
 * $Id$
 */

package org.eclipse.jet.internal.ui.filters;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filter Java elements from the Package Explorer view for JET2 projects.
 *
 */
public class JavaFilter extends ViewerFilter {

	/**
	 * 
	 */
	public JavaFilter() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		final boolean isJET2Project = isJET2Project(parentElement);
		// Note: ClassPathContainer is a JDT internal class - it doesn't implement
		// any useful public interfacts (IAdapter and IWorkspaceAdapter only). To
		// avoid a direct internal dependency, we check the class name instead of
		// doing an instanceof. The risk is that JDT will refactor, and we will loose 
		// this feature.
		if (element.getClass().getName().endsWith(".ClassPathContainer") && isJET2Project) { //$NON-NLS-1$
				return false;
		}
		if(element instanceof IPackageFragmentRoot && isJET2Project) {
			return false;
		}
		return true;
	}

	/**
	 * @param element
	 * @return
	 * @throws CoreException
	 */
	private boolean isJET2Project(Object element) {
		if(element instanceof IJavaProject) {
			IJavaProject jp = (IJavaProject)element;
			try {
				return jp.getProject().hasNature(JET2Platform.JET2_NATURE_ID);
			} catch (CoreException e) {
				// No action required:
				// only happens if the project doesn't exist. But we won't get here unless the project does
			}
		}
		return false;
	}

}
