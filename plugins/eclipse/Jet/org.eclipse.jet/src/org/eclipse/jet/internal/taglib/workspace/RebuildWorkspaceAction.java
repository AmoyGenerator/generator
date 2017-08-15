/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.internal.taglib.workspace;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.AbstractWorkspaceAction;

/*
 *  Begin custom imports
	 */

import org.eclipse.core.resources.ResourcesPlugin;

/*
 *  End custom imports
 */

public class RebuildWorkspaceAction extends AbstractWorkspaceAction {

	/*
	 *  Begin custom declarations
	 */

	/*
	 *  End custom declarations
	 */

	/*
	 *  Begin constructor
	 */

	public RebuildWorkspaceAction(TagInfo tagInfo, String templatePath) {
		super(tagInfo, templatePath);
	}

	/*
	 *  End constructor
	 */

	public IResource getResource() throws JET2TagException {
		/*
		 *  Begin getResource logic
		 */

		return null;

		/*
		 *  End getResource logic
		 */
	}

	public boolean requiresValidateEdit() throws JET2TagException {
		/*
		 *  Begin requiresValidate logic
		 */

		return false;

		/*
		 *  End requiresValidate logic
		 */
	}

	public void performAction(IProgressMonitor monitor) throws JET2TagException {
		/*
		 *  Begin action logic
		 */

		ResourcesPlugin.getWorkspace().checkpoint(true);

		/*
		 *  End action logic
		 */
	}

	/*
	 *  Begin custom logic
	 */

	/*
	 *  End custom logic
	 */

}
