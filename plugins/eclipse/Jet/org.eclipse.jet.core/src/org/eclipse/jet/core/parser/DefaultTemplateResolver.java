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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jet.internal.core.parser.DefaultTemplateResolverHelper;

/**
 * Default implementation of {@link ITemplateResolver} that is independent of
 * the Eclipse workspace. By specifying a {@link ITemplateResolverHelperFactory},
 * template resolution helpers {@link ITemplateResolverHelper} instances may be
 * created that leverage knowledge in the eclipse workspace.
 * 
 */
public final class DefaultTemplateResolver extends AbstractTemplateResolver
		implements ITemplateResolver {

	private static final String SLASH = "/"; //$NON-NLS-1$

	private static final ITemplateResolverHelperFactory defaultFactory = new ITemplateResolverHelperFactory() {

		public ITemplateResolverHelper getTemplateResolverHelper(
				URI baseLocation) {
			return new DefaultTemplateResolverHelper(baseLocation);
		}

	};

	/**
	 * Builder for DefaultTemplateResolver
	 * 
	 */
	public static final class Builder {

		private final URI[] baseLocations;

		private ITemplateResolverHelperFactory helper;

		/**
		 * Create an input manager drawing templates from the listed based
		 * locations URLs.
		 * 
		 * @param baseLocations
		 *            an array of base template locations, in the desired search
		 *            order
		 * @throws NullPointerException
		 *             if <code>baseLocations</code> or any of its elements is
		 *             <code>null</code>.
		 * @throws IllegalArgumentException
		 *             if any baseLocation is not a directory URL (ends in a
		 *             "/").
		 */
		public Builder(URI[] baseLocations) {
			if (baseLocations == null) {
				throw new NullPointerException();
			}
			for (int i = 0; i < baseLocations.length; i++) {
				if (baseLocations[i] == null) {
					throw new NullPointerException();
				}
				if (!baseLocations[i].getPath().endsWith(SLASH)) {
					throw new IllegalArgumentException();
				}
			}

			this.baseLocations = baseLocations;
		}

		/**
		 * Convenience constructor when templates are drawn from one location
		 * only
		 * 
		 * @param baseLocation
		 *            the base location of templates.
		 * @throws NullPointerException
		 *             if <code>baseLocation</code> is <code>null</code>.
		 */
		public Builder(URI baseLocation) {
			this(new URI[] { baseLocation });
		}

		/**
		 * Create the input manager instance described by the builder
		 * 
		 * @return the {@link DefaultTemplateResolver} instance.
		 */

		public Builder templateResolverHelperFactory(
				ITemplateResolverHelperFactory helper) {
			this.helper = helper;
			return this;
		}

		/**
		 * Builder the template resolver
		 * 
		 * @return the {@link DefaultTemplateResolver} instance
		 */
		public ITemplateResolver build() {
			return new DefaultTemplateResolver(this);
		}

	}

	private final Map delegateByURI = new HashMap();

	private DefaultTemplateResolver(Builder builder) {
		super(builder.baseLocations);
		final URI[] uris = getBaseLocations();
		for (int i = 0; i < uris.length; i++) {
			final URI baseLocation = uris[i];
			ITemplateResolverHelper helper = getTemplateResolverHelper(
					builder.helper, baseLocation);
			delegateByURI.put(baseLocation, helper);
		}
	}

	private ITemplateResolverHelper getTemplateResolverHelper(
			ITemplateResolverHelperFactory helperFactory, URI baseLocation) {
		ITemplateResolverHelper helper = helperFactory == null ? null
				: helperFactory.getTemplateResolverHelper(baseLocation);

		if (helper == null) {
			helper = defaultFactory.getTemplateResolverHelper(baseLocation);
		}
		return helper;
	}

	/**
	 * Create a Template Input from the give base location and templatePath
	 * 
	 * @param baseLocation
	 *            a base loction
	 * @param templatePath
	 *            a templatePath
	 * @return the template input.
	 */
	protected ITemplateInput createTemplateInput(URI baseLocation,
			String templatePath) {
		ITemplateResolverHelper helper = (ITemplateResolverHelper) delegateByURI
				.get(baseLocation);
		return helper.createTemplateInput(templatePath);
	}

	/**
	 * Test whether the template input exists. The default implementation opens
	 * in input stream to confirme the existance
	 * 
	 * @param baseLocation
	 *            the baseLocation
	 * @param templatePath
	 *            the template path
	 * @return <code>true</code> if {@link #createTemplateInput(URI, String)}}
	 *         will succeed, <code>false</code> otherwise.
	 */
	protected boolean inputExists(URI baseLocation, String templatePath) {
		ITemplateResolverHelper helper = (ITemplateResolverHelper) delegateByURI
				.get(baseLocation);
		return helper.inputExists(templatePath);
	}

}
