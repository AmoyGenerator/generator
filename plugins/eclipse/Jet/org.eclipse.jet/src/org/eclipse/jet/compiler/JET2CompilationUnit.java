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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.core.parser.ast.BodyElement;
import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETASTParser;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.core.parser.ast.JETASTParser.Builder;
import org.eclipse.jet.internal.core.parser.jasper.JETReader;
import org.eclipse.jet.internal.parser.resources.WorkspaceTemplateResolverBuilder;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryManager;
import org.eclipse.jet.taglib.TagLibraryReference;


/**
 * Represent a compilation unit (a template) in the JET2 AST.
 * @deprecated Since 0.8.0, use {@link JETCompilationUnit}
 *
 */
public final class JET2CompilationUnit extends JET2ASTElement
{
  private JETCompilationUnit delegate;
  
  private List wrappedBodyElements = null;

  private List wrappedProblems = null;

  
  private Map predefinedLibraryMap = Collections.EMPTY_MAP;

  private final URI baseLocation;

  private final String templatePath;
  
  private final ITemplateResolver templateResolver;

  private final String encoding;

  /**
   *
   */
   public JET2CompilationUnit()
  {
    super(new JET2AST(), null);
    this.baseLocation = null;
    this.templatePath = ""; //$NON-NLS-1$
    templateResolver = null;
    this.encoding = null;
    
    this.delegate = getAst().getDelegateAST().newJETCompilationUnit(baseLocation, templatePath, encoding);
    setDelegate(delegate);
  }

   /**
    * @param file
    */
  public JET2CompilationUnit(IFile file)
  {
    super(new JET2AST(), null);
    this.baseLocation = TemplatePathUtil.baseLocationURI(file);
    this.templatePath = TemplatePathUtil.templatePath(file);
    templateResolver = new WorkspaceTemplateResolverBuilder(file.getProject()).build();
    try
    {
      this.encoding = file.getCharset();
    }
    catch (CoreException e)
    {
      throw new IllegalArgumentException();
    }
    this.delegate = getAst().getDelegateAST().newJETCompilationUnit(baseLocation, templatePath, encoding);
    super.setDelegate(delegate);
  }
  
  public JET2CompilationUnit(JET2AST jet2ast, JETCompilationUnit unit)
  {
    super(jet2ast, unit);
    delegate = unit;
    this.baseLocation = null;
    this.templatePath = ""; //$NON-NLS-1$
    this.templateResolver = null;
    this.encoding = null;
  }

  /**
   * Define tag library prefixes (and associated tag library ids) that are
   * automatically available to the transform.
   * @param predefinedLibraryMap a map from prefix to tag library id.
   */
  public void setPredefinedTagLibraries(Map predefinedLibraryMap)
  {
    this.predefinedLibraryMap = predefinedLibraryMap;
  }
  
  /**
   * @deprecated
   * @param document
   */
  public void parse(String document)
  {
    final String ENCODING = "UTF-8"; //$NON-NLS-1$
    try
    {
      InputStream is = new ByteArrayInputStream(document.getBytes(ENCODING));
      internalParse(is, ENCODING);
    }
    catch (UnsupportedEncodingException e)
    {
      // Should not happen
      throw new RuntimeException("Should not have happened", e); //$NON-NLS-1$
    }

  }

  /**
   * @deprecated
   * @throws CoreException
   */
  public void parse() throws CoreException
  {
    if (templateResolver == null)
    {
      throw new IllegalStateException();
    }

    IFile file = TemplatePathUtil.workspaceFile(baseLocation, templatePath);
    final InputStream contents = file.getContents();
    final String charset = file.getCharset();
    internalParse(contents, charset);

  }

  /**
   * @deprecated
   * @param contents
   * @param charset
   */
  public void parse(InputStream contents, String charset)
  {
    internalParse(contents, charset);
  }

  /**
   * @param contents
   * @param charset
   */
  private void internalParse(final InputStream contents, final String charset)
  {
    // reset parse state...
    wrappedProblems = null;
    wrappedBodyElements = null;
    

    try
    {
      InputStreamReader isr = new InputStreamReader(contents, charset);
      
      JETReader reader = new JETReader(baseLocation == null ? null : baseLocation.toString(), templatePath, isr);
      
      
      final Builder builder = new JETASTParser.Builder(JETAST.JET_SPEC_V2)
              .predefinedTagLibraries(predefinedLibraryMap != null ? predefinedLibraryMap : Collections.EMPTY_MAP)
              .tagLibraryResolver(new ITagLibraryResolver() {
      
                public TagLibrary getLibrary(String tagLibraryID)
                {
                  return TagLibraryManager.getInstance().getTagLibrary(tagLibraryID);
                }});
      if(templateResolver != null) 
      {
        builder.templateResolver(templateResolver);
      }
      
      final JETASTParser parser = builder.build();
      
      delegate = (JETCompilationUnit)parser.parse(reader.getChars());
      setDelegate(delegate);

      return;
    }
    catch (org.eclipse.jet.internal.core.parser.jasper.JETException e)
    {
      // create a minimal compilation unit with the exeception recorded as the error.
      this.createProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), null, 0, 0, 1, 1);
    }
    catch (UnsupportedEncodingException e)
    {
      // create a minimal compilation unit with the exeception recorded as the error.
      this.createProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), null, 0, 0, 1, 1);
    }
    finally
    {
      try
      {
        contents.close();
      }
      catch (IOException e)
      {
        // ignore the io exception, can't do anything about it;
      }
    }
  }

  /**
   * Return a {@link List} of JET2 AST element (@link JETASTElement} instances.
   * @return a List. The empty list of there are no body elements.
   */
  public final List getBodyElements()
  {
    if (wrappedBodyElements == null)
    {
      List delegateBodyElements = delegate.getBodyElements();
      if(delegateBodyElements.size() == 0) {
        wrappedBodyElements = Collections.EMPTY_LIST;
      } else {
        wrappedBodyElements = new ArrayList(delegateBodyElements.size());
        for (Iterator i = delegateBodyElements.iterator(); i.hasNext();)
        {
          BodyElement bodyElement = (BodyElement)i.next();
          wrappedBodyElements.add(getAst().wrap(bodyElement));
        }
      }
    }
    return wrappedBodyElements;
  }

  /**
   * @see org.eclipse.jet.compiler.JET2ASTElement#accept(org.eclipse.jet.compiler.JET2ASTVisitor)
   */
  public void accept(JET2ASTVisitor visitor)
  {
    visitor.visit(this);
    for (Iterator i = getBodyElements().iterator(); i.hasNext();)
    {
      JET2ASTElement element = (JET2ASTElement)i.next();
      element.accept(visitor);
    }
    visitor.endVisit(this);
  }

  /**
   * Return a list of problems discovered in the compilation unit
   * @return a List of {@link Problem} objects. The empty list is returned if no problems were found.
   */
  public List getProblems()
  {
    if (wrappedProblems == null)
    {
      List delegateProblems = delegate.getProblems();
      if(delegateProblems.size() == 0) {
        wrappedProblems = Collections.EMPTY_LIST;
      } else {
        wrappedProblems = new ArrayList(delegateProblems.size());
        for (Iterator i = delegateProblems.iterator(); i.hasNext();)
        {
          org.eclipse.jet.core.parser.ast.Problem problem = (org.eclipse.jet.core.parser.ast.Problem)i.next();
          wrappedProblems.add(getAst().wrap(problem));
        }
      }
    }
    return wrappedProblems;
  }

  /**
   * Test if the compilation unit has any errors
   * @return <code>true</code> if the compilation unit had errors, <code>false</code> otherwise.
   */
  public boolean hasErrors()
  {
    return delegate.hasErrors();
  }

  /**
   * Test if the compilation unit has any warnings
   * @return <code>true</code> if the compilation unit had warnings, <code>false</code> otherwise.
   */
  public boolean hasWarnings()
  {
    return delegate.hasWarnings();
  }

  /**
   * Create a new problem on the compilation unit
   * @param error the severity of the problem
   * @param problemId the problem id. A value from {@link Problem} static files
   * @param message an error message, with optional replacement tokens
   * @param messageArgs the error message arguments
   * @param start the start offset of the problem (doc relative)
   * @param end the end offset of the problem (doc relative)
   * @param line the line number of the problem (1 based)
   * @param colOffset TODO
   */
  public void createProblem(ProblemSeverity error, int problemId, String message, Object[] messageArgs, int start, int end, int line, int colOffset)
  {
    delegate.createProblem(error, problemId, message, messageArgs, start, end, line, colOffset);
  }

  /**
   * Return the name of the Java package to which the compilation unit will be compiled.
   * @return a string
   */
  public String getOutputJavaPackage()
  {
    return delegate.getOutputJavaPackage();
  }

  /**
   * Return the unqualified name of the Java class into which the compilation unit will be compiled.
   * @return Returns the outputJavaClassName.
   */
  public String getOutputJavaClassName()
  {
    return delegate.getOutputJavaClassName();
  }

  /**
   * Set the unqualifeid name of the Java class into which the compilation unit will be compiled.
   * @param outputJavaClassName The outputJavaClassName to set.
   */
  public void setOutputJavaClassName(String outputJavaClassName)
  {
    delegate.setOutputJavaClassName(outputJavaClassName);
  }

  /**
   * Set the Java package into which the compilation unit will be compiled.
   * @param outputJavaPackage The outputJavaPackage to set.
   */
  public void setOutputJavaPackage(String outputJavaPackage)
  {
    delegate.setOutputJavaPackage(outputJavaPackage);
  }

  /**
   * Return an array of tag libraries referenced by this template.
   * @return a possibly empty array of tag library references.
   */
  public TagLibraryReference[] getTagLibraryReferences()
  {
    return delegate.getTagLibraryReferences();
  }

  public void addImports(List list)
  {
    delegate.addImports(list);
  }

  public Set getImports()
  {
    return delegate.getImports();
  }

  /**
   * Return the underlying {@link JETCompilationUnit} to which this instance delegates.
   * Useful for calling mashing only 0.7.x code with later code.
   * @return the underlying {@link JETCompilationUnit}
   * @since 0.8.0
   */
  public JETCompilationUnit getDelegateCU() {
    return delegate;
  }
}
