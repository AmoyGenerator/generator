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
package org.eclipse.jet.internal.ui.popup.actions;

import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.runtime.JETBundleInstaller;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class UnloadAction implements IObjectActionDelegate, IActionDelegate {

	private String bundleName;
	/**
	 * Constructor for Action1.
	 */
	public UnloadAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		Shell shell = new Shell();
		final JETBundleInstaller mgr = InternalJET2Platform.getDefault().getJETBundleInstaller();
		Bundle bundle = Platform.getBundle(bundleName);
		final String DialogTitle = Messages.UnloadAction_DialogTitle;
		if(bundle != null) {
		try {
			mgr.uninstallBundle(bundle);
			MessageDialog.openInformation(
					shell,
					DialogTitle,
					MessageFormat.format(Messages.UnloadAction_UnloadSuccessful, new Object[] {bundleName}));
		} catch (BundleException e) {
			e.printStackTrace();
			MessageDialog.openInformation(
					shell,
					DialogTitle,
					MessageFormat.format(Messages.UnloadAction_UnloadFailed, new Object[] {bundleName, e.toString()}));
		}}
		else {
			MessageDialog.openInformation(
					shell,
					DialogTitle,
					MessageFormat.format(Messages.UnloadAction_NotLoaded, new Object[] {bundleName}));
			
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IStructuredSelection se = (IStructuredSelection) selection;
		
		final Object firstElement = se.getFirstElement();
		if(firstElement instanceof IProject) {
			IProject project =  (IProject) firstElement;
			bundleName = project.getName();
		}
	}

}
