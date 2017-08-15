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

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.core.compiler.JETCompilerOptions;
import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.internal.JETPreferences;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * @author pelder
 *
 */
public class CompilePreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage, IWorkbenchPropertyPage {

	private IProject element;
	private StringFieldEditor packageFieldEditor;
	private ExtensionListEditor extFieldEditor;
	private StringFieldEditor srcFolderFieldEditor;
	private StringFieldEditor templatesFolderFieldEditor;
	private BooleanFieldEditor derivedJavaFieldEditor;
	private BooleanFieldEditor projectSpecificSettingsEditor = null;
	private RadioGroupFieldEditor jetVersionFieldEditor;
	private BooleanFieldEditor compileBaseTemplatesFieldEditor = null;
	private StringFieldEditor baseTransformationFieldEditor = null;
	private Composite javaGenerationGroup;
	private Composite v1OptionsGroup;
	private Composite v2OptionsGroup;
	private boolean v1OptionsEnabled;
	private BooleanFieldEditor useJava5FieldEditor;

	/**
	 * @param style
	 */
	public CompilePreferencePage() {
		super(GRID);
//		setDescription(Messages.CompilePreferencePage_Description);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		
		// determine whether the page is showing the workspace scope (Windows > Preferences)
		// or in project scope (Project properties)
		final boolean projectSettings = element != null;
		if(projectSettings) {
			setPreferenceStore(new ScopedPreferenceStore(new ProjectScope(element), JET2Platform.PLUGIN_ID));
		} else {
			setPreferenceStore(new ScopedPreferenceStore(new InstanceScope(), JET2Platform.PLUGIN_ID));
		}

		if(projectSettings) {
			// add a check box to use project specific settings
			projectSpecificSettingsEditor = new BooleanFieldEditor(JETPreferences.PROJECT_SPECIFIC_SETTINGS, 
											Messages.CompilePreferencePage_EnableProjectSettings, getFieldEditorParent());
			addField(projectSpecificSettingsEditor);
			Label horizontalLine= new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
			horizontalLine.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
			horizontalLine.setFont(getFieldEditorParent().getFont());
		}

		// create "global" option editors...
		jetVersionFieldEditor = new RadioGroupFieldEditor(JETCompilerOptions.OPTION_JET_SPECIFICATION_VERSION, Messages.CompilePreferencePage_JETComformanceGroupLabel, 1,
				new String[][] {
				  {Messages.CompilePreferencePage_JET1_OPTION, String.valueOf(JETAST.JET_SPEC_V1)}, 
				  {Messages.CompilePreferencePage_JET2_Option, String.valueOf(JETAST.JET_SPEC_V2)}, 
				},
				getFieldEditorParent(), true);
		addField(jetVersionFieldEditor);
		
		v1OptionsEnabled = getPreferenceStore().getInt(JETCompilerOptions.OPTION_JET_SPECIFICATION_VERSION) == JETAST.JET_SPEC_V1;
		
		

		// common generation group
		javaGenerationGroup = createGroup(Messages.CompilePreferencePage_JavaGenerationGroupLabel);
		
		srcFolderFieldEditor = new StringFieldEditor(JETCompilerOptions.OPTION_COMPILED_TEMPLATE_SRC_DIR, 
								Messages.CompilePreferencePage_SourceFolder, javaGenerationGroup);
		addField(srcFolderFieldEditor);
		
		derivedJavaFieldEditor = new BooleanFieldEditor(JETCompilerOptions.OPTION_SET_JAVA_FILES_AS_DERIVED, 
				Messages.CompilePreferencePage_DeriveJavaFiles, javaGenerationGroup);
		addField(derivedJavaFieldEditor);

		useJava5FieldEditor = new BooleanFieldEditor(JETCompilerOptions.OPTION_USE_JAVA5, Messages.CompilePreferencePage_use_java5, javaGenerationGroup);
		addField(useJava5FieldEditor);

		// v1 options group
		v1OptionsGroup = createGroup(Messages.CompilePreferencePage_JET1OptionsGroupLabel);
		templatesFolderFieldEditor = new StringFieldEditor(JETCompilerOptions.OPTION_V1_TEMPLATES_DIR, 
				Messages.CompilePreferencePage_TemplatesDirLabel, v1OptionsGroup);
		addField(templatesFolderFieldEditor);
		if(projectSettings) {
			baseTransformationFieldEditor = new StringFieldEditor(JETCompilerOptions.OPTION_V1_BASE_TRANSFORMATION, 
					Messages.CompilePreferencePage_BaseLocationsLabel, 
					StringFieldEditor.UNLIMITED, 
					StringFieldEditor.VALIDATE_ON_KEY_STROKE, 
					v1OptionsGroup) {
				protected boolean doCheckState() {
					String trimmedValue = getStringValue().trim();
					return trimmedValue.length() == 0 || isValidURIList(trimmedValue);
				}

			};
			baseTransformationFieldEditor.setErrorMessage(Messages.CompilePreferencePage_InvalidBaseLocations);
			addField(baseTransformationFieldEditor);
			
			compileBaseTemplatesFieldEditor = new BooleanFieldEditor(JETCompilerOptions.OPTION_V1_COMPILE_BASE_TEMPLATES, Messages.CompilePreferencePage_CompileBaseTemplates, v1OptionsGroup);
			addField(compileBaseTemplatesFieldEditor);
			
		}

		// v2 options group
		v2OptionsGroup = createGroup(Messages.CompilePreferencePage_JET2_OPTIONS_GROUP_LABEL);
		
		extFieldEditor = new ExtensionListEditor(JETCompilerOptions.OPTION_TEMPLATE_EXT, 
				Messages.CompilePreferencePage_FileExtensions, v2OptionsGroup);
		addField(extFieldEditor);
		
		packageFieldEditor = new StringFieldEditor(JETCompilerOptions.OPTION_COMPILED_TEMPLATE_PACKAGE, 
				Messages.CompilePreferencePage_JavaPackage, v2OptionsGroup);
		addField(packageFieldEditor);



	}
	
	private boolean isValidURIList(String uriList) {
		String[] uriStrings = uriList.split(","); //$NON-NLS-1$
		boolean ok = true;
		for (int i = 0; i < uriStrings.length; i++) {
			try {
				new URI(uriStrings[i]);
			} catch (URISyntaxException e) {
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * @param groupTitle
	 * @return
	 */
	private Composite createGroup(final String groupTitle) {
		
		final Group group = new Group(getFieldEditorParent(), SWT.NONE);
		group.setFont(getFieldEditorParent().getFont());
		group.setText(groupTitle); 
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		setGroupLayout(group, 2);

		return group;
	}

	/**
	 * @param group
	 * @param numColumns TODO
	 */
	private void setGroupLayout(final Composite group, int numColumns) {
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		group.setLayout(layout);
	}

	/**
	 * 
	 */
	private void updateControlEnablement() {
		final boolean projectSettings = projectSpecificSettingsEditor != null;
		// enable "global" options
		boolean enabledGlobalSettings =  !projectSettings || projectSpecificSettingsEditor.getBooleanValue();
		jetVersionFieldEditor.setEnabled(enabledGlobalSettings, getFieldEditorParent());
		srcFolderFieldEditor.setEnabled(enabledGlobalSettings, javaGenerationGroup);
		derivedJavaFieldEditor.setEnabled(enabledGlobalSettings, javaGenerationGroup);
		useJava5FieldEditor.setEnabled(enabledGlobalSettings, javaGenerationGroup);
		if(!enabledGlobalSettings) {
			srcFolderFieldEditor.loadDefault();
			derivedJavaFieldEditor.loadDefault();
			jetVersionFieldEditor.loadDefault();
			useJava5FieldEditor.loadDefault();
		}
		
		// Enable JET1 options
		if(projectSettings) {
			boolean enableV1Options = v1OptionsEnabled && enabledGlobalSettings;
			baseTransformationFieldEditor.setEnabled(enableV1Options, v1OptionsGroup);
			compileBaseTemplatesFieldEditor.setEnabled(enableV1Options, v1OptionsGroup);
			templatesFolderFieldEditor.setEnabled(enableV1Options, v1OptionsGroup);
			if(!enableV1Options) {
				baseTransformationFieldEditor.loadDefault();
				compileBaseTemplatesFieldEditor.loadDefault();
				templatesFolderFieldEditor.loadDefault();
			}
		}

		// Enable JET2 options
		if(projectSettings) {
			boolean enableV2Options = !v1OptionsEnabled && enabledGlobalSettings;
			extFieldEditor.setEnabled(enableV2Options, v2OptionsGroup);
			packageFieldEditor.setEnabled(enableV2Options, v2OptionsGroup);
			if(!enableV2Options) {
				extFieldEditor.loadDefault();
				packageFieldEditor.loadDefault();
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	public IAdaptable getElement() {
		return element;
	}

	public void setElement(IAdaptable element) {
		this.element = (IProject)element;
	}

	protected void initialize() {
		super.initialize();
		if(projectSpecificSettingsEditor != null)
		{
			updateControlEnablement();
		}
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if(event.getSource() == projectSpecificSettingsEditor && FieldEditor.VALUE.equals(event.getProperty())) {
			updateControlEnablement();
		} else if(event.getSource() == jetVersionFieldEditor && FieldEditor.VALUE.equals(event.getProperty())) {
			v1OptionsEnabled = String.valueOf(JETAST.JET_SPEC_V1).equals(event.getNewValue());
			updateControlEnablement();
		} 
	}
	
	protected void performDefaults() {
		super.performDefaults();
		if (projectSpecificSettingsEditor != null) {
			updateControlEnablement();
		}

	}
	
	public boolean performOk() {
		final boolean ok = super.performOk();
		if(ok) {
			try {
				if(element != null)
				{
					element.build(IncrementalProjectBuilder.CLEAN_BUILD, null);
				}
				else
				{
					ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.CLEAN_BUILD, null);
				}
			} catch (CoreException e) {
				// build failed, we don't care
			}
		}
		return ok;
	}
}
