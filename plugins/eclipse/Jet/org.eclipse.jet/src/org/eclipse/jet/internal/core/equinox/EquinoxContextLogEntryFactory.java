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
package org.eclipse.jet.internal.core.equinox;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.jet.ContextLogEntry;
import org.eclipse.jet.internal.core.DefaultContextLogEntryFactory;
import org.eclipse.jet.internal.core.IContextLogEntryFactory;

/**
 * An implementatino of {@link IContextLogEntryFactory} that is Equinox (OSGi) aware. In particular, it
 * makes use of the Equinox IAdaptable interfaces.
 *
 */
public class EquinoxContextLogEntryFactory implements IContextLogEntryFactory {

	private final IAdapterManager adapterManager;
	private IContextLogEntryFactory defaultContextLogEntryFactory;
	
	public EquinoxContextLogEntryFactory(IAdapterManager adapterManager) {
		this.adapterManager = adapterManager;
		this.defaultContextLogEntryFactory = new DefaultContextLogEntryFactory();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jet.internal.core.IContextLogEntryFactory#create(java.lang.Throwable)
	 */
	public ContextLogEntry create(Throwable t) {
		// TODO Auto-generated method stub
		final Object adapter = adapterManager.getAdapter(t, ContextLogEntry.class);
		
		return adapter != null ? (ContextLogEntry)adapter : defaultContextLogEntryFactory.create(t);
	}

}
