/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
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
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.ui.prefs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.JETPreferences;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * @author pelder
 *
 */
public class JETPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage, IWorkbenchPropertyPage {

	private IAdaptable element;

	/**
	 * @param style
	 */
	public JETPreferencePage() {
		super(GRID);
		
//		setDescription(Messages.JETPreferencePage_Description);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		if(element != null)
		{
			setPreferenceStore(new ScopedPreferenceStore(new ProjectScope((IProject)element), JET2Platform.PLUGIN_ID));
		}
		else {
			setPreferenceStore(new ScopedPreferenceStore(new InstanceScope(), JET2Platform.PLUGIN_ID));
		}
		addField(new PathEditor(JETPreferences.ADDITIONAL_TEMPLATE_JAR_LOCATIONS,
				Messages.JETPreferencePage_LocationsLabel,
				Messages.JETPreferencePage_LocationsAddDialogTitle,
				getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	public IAdaptable getElement() {
		return element;
	}

	public void setElement(IAdaptable element) {
		this.element = element;
	}

}
