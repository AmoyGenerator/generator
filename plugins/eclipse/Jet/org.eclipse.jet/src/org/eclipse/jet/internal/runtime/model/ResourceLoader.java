/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
 * $Id$
 */

package org.eclipse.jet.internal.runtime.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.runtime.model.IModelLoader;

/**
 * Model loader that provides IResource information for the loaded URL.
 *
 */
public class ResourceLoader implements IModelLoader {

	/* (non-Javadoc)
	 * @see org.eclipse.jet.runtime.model.IModelLoader#canLoad(java.lang.String)
	 */
	public boolean canLoad(String kind) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL)
	 */
	public Object load(URL modelUrl) throws IOException {
		if(!"platform".equals(modelUrl.getProtocol()) || !modelUrl.getPath().startsWith("/resource/")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new IOException("Resource loader requires platform:/resource/ URL format"); //$NON-NLS-1$
		}
		IPath path = new Path(modelUrl.getPath());
		path = path.removeFirstSegments(1);
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		if(resource == null) {
			throw new FileNotFoundException(path.toString());
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL, java.lang.String)
	 */
	public Object load(URL modelUrl, String kind) throws IOException {
		return load(modelUrl);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.runtime.model.IModelLoader#loadFromString(java.lang.String, java.lang.String)
	 */
	public Object loadFromString(String serializedModel, String kind)
			throws IOException {
		throw new UnsupportedOperationException();
	}

}
