/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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


import java.util.ArrayList;

import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.IWriterListener;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.java.JavaActionsUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;


/**
 * Implement the JET2 standard Java tag 'importsLocation'.
 *
 */
public class ImportsLocationTag extends AbstractEmptyTag
{

  public static final String IMPORTS_POSITION_CATEGORY = "org.eclipse.jet.internal.taglib.java.imports"; //$NON-NLS-1$

  private static final String IMPORTS_LISTENER_CATEGORY = "org.eclipse.jet.internal.taglib.java.imports"; //$NON-NLS-1$

  private static final class WriterEventListener implements IWriterListener
  {
    private WriterEventListener()
    {
      super();
    }

    public void finalizeContent(JET2Writer writer, Object file)
    {
      if(writer instanceof BufferedJET2Writer) {
        IDocument document = (IDocument)((BufferedJET2Writer)writer).getAdapter(IDocument.class);
        if(document != null) {
          try
          {
            Position[] importLocations = document.getPositions(ImportsLocationTag.IMPORTS_POSITION_CATEGORY);
  
            // there should be exactly one of these (see ensureNoEarlierImportLocations())
  
            if (importLocations.length > 0)
            {
              ImportsPosition position = (ImportsPosition)importLocations[0];
              document.replace(position.offset, position.length, position.getImportManager().computeSortedImports());
            }
          }
          catch (BadPositionCategoryException e)
          {
            // should not happen
            throw new RuntimeException(e);
          }
          catch (BadLocationException e)
          {
            // should not happen
            throw new RuntimeException(e);
          }
        }
      }
    }

    public void postCommitContent(JET2Writer writer, Object file)
    {
      // we have no interest in this event
    }
  }

  public static final class ImportsPosition extends DocumentHelper.InsertAfterEmptyPosition
  {

    private final ImportManager importManager;

    public ImportsPosition(int start, String compilationUnitPackage, String preceedingContents)
    {
      super(start);
      importManager = new ImportManager(compilationUnitPackage);
      importManager.addJavaLangImports(new ArrayList(CodeGenUtil.getJavaDefaultTypes()));
      importManager.addCompilationUnitImports(preceedingContents);
    }

    /**
     * Return the import manager associated with the position
     * @return Returns the importManager.
     */
    public final ImportManager getImportManager()
    {
      return importManager;
    }
  }

  /**
   * 
   */
  public ImportsLocationTag()
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
    final BufferedJET2Writer bufferedWriter = ((BufferedJET2Writer)out);
    final IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
    // validate that this is the first instance of this tag to execute...
    // We do this by looking for an IMPORTS_POSITION_CATEGORY on this writer and all parents.

    ensureNoEarlierImportsLocation(out);

    String compilationUnitPackage = getAttribute("package"); //$NON-NLS-1$

    int importsOffset = bufferedWriter.getContentLength();
    Position importsPosition = new ImportsPosition(importsOffset, compilationUnitPackage, bufferedWriter.getContent());

    DocumentHelper.installPositionCategory(document, IMPORTS_POSITION_CATEGORY);
    try
    {
      document.addPosition(IMPORTS_POSITION_CATEGORY, importsPosition);
    }
    catch (BadLocationException e)
    {
      throw new JET2TagException(e);
    }
    catch (BadPositionCategoryException e)
    {
      throw new JET2TagException(e);
    }

    bufferedWriter.addEventListener(IMPORTS_LISTENER_CATEGORY, new WriterEventListener());
  }

  private void ensureNoEarlierImportsLocation(JET2Writer writer) throws JET2TagException
  {
    if(writer instanceof BufferedJET2Writer) {
      BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)writer;
      IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
      // defense add of the category, in case it is not already added to the document
      DocumentHelper.installPositionCategory(document, IMPORTS_POSITION_CATEGORY);
  
      try
      {
        Position[] positions = document.getPositions(IMPORTS_POSITION_CATEGORY);
        if (positions.length > 0)
        {
          throw new JET2TagException(JET2Messages.ImportsLocationTag_AllowedOnlyOnce);
        }
      }
      catch (BadPositionCategoryException e)
      {
        // should not happen. Throw an runtime exception so we know about this
        throw new RuntimeException(e);
      }
  
      if (writer.getParentWriter() != null)
      {
        ensureNoEarlierImportsLocation(writer.getParentWriter());
      }
    }
  }

  /**
   * 
   * @param writer
   * @return
   * @throws JET2TagException
   * @deprecated Use {@link JavaActionsUtil#getImportManager(JET2Writer)} instead.
   */
  public static ImportManager getImportManager(JET2Writer writer) throws JET2TagException
  {
    return JavaActionsUtil.getImportManager(writer); 
  }

}
