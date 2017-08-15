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
 * $Id: JET2Writer.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet;


import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;


/**
 * Protocol for content writing in JET2 templates.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *
 */
public interface JET2Writer
{

  /**
   * Write the passed string.
   * @param string a string value.
   */
  public abstract void write(String string);

  /**
   * Write the contents of the passed writer to this writer.
   * @param bodyContent a writer
   */
  public abstract void write(JET2Writer bodyContent);

  /**
   * Write the passed boolean by calling {@link String#valueOf(boolean)}.
   * @param b a boolean value
   */
  public abstract void write(boolean b);

  /**
   * Write the passed character by calling {@link String#valueOf(char)}.
   * @param c a char value
   */
  public abstract void write(char c);

  /**
   * Write the passed character array by calling {@link String#valueOf(char[])}.
   * @param data an array of characters
   */
  public abstract void write(char[] data);

  /**
   * Write the passed double value by calling {@link String#valueOf(double)}.
   * @param d a double value
   */
  public abstract void write(double d);

  /**
   * Write the passed float value by calling {@link String#valueOf(float)}.
   * @param f a float value
   */
  public abstract void write(float f);

  /**
   * Write the passed integer by calling {@link String#valueOf(int)}.
   * @param i an integer value
   */
  public abstract void write(int i);

  /**
   * Write the passed long value calling {@link String#valueOf(long)}.
   * @param l a long value.
   */
  public abstract void write(long l);

  /**
   * Write the pass object by calling {@link Object#toString()}.
   * @param obj an object.
   */
  public abstract void write(Object obj);

  /**
   * Create a writer for handling nested content. The new writer will have access
   * to all position handlers defined on the parent writer (and its parents)
   * @return the nested content writer of type {@link BufferedJET2Writer}
   */
  public abstract JET2Writer newNestedContentWriter();

  /**
   * Return the parent of this writer, if it was created via {@link #newNestedContentWriter()}.
   * @return the parent writer, or <code>null</code>.
   */
  public abstract JET2Writer getParentWriter();

  /**
   * Return the current length (in characters) of the output
   * @return the current length
   * @deprecated Use {@link BufferedJET2Writer#getContentLength()}
   */
  public abstract int getLength();

  /**
   * Return the backing IDocument for this writer. Use this method to do advanced
   * writer processing, such as adding Positions for later re-writing of the document
   * contents.
   * @return the backing document
   * @deprecated Use {@link BufferedJET2Writer}.
   */
  public abstract IDocument getDocument();

  /**
   * Add a listener to the writer life cycle events. The writer records one listener per
   * category. Subsequent calls to this method with the same category value have no effect.
   * If the listener was created view {@link #newNestedContentWriter()}, then the listener is added
   * to the root writer, rather than the listener itself.
   * @param category the listener category
   * @param listener a listener
   * @throws NullPointerException if listener is <code>null</code>.
   */
  public abstract void addEventListener(String category, IWriterListener listener);

  /**
   * Return the registered writer event listeners
   * @return a possibly empty array of listeners
   */
  public abstract IWriterListener[] getEventListeners();
  
  /**
   * Convenience method wrapping getDocument().addPositionCategory(String).
   * @param category a Position Category
   * @throws IllegalArgumentException wrapping a {@link org.eclipse.jface.text.BadPositionCategoryException}
   * @see IDocument#addPositionCategory(java.lang.String)
   * @deprecated Use {@link BufferedJET2Writer#getAdapter(Class)} to return an IDocument, and then
   * use {@link IDocument#addPositionCategory(String)}.
   */
  public abstract void addPositionCategory(String category);

  /**
   * Convenience method wrapping getDocument().addPosition(String, Position).
   * Any
   * {@link org.eclipse.jface.text.BadPositionCategoryException} or
   * {@link org.eclipse.jface.text.BadLocationException} is wrapped in a
   * a runtime exception.
   * @param category a position category
   * @param position a position
   * @throws WriterPositionException wrapping a {@link org.eclipse.jface.text.BadPositionCategoryException}
   * or {@link org.eclipse.jface.text.BadLocationException}
   * @see IDocument#addPosition(java.lang.String, org.eclipse.jface.text.Position)
   * @deprecated Use {@link BufferedJET2Writer#getAdapter(Class)} to return an IDocument, and then
   * use {@link IDocument#addPosition(String, Position)}.
   */
  public abstract void addPosition(String category, Position position);

  /**
   * Convenience method wrapping getDocument().getPositions(String).
   * Any
   * {@link org.eclipse.jface.text.BadPositionCategoryException}
   * is wrapped in a runtime exception.
   * @param category a position category
   * @return an array of positions
   * @throws WriterPositionException wrapping a {@link org.eclipse.jface.text.BadPositionCategoryException}
   * @see IDocument#getPositions(java.lang.String)
   * @deprecated Use {@link BufferedJET2Writer#getAdapter(Class)} to return an IDocument, and then
   * use {@link IDocument#getPositions(String)}.
   */
  public abstract Position[] getPositions(String category);

  /**
   * Convenience method wrapping getDocument().replace(int,int,String).
   * Any
   * {@link org.eclipse.jface.text.BadLocationException}
   * is wrapped in a runtime exception.
   * @param offset the offset of the text to replace
   * @param length the length of the text to replace
   * @param text the replacement text
   * @throws WriterPositionException wrapping a {@link org.eclipse.jface.text.BadLocationException}
   * @see IDocument#replace(int, int, java.lang.String)
   * @deprecated Use {@link BufferedJET2Writer#replaceContent(int, int, String)}.
   */
  public abstract void replace(int offset, int length, String text);
}
