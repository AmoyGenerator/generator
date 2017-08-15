/*
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.core.parser;

import org.eclipse.jet.taglib.TagLibrary;

/**
 * Protocol for resolving Tag library ids into instances of {@link TagLibrary}
 *
 */
public interface ITagLibraryResolver {

	/**
	 * Return a TagLibrary instance given a tag library ID
	 * @param tagLibraryID a tag library id
	 * @return a {@link TagLibrary} instance or <code>null</code> if the library ID is not known to the resolver
	 */
	public abstract TagLibrary getLibrary(String tagLibraryID);
}
