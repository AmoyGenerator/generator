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
package org.eclipse.jet.transform;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jet.JET2TemplateLoader;

/**
 * Interface defining a series of actions to perform against a JET bundle. 
 */
public interface IJETRunnable
{

  public abstract void run(IJETBundleDescriptor descriptor, JET2TemplateLoader templateLoader, IProgressMonitor monitor);
}
