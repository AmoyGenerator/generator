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
 * $Id: WorkspaceCompiler.java,v 1.6 2009/04/07 17:44:54 pelder Exp $
 */
package org.eclipse.jet.internal.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.compiler.CompileOptionsManager;
import org.eclipse.jet.core.compiler.JETCompilerOptions;
import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.core.parser.ast.Problem;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.compiler.SimplifiedCompiler;
import org.eclipse.jet.internal.compiler.UnicodeEscapeUtil;
import org.eclipse.jet.internal.compiler.SimplifiedCompiler.Builder;
import org.eclipse.jet.internal.core.compiler.ICompilerOutput;
import org.eclipse.jet.internal.core.compiler.IJETCompiler;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.parser.resources.WorkspaceTemplateResolverBuilder;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryManager;
import org.eclipse.jet.taglib.TagLibraryReference;
import org.eclipse.jet.transform.IJETBundleDescriptor;

/**
 * Workspace compiler that delegates to the simplified compiler and adds Eclipse workspace operations
 */
public class WorkspaceCompiler implements IJETCompiler, ICompilerOutput, ITagLibraryResolver
{
  public static final String RUNTIME_PROBLEM_MARKER = JET2Platform.PLUGIN_ID + ".runtimeProblem"; //$NON-NLS-1$

  /**
   * The Marker created by the compiler on all identified problems.
   */
  public static final String COMPILE_PROBLEM_MARKER = JET2Platform.PLUGIN_ID + ".compileProblem"; //$NON-NLS-1$

  
  private final IJETCompiler simplifiedCompiler;
  private final IProject project;
  private final IContainer outputLocation;
  private final boolean writeFilesAsDerived;
  private final IProgressMonitor monitor;
  
  public WorkspaceCompiler(IProject project, Serializable savedState, IProgressMonitor monitor)
  {
    this(project, savedState, CompileOptionsManager.getOptions(project), monitor);
  }
  
  public WorkspaceCompiler(IProject project, Serializable savedState, Map options, IProgressMonitor monitor)
  {
    this.project = project;
    
    final IJETBundleDescriptor descriptor = JET2Platform.getProjectDescription(project.getName());
    final Builder builder = new SimplifiedCompiler.Builder(
      JETCompilerOptions.getIntOption(options, JETCompilerOptions.OPTION_JET_SPECIFICATION_VERSION),
      this)
          .templateResolver(buildTemplateResolver(project, options))
          .options(options)
          .enableEmbeddedExpressions(descriptor != null ? descriptor.isEnableEmbeddedExpressions() : false)
          .tagLibraryResolver(this);

    if(descriptor != null) {
      builder.predefinedTagLibraries(getPredefinitionMap(descriptor.getTagLibraryReferences()));
      final String templateLoaderClass = descriptor.getTemplateLoaderClass();
      if(templateLoaderClass != null && templateLoaderClass.length() > 0) {
        builder.templateLoaderFQN(templateLoaderClass);
      }
    }
    
    if(savedState != null) {
      builder.savedState(savedState);
    }
    this.simplifiedCompiler = builder.build();
    
    final String javaSrcFolder = JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_COMPILED_TEMPLATE_SRC_DIR).trim();

    this.outputLocation = javaSrcFolder.length() == 0 ? (IContainer)project : (IContainer)project.getFolder(javaSrcFolder);
    
    this.writeFilesAsDerived = JETCompilerOptions.getBooleanOption(options, JETCompilerOptions.OPTION_SET_JAVA_FILES_AS_DERIVED);
   
    this.monitor = monitor == null ? new NullProgressMonitor() : monitor;
  }

  /**
   * @param project
   * @param options
   * @return
   */
  private ITemplateResolver buildTemplateResolver(IProject project, final Map options)
  {
    final WorkspaceTemplateResolverBuilder templateResolverWorkspaceTemplateResolverBuilder = new WorkspaceTemplateResolverBuilder(project);
    URI altBaseLocations[] = getAltBaseLocations(options);
    if(altBaseLocations.length > 0) {
      templateResolverWorkspaceTemplateResolverBuilder.addAltBaseLocations(altBaseLocations);
    }
    final ITemplateResolver templateResolver = templateResolverWorkspaceTemplateResolverBuilder.build();
    return templateResolver;
  }
  
  private URI[] getAltBaseLocations(Map options)
  {
    final String value = JETCompilerOptions.getStringOption(options, JETCompilerOptions.OPTION_V1_BASE_TRANSFORMATION);
    if(value == null || value.trim().length() == 0) {
    	return new URI[0];
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
    
    return (URI[])altLocations.toArray(new URI[altLocations.size()]);
  }

  /**
   * Create a map of tag library prefix to tag library id.
   * @param tagLibraryReferences an array of tag library references
   * @return a map
   */
  private Map getPredefinitionMap(TagLibraryReference[] tagLibraryReferences)
  {
    final Map predefinedMap = new HashMap(tagLibraryReferences.length);
    for (int i = 0; i < tagLibraryReferences.length; i++)
    {
      if(tagLibraryReferences[i].isAutoImport())
      {
        predefinedMap.put(tagLibraryReferences[i].getPrefix(), tagLibraryReferences[i].getTagLibraryId());
      }
    }
    return predefinedMap;
  }

  public void clean()
  {
    simplifiedCompiler.clean();
  }

  public CompileResult compile(String templatePath)
  {
    return simplifiedCompiler.compile(templatePath);
  }

  public void finish()
  {
    simplifiedCompiler.finish();
  }

  public Serializable getMemento()
  {
    return simplifiedCompiler.getMemento();
  }

  public String[] getTagLibaryDependencies()
  {
    return simplifiedCompiler.getTagLibaryDependencies();
  }

  public void removeTemplate(String templatePath)
  {
    simplifiedCompiler.removeTemplate(templatePath);
  }

  public boolean isTemplate(String templatePath)
  {
    return simplifiedCompiler.isTemplate(templatePath);
  }
  
  public void removeOutput(String outputFilePath)
  {
    final IFile file = outputLocation.getFile(new Path(outputFilePath));
    if(file.exists() || file.isDerived()) {
      try
      {
        file.delete(false, new SubProgressMonitor(monitor, 1));
      }
      catch (CoreException e)
      {
        InternalJET2Platform.logError(JET2Messages.JET2Compiler_ErrorWritingJava, e);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.core.compiler.ICompilerOutput#writeOutput(java.lang.String, java.lang.String, java.lang.String)
   */
  public void writeOutput(String outputFilePath, String contents, String encoding)
  {
    // NOTE: encoding is ignored. Now always write Java files using the default encoding 
    // for those files. This plays better with PDE build and ant, which are incapable of dealing
    // with individual Java files having non-standard encodings.
    try
    {
      final IFile file = outputLocation.getFile(new Path(outputFilePath));
      ensureDirsExist(file.getParent());


      final Charset cs = Charset.forName(file.getCharset());
      final ByteBuffer newContents = UnicodeEscapeUtil.encode(contents, cs);
      // check if files exists
      if(file.exists())
      {
        // get the old file...
        boolean writeNewContents = true; // assume yes
        try
        {
          InputStream inputStream = file.getContents();
          final ReadableByteChannel channel = Channels.newChannel(inputStream);
          final ByteBuffer originalContents = ByteBuffer.allocate(inputStream.available());
          channel.read(originalContents);
          channel.close();
          writeNewContents = !newContents.equals(originalContents);
          newContents.rewind();
        }
        catch (IOException exception) 
        {
          // continue, assuming that the content has changed.
        }
        if(!writeNewContents) {
          return;
        }
      }

      if(file.exists() && file.isReadOnly()) {
        // play nicely with the team environment
        // This call should get the files checked out from version control if the project is a 'team' project.
        //
        IStatus status = ResourcesPlugin.getWorkspace().validateEdit(new IFile [] { file }, null);
        if (!status.isOK()) 
        {
          throw new CoreException(status);
        }
      }

      final ByteArrayInputStream stream = new ByteArrayInputStream(newContents.array(), 0, newContents.remaining());
      if (!file.exists())
      {
        file.create(stream, true, new SubProgressMonitor(monitor, 1));
      } else {
        file.setContents(stream, 
          true, false, new SubProgressMonitor(monitor, 1));
      }
      
      file.setDerived(writeFilesAsDerived);
    }
//    catch (UnsupportedEncodingException e)
//    {
//      InternalJET2Platform.logError(JET2Messages.JET2Compiler_ErrorWritingJava, e);
//    }
    catch (CoreException e)
    {
      InternalJET2Platform.logError(JET2Messages.JET2Compiler_ErrorWritingJava, e);
    }
    catch (CharacterCodingException e)
    {
      InternalJET2Platform.logError(JET2Messages.JET2Compiler_ErrorWritingJava, e);
    }
  }
  
  /**
   * Ensure the given container exists
   * @param container
   * @throws CoreException
   */
  private void ensureDirsExist(IContainer container) throws CoreException
  {
    if (container != null && !container.exists() && container.getType() == IResource.FOLDER)
    {
      ensureDirsExist(container.getParent());
      ((IFolder)container).create(false, true, new SubProgressMonitor(monitor, 1));
    }
  }

  public void recordProblems(String templatePath, List problems)
  {
    IFile file = project.getFile(templatePath);
    if(file.exists()) {
      try
      {
        for (Iterator i = problems.iterator(); i.hasNext();)
        {
          Problem problem = (Problem)i.next();
          IMarker marker = file.createMarker(COMPILE_PROBLEM_MARKER);
          marker.setAttribute(IMarker.LINE_NUMBER, problem.getLineNumber());
          marker.setAttribute(IMarker.CHAR_START, problem.getStart());
          marker.setAttribute(IMarker.CHAR_END, problem.getEnd());
          marker.setAttribute(IMarker.MESSAGE, problem.getMessage());
          marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
          if (problem.getProblemSeverity() == ProblemSeverity.ERROR)
          {
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
          }
          else if (problem.getProblemSeverity() == ProblemSeverity.WARNING)
          {
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
          }
          else
          {
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_LOW);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
          }
        }
      }
      catch (CoreException e)
      {
        InternalJET2Platform.logError(JET2Messages.JET2Compiler_ErrorWritingJava, e);
      }
    }
  }

  public void preCompile(String templatePath)
  {
    IFile file = project.getFile(templatePath);
    try
    {
      if(file.exists()) {
        file.deleteMarkers(COMPILE_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
        file.deleteMarkers(RUNTIME_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
      }
    }
    catch (CoreException e)
    {
      InternalJET2Platform.logError(JET2Messages.JET2Compiler_ErrorWritingJava, e);
    }    
  }

  public TagLibrary getLibrary(String tagLibraryID)
  {
    return TagLibraryManager.getInstance().getTagLibrary(tagLibraryID);
  }

  public String[] getAffectedTemplatePaths(String changedFilePath)
  {
    return simplifiedCompiler.getAffectedTemplatePaths(changedFilePath);
  }

  

}
