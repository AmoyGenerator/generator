/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.internal.ui.l10n;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jet.internal.ui.l10n.messages"; //$NON-NLS-1$

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String CompilePreferencePage_BaseLocationsLabel;

	public static String CompilePreferencePage_CompileBaseTemplates;

	public static String CompilePreferencePage_DependenciesGroupLabel;

	public static String CompilePreferencePage_DeriveJavaFiles;

	public static String CompilePreferencePage_Description;

	public static String CompilePreferencePage_EnableProjectSettings;

	public static String CompilePreferencePage_FileExtensions;

	public static String CompilePreferencePage_InvalidBaseLocations;

	public static String CompilePreferencePage_JavaGenerationGroupLabel;

	public static String CompilePreferencePage_JavaPackage;

	public static String CompilePreferencePage_JET1_OPTION;

	public static String CompilePreferencePage_JET1OptionsGroupLabel;

	public static String CompilePreferencePage_JET2_Option;

	public static String CompilePreferencePage_JET2_OPTIONS_GROUP_LABEL;

	public static String CompilePreferencePage_JETComformanceGroupLabel;

	public static String CompilePreferencePage_OverrideTemplatesOption;

	public static String CompilePreferencePage_SourceFolder;

	public static String CompilePreferencePage_TemplatesDirLabel;

	public static String CompilePreferencePage_use_java5;

	public static String ExtensionListEditor_AddDialogErrorMsg;

	public static String ExtensionListEditor_AddDialogPrompt;

	public static String ExtensionListEditor_AddDialogTitle;

	public static String JETPreferencePage_Description;

	public static String JETPreferencePage_LocationsAddDialogTitle;

	public static String JETPreferencePage_LocationsLabel;

	public static String JETTransformMainTab_BrowseButtonLabel;

	public static String JETTransformMainTab_DebugSeverity;

	public static String JETTransformMainTab_DescriptionLabel;

	public static String JETTransformMainTab_DisplayMessage_Gropu;

	public static String JETTransformMainTab_ErrorSeverity;

	public static String JETTransformMainTab_IdLabel;

	public static String JETTransformMainTab_InformationSeverity;

	public static String JETTransformMainTab_NameLabel;

	public static String JETTransformMainTab_SelectResourceDialogTitle;

	public static String JETTransformMainTab_SeverityLabel;

	public static String JETTransformMainTab_TraceSeverity;

	public static String JETTransformMainTab_TransformGroupLabel;

	public static String JETTransformMainTab_TransformInputGroup;

	public static String JETTransformMainTab_WarningSeverity;

	public static String LaunchShortcut_DefaultLaunchName;

	public static String NewProjectWizard_WindowTitle;

	public static String NewProjectWizard_ErrorCreatingProject;

	public static String NewProjectWizard_MainPageTabText;

	public static String NewProjectWizard_MainPageTitle;

	public static String NewProjectWizard_MainPageDescription;

	public static String LaunchWizard_MainTab_Name;

	public static String JET2TemplateAccess_ErrorLoadingTemplateStore;

	public static String UnloadAction_DialogTitle;

	public static String UnloadAction_UnloadSuccessful;

	public static String UnloadAction_UnloadFailed;

	public static String UnloadAction_NotLoaded;

	public static String NewProjectWizard_ErrorInJETTransform;
}
