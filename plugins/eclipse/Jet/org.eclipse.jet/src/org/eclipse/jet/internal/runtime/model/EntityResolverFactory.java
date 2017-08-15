/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.runtime.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Factory for constructing EntityResolver instances for XML and EMF modeling
 * loading
 * <p>
 * NOTE: This code depends on internals of the WebTools project. There are no guarantees that future versions
 * of WebTools project will support his. This dependency is implemented using reflection
 * so that future breakage of dependencies will permit the code to continue working, albeit
 * with reduced functionality.
 * </p>
 * 
 */
public final class EntityResolverFactory {
	private static final class URIResolverPluginClassProxy {

		private static final String WST_URIRESOLVER_PLUGIN_CLASS = "org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin"; //$NON-NLS-1$
		private static final String WST_URIRESOLVER_PLUGIN_ID = "org.eclipse.wst.common.uriresolver"; //$NON-NLS-1$
		private final Class pluginClass;
		private final Method createResolverMethod;

		public URIResolverPluginClassProxy() throws BundleException,
				ClassNotFoundException, SecurityException,
				NoSuchMethodException {
			Bundle bundle = Platform.getBundle(WST_URIRESOLVER_PLUGIN_ID);
			if (bundle == null) {
				throw new BundleException(NLS.bind(Messages.EntityResolverFactory_BundleNotResolved, WST_URIRESOLVER_PLUGIN_ID));
			}

			pluginClass = bundle.loadClass(WST_URIRESOLVER_PLUGIN_CLASS);
			createResolverMethod = pluginClass.getMethod("createResolver", //$NON-NLS-1$
					new Class[0]);
		}

		public URIResolverProxy createResolver() throws SecurityException,
				NoSuchMethodException, IllegalArgumentException,
				IllegalAccessException, InvocationTargetException {
			Object result = createResolverMethod.invoke(null, new Object[0]);

			return new URIResolverProxy(result);
		}
	}

	private static final class URIResolverProxy {

		private final Object uriResolver;
		private final Method resolveMethod;
		private final Method resolvePhysicalLocationMethod;

		public URIResolverProxy(Object uriResolver) throws SecurityException,
				NoSuchMethodException {
			this.uriResolver = uriResolver;
			Class resolverClass = uriResolver.getClass();

			resolveMethod = resolverClass.getMethod("resolve", new Class[] { //$NON-NLS-1$
					String.class, String.class, String.class });
			resolvePhysicalLocationMethod = resolverClass.getMethod(
					"resolvePhysicalLocation", new Class[] { String.class, //$NON-NLS-1$
							String.class, String.class });
		}

		private String invokeResolveMethod(Method method, String baseLocation,
				String publicId, String systemId) {
			try {
				Object result = method.invoke(uriResolver, new Object[] {
						baseLocation, publicId, systemId });
				return (String) result;
			} catch (IllegalArgumentException e) {
				// ignore;
			} catch (IllegalAccessException e) {
				// ignore;
			} catch (InvocationTargetException e) {
				// ignore;
			}
			return null;
		}

		public String resolve(String baseLocation, String publicId,
				String systemId) {
			return invokeResolveMethod(resolveMethod, baseLocation, publicId,
					systemId);
		}

		public String resolvePhysicalLocation(String baseLocation,
				String publicId, String systemId) {
			return invokeResolveMethod(resolvePhysicalLocationMethod,
					baseLocation, publicId, systemId);
		}
	}

	private static final class WSTEntityResolver extends DefaultEntityResolver implements EntityResolver {

		private final String baseLocation;
		private final URIResolverProxy uriResolver;

		public WSTEntityResolver(String baseLocation,
				URIResolverProxy uriResolver) {
			this.baseLocation = baseLocation;
			this.uriResolver = uriResolver;
		}

		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {

			final String resolvedURI = uriResolver.resolve(baseLocation,
					publicId, systemId);
			return super.resolveEntity(publicId, resolvedURI);
		}

	}

	private static class DefaultEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {

			final URL systemURL = new URL(systemId);
			InputStream stream;
			try {
				stream = systemURL.openStream();
			} catch (FileNotFoundException e) {
				// the default FNF exception is doesn't explain much, use a better one.
				throw new FileNotFoundException(
						NLS.bind(
							Messages.EntityResolverFactory_FileNotFound,
							publicId, systemId));
			}
			return new InputSource(stream);
		}

	}

	private static boolean initialized = false;
	private static URIResolverProxy uriResolver;

	public static EntityResolver getEntityResolver(String baseLocation) {
		initURIResolver();

		return uriResolver != null ? new WSTEntityResolver(baseLocation,
				uriResolver) : new DefaultEntityResolver();
	}

	private static void initURIResolver() {
		if (!initialized) {
			initialized = true;
			try {
				uriResolver = new URIResolverPluginClassProxy()
						.createResolver();
			} catch (SecurityException e) {
				InternalJET2Platform.logError("Error initializing access to WST URIResolver", e); //$NON-NLS-1$
			} catch (IllegalArgumentException e) {
                InternalJET2Platform.logError("Error initializing access to WST URIResolver", e); //$NON-NLS-1$
			} catch (NoSuchMethodException e) {
                InternalJET2Platform.logError("Error initializing access to WST URIResolver", e); //$NON-NLS-1$
			} catch (IllegalAccessException e) {
                InternalJET2Platform.logError("Error initializing access to WST URIResolver", e); //$NON-NLS-1$
			} catch (InvocationTargetException e) {
                InternalJET2Platform.logError("Error initializing access to WST URIResolver", e); //$NON-NLS-1$
			} catch (BundleException e) {
				// ignore, this indicates the WST bundle not present.
			} catch (ClassNotFoundException e) {
                InternalJET2Platform.logError("Error initializing access to WST URIResolver", e); //$NON-NLS-1$
			}
		}
	}

}
