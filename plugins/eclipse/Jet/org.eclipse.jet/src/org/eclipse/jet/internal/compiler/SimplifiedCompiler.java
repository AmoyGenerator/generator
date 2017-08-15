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
 * $Id: SimplifiedCompiler.java,v 1.5 2009/04/07 17:44:54 pelder Exp $
 */
package org.eclipse.jet.internal.compiler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jet.core.compiler.JETCompilerOptions;
import org.eclipse.jet.core.parser.DefaultTemplateResolver;
import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.core.parser.RecursiveIncludeException;
import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.internal.core.NewLineUtil;
import org.eclipse.jet.internal.core.compiler.DuplicateGeneratedClassException;
import org.eclipse.jet.internal.core.compiler.ICompilerOutput;
import org.eclipse.jet.internal.core.compiler.IJETCompiler;
import org.eclipse.jet.internal.core.compiler.IncludeDependencies;
import org.eclipse.jet.internal.core.compiler.IncludeDependenciesUtil;
import org.eclipse.jet.internal.core.compiler.UniqueNameGenerator;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryReference;

/**
 * Implementation of IJETCompiler
 */
public final class SimplifiedCompiler implements IJETCompiler
{
 
  private static final String JET_TEMPLATE_MAP_PROPERTIES = "jetTemplateMap.properties"; //$NON-NLS-1$

  private static final class NullTemplateResolver implements ITemplateResolver
  {
    public URI[] getBaseLocations()
    {
      return new URI[0];
    }

    public ITemplateInput getIncludedInput(String templatePath, ITemplateInput[] activeInputs) throws RecursiveIncludeException
    {
      return null;
    }

    public ITemplateInput getInput(String templatePath)
    {
      return null;
    }
  }
  private static final class JET1TemplateMatcher implements ITemplateMatcher
  {
    private final String templatesDir;
    
    public JET1TemplateMatcher(String templatesDir)
    {
      if(templatesDir == null || templatesDir.length() == 0)
      {
        this.templatesDir = ""; //$NON-NLS-1$
      }
      else
      {
        this.templatesDir = templatesDir.endsWith("/") ? templatesDir : templatesDir + "/";  //$NON-NLS-1$ //$NON-NLS-2$
        
      }
    }
    
    public boolean isTemplate(String templatePath)
    {
      return templatePath.startsWith(templatesDir) && templatePath.endsWith("jet"); //$NON-NLS-1$
    }
  }
  public interface ITemplateMatcher
  {
    public abstract boolean isTemplate(String templatePath);
  }
  
  public static final class JET2TemplateMatcher implements ITemplateMatcher
  {
    private final Set sourceExtensions;

    public JET2TemplateMatcher(Set sourceExtensions)
    {
      this.sourceExtensions = sourceExtensions;
    }

    public boolean isTemplate(String templatePath)
    {
      final int extensionIndex = templatePath.lastIndexOf("."); //$NON-NLS-1$
      if(extensionIndex >= 0) 
      {
        final String extension = templatePath.substring(extensionIndex + 1);
        return sourceExtensions.contains(extension);
      }
      else
      {
        return false;
      }
    }
  }
  

  /**
   * Memento for shared state
   */
  public static final class SaveStateMemento implements Serializable
  {
    /**
     * 
     */
    private static final long serialVersionUID = -5023383826047467196L;
    private final Map fqnToPathMap;
    private final Map pathToFQNMap;
    private final Map pathToRefTagLibSet;
    private final IncludeDependencies includeDependencies;

    /**
     * Create a SaveStateMemento for the specified compiler
     * @param compiler a compiler instance
     */
    public SaveStateMemento(SimplifiedCompiler compiler)
    {
      this.fqnToPathMap = compiler.uniqueNameGenerator.getFQNToPathMap();
      this.pathToFQNMap = compiler.uniqueNameGenerator.getPathToFQNMap();
      this.pathToRefTagLibSet = new HashMap(compiler.pathToRefTagLibSet);
      this.includeDependencies = compiler.includeDependencies;
    }
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      
      if(includeDependencies == null) {
        throw new IOException("includeDependencies is null"); //$NON-NLS-1$
      }
    }
  }

  /**
   * Builder for SimplifiedCompiler
   */
  public static final class Builder
  {

    final int jetSpec;
    ITemplateResolver templateResolver;
    SaveStateMemento savedState;
    Map options;
    final ICompilerOutput compilerOutput;
    String templateLoaderFQN;
    Map predefinedTagLibraries;
    ITagLibraryResolver tagLibraryResolver;
    boolean enabledEmbeddedExpressions = false;

    
    public Builder(int jetSpec, URI baseLocation, ICompilerOutput compilerOutput)
    {
      this(jetSpec, compilerOutput);
      templateResolver(new DefaultTemplateResolver.Builder(baseLocation).build());
    }
    
    public Builder(int jetSpec, ICompilerOutput compilerOutput)
    {
      this.jetSpec = jetSpec;
      this.compilerOutput = compilerOutput;
      
    }
    public Builder templateResolver(ITemplateResolver templateResolver)
    {
      this.templateResolver = templateResolver;
      return this;
    }
    
    /**
     * Specify the saved state
     * @param savedState
     * @return
     */
    public Builder savedState(Serializable savedState)
    {
      if(!(savedState instanceof SaveStateMemento))
      {
        throw new IllegalArgumentException();
      }
      this.savedState = (SaveStateMemento)savedState;
      return this;
    }

    public Builder options(Map options)
    {
      this.options = options;
      return this;
    }
    
    /**
     * Set the fully qualified Java class name of the template loader
     * @param templateLoader the fully qualified Java class name of the template laoder
     * @return the bulder
     */
    public Builder templateLoaderFQN(String templateLoaderFQN)
    {
      this.templateLoaderFQN = templateLoaderFQN;
      return this;
    }
    
    /**
     * Set the predefined tag libraries map (key = prefix, value = id).
     * @param predefinedTagLibraries a map of tag library prefixes to tag library ids
     * @return the builder
     */
    public Builder predefinedTagLibraries(Map predefinedTagLibraries)
    {
      this.predefinedTagLibraries = predefinedTagLibraries;
      return this;
    }

    public Builder tagLibraryResolver(ITagLibraryResolver tagLibraryResolver) {
      this.tagLibraryResolver = tagLibraryResolver;
      return this;
    }
    
    public Builder enableEmbeddedExpressions(boolean enableEmbeddedExpressions) {
      this.enabledEmbeddedExpressions = enableEmbeddedExpressions;
      return this;
    }
    /**
     * Build the compiler instance 
     * @return
     */
    public SimplifiedCompiler build()
    {
      return new SimplifiedCompiler(this);
    }
  }

  private final int jetSpec;
  private final ITemplateResolver templateResolver;
  private final Map predefinedTagLibraries;
  private final Map options;
  private final UniqueNameGenerator uniqueNameGenerator;
  private final Map pathToRefTagLibSet; // Map<String,Set<String>>
  private final ICompilerOutput compilerOutput;
  private final ITemplateMatcher templateMatcher;
  private final String templateLoaderClassName;
  private final String templateLoaderPackage;
  private final ITagLibraryResolver tagLibraryResolver;
  private final IncludeDependencies includeDependencies;
  private boolean enableEmbeddedExpressions;
  
  private static final ITagLibraryResolver nullTagLibraryResolver = new ITagLibraryResolver() {
  
        public TagLibrary getLibrary(String tagLibraryID)
        {
          return null;
        }};

  private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$
        
  private SimplifiedCompiler(Builder builder)
  {
    this.jetSpec = builder.jetSpec;
    this.templateResolver = builder.templateResolver == null ?
      new NullTemplateResolver()
      : builder.templateResolver;
    // Make a defensive copy of the builder provided tag library maps
    this.predefinedTagLibraries = builder.predefinedTagLibraries == null ? Collections.EMPTY_MAP : new HashMap(
      builder.predefinedTagLibraries);
    
    // Make a defensive copy of the options passed to the builder.
    this.options = builder.options == null ? JETCompilerOptions.getDefaultCompilerOptions() : new HashMap(builder.options);
    
    final Map pathToFQNMap = builder.savedState == null ? Collections.EMPTY_MAP : builder.savedState.pathToFQNMap;
    final Map fqnToPathMap = builder.savedState == null ? Collections.EMPTY_MAP : builder.savedState.fqnToPathMap;
    this.uniqueNameGenerator = new UniqueNameGenerator(pathToFQNMap, fqnToPathMap,
      JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_COMPILED_TEMPLATE_PACKAGE));
    
    this.compilerOutput = builder.compilerOutput;
    
    if(builder.templateLoaderFQN == null)
    {
      this.templateLoaderPackage = null;
      this.templateLoaderClassName = null;
    }
    else
    {
      int index = builder.templateLoaderFQN.lastIndexOf('.');
      templateLoaderPackage = index >= 0 ? builder.templateLoaderFQN.substring(0, index) : ""; //$NON-NLS-1$
      templateLoaderClassName = index >= 0 ? builder.templateLoaderFQN.substring(index + 1) : builder.templateLoaderFQN;
    }
    
    Set sourceExtensions = new HashSet();
    final String[] extensions = JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_TEMPLATE_EXT).split(","); //$NON-NLS-1$
    for (int i = 0; i < extensions.length; i++)
    {
      sourceExtensions.add(extensions[i].trim());
    }
    
    this.pathToRefTagLibSet = builder.savedState == null ? new HashMap() : new HashMap(builder.savedState.pathToRefTagLibSet);
    
    this.tagLibraryResolver = builder.tagLibraryResolver == null ? nullTagLibraryResolver : builder.tagLibraryResolver;
    
    this.includeDependencies = builder.savedState == null ? new IncludeDependencies() : builder.savedState.includeDependencies;
    
    templateMatcher = builder.jetSpec == JETAST.JET_SPEC_V1 ? 
        (ITemplateMatcher)new JET1TemplateMatcher(JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_V1_TEMPLATES_DIR)) 
        : (ITemplateMatcher)new JET2TemplateMatcher(sourceExtensions);
        
    enableEmbeddedExpressions = builder.enabledEmbeddedExpressions;
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.compiler.IJETCompiler#compile(java.lang.String)
   */
  public CompileResult compile(String templatePath)
  {
    if(!isJETSourceFile(templatePath)) 
    {
      return CompileResult.IGNORED;
    }
    
    compilerOutput.preCompile(templatePath);
    
    final JETCompilationUnit cu 
      = (JETCompilationUnit)new org.eclipse.jet.core.parser.ast.JETASTParser.Builder(jetSpec)
            .templateResolver(templateResolver)
            .predefinedTagLibraries(predefinedTagLibraries)
            .tagLibraryResolver(tagLibraryResolver)
            .enableEmbeddedExpressions(enableEmbeddedExpressions)
            .build()
            .parse(templatePath);
    
    try
    {
      String oldOutputPath = uniqueNameGenerator.getGeneratedOutputPath(templatePath);
      
      uniqueNameGenerator.ensureJavaOutputSet(templatePath, cu);
      
      // run the code generator
      TemplateRunner templateRunner = new TemplateRunner();
      // XXX make this more open
      String cgTemplatePath = jetSpec == JETAST.JET_SPEC_V1 ? "templates/v1/jet2java.jet" : "templates/v2/jet2java.jet"; //$NON-NLS-1$ //$NON-NLS-2$
      String code = templateRunner.generate(cgTemplatePath, Collections.singletonMap("cu", cu));  //$NON-NLS-1$
      
      // save the code to the output location
      String outputPath = outputPath(cu.getOutputJavaPackage(), cu.getOutputJavaClassName());
      
      if(oldOutputPath != null && !oldOutputPath.equals(outputPath))
      {
        compilerOutput.removeOutput(oldOutputPath);
      }
      
      // Don't encode the .java file the same as the template. This causes
      // problems with ANT/PDE build, which typically expects default encodings on all Java
      // files.
      compilerOutput.writeOutput(outputPath, NewLineUtil.setLineTerminator(code, NL), null);
      
      if(cu.hasErrors() || cu.hasWarnings()) {
        compilerOutput.recordProblems(templatePath, cu.getProblems());
      }
      
      updateDependencies(templatePath, cu);
      
    }
    catch (DuplicateGeneratedClassException e)
    {
      cu.createProblem(
        ProblemSeverity.ERROR,
        IProblem.MultipleTemplatesWithSameJavaClass,
        JET2Messages.JET2Compiler_SameJavaClassAsOther,
        new Object []{ e.getTemplatePath(), e.getOtherTemplatePath(), },
        -1,
        -1,
        -1,
        -1);
    }

    if (cu.hasErrors())
    {
      return CompileResult.ERRORS;
    }
    else if (cu.hasWarnings())
    {
      return CompileResult.WARNINGS;
    }
    else
    {
      return CompileResult.OK;
    }
  }

  /**
   * Update dependencies tracked by the compiler
   * @param templatePath the templatePath
   * @param cu the compilation unit
   */
  private void updateDependencies(String templatePath, JETCompilationUnit cu)
  {
    final TagLibraryReference[] tlRefs = cu.getTagLibraryReferences();
    Set tlIdRefs = tlRefs.length == 0 ? Collections.EMPTY_SET : new HashSet(tlRefs.length);
    for (int i = 0; i < tlRefs.length; i++)
    {
      tlIdRefs.add(tlRefs[i].getTagLibraryId());
    }
    pathToRefTagLibSet.put(templatePath, tlIdRefs);
    
    includeDependencies.removeDependencies(templatePath);
    includeDependencies.addDependencies(templatePath, IncludeDependenciesUtil.getDependencies(cu));
  }

  /**
   * calculate the output path for a Java file given a Java package and classname
   * @param javaPackage a Java package name, possible the empty string for the default package
   * @param className a Java class name
   * @return the output path
   */
  private String outputPath(final String javaPackage, final String className)
  {
    StringBuffer buffer = new StringBuffer(javaPackage.length() + className.length() + 6);
    if(javaPackage.length() > 0) {
      buffer.append(javaPackage.replace('.', '/')).append('/');
    }
    buffer.append(className).append(".java"); //$NON-NLS-1$
    return buffer.toString();
  }
  
  /**
   * Calculate the output path for a Java class froma fully qualified class name
   * @param fqClassName a fully qualified class name
   * @return the output path
   */
  private String outputPath(final String fqClassName)
  {
    StringBuffer buffer = new StringBuffer(fqClassName.length() + ".java".length()); //$NON-NLS-1$
    buffer.append(fqClassName.replace('.', '/')).append(".java"); //$NON-NLS-1$
    return buffer.toString();
  }

  /**
   * Test whether a templatePath is to be compiled by this compiler
   * @param templatePath a template path
   * @return <code>true</code> if the template path has a known extension
   */
  private boolean isJETSourceFile(String templatePath)
  {
    return templateMatcher.isTemplate(templatePath);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.compiler.IJETCompiler#retrieveState()
   */
  public Serializable getMemento()
  {
    return new SaveStateMemento(this);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.compiler.IJETCompiler#finish()
   */
  public void finish()
  {
    TemplateRunner templateRunner = new TemplateRunner();

    if(jetSpec == JETAST.JET_SPEC_V1)
    {
      // enable dynamic loading of JET1 templates
      String mapContents = templateRunner.generate("templates/jetTemplateMap.properties.jet",  //$NON-NLS-1$
        Collections.singletonMap("args",uniqueNameGenerator.getPathToFQNMap())); //$NON-NLS-1$
      compilerOutput.writeOutput(JET_TEMPLATE_MAP_PROPERTIES,
        NewLineUtil.setLineTerminator(mapContents, NL), null);
    }

    if(templateLoaderClassName != null) 
    {
      
      final LoaderGenerationParameters args = new LoaderGenerationParameters(templateLoaderPackage, 
        templateLoaderClassName, uniqueNameGenerator.getPathToFQNMap());
      
      // run the code generator
      Map argsMap = new HashMap(2);
      argsMap.put("args", args); //$NON-NLS-1$
      argsMap.put("useJava5", options.get(JETCompilerOptions.OPTION_USE_JAVA5)); //$NON-NLS-1$
      String code = templateRunner.generate("templates/jet2transform.jet",  //$NON-NLS-1$
        argsMap);
      
      compilerOutput.writeOutput(outputPath(templateLoaderPackage, templateLoaderClassName), 
        NewLineUtil.setLineTerminator(code, NL), null);
    }
  }


  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.compiler.IJETCompiler#clean()
   */
  public void clean()
  {
    // tell the output manager to remove generated template Class files
    for (Iterator i = uniqueNameGenerator.clean().iterator(); i.hasNext();)
    {
      String fqJavaClass = (String)i.next();
      compilerOutput.removeOutput(outputPath(fqJavaClass));
    }
    // tell the output manager to remove the generated class loader
    if(templateLoaderClassName != null)
    {
      compilerOutput.removeOutput(outputPath(templateLoaderPackage, templateLoaderClassName));
      if(jetSpec == JETAST.JET_SPEC_V1)
      {
        compilerOutput.removeOutput(JET_TEMPLATE_MAP_PROPERTIES);
      }
    }
    
    // forget all our internal state
    pathToRefTagLibSet.clear();
  }

  public void removeTemplate(String templatePath)
  {
    final String outputPath = uniqueNameGenerator.remove(templatePath);
    if(outputPath != null)
    {
      compilerOutput.removeOutput(outputPath);
    }
    pathToRefTagLibSet.remove(templatePath);
  }

  public String[] getTagLibaryDependencies()
  {
    Set dependencies = new HashSet(predefinedTagLibraries.values());
    for (Iterator i = pathToRefTagLibSet.values().iterator(); i.hasNext();)
    {
      Set templateDependencies = (Set)i.next();
      dependencies.addAll(templateDependencies);
    }
    return (String[])dependencies.toArray(new String[dependencies.size()]);
  }

  public boolean isTemplate(String templatePath)
  {
    return isJETSourceFile(templatePath);
  }

  public String[] getAffectedTemplatePaths(String changedFilePath)
  {
    Set templatesToCompile = new HashSet(Arrays.asList(includeDependencies.getAffectedTemplates(changedFilePath)));
    return (String[])templatesToCompile.toArray(new String[templatesToCompile.size()]);
  }

}
