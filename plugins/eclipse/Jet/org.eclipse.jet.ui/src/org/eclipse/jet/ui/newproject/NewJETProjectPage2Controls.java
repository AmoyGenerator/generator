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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public abstract class NewJETProjectPage2Controls 
	extends WizardPage {

	protected Text txtID;
	protected Text txtName;
	protected Text txtDescription;
	protected Text txtTemplateLoader;
	protected Group grpExtensions;
	protected Button cbxExtends;
	protected Label lblBaseTx;
	protected Combo ddlBaseTx;
	

	protected NewJETProjectPage2Controls(String pageName) {
		super(pageName);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        // working variables for creating grid layout info
		GridLayout _gl;
		GridData _gd;

        Composite _compositetop;
		_compositetop = new Composite(parent, SWT.NULL);
        _compositetop.setFont(parent.getFont());
    	setControl(_compositetop);
        org.eclipse.ui.PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.eclipse.jet.ui.trjt0020"); //$NON-NLS-1$


        // layout _compositetop within parent
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        _gd.verticalAlignment = GridData.FILL;
        _gd.grabExcessVerticalSpace = true;
        _compositetop.setLayoutData(_gd);

        // Initialize _compositetop layout
        _gl = new GridLayout();
        _gl.numColumns = 1;
        _compositetop.setLayout(_gl);

        Group _groupgrpTransProps;
		_groupgrpTransProps = new Group(_compositetop, SWT.NONE);
		_groupgrpTransProps.setText(Messages.NewJETProjectPage2Controls__groupgrpTransProps_text); 
        _groupgrpTransProps.setFont(_compositetop.getFont());


        // layout _groupgrpTransProps within _compositetop
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        _groupgrpTransProps.setLayoutData(_gd);

        // Initialize _groupgrpTransProps layout
        _gl = new GridLayout();
        _gl.numColumns = 2;
        _groupgrpTransProps.setLayout(_gl);

        Label _labellblID;
		_labellblID = new Label(_groupgrpTransProps, SWT.NONE);
		_labellblID.setText(Messages.NewJETProjectPage2Controls__labellblID_text);
        _labellblID.setFont(_groupgrpTransProps.getFont());



		txtID = new Text(_groupgrpTransProps, SWT.BORDER);
        txtID.setFont(_groupgrpTransProps.getFont());


        // layout txtID within _groupgrpTransProps
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtID.setLayoutData(_gd);
		// register event handlers for txtID	
		txtID.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				txtID_modify(e);
			}});

        Label _labellblName;
		_labellblName = new Label(_groupgrpTransProps, SWT.NONE);
		_labellblName.setText(Messages.NewJETProjectPage2Controls__labellblName_text);
        _labellblName.setFont(_groupgrpTransProps.getFont());



		txtName = new Text(_groupgrpTransProps, SWT.BORDER);
        txtName.setFont(_groupgrpTransProps.getFont());


        // layout txtName within _groupgrpTransProps
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtName.setLayoutData(_gd);

        Label _labellblDescription;
		_labellblDescription = new Label(_groupgrpTransProps, SWT.NONE);
		_labellblDescription.setText(Messages.NewJETProjectPage2Controls__labellblDescription_text);
        _labellblDescription.setFont(_groupgrpTransProps.getFont());



		txtDescription = new Text(_groupgrpTransProps, SWT.BORDER);
        txtDescription.setFont(_groupgrpTransProps.getFont());


        // layout txtDescription within _groupgrpTransProps
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtDescription.setLayoutData(_gd);

        Label _labellblTemplateLoader;
		_labellblTemplateLoader = new Label(_groupgrpTransProps, SWT.NONE);
		_labellblTemplateLoader.setText(Messages.NewJETProjectPage2Controls__labellblTemplateLoader_text);
        _labellblTemplateLoader.setFont(_groupgrpTransProps.getFont());



		txtTemplateLoader = new Text(_groupgrpTransProps, SWT.BORDER);
        txtTemplateLoader.setFont(_groupgrpTransProps.getFont());


        // layout txtTemplateLoader within _groupgrpTransProps
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        txtTemplateLoader.setLayoutData(_gd);
		// register event handlers for txtTemplateLoader	
		txtTemplateLoader.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				txtTemplateLoader_modify(e);
			}});

		grpExtensions = new Group(_compositetop, SWT.NONE);
		grpExtensions.setText(Messages.NewJETProjectPage2Controls_grpExtensions_text); 
        grpExtensions.setFont(_compositetop.getFont());


        // layout grpExtensions within _compositetop
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        grpExtensions.setLayoutData(_gd);

        // Initialize grpExtensions layout
        _gl = new GridLayout();
        _gl.numColumns = 2;
        grpExtensions.setLayout(_gl);

		cbxExtends = new Button(grpExtensions, SWT.CHECK | SWT.RIGHT);
		cbxExtends.setText(Messages.NewJETProjectPage2Controls_cbxExtends_text);
 
        cbxExtends.setFont(grpExtensions.getFont());


        // layout cbxExtends within grpExtensions
        _gd = new GridData();
        _gd.horizontalSpan = 2;
        cbxExtends.setLayoutData(_gd);
		// register event handlers for cbxExtends	
		cbxExtends.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				cbxExtends_selection(e);
			}
		});

		lblBaseTx = new Label(grpExtensions, SWT.NONE);
		lblBaseTx.setText(Messages.NewJETProjectPage2Controls_lblBaseTx_text);
        lblBaseTx.setFont(grpExtensions.getFont());


        // layout lblBaseTx within grpExtensions
        _gd = new GridData();
        _gd.horizontalIndent = convertWidthInCharsToPixels(4);
        lblBaseTx.setLayoutData(_gd);

		ddlBaseTx = new Combo(grpExtensions, SWT.RIGHT
				| SWT.DROP_DOWN
				| SWT.READ_ONLY
		);
        ddlBaseTx.setFont(grpExtensions.getFont());


        // layout ddlBaseTx within grpExtensions
        _gd = new GridData();
        _gd.horizontalAlignment = GridData.FILL;
        _gd.grabExcessHorizontalSpace = true;
        ddlBaseTx.setLayoutData(_gd);
		// register event handlers for ddlBaseTx	
		ddlBaseTx.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ddlBaseTx_modify(e);
			}});

 
	}


	/**
	 * Modify event handler for {@link #txtID}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void txtID_modify(ModifyEvent e);

	/**
	 * Modify event handler for {@link #txtTemplateLoader}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void txtTemplateLoader_modify(ModifyEvent e);

	/**
	 * Selection event handler for {@link #cbxExtends}.
	 * @param e an event containing information on the selection
	 * @see SelectionListener
	 * @see SelectionListener#widgetSelected(SelectionEvent)
	 */
	protected abstract void cbxExtends_selection(SelectionEvent e);

	/**
	 * Modify event handler for {@link #ddlBaseTx}.
	 * 
	 * @param e the Modify Event
	 * @see ModifyListener
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	protected abstract void ddlBaseTx_modify(ModifyEvent e);



}
