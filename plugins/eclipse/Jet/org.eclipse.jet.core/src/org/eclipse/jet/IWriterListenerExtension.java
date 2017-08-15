/*
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet;

import org.eclipse.jet.taglib.JET2TagException;

/**
 * Extension interface to {@link IWriterListener} allowing finalizeContent
 * handlers to receive existing file contents
 * @since 0.9
 */
public interface IWriterListenerExtension {

	/**
	 * Perform any finalization of the content in the writer. If the file represented by 
	 * fileObject does not exist, then {@link IWriterListener#finalizeContent(JET2Writer, Object)}
	 * is called.
	 * 
	 * @param writer
	 *            the writer in the process of being finalized
	 * @param file
	 *            a handle to object to which the content will ultimately be
	 *            written. The standard JET2 Workspace tags pass an
	 *            org.eclipse.core.resources.IFile, but other tags may pass
	 *            objects of other types.
	 * @param existingContent the existing file content
	 * @throws JET2TagException
	 *             if the method cannot complete normally
	 */
	void finalizeContent(JET2Writer writer, Object fileObject,
			String existingContent) throws JET2TagException;

}
