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
 * $Id: ContextLogEntryAdapterFactory.java,v 1.1 2007/12/21 20:29:19 pelder Exp $
 */
package org.eclipse.jet.internal.core.equinox;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jet.ContextLogEntry;

/**
 * Adapter factory that creates a {@link ContextLogEntry} for a CoreException
 */
public class ContextLogEntryAdapterFactory implements IAdapterFactory
{

  private static final Class[] adapterList = new Class[] {
    ContextLogEntry.class,
  };
  
  /* (non-Javadoc)
   * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
   */
  public Object getAdapter(Object adaptableObject, Class adapterType)
  {
    if(!(adaptableObject instanceof CoreException)
        || !ContextLogEntry.class.equals(adapterType)) {
      throw new IllegalArgumentException();
    }
    final CoreException ex = (CoreException)adaptableObject;
    return buildLogEntry(ex.getStatus());
  }

  /**
   * @param status
   * @return
   */
  private ContextLogEntry buildLogEntry(IStatus status)
  {
    final ContextLogEntry.Builder builder;
    if(status.isMultiStatus()) {
      final IStatus[] childStatus = status.getChildren();
      ContextLogEntry[] childEntries = new ContextLogEntry[childStatus.length];
      for (int i = 0; i < childStatus.length; i++)
      {
        childEntries[i] = buildLogEntry(childStatus[i]);
      }
      builder = new ContextLogEntry.Builder(childEntries);
    } else {
      builder = new ContextLogEntry.Builder(ContextLogEntry.ERROR);
    }
    final String message = status.getMessage();
    final Throwable exception = status.getException();
    if(exception != null) {
      builder.exception(exception);
    }
    if(message != null && message.length() >= 0) {
      builder.message(message);
    }
    else if(exception != null ) {
        builder.message(exception.toString());
    }
    return builder.build();
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
   */
  public Class[] getAdapterList()
  {
    return adapterList;
  }

}
