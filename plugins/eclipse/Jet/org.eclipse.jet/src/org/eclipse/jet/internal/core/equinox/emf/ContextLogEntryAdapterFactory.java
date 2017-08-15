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
package org.eclipse.jet.internal.core.equinox.emf;

import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticException;
import org.eclipse.jet.ContextLogEntry;

/**
 * @author pelder
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
    if(!(adaptableObject instanceof DiagnosticException)
        || !ContextLogEntry.class.equals(adapterType)) {
      throw new IllegalArgumentException();
    }
    final DiagnosticException ex = (DiagnosticException)adaptableObject;
    return buildLogEntry(ex.getDiagnostic());
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
   */
  public Class[] getAdapterList()
  {
    return adapterList;
  }

  /**
   * @param diagnostic
   * @return
   */
  private ContextLogEntry buildLogEntry(Diagnostic diagnostic)
  {
    final ContextLogEntry.Builder builder;
    if(diagnostic.getChildren().size() > 0) {
      final List childDiagnostic = diagnostic.getChildren();
      ContextLogEntry[] childEntries = new ContextLogEntry[childDiagnostic.size()];
      for (int i = 0; i < childDiagnostic.size(); i++)
      {
        childEntries[i] = buildLogEntry((Diagnostic)childDiagnostic.get(i));
      }
      builder = new ContextLogEntry.Builder(childEntries);
    } else {
      builder = new ContextLogEntry.Builder(ContextLogEntry.ERROR);
    }
    final String message = diagnostic.getMessage();
    final Throwable exception = diagnostic.getException();
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


}
