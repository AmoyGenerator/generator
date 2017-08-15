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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.runtime.RuntimeLoggerContextExtender;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.transform.JETLaunchConstants;
import org.eclipse.jet.ui.Activator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Main tab for JET Launch Configuraitons
 *
 */
public class JETTransformMainTab extends AbstractLaunchConfigurationTab {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	// input block controls
	private Text sourceField;
	private Button workspaceLocationButton;

	private final Listener fListener  = new Listener();;

	private Image tabImage;
	private Combo transformCombo;
	private Text transformName;
	private Text transformDescription;
	private Combo logLevelCombo;
	private String[] logLevels;


	private class Listener extends SelectionAdapter implements ModifyListener
	{

		public void widgetSelected(SelectionEvent e) {
			setDirty(true);
			final Object source = e.getSource();
			if(source == transformCombo) {
				handleTransformSelection();
			}
			updateLaunchConfigurationDialog();
		}
		public void modifyText(ModifyEvent e) {
			setDirty(true);
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * 
	 */
	public JETTransformMainTab() {
		super();
	}

	private void handleTransformSelection() {
		String id = transformCombo.getText();
		final IJETBundleDescriptor descriptor = JET2Platform.getJETBundleManager().getDescriptor(id);
		transformName.setText(descriptor.getName());
		transformDescription.setText(descriptor.getDescription().trim());
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		createInputBlock(composite);
		createTransformBlock(composite);
		createMessagesBlock(composite);
		
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	private void createMessagesBlock(Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(Messages.JETTransformMainTab_DisplayMessage_Gropu); 
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createLogLevelsSection(group);
	}

	private void createLogLevelsSection(Group parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.JETTransformMainTab_SeverityLabel); 
		
		logLevelCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		logLevelCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		logLevelCombo.setItems(getLogLevels());
		logLevelCombo.addSelectionListener(fListener);
	}

	/**
	 * @param composite
	 */
	private void createTransformBlock(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.JETTransformMainTab_TransformGroupLabel); 
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createTransformIdSection(group);
		createTransformNameSection(group);
		createTransformDescriptionSection(group);
	}

	private void createTransformDescriptionSection(Group parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.JETTransformMainTab_DescriptionLabel); 
		
		transformDescription = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		gd.heightHint = 40;
		transformDescription.setLayoutData(gd);
	}

	private void createTransformNameSection(Group parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.JETTransformMainTab_NameLabel); 
		
		transformName = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		transformName.setLayoutData(gd);
		
	}

	private void createTransformIdSection(Group parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.JETTransformMainTab_IdLabel); 
		
		transformCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		transformCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		transformCombo.setItems(JET2Platform.getJETBundleManager().getAllTransformIds());
		transformCombo.addSelectionListener(fListener);
	}

	private void createInputBlock(Composite parent)
	{
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.JETTransformMainTab_TransformInputGroup); 
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		sourceField = new Text(group, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		sourceField.setLayoutData(gridData);
		sourceField.addModifyListener(fListener);
		final String accessibleName = group.getText().replaceFirst("(?<!&)&", ""); //$NON-NLS-1$ //$NON-NLS-2$
		sourceField.getAccessible().addAccessibleListener(new AccessibleAdapter() {

				public void getName(AccessibleEvent e) {
					e.result = accessibleName;
				}
			});
		
		Composite buttonComposite = new Composite(group, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
        layout.marginWidth = 0;   
		layout.numColumns = 3;
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonComposite.setLayout(layout);
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setFont(parent.getFont());

		workspaceLocationButton= createPushButton(buttonComposite, Messages.JETTransformMainTab_BrowseButtonLabel, null);
		workspaceLocationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				IResource resource = chooseResource(getShell());
				if(resource != null) {
					sourceField.setText(resource.getFullPath().makeRelative().toString());
				}
			}
		});

	}

	private static IResource chooseResource(Shell shell) {
		IResource resource = null;
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				shell, new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());

		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.setTitle(Messages.JETTransformMainTab_SelectResourceDialogTitle);
		dialog.setMessage(Messages.JETTransformMainTab_SelectResourceDialogTitle);
		dialog.setValidator(new ISelectionStatusValidator() {
			public IStatus validate(Object[] selection) {
				if (selection.length > 0)
					return new Status(IStatus.OK, Activator.getDefault().getBundle().getSymbolicName(),
							IStatus.OK, "", null); //$NON-NLS-1$

				return new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(),
						IStatus.ERROR, "", null); //$NON-NLS-1$
			}
		});
		if (dialog.open() == ElementTreeSelectionDialog.OK) {
			resource = (IResource) dialog.getFirstResult();
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return Messages.LaunchWizard_MainTab_Name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			final String id = configuration.getAttribute(JETLaunchConstants.ID, EMPTY_STRING);
			transformCombo.setText(id);
			final IJETBundleDescriptor descriptor = JET2Platform.getJETBundleManager().getDescriptor(id);
			if(descriptor != null)
			{
				transformName.setText(descriptor.getName());
				transformDescription.setText(descriptor.getDescription().trim());
			}
		} catch (CoreException e) {
			transformCombo.setText(EMPTY_STRING);
		}
		try {
			sourceField.setText(configuration.getAttribute(JETLaunchConstants.SOURCE, EMPTY_STRING));
		} catch (CoreException e) {
			sourceField.setText(EMPTY_STRING);
		}
		
		int level;
		try {
			level = configuration.getAttribute(JETLaunchConstants.LOG_FILTER_LEVEL, RuntimeLoggerContextExtender.INFO_LEVEL);
		} catch (CoreException e) {
			level = RuntimeLoggerContextExtender.INFO_LEVEL;
		}
		logLevelCombo.setText(getLogLevelText(level));
	}

	private String[] getLogLevels() {
		if (logLevels == null) {
			logLevels = new String[] { 
					Messages.JETTransformMainTab_ErrorSeverity, 
					Messages.JETTransformMainTab_WarningSeverity, 
					Messages.JETTransformMainTab_InformationSeverity,
					Messages.JETTransformMainTab_TraceSeverity, 
					Messages.JETTransformMainTab_DebugSeverity, 
					};
		}
		return logLevels;
	}

	private int getLogLevelFromIndex(int selectionIndex) {
		// list is from highest (5) to lowest (1)
		return getLogLevels().length - selectionIndex;
	}
	
	private String getLogLevelText(int logLevel) {
		// log levels list are from highest (5) to lowest (1)
		return getLogLevels()[getLogLevels().length - logLevel];
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(JETLaunchConstants.ID, transformCombo.getText());
		
		configuration.setAttribute(JETLaunchConstants.SOURCE, sourceField.getText());
		
		configuration.setAttribute(JETLaunchConstants.LOG_FILTER_LEVEL, getLogLevelFromIndex(logLevelCombo.getSelectionIndex()));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
		
		IFile resource = null;
		String id = null;
		
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection iss = (IStructuredSelection) selection;
			Object firstElement = iss.getFirstElement();
			if(firstElement instanceof IFile) {
				resource = (IFile)firstElement;
			} else if (firstElement instanceof IAdaptable) {
				resource = (IFile) ((IAdaptable)firstElement).getAdapter(IFile.class);
			}
		}
		if(resource != null) {
			configuration.setAttribute(JETLaunchConstants.SOURCE,
					resource.getFullPath().makeRelative().toString());
			
			final IProject project = resource.getProject();
			final IJETBundleDescriptor descriptor = JET2Platform.getProjectDescription(project.getName());
			if(descriptor != null) {
				id = descriptor.getId();
				configuration.setAttribute(JETLaunchConstants.ID, id);
				
				
				
			}
		}
		configuration.setAttribute(JETLaunchConstants.LOG_FILTER_LEVEL, RuntimeLoggerContextExtender.INFO_LEVEL);
		
		configuration.rename(LaunchShortcut.generateLaunchName(id, resource));
	}

	public Image getImage() {
		if(tabImage == null) {
			ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/JET2LaunchIcon.gif"); //$NON-NLS-1$
			tabImage = imageDescriptor.createImage();
		}
		return tabImage;
	}
	
	public void dispose() {
		if(tabImage != null) {
			tabImage.dispose();
		}
		super.dispose();
	}

}
