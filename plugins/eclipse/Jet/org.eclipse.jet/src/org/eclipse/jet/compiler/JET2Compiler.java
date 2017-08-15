/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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

package org.eclipse.jet.compiler;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.core.compiler.JETCompilerOptions;
import org.eclipse.jet.internal.builder.WorkspaceCompiler;
import org.eclipse.jet.internal.compiler.JETCompilerState;


/**
 * The compiler that turns a JET2 sources files Java files in the Eclipse workspace.
 * @deprecated Since 0.8.0, use {@link WorkspaceCompiler} or {@link JETCompilerOptions} for options
 */
public class JET2Compiler
{

  public static final String RUNTIME_PROBLEM_MARKER = JET2Platform.PLUGIN_ID + ".runtimeProblem"; //$NON-NLS-1$

  /**
   * The Marker created by the compiler on all identified problems.
   */
  public static final String COMPILE_PROBLEM_MARKER = JET2Platform.PLUGIN_ID + ".compileProblem"; //$NON-NLS-1$

  /**
   * The default value of the  {@link #OPTION_COMPILED_TEMPLATE_PACKAGE}  option; value: "org.eclipse.jet2.internal.compiled".
   * @deprecated Use {@link JETCompilerOptions#DEFAULT_COMPILED_TEMPLATE_PACKAGE} instead
   */
  public static final String DEFAULT_COMPILED_TEMPLATE_PACKAGE = JETCompilerOptions.DEFAULT_COMPILED_TEMPLATE_PACKAGE; 

  /**
   * The default value of the  {@link #OPTION_COMPILED_TEMPLATE_SRC_DIR}  option; value: "jet2java".
   * @deprecated Use {@link JETCompilerOptions#DEFAULT_COMPILED_TEMPLATE_SRC_DIR} instead
   */
  public static final String DEFAULT_COMPILED_TEMPLATE_SRC_DIR = JETCompilerOptions.DEFAULT_COMPILED_TEMPLATE_SRC_DIR; 

  /**
   * The default value of the  {@link #OPTION_TEMPLATE_EXT}  option; value: "jet".
   * @deprecated Use {@link JETCompilerOptions#DEFAULT_TEMPLATE_EXT} instead
   */
  public static final String DEFAULT_TEMPLATE_EXT = JETCompilerOptions.DEFAULT_TEMPLATE_EXT; 
  
  /**
   * @deprecated Use {@link JETCompilerOptions#DEFAULT_SET_JAVA_FILES_AS_DERIVED} instead
   */
  public static final Boolean DEFAULT_SET_JAVA_FILES_AS_DERIVED = JETCompilerOptions.DEFAULT_SET_JAVA_FILES_AS_DERIVED;

  /**
   * Compiler option specifying the package to which compiled templates are written; value: "compiledTemplatePackage".
   * @deprecated Use {@link JETCompilerOptions#OPTION_COMPILED_TEMPLATE_PACKAGE} instead
   */
  public static final String OPTION_COMPILED_TEMPLATE_PACKAGE = JETCompilerOptions.OPTION_COMPILED_TEMPLATE_PACKAGE; 

  /**
   * Compiler option specifying the Java project source directory to which compiled templates are written; value: "compiledTemplateSrcDir".
   * @deprecated Use {@link JETCompilerOptions#OPTION_COMPILED_TEMPLATE_SRC_DIR} instead
   */
  public static final String OPTION_COMPILED_TEMPLATE_SRC_DIR = JETCompilerOptions.OPTION_COMPILED_TEMPLATE_SRC_DIR; 

  /**
   * Compiler option specifying which extensions are recognized as extensions; value: "templateExt".
   * @deprecated Use {@link JETCompilerOptions#OPTION_TEMPLATE_EXT} instead
   */
  public static final String OPTION_TEMPLATE_EXT = JETCompilerOptions.OPTION_TEMPLATE_EXT; 
  
  /**
   * @deprecated Use {@link JETCompilerOptions#OPTION_SET_JAVA_FILES_AS_DERIVED} instead
   */
  public static final String OPTION_SET_JAVA_FILES_AS_DERIVED = JETCompilerOptions.OPTION_SET_JAVA_FILES_AS_DERIVED; 

  // Return the default compile options...
  /**
   * @deprecated Use {@link JETCompilerOptions#getDefaultCompilerOptions()} instead
   */
  public static Map getDefaultCompilerOptions()
  {
    return JETCompilerOptions.getDefaultCompilerOptions();
  }
  
  private int noCompiles;

  private final Map options;

  private Set sourceExtensions = null;

  private final WorkspaceCompiler workspaceCompiler;

  /**
   * Create an instance of the JET compiler capable of incremental compilation from a saved state.
   * @param project the project containing templates to be compiled.
   * @param options the compiler options
   * @param compilerState the saved state from previous a previous compile
   * @param monitor a progress monitor
   */
  public JET2Compiler(IProject project, Map options, IJETCompilerState compilerState, IProgressMonitor monitor)
  {
    this.workspaceCompiler = new WorkspaceCompiler(project, null, options, monitor);
    this.options = options == null ? JETCompilerOptions.getDefaultCompilerOptions() : options;

    this.noCompiles = 0;
  }
  /**
   * Create an instance
   * @param project the project upon which the compiler will act
   * @param monitor a progress monitor for compiler actions 
   * @param options a map of compiler options
   * 
   */
  public JET2Compiler(IProject project, IProgressMonitor monitor, Map options)
  {
    this(project, options, new JETCompilerState(), monitor);
  }

  /**
   * Create the Template Loader class for the project 
   */
  public void createTransformFiles()
  {
    workspaceCompiler.finish();
  }

  private boolean isJETSource(IPath path)
  {
    if(sourceExtensions == null) {
      sourceExtensions = new HashSet();
      final String[] extensions = JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_TEMPLATE_EXT)
                                      .split(","); //$NON-NLS-1$
      for (int i = 0; i < extensions.length; i++)
      {
        sourceExtensions.add(extensions[i]);
      }
    }
    return sourceExtensions.contains(path.getFileExtension());
  }
  /**
   * Test whether the supplied resource is a compilable JET2 source file.
   * @param resource
   * @return <code>true</code> if the identified resource is a jet2 source file
   */
  public boolean isJET2SourceFile(IResource resource)
  {
    return resource.getType() == IResource.FILE && isJETSource(resource.getFullPath());
  }

  /**
   * Compile the passed resource into a Java file. Does nothing if the passed resource
   * is not a JET2 template.
   * @param resource a project resource
   */
  public void compile(IResource resource)
  {
    ++ noCompiles;
    workspaceCompiler.compile(resource.getProjectRelativePath().toString());
  }

  /**
   * Remove any resources for the passed resource representing a JET template.
   * The method will remove any Java files created by the compiler for the resource.
   * <p>
   * This method is equivalent to:
   * <code>removeDerivedResources(resource, false)</code>.
   * 
   * @param resource a resource
   * @throws CoreException if a derived resource cannot be removed.
   * @see #removeDerivedResources(IResource, boolean)
   */
  public void removeDerivedResources(IResource resource) throws CoreException
  {
    removeDerivedResources(resource, false);
  }
  /**
   * Remove any derived resources for this resource
   * @param resource a resource
   * @param derivedOnly if <code>true</code>, Java files with the derived attribute set are removed.
   * @throws CoreException if a derived resource cannot be removed.
   */
  public void removeDerivedResources(IResource resource, boolean derivedOnly) throws CoreException
  {
    workspaceCompiler.removeTemplate(resource.getProjectRelativePath().toString());
  }
  
  /**
   * @return Returns the noCompiles.
   */
  public final int getNoCompiles()
  {
    return noCompiles;
  }
  
  public void clean() 
  {
    workspaceCompiler.clean();
  }
  
  
}
