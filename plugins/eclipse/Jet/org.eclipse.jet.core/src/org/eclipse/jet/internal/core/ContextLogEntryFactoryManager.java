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
package org.eclipse.jet.internal.core;

/**
 * Manager for the {@link IContextLogEntryFactory}. This class allows the replacement of the
 * default context log entry factory with a factory more tighly adapted to the execution enviornment.
 * <p>
 * This manager is type safe.
 * </p>
 *
 */
public class ContextLogEntryFactoryManager {

	private static IContextLogEntryFactory factory = new DefaultContextLogEntryFactory();
	
	/**
	 * Prevent instantiation 
	 */
	private ContextLogEntryFactoryManager() {
		// do nothing
	}

	/**
	 * Return the context log entry factory
	 * @return on instance of {@link IContextLogEntryFactory}. Will not be <code>null</code>.
	 */
	public static synchronized IContextLogEntryFactory getFactory() {
		return factory;
	}

	/**
	 * Set the context log entry factory
	 * @param factory an implementation of {@link IContextLogEntryFactory}. Cannot be <code>null</code>.
	 * @throws NullPointerException if <code>factory</code> is <code>null</code>
	 */
	public static synchronized void setFactory(IContextLogEntryFactory factory) {
		if(factory == null) {
			throw new NullPointerException();
		}
		ContextLogEntryFactoryManager.factory = factory;
	}
	
	
	
}
