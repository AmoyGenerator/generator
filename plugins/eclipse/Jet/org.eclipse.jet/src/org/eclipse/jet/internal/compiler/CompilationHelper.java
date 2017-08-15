/**
 * <copyright>
 *
 * Copyright (c) 2007, 2009 IBM Corporation and others.
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
 * $Id: CompilationHelper.java,v 1.4 2009/08/20 13:22:49 pelder Exp $
 */
package org.eclipse.jet.internal.compiler;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.compiler.CompileOptionsManager;
import org.eclipse.jet.core.compiler.JETCompilerOptions;
import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETASTParser;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.parser.resources.WorkspaceTemplateResolverBuilder;
import org.eclipse.jet.internal.runtime.model.XMLDOMLoader;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryManager;
import org.eclipse.jet.taglib.TagLibraryReference;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathFactory;


/**
 * Utility class for accessing JET compiler components. This is particularly useful for editors.
 * 
 * <p>
 * Each compilation helper is specific to a given project. Once constructed, the helper is based on 
 * a snapshot of the project's JET state (compilation options). 
 * </p>
 * <p>
 * Provisional API. This method is subject to significant change or removal in a future release.
 * </p>
 */
public class CompilationHelper
{
  private static final String EMF_CODEGEN_JETPROPERTIES = ".jetproperties"; //$NON-NLS-1$

  private static final String EMF_CODEGEN_JET_NATURE_ID = "org.eclipse.emf.codegen.jet.IJETNature"; //$NON-NLS-1$

  private static final ITagLibraryResolver TAG_LIBRARY_RESOLVER = new ITagLibraryResolver()
    {

      public TagLibrary getLibrary(String tagLibraryID)
      {
        return TagLibraryManager.getInstance().getTagLibrary(tagLibraryID);
      }

    };

  private final IProject project;

  private IJETBundleDescriptor projectDescription;

  private Map options;

  private Map predefinedTagLibraryMap;

  private JETASTParser astParser;

  /**
   * Create a compilation helper for a specific project.
   * @param project an open Eclipse project
   * @throws NullPointerException if project is <code>null</code>
   * @throws IllegalArgumentException if project does not exist or is not open
   */
  public CompilationHelper(IProject project)
  {  
    
    if (project == null)
    {
      throw new NullPointerException();
    }
    if (!project.exists() || !project.isOpen())
    {
      throw new IllegalArgumentException();
    }
    this.project = project;
  }

  /**
   * Return an AST parser capable of compiling JET templates in the given Eclipse project.
   * @return an AST Parser
   * 
   */
  public JETASTParser getASTParser()
  {
    if(astParser == null) {
      Map options = getOptions();
  
      int jetSpec = JETCompilerOptions.getIntOption(options, JETCompilerOptions.OPTION_JET_SPECIFICATION_VERSION);
  
      JETASTParser.Builder builder = new JETASTParser.Builder(jetSpec);
      builder.tagLibraryResolver(TAG_LIBRARY_RESOLVER);
      builder.templateResolver(buildTemplateResolver());
      builder.predefinedTagLibraries(getPredefinedTagLibraryMap());
      final IJETBundleDescriptor descriptor = getDescriptor();
      builder.enableEmbeddedExpressions(
          descriptor != null ? descriptor.isEnableEmbeddedExpressions() : false);
      astParser = builder.build();
    }
    return astParser;
  }
  
  /**
   * Return the JET AST for the given template path. 
   * This method always returns an AST; use {@link JETCompilationUnit#getProblems()} to discover problems.
   * @param templatePath the JET template path
   * @return a {@link JETCompilationUnit}. 
   */
  public JETCompilationUnit getAST(String templatePath) {
    return (JETCompilationUnit)getASTParser().parse(templatePath);
  }
  
  /**
   * Return the JET AST for the given template source.
   * This method always returns an AST; use {@link JETCompilationUnit#getProblems()} to discover problems.
   * @param source
   * @return a {@link JETCompilationUnit}. 
   * @deprecated Use {@link #getASTFromSource(String,String)} instead
   */
  public JETCompilationUnit getASTFromSource(String source) {
    return getASTFromSource(source, null);
  }

  /**
   * Return the JET AST for the given template source.
   * This method always returns an AST; use {@link JETCompilationUnit#getProblems()} to discover problems.
   * @param source
   * @param templatePath TODO
   * @return a {@link JETCompilationUnit}. 
   */
  public JETCompilationUnit getASTFromSource(String source, String templatePath) {
    return (JETCompilationUnit)getASTParser().parse(source.toCharArray(),templatePath);
  }
  
  /**
   * Return the Java code corresponding to the given compilation unit.
   * @param ast
   * @return a string
   */
  public String getJavaCode(JETCompilationUnit ast) {
    ensurePackageAndClassSet(ast);
    // run the code generator
    TemplateRunner templateRunner = new TemplateRunner();
    String cgTemplatePath = getJetVersion() == JETAST.JET_SPEC_V1 ? "templates/v1/jet2java.jet" : "templates/v2/jet2java.jet"; //$NON-NLS-1$ //$NON-NLS-2$
    String code = templateRunner.generate(cgTemplatePath, Collections.singletonMap("cu", ast));  //$NON-NLS-1$
    return code;
  }
  
  private void ensurePackageAndClassSet(JETCompilationUnit ast)
  {
    if(ast.getOutputJavaPackage() == null) {
      ast.setOutputJavaPackage(JETCompilerOptions.getStringOption(getOptions(), JETCompilerOptions.OPTION_COMPILED_TEMPLATE_PACKAGE));
    }
    if(ast.getOutputJavaClassName() == null ) {
      ast.setOutputJavaClassName("_jet_Unkonwn_"); //$NON-NLS-1$
    }
  }

  /**
   * Return the Java code corresponding to the given compilation unit.
   * @param ast
   * @return a string
   */
  public String getJavaCode(JETCompilationUnit ast, Map javaElementPositionMap) {
    // run the code generator
    Map arguments = new HashMap(2);
    arguments.put("cu", ast); //$NON-NLS-1$
    arguments.put("positionMap", javaElementPositionMap); //$NON-NLS-1$
    TemplateRunner templateRunner = new TemplateRunner();
    String cgTemplatePath = getJetVersion() == JETAST.JET_SPEC_V1 ? "templates/v1/jet2java.jet" : "templates/v2/jet2java.jet"; //$NON-NLS-1$ //$NON-NLS-2$
    String code = templateRunner.generate(cgTemplatePath, arguments);
    return code;
  }
  
  /**
   * Return the JET Version
   * @return either {@link JETAST#JET_SPEC_V1} or {@link JETAST#JET_SPEC_V2}.
   */
  public int getJetVersion()
  {
    return JETCompilerOptions.getIntOption(getOptions(), JETCompilerOptions.OPTION_JET_SPECIFICATION_VERSION);
  }

  /**
   * Return the project's compiler options
   * @return
   */
  public Map getOptions()
  {
    if(options == null) {
      if(isLegacyJET1Project(project)) {
        options = getLegacyJETOptions(project);
      } else {
        options = CompileOptionsManager.getOptions(project);
      }
    }
    return options;
  }

  private Map getLegacyJETOptions(IProject project)
  {
    final IFile jetProperties = project.getFile(EMF_CODEGEN_JETPROPERTIES);
    final Map options = new HashMap();
    options.put(JETCompilerOptions.OPTION_JET_SPECIFICATION_VERSION, new Integer(JETAST.JET_SPEC_V1));
    options.put(JETCompilerOptions.OPTION_V1_BASE_TRANSFORMATION, JETCompilerOptions.DEFAULT_V1_BASE_TRANSFORMATION);
    options.put(JETCompilerOptions.OPTION_V1_COMPILE_BASE_TEMPLATES, JETCompilerOptions.DEFAULT_V1_COMPILE_BASE_TEMPLATES);
    if(jetProperties.exists()) {
      loadLegacyJetProperties(jetProperties, options);
    } else {
      options.put(JETCompilerOptions.OPTION_V1_TEMPLATES_DIR, JETCompilerOptions.DEFAULT_V1_TEMPLATES_DIR);
      options.put(JETCompilerOptions.OPTION_JAVA_OUTPUT_FOLDER, "src"); //$NON-NLS-1$
    }
    return options;
  }

  private void loadLegacyJetProperties(final IFile jetProperties, final Map options)
  {
    try
    {
      final Object doc = new XMLDOMLoader().load(jetProperties.getLocationURI().toURL());
      final XPath xpath = XPathFactory.newInstance().newXPath(null);
      final String templatePath = xpath.compile("/jet-settings/template-container").evaluateAsString(doc); //$NON-NLS-1$
      final String outputPath = xpath.compile("/jet-settings/source-container").evaluateAsString(doc); //$NON-NLS-1$
      options.put(JETCompilerOptions.OPTION_V1_TEMPLATES_DIR, templatePath);
      options.put(JETCompilerOptions.OPTION_JAVA_OUTPUT_FOLDER, outputPath);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (XPathException e)
    {
      e.printStackTrace();
    }
  }

  private boolean isLegacyJET1Project(IProject project2)
  {
    try
    {
      return project.hasNature(EMF_CODEGEN_JET_NATURE_ID) || !project.hasNature(JET2Platform.JET2_NATURE_ID);
    }
    catch (CoreException e)
    {
      // only happens if project is closed - can't do much in that case, anyhow...
      return false;
    }
  }

  /**
   * Create a map of tag library prefix to tag library id.
   * @param tagLibraryReferences an array of tag library references
   * @return a map
   */
  public Map getPredefinedTagLibraryMap()
  {
    if(predefinedTagLibraryMap == null) {
      IJETBundleDescriptor descriptor = getDescriptor();
      if (descriptor != null)
      {
        TagLibraryReference[] tagLibraryReferences = descriptor.getTagLibraryReferences();
        predefinedTagLibraryMap = new HashMap(tagLibraryReferences.length);
        for (int i = 0; i < tagLibraryReferences.length; i++)
        {
          if (tagLibraryReferences[i].isAutoImport())
          {
            predefinedTagLibraryMap.put(tagLibraryReferences[i].getPrefix(), tagLibraryReferences[i].getTagLibraryId());
          }
        }
      }
      else
      {
        predefinedTagLibraryMap = Collections.EMPTY_MAP;
      }
    }
    return predefinedTagLibraryMap;
  }

  private IJETBundleDescriptor getDescriptor()
  {
    if (projectDescription == null)
    {
      projectDescription = JET2Platform.getProjectDescription(project.getName());
    }
    
    return projectDescription;
  }

  /**
   * @param project
   * @param options
   * @return
   */
  private ITemplateResolver buildTemplateResolver()
  {
    final Map options = getOptions();
    final WorkspaceTemplateResolverBuilder templateResolverWorkspaceTemplateResolverBuilder = new WorkspaceTemplateResolverBuilder(project);
    URI altBaseLocations[] = getAltBaseLocations(options);
    if (altBaseLocations.length > 0)
    {
      templateResolverWorkspaceTemplateResolverBuilder.addAltBaseLocations(altBaseLocations);
    }
    final ITemplateResolver templateResolver = templateResolverWorkspaceTemplateResolverBuilder.build();
    return templateResolver;
  }

  private URI[] getAltBaseLocations(Map options)
  {
    final String value = JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_V1_BASE_TRANSFORMATION);
    if (value == null || value.trim().length() == 0)
    {
      return new URI [0];
    }
    String[] uriStrings = value.split(","); //$NON-NLS-1$

    List altLocations = new ArrayList(uriStrings.length);

    for (int i = 0; i < uriStrings.length; i++)
    {
      String uriString = uriStrings[i].endsWith("/") ? uriStrings[i] : uriStrings[i] + "/"; //$NON-NLS-1$ //$NON-NLS-2$
      try
      {
        URI uri = new URI(uriString);
        altLocations.add(uri);
      }
      catch (URISyntaxException e)
      {
        InternalJET2Platform.logError(null, e);
      }
    }

    return (URI[])altLocations.toArray(new URI [altLocations.size()]);
  }

}
