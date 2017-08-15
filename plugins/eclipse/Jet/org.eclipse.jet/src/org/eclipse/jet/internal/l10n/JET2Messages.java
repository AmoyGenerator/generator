/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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
package org.eclipse.jet.internal.l10n;


import org.eclipse.osgi.util.NLS;


/**
 * Access class for string resources.
 */
public class JET2Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.jet.internal.l10n.JET2Messages"; //$NON-NLS-1$

  private JET2Messages()
  {
  }

  static
  {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, JET2Messages.class);
  }

  public static String AbstractJavaFileTag_CouldNotWrite_UnknownSourceFolder;

  public static String AbstractJavaFileTag_EmptyAttributeNotAllowed;

  public static String AnyTag_IllegalParent;

  public static String AnyTag_MutuallyExclusiveAttributes;

  public static String ASTCompilerParseListener_UnsupportedDirective;

  public static String JavaActionsUtil_CannotFindSourceFolder;

  public static String JavaActionsUtil_InvalidSourceFolder;

  public static String JavaActionsUtil_RequiresAttrOrContainerTag;

  public static String JET2Builder_CompilingChanged;

  public static String JET2Builder_Compiling;

  public static String JET2Builder_Cleaning;

  public static String JET2Builder_CompilingAll;

  public static String JET2BundleBuildTool_CreatingBundle;

  public static String JET2BundleBuildTool_CreatingJar;

  public static String JET2Compiler_TagShouldBeEmptyFormat;

  public static String JET2Context_ExecutionCancelled;

  public static String JET2Context_InvalidVariableName;

  public static String JETStreamMonitor_Debug;

  public static String JETStreamMonitor_Error;

  public static String JETStreamMonitor_Trace;

  public static String JETStreamMonitor_Warning;

  public static String LoaderManager_CouldNotFindLoader;

  public static String LoaderManager_MultipleLoaders;

  public static String MarkerHelper_CouldNotCreateMarker;

  public static String MergeTag_CouldNotCreateURL;

  public static String MergeTag_CouldNotRead;

  public static String MergeTag_FailedOnCompilerError;

  public static String MergeTag_GeneratedJavaError;

  public static String MergeTag_UnknownJMergeFacadeHelper;

  public static String ModelLoaderExtManager_TypeAlreadyDefined;

  public static String ReplaceStringsTag_BadList;

  public static String ReplaceStringsTag_EmptyList;

  public static String ReplaceStringsTag_ListsNotSameLength;

  public static String TransformContextExtender_NeedsRebuildForOverride;

  public static String XMLElementDelegate_DuplicateAttribute;

  public static String XMLElementDelegate_UnknownXMLTag;

  public static String XPath_NoElementSelected;

  public static String GetTag_NoResult;

  public static String IterateTag_CannotIterateOnResult;

  public static String LoadTag_UnknownSrcContext;

  public static String LoadTag_CouldNotLoad;

  public static String LogTag_BadSeverity;

  public static String AnyTag_AttributeMustBeInteger;

  public static String ImportsLocationTag_AllowedOnlyOnce;

  public static String ImportsLocationTag_MissingImportsLocation;

  public static String IndentTag_AttributeValueMustBeInteger;

  public static String JET2Bundle_ErrorOpeningJar;

  public static String JET2Bundle_CouldNotLoadLoader;

  public static String JET2Bundle_CouldNotInstantiateLoader;

  public static String JET2Bundle_NoRightsToLoadLoader;

  public static String JET2Bundle_CouldNotLoadTemplate;

  public static String JET2Bundle_CouldNotLoadJetBundle;

  public static String PluginDeployedTemplateBundleSupplier_BadExtensionElement;

  public static String PluginReferencedTemplateBundleDescriptor_Retrieving;

  public static String ProjectTemplateBundleDescriptor_WaitingForBuild;

  public static String ProjectTemplateBundleDescriptor_Retreiving;

  public static String ProjectTemplateBundleSupplier_CouldNotRead;

  public static String SafeCustomRuntimeTag_ErrorExecutingHandler;

  public static String TagFactoryImpl_TagCreateFailed;

  public static String TemplateBundleManager_CannotLoad;

  public static String TemplateBundleManager_CannotUnload;

  public static String TagDefinitionImpl_MissingName;

  public static String CopyFileTag_BadSrcContext;

  public static String WorkspaceContextExtender_Commiting;

  public static String WorkspaceContextExtender_ConfirmingTeamAccess;

  public static String WorkspaceContextExtender_WritingFiles;

  public static String WorkspaceContextExtender_Writing;

  public static String WorkspaceContextExtender_NoParent;

  public static String WsCopyBinaryFileAction_WritingFile;

  public static String WsCopyTextFileAction_WritingFile;

  public static String WsFileFromWriterAction_WritingFile;

  public static String WsFolderAction_CreatingFolder;

  public static String WsProjectAction_CreatingProject;

  public static String XPath_VariableUndefined;

  public static String XPath_MustBeNodeNodeSet;

  public static String XPathTokenScanner_UnterminatedStringLiteral;

  public static String DOMInspector_InconsistentElementName;

  public static String DOMInspector_NotDOMObject;

  public static String EObjectInspector_SourceIncompatibleWithTarget;

  public static String EObjectInspector_NotSimpleTextElement;

  public static String XPath_UnrecognizedToken;

  public static String XPath_Expected;

  public static String XPath_UnknownFunction;

  public static String XPath_UnknownAxis;

  public static String XPath_UnknownNSPrefix;

  public static String XPath_UnknownNodeTest;

  public static String XPath_ExpressionMustBeNodeSet;

  public static String XPath_DynamicExpressionIsNull;

  public static String XPath_NotAnElement;

  public static String XPath_UseAddTextElement;

  public static String JET2Context_SuccessfulExecution;

  public static String JET2Context_SuccessfulWithMessages;

  public static String JET2Context_SuccessfulWithWarnings;

  public static String JET2Context_ErrorsInExecution;

  public static String JET2Context_CouldNotFindTemplate;

  public static String JET2Context_CommittingActions;

  public static String JET2Context_VariableNotFound;

  public static String JET2Platform_CouldNotParseString;

  public static String JET2Platform_Executing;

  public static String JET2Platform_RetrievingBundle;

  public static String JET2Platform_ExecutingTemplates;

  public static String JET2Platform_CommittingResults;

  public static String JET2Platform_ErrorMarkingProject;

  public static String JET2Compiler_ErrorWritingJava;

  public static String JET2Compiler_ErrorRemovingMarkers;

  public static String JET2Compiler_ErrorCreatingMarkers;

  public static String JET2Compiler_SameJavaClassAsOther;

  public static String JET2Compiler_MissingDirectiveAttribute;

  public static String JET2Compiler_PrefixAlreadyAssigned;

  public static String JET2Compiler_UnknownTagLibrary;

  public static String JET2Compiler_MissingEndTag;

  public static String JET2Compiler_MissingStartTag;

  public static String JET2Compiler_TagCannotHaveContent;

  public static String JET2Compiler_DeprecatedTag;

  public static String JET2Compiler_UnknownAttribute;

  public static String JET2Compiler_MissingRequiredAttribute;

  public static String JET2Compiler_DeprecatedAttribute;

  public static String JET2Compiler_TagRequiresContent;

  public static String JET2Platform_TransformNotFound;

  public static String JET2TransformationDelegate_MissingAttribute;

  public static String SetTag_CoundNotSet;

  public static String XPath_NoValueForAttribute;

  public static String XPathParser_relativeLocation;

  public static String TraceRuntimeTags_ProcessingBody;

  public static String TraceRuntimeTags_SkippingBody;

  public static String TraceRuntimeTags_ActionCompleted;

  public static String TraceRuntimeTags_BeforeBodyCompleted;

  public static String TraceRuntimeTags_SetBodyContents;

  public static String TraceRuntimeTags_FunctionResult;

  public static String TraceRuntimeTags_LoopInitialized;

  public static String TraceRuntimeTags_ProcessingLoopBody;

  public static String TraceRuntimeTags_LoopFinished;
}
