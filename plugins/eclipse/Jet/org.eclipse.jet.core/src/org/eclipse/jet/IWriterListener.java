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
 * $Id: IWriterListener.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet;

import org.eclipse.jet.taglib.JET2TagException;


/**
 * Callback interface allowing participation in the finalization of a {@link JET2Writer}'s content.
 *
 */
public interface IWriterListener
{

  /**
   * Perform any finalization of the content in the writer.
   * @param writer the writer in the process of being finalized
   * @param file a handle to object to which the content will ultimately be written. The standard
   * JET2 Workspace tags pass an org.eclipse.core.resources.IFile, but other tags may pass objects
   * of other types.
   * @throws JET2TagException if the method cannot complete normally
   */
  public abstract void finalizeContent(JET2Writer writer, Object file) throws JET2TagException;

  /**
   * Perform any post processing on the committed file based on content written.
   * @param writer the writer that provided the committed content.
   * @param file a handle to the object containing the comitted content. The standard
   * JET2 Workspace tags pass an org.eclipse.core.resources.IFile, but other tags may pass objects
   * of other types.
   * @throws JET2TagException if method cannot complete normally.
   */
  public abstract void postCommitContent(JET2Writer writer, Object file) throws JET2TagException;

}
