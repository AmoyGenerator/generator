/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: JavaFormatTag.java,v 1.1 2009/04/30 16:27:33 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.java;


import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;


/**
 * Invokes the Eclipse JDT Code formatter on its body
 * @author Edoardo Comar ecomar@uk.ibm.com
 */
public class JavaFormatTag extends AbstractFunctionTag
{
  public static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/javatag")).booleanValue(); //$NON-NLS-1$

  public JavaFormatTag()
  {
  }

  public String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {
    try
    {
      String snippetKindString = getAttribute("kind"); //$NON-NLS-1$
      String projectName = getAttribute("project"); //$NON-NLS-1$
      
      int snippetKind = getSnippetKind(snippetKindString, context);
      Map options = getJavaCoreOptions(projectName, context); 
      
      String result = invokeJavaFormatterOn(bodyContent, options, snippetKind, context);
      return result;
    }
    catch  (JET2TagException j2tex) 
    {
      throw j2tex;
    }
    catch (Exception e)
    {
      throw new JET2TagException(e);
    }

  }

  private int getSnippetKind(String snippetKindString, JET2Context context)
  {
    if(DEBUG) System.out.println("JavaFormatTag.getSnippetKind - value specified=" + snippetKindString); //$NON-NLS-1$

    if("K_COMPILATION_UNIT".equals(snippetKindString)) { //$NON-NLS-1$
      return CodeFormatter.K_COMPILATION_UNIT + CodeFormatter.F_INCLUDE_COMMENTS;
    }  
    else if("K_CLASS_BODY_DECLARATIONS".equals(snippetKindString)) { //$NON-NLS-1$
      return CodeFormatter.K_CLASS_BODY_DECLARATIONS;
    }
    else if("K_EXPRESSION".equals(snippetKindString)) { //$NON-NLS-1$
      return CodeFormatter.K_EXPRESSION;
    }
    else if("K_STATEMENTS".equals(snippetKindString)) { //$NON-NLS-1$
      return CodeFormatter.K_STATEMENTS;
    }

    if(DEBUG) System.out.println("JavaFormatTag.getSnippetKind - Falling back to K_UNKNOWN"); //$NON-NLS-1$
    return CodeFormatter.K_UNKNOWN + CodeFormatter.F_INCLUDE_COMMENTS;
  }

  private Map getJavaCoreOptions(String projectName, JET2Context context) throws CoreException
  {
    if(DEBUG) System.out.println("JavaFormatTag.getJavaCoreOptions - project specified=" + projectName); //$NON-NLS-1$
    Map options = null;
    
    if(projectName!=null) {
      IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = wsroot.getProject(projectName);
      if(project.isAccessible() && project.hasNature(JavaCore.NATURE_ID)) {
        IJavaProject javaProject = JavaCore.create(project);

        if(DEBUG) System.out.println("JavaFormatTag.getJavaCoreOptions - using Options from java project " + projectName); //$NON-NLS-1$
        options = javaProject.getOptions(true);
      }
    }
    
    if(options==null) {
      if(DEBUG) System.out.println("JavaFormatTag.getJavaCoreOptions - Falling back to generic JavaCore options"); //$NON-NLS-1$
      options = JavaCore.getOptions();
    }
    
    return options;
  }

  private String invokeJavaFormatterOn(String bodyContent, Map options, int snippetKind, JET2Context context) 
  throws MalformedTreeException, BadLocationException
  {
    CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options, ToolFactory.M_FORMAT_EXISTING);
    
    TextEdit textEdit = codeFormatter.format(snippetKind, 
      bodyContent, /* the whole body of the tag */
    0 /*from beginning */, 
    bodyContent.length() /* till the end */, 
    0 /* initial indentation */, 
    null /* use platform default */);

    if(textEdit==null) {
      //context.logError(Messages.JavaFormatTag_CouldNotFormat);
      return bodyContent;
    }
    
    IDocument document = new Document(bodyContent);
    textEdit.apply(document);

    String result = document.get();
    return result;
  }

}
