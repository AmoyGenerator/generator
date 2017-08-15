/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * /
 *******************************************************************************/

package org.eclipse.jet.ui.newproject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jet.JET2Platform;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

public class NewJETProjectPage2 extends NewJETProjectPage2Controls {


	private NewJETProjectPage1 page1;
	private boolean showExtensionsGroup;


	public NewJETProjectPage2(String pageName) {
		super(pageName);
		setTitle(Messages.NewJETProjectPage2_title);
		setDescription(Messages.NewJETProjectPage2_description);
	}

	public void createControl(Composite parent) {
		// super.createControl guarantees all controls are created.
		super.createControl(parent);
		
		// initialize control data
		setExtends(false);
		
		final String[] allIDs = JET2Platform.getJETBundleManager().getAllTransformIds();
		for (int i = 0; i < allIDs.length; i++) {
			ddlBaseTx.add(allIDs[i]);
		}
		
		if(!showExtensionsGroup) {
			final boolean visible = false;
			grpExtensions.setVisible(visible);
			ddlBaseTx.setVisible(visible);
			cbxExtends.setVisible(visible);
			lblBaseTx.setVisible(visible);
		}
		
		setPageComplete(validatePage());
	}
	
	private boolean validatePage() {
		if(!validateTranformID()) {
			return false;
		}
		if(!validateTemplateLoader()) {
			return false;
		}
		if(cbxExtends.getSelection() && cbxExtends.isVisible() && ddlBaseTx.getText().trim().length() == 0) {
			return false;
		}
		
		setMessage(null);
		setErrorMessage(null);
		return true;
	}

	private boolean validateTemplateLoader() {
		final IStatus status = JavaConventions.validateJavaTypeName(getTemplateLoader());
		if(status.getSeverity() == IStatus.ERROR) {
			setErrorMessage(status.getMessage());
			return false;
		}
		return true;
	}

	private boolean validateTranformID() {
		final String transformID = getTransformID();
		if(!transformID.matches("[a-zA-Z0-9\\._]*")) { //$NON-NLS-1$
			setErrorMessage(Messages.NewJETProjectPage2_InvalidID);
			return false;
		}
		return true;
	}

	protected void txtID_modify(ModifyEvent e) {
		setPageComplete(validatePage());
	}

	protected void txtTemplateLoader_modify(ModifyEvent e) {
		setPageComplete(validatePage());
	}

	private void setExtends(boolean setExtends) {
		if(setExtends != cbxExtends.getSelection()) {
			cbxExtends.setSelection(setExtends);
		}
		lblBaseTx.setEnabled(setExtends);
		ddlBaseTx.setEnabled(setExtends);
		
	}

	public void setJETProjectPage1(NewJETProjectPage1 page1) {
		this.page1 = page1;
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jet.ui.newproject.NewJETProjectPage2Controls#cbxExtends_selection(org.eclipse.swt.events.SelectionEvent)
	 */
	protected void cbxExtends_selection(SelectionEvent e) {
		final boolean selected = cbxExtends.getSelection();
		setExtends(selected);
		if(selected) {
			ddlBaseTx.setFocus();
		}
		setPageComplete(validatePage());
		getContainer().updateButtons();
	}


	protected void ddlBaseTx_modify(ModifyEvent e) {
		setPageComplete(validatePage());
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible && page1 != null) {
			txtTemplateLoader.setText(page1.getDefJavaPackage() + "._jet_transformation"); //$NON-NLS-1$
			txtID.setText(page1.getBaseID());
			txtName.setText(page1.getBaseID());
			txtID.setFocus();
		}
	}
	
	public boolean isExtension() {
		return cbxExtends.getSelection();
	}
	
	public String getTransformID() {
		return txtID.getText().trim();
	}
	
	public String getTransformName() {
		return txtName.getText().trim();
	}
	
	public String getTransformDescription() {
		return txtDescription.getText().trim();
	}
	
	public String getTemplateLoader() {
		return txtTemplateLoader.getText().trim();
	}
	
	public String getBaseTransformID() {
		return ddlBaseTx.getText().trim();
	}
	
	void showExtensionsGroup(boolean show) {
		this.showExtensionsGroup = show;
	}
}
