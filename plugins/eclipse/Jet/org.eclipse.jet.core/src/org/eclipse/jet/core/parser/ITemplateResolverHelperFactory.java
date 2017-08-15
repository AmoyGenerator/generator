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

import java.net.URI;

/**
 * Protocol for a factory that creates {@link ITemplateResolverHelper} instances.
 * A helper factory may be optionally passed to a {@link DefaultTemplateResolver} instance
 * during creation to allow for resolution template base URIs in ways other than
 * using {@link java.net.URL#openConnection()}. An example is mapping a base URI to the eclipse workspace
 * and using the workspace APIs instead.
 *
 */
public interface ITemplateResolverHelperFactory {

	/**
	 * Return a template resolver helper for the base location.
	 * If no appropriate helper can be returned, return <code>null</code>.
	 * @param baseLocation a template base location
	 * @return a {@link ITemplateResolverHelper} instance or <code>null</code>
	 */
	public abstract ITemplateResolverHelper getTemplateResolverHelper(URI baseLocation);
}
