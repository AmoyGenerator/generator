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
package org.eclipse.jet.internal.taglib.java;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.codegen.CodeGenPlugin;
import org.eclipse.emf.codegen.merge.java.JControlModel;
import org.eclipse.emf.codegen.merge.java.JMerger;
import org.eclipse.emf.codegen.merge.java.facade.FacadeHelper;
import org.eclipse.emf.codegen.merge.java.facade.JCompilationUnit;
import org.eclipse.emf.codegen.merge.properties.PropertyMerger;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.IWriterListener;
import org.eclipse.jet.IWriterListenerExtension;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.TagUtil;
import org.eclipse.jet.transform.TransformContextExtender;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

/**
 * Implement the Java tag &lt;java:merge&gt;.
 */
public class MergeTag extends AbstractEmptyTag
{

  private static final String EMF_CODEGEN_RULES_URL = "platform:/plugin/org.eclipse.emf.codegen.ecore/templates/emf-merge.xml"; //$NON-NLS-1$
  private static final String AST_FACADE = "org.eclipse.emf.codegen.merge.java.facade.ast.ASTFacadeHelper"; //$NON-NLS-1$
  private static final String JDOM_FACADE = "org.eclipse.emf.codegen.merge.java.facade.jdom.JDOMFacadeHelper"; //$NON-NLS-1$
  /**
   * Position marker for JMergeable content.
   */
  public static class MergePosition extends Position
  {

    private final URL rulesURL;
    private final FacadeHelper facadeHelper;

    public MergePosition(URL rulesURL, int offset, FacadeHelper facadeHelper)
    {
      super(offset);
      this.rulesURL = rulesURL;
      this.facadeHelper = facadeHelper;
      
    }

    /**
     * @return Returns the rulesURL.
     */
    public final URL getRulesURL()
    {
      return rulesURL;
    }

    public FacadeHelper getFacadeHelper()
    {
      return facadeHelper;
    }

  }

/**
   * Process a JMergeTag on the template content.
   */
  public static class JMergeProcessor implements IWriterListener, IWriterListenerExtension
  {

    /**
     * 
     */
    public JMergeProcessor()
    {
      super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jet.IWriterListener#finalizeContent(org.eclipse.jet.JET2Writer, java.lang.Object)
     */
    public void finalizeContent(JET2Writer writer, Object file) throws JET2TagException
    {
      if(file instanceof IFile)
      {
        IFile iFile = (IFile)file;
        if(!iFile.exists())
        {
          return;
        }
        if("java".equals(iFile.getFileExtension())) //$NON-NLS-1$
        {
          jmerge(writer, iFile);
        }
        else if("properties".equals(iFile.getFileExtension())) //$NON-NLS-1$
        {
          propmerge(writer, iFile);
        }
      }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jet.IWriterListenerExtension#finalizeContent(org.eclipse.jet.JET2Writer, java.lang.Object, java.lang.String)
     */
    public void finalizeContent(JET2Writer writer, Object fileObject, String existingContent) throws JET2TagException
    {
      if(fileObject instanceof IFile && existingContent != null)
      {
        IFile iFile = (IFile)fileObject;
        if("java".equals(iFile.getFileExtension())) //$NON-NLS-1$
        {
          jmerge(writer, iFile, existingContent);
        }
        else if("properties".equals(iFile.getFileExtension())) //$NON-NLS-1$
        {
          propmerge(writer, iFile, existingContent);
        }
      }
    }

    private void propmerge(JET2Writer writer, IFile file) throws JET2TagException
    {
      try
      {
        propmerge(writer, file, TagUtil.getContents(file.getLocation()));
      }
      catch (CoreException e)
      {
        final String msg = JET2Messages.MergeTag_CouldNotRead;
        throw new JET2TagException(MessageFormat.format(msg, new Object []{ file.getFullPath() }));
      }
    }


    private void propmerge(JET2Writer writer, IFile file, String targetPropertiesString) throws JET2TagException
    {
      PropertyMerger merger = new PropertyMerger();

      merger.setSourceProperties(((BufferedJET2Writer)writer).getContent());
      merger.setTargetProperties(targetPropertiesString);

      merger.merge();

      ((BufferedJET2Writer)writer).setContent(merger.getTargetProperties());
    }

    /**
     * @param writer
     * @param iFile
     * @throws JET2TagException
     */
    private void jmerge(JET2Writer writer, IFile iFile) throws JET2TagException
    {
      try
      {
        jmerge(writer, iFile, TagUtil.getContents(iFile.getLocation()));
      }
      catch (CoreException e1)
      {
        final String msg = JET2Messages.MergeTag_CouldNotRead;
        throw new JET2TagException(MessageFormat.format(msg, new Object []{ iFile.getFullPath() }));
      }
    }
    
    /**
     * @param writer
     * @param iFile
     * @param targetCUSource
     * @throws JET2TagException
     */
    private void jmerge(JET2Writer writer, IFile iFile, String targetCUSource) throws JET2TagException
    {
      // cast with impunity - we know the following will work because the writer was set
      // up this way. If we're wrong, the runtime exception is as good as any.
      final IDocument document = (IDocument)((BufferedJET2Writer)writer).getAdapter(IDocument.class);
      try
      {
        final Position[] positions = document.getPositions(JMERGE_POSITION_CATEGORY);
        
        MergePosition mergePosition = (MergePosition)positions[0];
        FacadeHelper facadeHelper = mergePosition.getFacadeHelper();
        JControlModel controlModel = new JControlModel();
        
        controlModel.initialize(facadeHelper, mergePosition.getRulesURL().toExternalForm());

        JMerger merger = new JMerger(controlModel);
        JCompilationUnit sourceCU = getJCompilationUnit(facadeHelper, iFile, writer.toString(), true);
        JCompilationUnit targetCU = getJCompilationUnit(facadeHelper, iFile, targetCUSource, false);

        merger.setSourceCompilationUnit(sourceCU);
        merger.setTargetCompilationUnit(targetCU);
        
        merger.merge();
        ((BufferedJET2Writer)writer).setContent(targetCU.getContents());
      }
      catch (BadPositionCategoryException e)
      {
        // This should not happen. Wrap in a runtime exception
        throw new RuntimeException(e);
      }
      catch (WrappedException e)
      {
        final String baseMsg = JET2Messages.MergeTag_FailedOnCompilerError;
        final String msg = MessageFormat.format(baseMsg, new Object[] {iFile.getFullPath().makeRelative()});
        throw new JET2TagException(msg, e.exception());
      }
      
    }

   /**
     * @param facadeHelper
     * @param iFile
     * @param content
     * @param isGenContent 
     * @return
     */
    private JCompilationUnit getJCompilationUnit(FacadeHelper facadeHelper, IFile iFile, final String content, boolean isGenContent)
    {
      try {
        JCompilationUnit sourceCU = facadeHelper.createCompilationUnit(iFile.getName(), content);
      return sourceCU;
      } catch( WrappedException e ) {
        final String baseMsg = isGenContent ? JET2Messages.MergeTag_GeneratedJavaError : JET2Messages.MergeTag_FailedOnCompilerError;
        final String msg = MessageFormat.format(baseMsg, new Object[] {iFile.getFullPath().makeRelative()});
        throw new JET2TagException(msg, e.exception());
      }
    }
    /* (non-Javadoc)
     * @see org.eclipse.jet.IWriterListener#postCommitContent(org.eclipse.jet.JET2Writer, java.lang.Object)
     */
    public void postCommitContent(JET2Writer writer, Object file)
    {
      // nothing to do.
    }

  }

private static final String JMERGE_POSITION_CATEGORY = MergeTag.class.getName();;

  /**
   * 
   */
  public MergeTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    if(!(out instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)out;
    IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
    
    String rules = getAttribute("rules"); //$NON-NLS-1$
    if(rules == null)
    {
      rules = EMF_CODEGEN_RULES_URL;
    }
    String rulesContext = getAttribute("rulesContext"); //$NON-NLS-1$
    if(rulesContext == null)
    {
      rulesContext = "transform"; //$NON-NLS-1$
    }
    
    String facade = getAttribute("facade"); //$NON-NLS-1$
    if(facade == null) {
      String emfCodeGenVersion = (String)CodeGenPlugin.getPlugin().getBundle().getHeaders().get("Bundle-Version"); //$NON-NLS-1$
      // The AST facade rules don't seem to work well in 2.2.x
      facade = emfCodeGenVersion.startsWith("2.2.") ? JDOM_FACADE : AST_FACADE; //$NON-NLS-1$
    }
    
    final URL baseURL = TransformContextExtender.getInstance(context).getBaseURL(rulesContext);
    
    if(rules.startsWith("/")) //$NON-NLS-1$
    {
      rules = rules.substring(1);
    }
    
    try
    {
      FacadeHelper facadeHelper = CodeGenUtil.instantiateFacadeHelper(facade); 
      if(facadeHelper == null) {
        throw new JET2TagException(MessageFormat.format(JET2Messages.MergeTag_UnknownJMergeFacadeHelper, 
          new Object []{ facade }));
      }
      URL rulesURL = new URL(baseURL, rules);
      
      DocumentHelper.installPositionCategory(document, JMERGE_POSITION_CATEGORY);
      
      document.addPosition(JMERGE_POSITION_CATEGORY, new MergePosition(rulesURL, bufferedWriter.getContentLength(), facadeHelper));
      
      bufferedWriter.addEventListener(JMERGE_POSITION_CATEGORY, new JMergeProcessor());
    }
    catch (MalformedURLException e)
    {
      String msg = JET2Messages.MergeTag_CouldNotCreateURL;
      throw new JET2TagException(MessageFormat.format(msg, 
        new Object[] {baseURL.toExternalForm(), rules}));
    }
    catch (BadLocationException e)
    {
      throw new JET2TagException(e);
    }
    catch (BadPositionCategoryException e)
    {
      throw new JET2TagException(e);
    }
  }

}
