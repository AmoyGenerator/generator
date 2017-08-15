/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: BufferedJET2Writer.java,v 1.1 2007/04/04 14:53:54 pelder Exp $
 */
package org.eclipse.jet;



/**
 * Protocol defining a buffered writer for JET. A buffered writer does not
 * write directly to an output source. Instead, it writes to an internal buffer
 * that may be later retrieved and modified.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * @since 0.8.0
 */
public interface BufferedJET2Writer extends JET2Writer
{
  /**
   * Return the length of the buffered content.
   * @return the length
   */
  public abstract int getContentLength();
  
  /**
   * Return the buffered content of the writer as a string.
   * @return the content
   */
  public abstract String getContent();
  
  /**
   * Return a ranged of text within the buffered cotnent of the writer.
   * @param offset the offset of the text to return
   * @param length the length of the text to return
   * @return the content
   * @throws IllegalArgumentException if offset or length do not fail within
   * the current contents
   */
  public abstract String getContent(int offset, int length);
  
  /**
   * Replace content in the buffer.
   * @param offset the offset of the text to replace
   * @param length the length of the text to replace
   * @param text the replacement text
   * @throws IllegalArgumentException if offset or length do not fall within
   * the current contents
   */
  public abstract void replaceContent(int offset, int length, String text);

  /**
   * Set the buffer content, removing any previously written content.
   * @param content
   */
  public abstract void setContent(String content);
  
  /**
   * Adapt the writer to the given class. The primary use of this method
   * is to access the underlying buffer implementation.
   * @param adapterClass the class to return
   * @return the adapter or <code>null</code> if adapterClass is not supported.
   */
  public abstract Object getAdapter(Class adapterClass);
}
