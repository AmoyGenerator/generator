
package org.eclipse.jet.ui.newproject;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jet.core.compiler.JETCompilerOptions;
import org.eclipse.jet.ui.Activator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class NewJETProjectPage1 extends NewJETProjectPage1Controls {


	private String lastBaseID = null;
	
	public NewJETProjectPage1(String pageName) {
		super(pageName);
	}

	public void createControl(Composite parent) {
		// super.createControl guarantees all controls are created.
		super.createControl(parent);
		
		// initialize control data
		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		
		txtTemplateFolder.setText("templates"); //$NON-NLS-1$
		txtJETOutput.setText(preferenceStore.getDefaultString(JETCompilerOptions.OPTION_COMPILED_TEMPLATE_SRC_DIR));
		txtJavaOutput.setText(preferenceStore.getDefaultString(JETCompilerOptions.OPTION_JAVA_OUTPUT_FOLDER));
		txtDefJavaPackage.setText(preferenceStore.getDefaultString(JETCompilerOptions.OPTION_COMPILED_TEMPLATE_PACKAGE));
		
	}

	protected void txtTemplateFolder_modify(ModifyEvent e) {
		validatePage();
	}

	protected void txtJETOutput_modify(ModifyEvent e) {
		validatePage();
	}

	protected void txtJavaOutput_modify(ModifyEvent e) {
		validatePage();
	}

	protected void txtDefJavaPackage_modify(ModifyEvent e) {
		validatePage();
	}

	protected boolean validatePage() {
		final boolean parentValid = super.validatePage();
		if(!parentValid) {
			return false;
		}

		final String id = getBaseID();
		if(!id.equals(lastBaseID)) {
			lastBaseID = id;
			final String newDefPackage = id + ".compiled"; //$NON-NLS-1$
			txtDefJavaPackage.setText(newDefPackage);
		}
		
		if(!validateFolder(txtTemplateFolder)) {
			return false;
		}
		if(!validateFolder(txtJavaOutput)) {
			return false;
		}
		if(!validateFolder(txtJETOutput)) {
			return false;
		}
		final String defaultJavaPackage = getDefaultJavaPackage();
		final IStatus status = validateJavaPackage(defaultJavaPackage);
		if(!status.isOK()) {
			setErrorMessage(status.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * @param defaultJavaPackage
	 * @return
	 */
	private IStatus validateJavaPackage(final String defaultJavaPackage) {
		IStatus status = JavaConventions.validatePackageName(defaultJavaPackage);
		return status;
	}

	/**
	 * @param folderField
	 */
	private boolean validateFolder(final Text folderField) {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IStatus status = workspace.validatePath("/" + getProjectName() + "/" + folderField.getText().trim(), IResource.FOLDER); //$NON-NLS-1$ //$NON-NLS-2$
		if(!status.isOK()) {
			setErrorMessage(status.getMessage());
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return
	 */
	String getBaseID() {
		final String projectName = getProjectName();
		
		final String id = projectName.replaceAll("[^a-zA-Z0-9\\._]", "_");  //$NON-NLS-1$//$NON-NLS-2$
		return id;
	}

	public String getDefJavaPackage() {
		return txtDefJavaPackage.getText();
	}
	
	public String getTemplateFolder() {
		return txtTemplateFolder.getText().trim();
	}
	
	public String getJETOutputFolder() {
		return txtJETOutput.getText().trim();
	}
	
	public String getJavaOutputFolder() {
		return txtJavaOutput.getText().trim();
	}
	
	public String getDefaultJavaPackage() {
		return txtDefJavaPackage.getText().trim();
	}
	
}
