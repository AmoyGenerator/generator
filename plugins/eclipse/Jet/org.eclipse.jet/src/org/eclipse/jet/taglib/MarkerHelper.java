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
 * $Id$
 * /
 *******************************************************************************/
package org.eclipse.jet.taglib;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.IWriterListener;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;

/**
 * Helper for implementing task markers on writer output.
 */
public class MarkerHelper implements IWriterListener
{

  private static final String TASKMARKER_ID = "org.eclipse.jet.taskmarker"; //$NON-NLS-1$

  private static final class MarkerPosition extends Position
  {

    public final String description;
    public final TagInfo tagInfo;
    public final String templatePath;

    public MarkerPosition(int start, int length, String description, TagInfo tagInfo, String templatePath)
    {
      super(start, length);
      this.description = description;
      this.tagInfo = tagInfo;
      this.templatePath = templatePath;
    }
    
  }
  
  private static final String MARKER_CATEGORY = MarkerHelper.class.getName();
  /**
   * 
   */
  private MarkerHelper()
  {
    // do nothing
  }

  public void finalizeContent(JET2Writer writer, Object file) throws JET2TagException
  {
    // nothing to do...
  }

  public void postCommitContent(JET2Writer writer, Object file) throws JET2TagException
  {
    if(!(writer instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)writer;
    IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
    if(file instanceof IFile)
    {
      IFile iFile = (IFile)file;
      try
      {
        iFile.deleteMarkers(TASKMARKER_ID, false, IResource.DEPTH_ZERO);
        Position[] positions = document.getPositions(MARKER_CATEGORY);
        for (int i = 0; i < positions.length; i++)
        {
          MarkerPosition markerPos = (MarkerPosition)positions[i];
          
          final IMarker marker = iFile.createMarker(TASKMARKER_ID);
          int line = document.getLineOfOffset(markerPos.offset);
          int start = markerPos.offset;
          int end = markerPos.offset + markerPos.length;

          // for empty marked regions, turn this into a marker on the line.
          if(start == end)
          {
            final IRegion lineInformation = document.getLineInformation(line);
            start = lineInformation.getOffset();
            end = start + lineInformation.getLength();
          }
          
          String msg = markerPos.description;
          if(msg == null || msg.length() == 0)
          {
            msg = bufferedWriter.getContent(start, end - start);
          }
          marker.setAttribute(IMarker.LINE_NUMBER, line);
          marker.setAttribute(IMarker.CHAR_START, start);
          marker.setAttribute(IMarker.CHAR_END, end);
          marker.setAttribute(IMarker.MESSAGE, msg);
//          marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
        }
      }
      catch (CoreException e)
      {
        final String msg = JET2Messages.MarkerHelper_CouldNotCreateMarker;
        throw new JET2TagException(MessageFormat.format(msg, new Object[] {iFile.getFullPath().toString()}), e);
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

  /**
   * Specify a region of the writer that will become a workspace marker when the
   * file contents are committed.
   * @param writer the current writter
   * @param start the start offset of the marker within the current buffer
   * @param end the offset of the first character after the marker
   * @param description the marker descrpition
   * @param tagInfo the tag information of the tag creating the marker. May be <code>null</code>
   * @param templatePath the path of the currently executing template. May be <code>null</code>.
   * @throws IllegalArgumentException if writer is not a {@link BufferedJET2Writer}, or if
   * start and end are not with in the writer contents.
   */
  public static void createMarkerOnWriter(JET2Writer writer, int start, int end, String description, TagInfo tagInfo, String templatePath)
  {
    if(!(writer instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    try
    {
      IDocument document = (IDocument)((BufferedJET2Writer)writer).getAdapter(IDocument.class);
      if(document == null) {
        throw new UnsupportedOperationException();
      }
      DocumentHelper.installPositionCategory(document, MARKER_CATEGORY);
      final MarkerPosition markerPosition = new MarkerPosition(start, end - start, description, tagInfo, templatePath);
      document.addPosition(MARKER_CATEGORY, markerPosition);
      writer.addEventListener(MARKER_CATEGORY, new MarkerHelper());
    }
    catch (BadLocationException e)
    {
      throw new IllegalArgumentException();
    }
    catch (BadPositionCategoryException e)
    {
      // should not happen, wrap in RuntimeException
      throw new RuntimeException();
    }
  }
}
