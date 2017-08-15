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

import org.eclipse.jet.ContextLogEntry;

/**
 * A default implementation of {@link IContextLogEntryFactory} that is independent of OSGi and Eclipse.
 *
 */
public class DefaultContextLogEntryFactory implements IContextLogEntryFactory {

	public ContextLogEntry create(Throwable t) {
		return new ContextLogEntry.Builder(ContextLogEntry.ERROR).exception(t).build();
	}
}
