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
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.JET2TagException;


/**
 * Listener to events on {@link TransformContextExtender}.
 *
 */
public interface TransformContextListener
{

  /**
   * 
   * @param context
   * @param monitor
   * @throws JET2TagException if the commit fails
   */
  public abstract void commit(JET2Context context, IProgressMonitor monitor) throws JET2TagException;

}
