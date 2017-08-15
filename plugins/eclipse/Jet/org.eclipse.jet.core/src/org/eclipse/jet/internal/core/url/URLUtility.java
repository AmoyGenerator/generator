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
package org.eclipse.jet.internal.core.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * A utility class for handling URLs, and in particular, for handling ensuring that URL formats are correct
 * and that URLs are properly encoded.
 * <p>
 * <string>Important Note</strong>: Code in org.eclipse.jet should avoid using the following methods, as they
 * are known to have invalid or unexpected results.
 * <table>
 * <tr><td>Class/Method</td><td>Problem</td></tr>
 * <tr><td>{@link URL#URL(String, String, String)}</td><td>Arguments are not URL encoded</td></tr>
 * <tr><td>{@link URL#URL(String, String, int, String)}</td><td>Arguments are not URL encoded</td></tr>
 * <tr><td>{@link URL#URL(String, String, int, String, java.net.URLStreamHandler)}</td><td>Arguments are not URL encoded</td></tr>
 * <tr><td>{@link java.net.URI#toURL()}</td><td>Properly encoded URI gets encoded again, platform:/ URI don't propertly converted</td></tr>
 * </table>
 * </p>
 *
 */
public class URLUtility {

	private URLUtility() {
		
	}
	
	/**
	 * Return an URL for an entry within the given JAR file
	 * @param jarFile an URL for the JAR file
	 * @param unEncodedPath the unencoded path for the entry within the JAR
	 * @return the JAR entry URL
	 * @throws NullPointerException if either argument is <code>null</code>
	 */
	public static URL jarEntryURL(URL jarFile, String unEncodedPath) {
		if(jarFile == null || unEncodedPath == null) {
			throw new NullPointerException();
		}
		try {
			return new URL(jarRootEntryURL(jarFile), unEncodedPath);
		} catch (MalformedURLException e) {
			// This should not happen. This exception only happens if no protocol specified
			// or if the protocal is unknown. But neither of this is the case.
			// Wrap in a runtime exception.
			throw new RuntimeException("Unexpected exception", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * Return an URL for the root entry. The calculated URL may then be used as a base URL
	 * for resolving entries within the JAR.
	 * @param jarFile the URL for a JAR file
	 * @return the URL for the jar root entry.
	 * @throws NullPointerException if either argument is <code>null</code>.
	 */
	public static URL jarRootEntryURL(URL jarFile) {
		if(jarFile == null) {
			throw new NullPointerException();
		}
		try {
			// N.B. This is a legitimate use of URL(String,String,String)
			// as URL.toExternalForm() returns a corrected encoded URL
			// provided the original URL was properly constructed.
			return new URL("jar", null, jarFile.toExternalForm() + "!/");
		} catch (MalformedURLException e) {
			// This should not happen, wrap in a runtime exception
			throw new RuntimeException("Unexpected exception", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * Return the corresponding URL for the given URI. Use as an alternative for {@link URI#toURL()}.
	 * @param uri the URI to convert
	 * @return the converted URI
	 * @throws MalformedURLException if the URI includes an unknown protocol
	 * @throws NullPointerException if uri is <code>null</code>
	 */
	public static URL toURL(URI uri) throws MalformedURLException {
		if(uri == null) {
			throw new NullPointerException();
		}
		// Don't use URI.toURL(), platform:/ URI's don't properly transform into URLs
		// Instead, we use:
		return new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath());
	}
	
	public static URL relativeURL(URL baseLocation, String location) throws MalformedURLException {
		if(baseLocation == null || location == null) {
			throw new NullPointerException();
		}
		
		if("platform".equals(baseLocation.getProtocol())) {
			final String basePath = baseLocation.getPath();
			if(location.startsWith("/") && !basePath.endsWith("/")) {
				String firstSegment = basePath.substring(0, basePath.indexOf('/'));
				URL adjustedBase = new URL(baseLocation.getProtocol(), null, firstSegment);
				return new URL(adjustedBase, location);
			}
		}
		
		return new URL(baseLocation, location);
	}
}
