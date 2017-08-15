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
package org.eclipse.jet.internal.extensionpoints;

import org.eclipse.core.resources.IProject;
import org.eclipse.jet.internal.runtime.BundleManifest;

/**
 * Event listener for {@link PluginProjectMonitor}.
 */
public interface IPluginChangeListener
{

  void projectUpdated(IProject project, BundleManifest manifest, Object pluginDocumentRoot);

  void projectRemoved(IProject project);

}
