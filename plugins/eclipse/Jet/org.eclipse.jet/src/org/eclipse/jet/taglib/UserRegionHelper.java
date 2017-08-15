/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.taglib;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.IWriterListener;
import org.eclipse.jet.IWriterListenerExtension;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;


/**
 * Utility class that allows implementation of user regions like &lt;c:userRegion&;gt.
 * <p>
 * To consume this class, the document must include two regions, one nested inside the
 * other. Two static methods are used to mark these regions:
 * <bl>
 * <li> {@link #markUserRegion(JET2Writer, int, int) markUserRegion()}</li>
 * <li> {@link #markInitialCode(JET2Writer, int, int) markInitialCode()}</li>
 * </bl>
 * </p>
 *
 */
public final class UserRegionHelper implements IWriterListener, IWriterListenerExtension
{
  /**
   * Position subclass that tracks user regions that include an unmodified marker.
   */
  private static class PositionWithUnmodifiedMarker extends Position
  {

    public final String unmodifiedMarker;

    /**
     * @param offset
     * @param length
     */
    public PositionWithUnmodifiedMarker(int offset, int length, String unmodifiedMarker)
    {
      super(offset, length);
      this.unmodifiedMarker = unmodifiedMarker;

    }

  }

  private static final String INITIALCODE_POSITION_CATEGORY = "org.eclipse.jet.internal.taglib.control.initialcode"; //$NON-NLS-1$

  private static final String ROUNDTRIP_LISTENER_CATEGORY = "org.eclipse.jet.internal.taglib.control.userRegion"; //$NON-NLS-1$

  private static final String USERREGION_POSITION_CATEGORY = "org.eclipse.jet.internal.taglib.control.userRegion"; //$NON-NLS-1$

  private UserRegionHelper()
  {
    // do nothing, but prevent external construction.
  }

  public void finalizeContent(JET2Writer writer, Object file)
  {
    if(writer instanceof BufferedJET2Writer) {
      BufferedJET2Writer bufferedwriter = (BufferedJET2Writer)writer;
      IDocument document = (IDocument)bufferedwriter.getAdapter(IDocument.class);
      if (file instanceof IFile)
      {
        IFile iFile = (IFile)file;
        if (iFile.exists() && document != null)
        {
          String fileContents;
          try
          {
            fileContents = TagUtil.getContents(iFile.getLocation());
          }
          catch (CoreException e)
          {
            return;
          }
  
          mergeUserRegions(bufferedwriter, document, fileContents);
  
        }
      }
    }

  }

  /**
   * @param bufferedwriter
   * @param document
   * @param fileContents
   * @throws RuntimeException
   */
  private void mergeUserRegions(BufferedJET2Writer bufferedwriter, IDocument document, String fileContents) throws RuntimeException
  {
    try
    {
      Position[] rtPositions = document.getPositions(UserRegionHelper.USERREGION_POSITION_CATEGORY);
      Position[] icPositions = document.getPositions(UserRegionHelper.INITIALCODE_POSITION_CATEGORY);
      for (int i = 0; i < rtPositions.length; i++)
      {
        // don't assume there will be an initial code
        // position, find it...
        final Position userRegionPosition = rtPositions[i];
        Position initialCodePosition = findContainedInitialCodePosition(userRegionPosition, icPositions);

        if (initialCodePosition == null)
        {
          continue;
        }

        String unmodifiedMarker = initialCodePosition instanceof PositionWithUnmodifiedMarker
          ? ((PositionWithUnmodifiedMarker)initialCodePosition).unmodifiedMarker : null;

        final Position beginMarkerPosition = new Position(userRegionPosition.offset, initialCodePosition.offset
          - userRegionPosition.offset);
        int endMarkerOffset = initialCodePosition.offset + initialCodePosition.length;
        final Position endMarkerPosition = new Position(endMarkerOffset, userRegionPosition.offset + userRegionPosition.length
          - endMarkerOffset);

        final String beginMarker = bufferedwriter.getContent(beginMarkerPosition.offset, beginMarkerPosition.length);
        final String endMarker = bufferedwriter.getContent(endMarkerPosition.offset, endMarkerPosition.length);

        final int existingRegionOffset = fileContents.indexOf(beginMarker);
        if (existingRegionOffset != -1)
        {
          final int existingEndMarkerOffset = fileContents.indexOf(endMarker, existingRegionOffset + beginMarker.length());
          if (existingEndMarkerOffset != -1)
          {
            final String existingRegion = fileContents.substring(existingRegionOffset, existingEndMarkerOffset + endMarker.length());
            if (!existingRegion.equals(bufferedwriter.getContent(userRegionPosition.offset, userRegionPosition.length))
              && (unmodifiedMarker == null || existingRegion.indexOf(unmodifiedMarker) == -1))
            {
              bufferedwriter.replaceContent(userRegionPosition.offset, userRegionPosition.length, existingRegion);
            }
          }
        }
      }
    }
    catch (BadPositionCategoryException e)
    {
      // should not happen. Track with a Runtime exception
      throw new RuntimeException(e);
    }
  }

  private Position findContainedInitialCodePosition(Position userRegionPosition, Position[] icPositions)
  {
    // simple linear search. Could be faster as a binary search...
    for (int i = 0; i < icPositions.length; i++)
    {
      Position pos = icPositions[i];
      if(pos.isDeleted()) {
        // skip deleted positions - they are not updated
        continue;
      }
      if (pos.offset + pos.length > userRegionPosition.offset + userRegionPosition.length)
      {
        return null;
      }
      else if (userRegionPosition.offset <= pos.offset)
      {
        return pos;
      }
    }
    return null;
  }

  public void postCommitContent(JET2Writer writer, Object file)
  {
    // nothing to do here...
  }

  /**
   * Mark the initial code portion of the user region.
   * @param out a JET2Writer
   * @param initialCodeStart the inclusive offset (zero-based) of the start of the region's initial code block (immediately after the start tag).
   * @param initialCodeEnd the exclusive offset (zero-based) of the end of the region's initial code block (offset of the end tag).
   * @param unmodifiedMarker if non-null, indicates a string, that if present in the documents initial code indicates the initial code is unmodifiedand may be replaced
   * @since 0.7.1
   */
  public static void markInitialCode(JET2Writer out, final int initialCodeStart, int initialCodeEnd, String unmodifiedMarker)
  {
    if(!(out instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)out;
    IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
    if(document == null) {
      throw new NullPointerException();
    }
    DocumentHelper.installPositionCategory(document, INITIALCODE_POSITION_CATEGORY);
    try
    {
      if (unmodifiedMarker == null)
      {
        document.addPosition(INITIALCODE_POSITION_CATEGORY, new Position(initialCodeStart, initialCodeEnd - initialCodeStart));
      }
      else
      {
        document.addPosition(INITIALCODE_POSITION_CATEGORY, new PositionWithUnmodifiedMarker(
          initialCodeStart,
          initialCodeEnd - initialCodeStart,
          unmodifiedMarker));
      }
    }
    catch (BadLocationException e)
    {
      throw new IllegalArgumentException();
    }
    catch (BadPositionCategoryException e)
    {
      // should not happen. We did an addPositionCategory. Throw a runtime exception to track
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Mark the initial code portion of the user region. Equivalent to 
   * <code>markInitialCode(out, initialCodeStart, initialCodeEnd, null)</code>.
   * @param out a JET2Writer
   * @param initialCodeStart the inclusive offset (zero-based) of the start of the region's initial code block (immediately after the start tag).
   * @param initialCodeEnd the exclusive offset (zero-based) of the end of the region's initial code block (offset of the end tag).
   * @see #markInitialCode(JET2Writer, int, int, String)
   */
  public static void markInitialCode(JET2Writer out, final int initialCodeStart, int initialCodeEnd) {
    markInitialCode(out, initialCodeStart, initialCodeEnd, null);
  }
  
  /**
   * Mark the user region on the output writer
   * @param out a JET2Writer
   * @param regionStart the inclusive offset (zero-based) of the start of the region's content (immediately after the start tag).
   * @param regionEnd the exclusive offset (zero-based) of the end of the region's content (offset of the end tag).
   */
  public static void markUserRegion(JET2Writer out, int regionStart, int regionEnd)
  {
    if(!(out instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)out;
    IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
    if(document == null) {
      throw new NullPointerException();
    }
    DocumentHelper.installPositionCategory(document, INITIALCODE_POSITION_CATEGORY);
    DocumentHelper.installPositionCategory(document, USERREGION_POSITION_CATEGORY);
    try
    {
      document.addPosition(USERREGION_POSITION_CATEGORY, new Position(regionStart, regionEnd - regionStart));
      out.addEventListener(ROUNDTRIP_LISTENER_CATEGORY, new UserRegionHelper());
    }
    catch (BadLocationException e)
    {
      throw new IllegalArgumentException();
    }
    catch (BadPositionCategoryException e)
    {
      // Should not happen - we did addPositionCategory. Throw a RuntimeException to track this.
      throw new RuntimeException(e);
    }
  }

  public void finalizeContent(JET2Writer writer, Object fileObject, String existingContent) throws JET2TagException
  {
    if(writer instanceof BufferedJET2Writer) {
      BufferedJET2Writer bufferedwriter = (BufferedJET2Writer)writer;
      IDocument document = (IDocument)bufferedwriter.getAdapter(IDocument.class);
      if (existingContent != null)
      {
        mergeUserRegions(bufferedwriter, document, existingContent);
      }
    }

  }
}