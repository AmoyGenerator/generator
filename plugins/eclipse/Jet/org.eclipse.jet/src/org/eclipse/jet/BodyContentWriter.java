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

package org.eclipse.jet;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jface.text.AbstractDocument;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.ILineTrackerExtension;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextStore;
import org.eclipse.jface.text.Position;


/**
 * Standard implementation of BufferedJET2Writer that uses on {@link IDocument}
 * as its buffer.
 *
 */
public class BodyContentWriter implements BufferedJET2Writer
{

  /**
   * A line track that assumes that the document in being written too way more than
   * line information is required. It constructs a line tracker only when line information
   * is required. Once used, the line tracker is discarded, rather than maintaining it.
   */
  private static class LazyLineTracker implements ILineTracker, ILineTrackerExtension {
    
    
    private final IDocument document;

    public LazyLineTracker(IDocument document) {
      this.document = document;
    }

    /**
     * Return a delegate line tracker that does the actual work...
     * @return
     */
    private ILineTracker getDelegate() {
      final ILineTracker lineTracker = new DefaultLineTracker();
      lineTracker.set(document.get());
      return lineTracker;
    }
    
    public int computeNumberOfLines(String text)
    {
      return getDelegate().computeNumberOfLines(text);
    }

    public String[] getLegalLineDelimiters()
    {
      return getDelegate().getLegalLineDelimiters();
    }

    public String getLineDelimiter(int line) throws BadLocationException
    {
      return getDelegate().getLineDelimiter(line);
    }

    public IRegion getLineInformation(int line) throws BadLocationException
    {
      return getDelegate().getLineInformation(line);
    }

    public IRegion getLineInformationOfOffset(int offset) throws BadLocationException
    {
      return getDelegate().getLineInformationOfOffset(offset);
    }

    public int getLineLength(int line) throws BadLocationException
    {
      return getDelegate().getLineLength(line);
    }

    public int getLineNumberOfOffset(int offset) throws BadLocationException
    {
      return getDelegate().getLineNumberOfOffset(offset);
    }

    public int getLineOffset(int line) throws BadLocationException
    {
      return getDelegate().getLineOffset(line);
    }

    public int getNumberOfLines()
    {
      return getDelegate().getNumberOfLines();
    }

    public int getNumberOfLines(int offset, int length) throws BadLocationException
    {
      return getDelegate().getNumberOfLines(offset, length);
    }

    public void replace(int offset, int length, String text) throws BadLocationException
    {
      // do nothing...
    }

    public void set(String text)
    {
      // do nothing...
    }

    public void startRewriteSession(DocumentRewriteSession session) throws IllegalStateException
    {
      // do nothing...
    }

    public void stopRewriteSession(DocumentRewriteSession session, String text)
    {
      // do nothing...
    }
    
  }
  /**
   * IDocument implementationt that is optimized for appending, and that provides not
   * document change notifications.
   */
  private static class StringBufferDocument extends AbstractDocument
  {
    
    private boolean defaultPositionCategoryInitialized = false;
    private static final Position[] EMPTY_POSITION_ARRAY = new Position[0];
    private ILineTracker lazyLineTracker;
    private final ITextStore textStore = new StringBufferTextStore(); 
    
    public StringBufferDocument() {
      super();
      // no need to setTextStore() - we override getStore()
      // no need to setLineTracker() - we override getTracker()
      completeInitialization();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#getTracker()
     */
    protected ILineTracker getTracker()
    {
      if(lazyLineTracker == null) {
        lazyLineTracker = new LazyLineTracker(this);
      }
      return lazyLineTracker;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#getStore()
     */
    protected ITextStore getStore()
    {
      // override to avoid overhead of base versions validity checking (Assert.isNotNull).
      return textStore;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#completeInitialization()
     */
    protected void completeInitialization()
    {
      super.completeInitialization();
      // uninstall the default position updater (which should be the only one
      removePositionUpdater(getPositionUpdaters()[0]);
      // and the default position category
      try
      {
        // by-pass the overridden version to actually remove the category
        super.removePositionCategory(DEFAULT_CATEGORY);
      }
      catch (BadPositionCategoryException e)
      {
        // won't happen
      }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#addPosition(java.lang.String, org.eclipse.jface.text.Position)
     */
    public void addPosition(String category, Position position) throws BadLocationException, BadPositionCategoryException
    {
      if(!defaultPositionCategoryInitialized && DEFAULT_CATEGORY.equals(category)) {
        addPositionCategory(DEFAULT_CATEGORY);
        addPositionUpdater(new DefaultPositionUpdater(DEFAULT_CATEGORY));
        defaultPositionCategoryInitialized = true;
      }
      super.addPosition(category, position);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#computeIndexInCategory(java.lang.String, int)
     */
    public int computeIndexInCategory(String category, int offset) throws BadLocationException, BadPositionCategoryException
    {
      if(!defaultPositionCategoryInitialized && DEFAULT_CATEGORY.equals(category)) {
        return 0;
      }
      return super.computeIndexInCategory(category, offset);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#getPositions(java.lang.String)
     */
    public Position[] getPositions(String category) throws BadPositionCategoryException
    {
      if(!defaultPositionCategoryInitialized && DEFAULT_CATEGORY.equals(category)) {
        return EMPTY_POSITION_ARRAY;
      }
      return super.getPositions(category);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#removePosition(java.lang.String, org.eclipse.jface.text.Position)
     */
    public void removePosition(String category, Position position) throws BadPositionCategoryException
    {
      if(!defaultPositionCategoryInitialized && DEFAULT_CATEGORY.equals(category)) {
        return;
      }
      super.removePosition(category, position);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#removePositionCategory(java.lang.String)
     */
    public void removePositionCategory(String category) throws BadPositionCategoryException
    {
      if(!defaultPositionCategoryInitialized && DEFAULT_CATEGORY.equals(category)) {
        // the category has been removed, mark as initialized so we won't automatically
        // add it again.
        defaultPositionCategoryInitialized = true;
        return;
      }
      super.removePositionCategory(category);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#addDocumentListener(org.eclipse.jface.text.IDocumentListener)
     */
    public void addDocumentListener(IDocumentListener listener)
    {
      // this document does not support change notifications.
      throw new UnsupportedOperationException();
    }
    
    public void set(String text, long modificationStamp)
    {
      // implement set without any change notifications
      getStore().set(text);
      getTracker().set(text);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.AbstractDocument#replace(int, int, java.lang.String, long)
     */
    public void replace(int pos, int length, String text, long modificationStamp) throws BadLocationException
    {
      // implement replace without any change notifications
      if ((0 > pos) || (0 > length) || (pos + length > getLength()))
        throw new BadLocationException();

      getStore().replace(pos, length, text);
      getTracker().replace(pos, length, text);
      
      // But, update positions, if we have any...
      DocumentEvent e= new DocumentEvent(this, pos, length, text);
      updatePositions(e);
    }
  }

/**
   * Simple text store optimized for appending
   */
  private static class StringBufferTextStore implements ITextStore
  {

    private final StringBuffer buffer = new StringBuffer(1024);
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.ITextStore#get(int)
     */
    public char get(int offset)
    {
      return buffer.charAt(offset);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.ITextStore#get(int, int)
     */
    public String get(int offset, int length)
    {
      return buffer.substring(offset, offset + length);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.ITextStore#set(java.lang.String)
     */
    public void set(String text)
    {
      buffer.replace(0, buffer.length(), text);
    }

    public int getLength()
    {
      return buffer.length();
    }

    public void replace(int offset, int length, String text)
    {
      if(offset == buffer.length() && length == 0)
      {
        buffer.append(text);
      }
      else
      {
        buffer.replace(offset, offset + length, text);
      }
    }

  }

//	private StringBuffer buffer = new StringBuffer();
  private final BodyContentWriter parentWriter;

  private final IDocument document = new StringBufferDocument();

  private final Map listeners;

  /**
   * 
   */
  public BodyContentWriter()
  {
    super();
    parentWriter = null;
    listeners = new LinkedHashMap(5); // ensure listeners are returned in the order they are added...
  }

  /**
   * Create a nested writer with the passed writer as the parent writer.
   * @param parentWriter the parent writer
   */
  private BodyContentWriter(BodyContentWriter parentWriter)
  {
    this.parentWriter = parentWriter;
    listeners = Collections.EMPTY_MAP; // don't record listeners on child writers...
  }

  /**
   * @see org.eclipse.jet.JET2Writer#write(java.lang.String)
   */
  public final void write(String string)
  {
    try
    {
      document.replace(document.getLength(), 0, string != null ? string : "null"); //$NON-NLS-1$
    }
    catch (BadLocationException e)
    {
      // this should not happen. Log the error.
      InternalJET2Platform.logError("Internal Error", e); //$NON-NLS-1$
    }
  }

  /**
   * @see org.eclipse.jet.JET2Writer#write(org.eclipse.jet.JET2Writer)
   */
  public final void write(JET2Writer bodyContent)
  {
    if(!(bodyContent instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)bodyContent;
    int positionDelta = document.getLength();
    write(bufferedWriter.getContent());
    try
    {
      // transfer position categories, position updaters and positions
      IDocument childDocument = (IDocument)bufferedWriter.getAdapter(IDocument.class);

      // Position updaters should not be copied, instead, they will be added as part of adding
      // any new categories.
      String categories[] = childDocument.getPositionCategories();
      for (int i = 0; i < categories.length; i++)
      {
        // ensure 'document' has the category - no harm in doing this if the category is already there
        DocumentHelper.installPositionCategory(document, categories[i]);
        Position[] positions = childDocument.getPositions(categories[i]);
        for (int j = 0; j < positions.length; j++)
        {
          childDocument.removePosition(categories[i], positions[j]);
          positions[j].setOffset(positions[j].getOffset() + positionDelta);
          document.addPosition(categories[i], positions[j]);
        }
      }
    }
    catch (BadPositionCategoryException e)
    {
      // this exception should not occur in this circumstance. We are getting categories
      // from the document we are applying them against, so we should never encounter problems.
      InternalJET2Platform.logError("Internal Error", e); //$NON-NLS-1$
    }
    catch (BadLocationException e)
    {
      // this exception should not occur in this circumstance. If we are creating invalid positions
      // then this is a sign of programming errors.
      InternalJET2Platform.logError("Internal Error", e); //$NON-NLS-1$
    }
  }

  /**
   * @see org.eclipse.jet.JET2Writer#write(int)
   */
  public final void write(int i)
  {
    write(String.valueOf(i));
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return document.get();
  }

  public final JET2Writer newNestedContentWriter()
  {
    return new BodyContentWriter(this);
  }

  public final int getLength()
  {
    return getContentLength();
  }

  public final IDocument getDocument()
  {
    return document;
  }

  public final void addEventListener(String category, IWriterListener listener)
  {
    if (parentWriter != null)
    {
      parentWriter.addEventListener(category, listener);
    }
    else if (!listeners.containsKey(category))
    {
      listeners.put(category, listener);
    }
  }

  public final void addPositionCategory(String category)
  {
    DocumentHelper.installPositionCategory(document, category);
  }

  public final void addPosition(String category, Position position)
  {
    try
    {
      document.addPosition(category, position);
    }
    catch (BadLocationException e)
    {
      throw new WriterPositionException(e);
    }
    catch (BadPositionCategoryException e)
    {
      throw new WriterPositionException(e);
    }

  }

  public final Position[] getPositions(String category)
  {
    try
    {
      return document.getPositions(category);
    }
    catch (BadPositionCategoryException e)
    {
      throw new WriterPositionException(e);
    }
  }

  public final void replace(int offset, int length, String text)
  {
    try
    {
      document.replace(offset, length, text);
    }
    catch (BadLocationException e)
    {
      throw new WriterPositionException(e);
    }
  }

  public final JET2Writer getParentWriter()
  {
    return parentWriter;
  }

  public final void write(boolean b)
  {
    write(String.valueOf(b));
  }

  public final void write(char c)
  {
    write(String.valueOf(c));
  }

  public final void write(char[] data)
  {
    write(String.valueOf(data));
  }

  public final void write(double d)
  {
    write(String.valueOf(d));
  }

  public final void write(float f)
  {
    write(String.valueOf(f));
  }

  public final void write(long l)
  {
    write(String.valueOf(l));
  }

  public final void write(Object obj)
  {
    write(obj != null ? obj.toString() : ""); //$NON-NLS-1$
  }


  public String getContent()
  {
    return document.get();
  }
  

  public int getContentLength()
  {
    return getContent().length();
  }
  

  public IWriterListener[] getEventListeners()
  {
    return (IWriterListener[])listeners.values().toArray(new IWriterListener[listeners.size()]);
  }

  public void replaceContent(int offset, int length, String text)
  {
    try {
      document.replace(offset, length, text);
    }
    catch (BadLocationException e) {
      throw new IllegalArgumentException();
    }
  }

  public void setContent(String content)
  {
    document.set(content);
  }

  public String getContent(int offset, int length)
  {
    try
    {
      return document.get(offset, length);
    }
    catch (BadLocationException e)
    {
      throw new IllegalArgumentException();
    }
  }

  public Object getAdapter(Class adapterClass)
  {
    if(adapterClass == IDocument.class) {
      return document;
    }
    return null;
  }

}
