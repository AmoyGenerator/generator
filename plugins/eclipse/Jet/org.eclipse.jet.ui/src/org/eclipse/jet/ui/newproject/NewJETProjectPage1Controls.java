package org.eclipse.jet.ui.newproject;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;    
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public abstract class NewJETProjectPage1Controls 
	extends WizardNewProjectCreationPage {

	protected Text txtTemplateFolder;
	protected Text txtJETOutput;
	protected Text txtJavaOutput;
	protected Text txtDefJavaPackage;
	

	protected NewJETProjectPage1Controls(String pageName) {
		super(pageName);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
        // retarget parent to the control composite established by the base class
        parent = (Composite)getControl();
        initializeDialogUnits(parent);

        // working variables for creating grid layout info
		GridLayout _gl;
		GridData _gd;

        Group _groupgProjSet;
		_groupgProjSet = new Group(parent, SWT.NONE);
		_groupgProjSet.setText(Messages.NewJETProjectPage1Controls__groupgProjSet_text); 
        _groupgProjSet.setFont(parent.getFont());
        org.eclipse.ui.PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.eclipse.jet.ui.trjt0010"); //$NON-NLS-1$


        // layout _groupgProjSet within parent
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        _groupgProjSet.setLayoutData(_gd);

        // Initialize _groupgProjSet layout
        _gl = new GridLayout();
        _gl.numColumns = 2;
        _groupgProjSet.setLayout(_gl);

        Label _labellblTemplateFolder;
		_labellblTemplateFolder = new Label(_groupgProjSet, SWT.NONE);
		_labellblTemplateFolder.setText(Messages.NewJETProjectPage1Controls__labellblTemplateFolder_text);
        _labellblTemplateFolder.setFont(_groupgProjSet.getFont());



		txtTemplateFolder = new Text(_groupgProjSet, SWT.BORDER);
        txtTemplateFolder.setFont(_groupgProjSet.getFont());


        // layout txtTemplateFolder within _groupgProjSet
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtTemplateFolder.setLayoutData(_gd);
		// register event handlers for txtTemplateFolder	
		txtTemplateFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				txtTemplateFolder_modify(e);
			}});

        Label _labellblJETOutput;
		_labellblJETOutput = new Label(_groupgProjSet, SWT.NONE);
		_labellblJETOutput.setText(Messages.NewJETProjectPage1Controls__labellblJETOutput_text);
        _labellblJETOutput.setFont(_groupgProjSet.getFont());



		txtJETOutput = new Text(_groupgProjSet, SWT.BORDER);
        txtJETOutput.setFont(_groupgProjSet.getFont());


        // layout txtJETOutput within _groupgProjSet
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtJETOutput.setLayoutData(_gd);
		// register event handlers for txtJETOutput	
		txtJETOutput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				txtJETOutput_modify(e);
			}});

        Label _labellblJavaOutput;
		_labellblJavaOutput = new Label(_groupgProjSet, SWT.NONE);
		_labellblJavaOutput.setText(Messages.NewJETProjectPage1Controls__labellblJavaOutput_text);
        _labellblJavaOutput.setFont(_groupgProjSet.getFont());



		txtJavaOutput = new Text(_groupgProjSet, SWT.BORDER);
        txtJavaOutput.setFont(_groupgProjSet.getFont());


        // layout txtJavaOutput within _groupgProjSet
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtJavaOutput.setLayoutData(_gd);
		// register event handlers for txtJavaOutput	
		txtJavaOutput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				txtJavaOutput_modify(e);
			}});

        Label _labellblDefJavaPackage;
		_labellblDefJavaPackage = new Label(_groupgProjSet, SWT.NONE);
		_labellblDefJavaPackage.setText(Messages.NewJETProjectPage1Controls__labellblDefJavaPackage_text);
        _labellblDefJavaPackage.setFont(_groupgProjSet.getFont());



		txtDefJavaPackage = new Text(_groupgProjSet, SWT.BORDER);
        txtDefJavaPackage.setFont(_groupgProjSet.getFont());


        // layout txtDefJavaPackage within _groupgProjSet
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtDefJavaPackage.setLayoutData(_gd);
		// register event handlers for txtDefJavaPackage	
		txtDefJavaPackage.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				txtDefJavaPackage_modify(e);
			}});

 
	}


	/**
	 * Modify event handler for {@link #txtTemplateFolder}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void txtTemplateFolder_modify(ModifyEvent e);

	/**
	 * Modify event handler for {@link #txtJETOutput}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void txtJETOutput_modify(ModifyEvent e);

	/**
	 * Modify event handler for {@link #txtJavaOutput}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void txtJavaOutput_modify(ModifyEvent e);

	/**
	 * Modify event handler for {@link #txtDefJavaPackage}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void txtDefJavaPackage_modify(ModifyEvent e);



}
