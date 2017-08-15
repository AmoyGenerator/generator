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

package org.eclipse.jet.internal.ui.prefs;

import java.util.regex.Pattern;

import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;


/**
 * @author pelder
 *
 */
public class ExtensionListEditor extends ListEditor {

	/**
	 * 
	 */
	public ExtensionListEditor(String name, String labelText, Composite parent) {
        init(name, labelText);
        createControl(parent);
	}

	protected String createList(String[] items) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			if(i != 0)
			{
				buffer.append(","); //$NON-NLS-1$
			}
			buffer.append(items[i]);
		}
		return buffer.toString();
	}

	protected String getNewInputObject() {
		InputDialog dialog = new InputDialog(getShell(), Messages.ExtensionListEditor_AddDialogTitle, Messages.ExtensionListEditor_AddDialogPrompt, "", new IInputValidator() { //$NON-NLS-1$

			public String isValid(String newText) {
				return !Pattern.matches("(\\p{L}|\\d)+", newText) ? //$NON-NLS-1$
						Messages.ExtensionListEditor_AddDialogErrorMsg : null; 
			}});
		final int result = dialog.open();
		return result == InputDialog.OK ? dialog.getValue() : null;
	}

	protected String[] parseString(String stringList) {
		return stringList.split(","); //$NON-NLS-1$
	}

}
