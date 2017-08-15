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

package org.eclipse.jet.internal.ui.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * Launch shortcut for launching JET transforms on an XML document.
 *
 */
public class LaunchShortcut implements ILaunchShortcut {

	/**
	 * 
	 */
	public LaunchShortcut() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection, java.lang.String)
	 */
	public void launch(ISelection selection, String mode) {
		// TODO Auto-generated method stub
		if(!(selection instanceof IStructuredSelection)) {
			return;
		}
		IResource resource = (IResource) ((IStructuredSelection)selection).getFirstElement();
		JETLaunchHelper.findAndLauchForResource(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				ILaunchManager.RUN_MODE, resource);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart, java.lang.String)
	 */
	public void launch(IEditorPart editor, String mode) {
		final IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
		JETLaunchHelper.findAndLauchForResource(editor.getSite().getShell(), ILaunchManager.RUN_MODE, resource);
//		findAndLaunch(resource, mode);

	}

	public static String generateLaunchName(String id, IResource input)
	{
		return JETLaunchHelper.generateLaunchName(id, input, Messages.LaunchShortcut_DefaultLaunchName);
	}
}
