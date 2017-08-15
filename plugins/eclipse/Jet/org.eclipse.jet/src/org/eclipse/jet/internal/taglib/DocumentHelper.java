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
 * $Id: DocumentHelper.java,v 1.3 2009/05/02 02:58:59 pelder Exp $
 */
package org.eclipse.jet.internal.taglib;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;

/**
 * Helper class for handling IDocument instances
 */
public class DocumentHelper
{
  /**
   * Optional interface on a {@link Position} that allows the position
   * to control how it is updated rather than a position updater
   */
  public interface IPositionUpdaterOverride {
    /**
     * Allow the position to update itself
     * @param changeOffset the offset of the change occuring
     * @param changeLength the original length of the changed text
     * @param replacementLength the length of the new text
     * @param fDocument 
     * @return <code>true</code> if the position was updated and the default updater should not be used.
     */
    public boolean update(int changeOffset, int changeLength, int replacementLength, IDocument fDocument);
  }
  
  /**
   * Implement a zero-length position that stays in front of an insertion at its position.
   * @see IPositionUpdaterOverride
   * @see OverrideablePositionUpdater
   */
  public static class InsertAfterEmptyPosition extends Position implements IPositionUpdaterOverride {

    private boolean enableInsertBefore;

    public InsertAfterEmptyPosition(int length)
    {
      super(length);
    }

    public boolean update(int changeOffset, int changeLength, int replacementLength, IDocument document)
    {
      // if this is an insertion (changeLength == 0) and this is a zero-length position
      // and its happening at this position, we'll handle the update...
      if( changeLength == 0 && this.length == 0 && changeOffset == this.offset) {
        
        // N.B. The document has already been modified, the original document length is:
        final int originalDocumentLength = document.getLength() - replacementLength;
        if(this.offset == originalDocumentLength) {
          // at the end of the buffer, don't move or expand the position
          // NO code necessary!
          return true;
        } else if(enableInsertBefore) {
          // move the position to after the insertion
          this.offset += replacementLength;
          return true;
        } else {
          return true;
        }
      } else {
        return false;
      }
    }

    public void setEnableInsertBefore(boolean enableInsertBefore)
    {
      this.enableInsertBefore = enableInsertBefore;
    }
    
  }
  /**
   * An extension of {@link DefaultPositionUpdater} that respects positions that
   * implement {@link IPositionUpdaterOverride}.
   */
  public static class OverrideablePositionUpdater extends DefaultPositionUpdater {

    /**
     * @see DefaultPositionUpdater#DefaultPositionUpdater(String)
     */
    public OverrideablePositionUpdater(String category)
    {
      super(category);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.DefaultPositionUpdater#adaptToReplace()
     */
    protected void adaptToReplace()
    {
      if(fPosition instanceof IPositionUpdaterOverride 
          && ((IPositionUpdaterOverride)fPosition).update(fOffset, fLength, fReplaceLength, fDocument)) {
        return;
      }
      super.adaptToReplace();
    }
  }
  
  private DocumentHelper() {
    // prevent instantiation.
  }
  
  /**
   * Install the position category if it is not already installed.
   * The position category is configured with a {@link OverrideablePositionUpdater}
   * so that positions may control their updating by implementing {@link IPositionUpdaterOverride}.
   * @param document the document into which the category should be installed
   * @param category the category.
   */
  public static void installPositionCategory(IDocument document, String category)
  {
    if(!document.containsPositionCategory(category)) {
      document.addPositionCategory(category);
      document.addPositionUpdater(new OverrideablePositionUpdater(category));
    }
  }

  public static void indent(BufferedJET2Writer bodyContent, int depth, String indent) {
    final char[] indentArray = indent.toCharArray();
    final int indentLength = indentArray.length;
    
    final char fullIndent[] = new char[indentLength * depth];
    for(int i = 0; i < depth; i++) {
      System.arraycopy(indentArray, 0, fullIndent, i * indentLength, indentLength);
    }
    final String indentation = new String(fullIndent);
    
    final IDocument document = (IDocument)bodyContent.getAdapter(IDocument.class);
    final int numberOfLines = document.getNumberOfLines();
    for(int line = 0; line < numberOfLines; line++) {
        try {
            final IRegion lineInformation = document.getLineInformation(line);
            if(lineInformation.getLength() > 0 || line < numberOfLines - 1) {
                document.replace(lineInformation.getOffset(), 0, indentation);
            }
        } catch (BadLocationException e) {
            final RuntimeException rte = new RuntimeException("Unexpected exception: line = " + line ); //$NON-NLS-1$
            rte.initCause(e);
            throw rte;
        }
        
    }
  }

}
