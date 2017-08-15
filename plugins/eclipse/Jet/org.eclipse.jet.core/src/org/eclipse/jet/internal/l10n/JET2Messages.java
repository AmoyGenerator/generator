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
 * $Id: JET2Messages.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.l10n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Access class for string resources.
 */
public class JET2Messages {
	private static final String BUNDLE_NAME = "org.eclipse.jet.internal.l10n.JET2Messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	private JET2Messages() {
	}

	public static String JET2Compiler_DeprecatedAttribute = getString("JET2Compiler_DeprecatedAttribute"); //$NON-NLS-1$

	public static String JET2Compiler_DeprecatedTag = getString("JET2Compiler_DeprecatedTag"); //$NON-NLS-1$

	public static String JET2Compiler_ErrorCreatingMarkers = getString("JET2Compiler_ErrorCreatingMarkers"); //$NON-NLS-1$

	public static String JET2Compiler_ErrorRemovingMarkers = getString("JET2Compiler_ErrorRemovingMarkers"); //$NON-NLS-1$

	public static String JET2Compiler_ErrorWritingJava = getString("JET2Compiler_ErrorWritingJava"); //$NON-NLS-1$

	public static String JET2Compiler_MissingDirectiveAttribute = getString("JET2Compiler_MissingDirectiveAttribute"); //$NON-NLS-1$

	public static String JET2Compiler_MissingEndTag = getString("JET2Compiler_MissingEndTag"); //$NON-NLS-1$

	public static final String JET2Compiler_MissingFile = getString("JET2Compiler_MissingFile"); //$NON-NLS-1$;

	public static final String JET2Compiler_StartDirectiveOutOfContext = getString("JET2Compiler_StartDirectiveOutOfContext"); //$NON-NLS-1$

	public static final String JET2Compiler_EndDirectiveOutOfContext = getString("JET2Compiler_EndDirectiveOutOfContext"); //$NON-NLS-1$

	public static String JET2Compiler_MissingRequiredAttribute = getString("JET2Compiler_MissingRequiredAttribute"); //$NON-NLS-1$

	public static String JET2Compiler_MissingStartTag = getString("JET2Compiler_MissingStartTag"); //$NON-NLS-1$

	public static String JET2Compiler_PrefixAlreadyAssigned = getString("JET2Compiler_PrefixAlreadyAssigned"); //$NON-NLS-1$

	public static String JET2Compiler_SameJavaClassAsOther = getString("JET2Compiler_SameJavaClassAsOther"); //$NON-NLS-1$

	public static String JET2Compiler_TagCannotHaveContent = getString("JET2Compiler_TagCannotHaveContent"); //$NON-NLS-1$

	public static String JET2Compiler_TagRequiresContent = getString("JET2Compiler_TagRequiresContent"); //$NON-NLS-1$

	public static String JET2Compiler_TagShouldBeEmptyFormat = getString("JET2Compiler_TagShouldBeEmptyFormat"); //$NON-NLS-1$

	public static String JET2Compiler_UnknownAttribute = getString("JET2Compiler_UnknownAttribute"); //$NON-NLS-1$

	public static String JET2Compiler_UnknownTagLibrary = getString("JET2Compiler_UnknownTagLibrary"); //$NON-NLS-1$

	public static String ASTCompilerParseListener_UnsupportedDirective = getString("ASTCompilerParseListener_UnsupportedDirective"); //$NON-NLS-1$

	public static String JET2Context_ExecutionCancelled = getString("JET2Context_ExecutionCancelled"); //$NON-NLS-1$

	public static String JET2Context_InvalidVariableName = getString("JET2Context_InvalidVariableName"); //$NON-NLS-1$

	public static String JET2Context_SuccessfulExecution = getString("JET2Context_SuccessfulExecution"); //$NON-NLS-1$

	public static String JET2Context_SuccessfulWithMessages = getString("JET2Context_SuccessfulWithMessages"); //$NON-NLS-1$

	public static String JET2Context_SuccessfulWithWarnings = getString("JET2Context_SuccessfulWithWarnings"); //$NON-NLS-1$

	public static String JET2Context_ErrorsInExecution = getString("JET2Context_ErrorsInExecution"); //$NON-NLS-1$

	public static String JET2Context_CouldNotFindTemplate = getString("JET2Context_CouldNotFindTemplate"); //$NON-NLS-1$

	public static String JET2Context_CommittingActions = getString("JET2Context_CommittingActions"); //$NON-NLS-1$

	public static String JET2Context_VariableNotFound = getString("JET2Context_VariableNotFound"); //$NON-NLS-1$

}
