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
import java.net.URISyntaxException;

/**
 * Abstract implementatino of {@link ITemplateResolver}. Extenders must only implement
 * the following methods.
 *
 */
public abstract class AbstractTemplateResolver implements ITemplateResolver {

	private static final String SLASH = "/"; //$NON-NLS-1$

	private final URI[] baseLocations;

	/**
	 * Create a template resolver taking templates from one or more base locations
	 * @param baseLocations an array of template URIs.
	 */
	protected AbstractTemplateResolver(URI[] baseLocations) {
		if(baseLocations == null) {
			throw new NullPointerException();
		}
		// make a defensive copy of base locations.
		this.baseLocations = new URI[baseLocations.length];
		for (int i = 0; i < baseLocations.length; i++) {
			if(baseLocations[i] == null) {
				throw new NullPointerException();
			}
			// make sure all URI's end in a SLASH
			this.baseLocations[i] = baseLocations[i].toString().endsWith(SLASH)
			 	? baseLocations[i]
			 	: baseLocations[i].resolve(baseLocations[i].getPath() + "/"); //$NON-NLS-1$
		}
	}

	/**
	 * Create a template resolver taking templates from a single base location.
	 * @param baseLocation a base location URI
	 */
	protected AbstractTemplateResolver(URI baseLocation) {
		this(new URI[] {baseLocation});
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.ITemplateResolver#getBaseLocations()
	 */
	public final URI[] getBaseLocations() {
		final URI[] tempArray = new URI[baseLocations.length];
		// return a defensive copy
		System.arraycopy(baseLocations, 0, tempArray, 0, baseLocations.length);
		
		return tempArray;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.ITemplateResolver#getIncludedInput(java.lang.String, org.eclipse.jet.tools.parser.ITemplateInput[])
	 */
	public final ITemplateInput getIncludedInput(String includePath,
			ITemplateInput[] activeInputs) throws RecursiveIncludeException {
		if(includePath == null || activeInputs == null) {
			throw new NullPointerException();
		}
		if(activeInputs.length == 0) {
			throw new IllegalArgumentException();
		}
		
		String templatePath = resolveToTemplatePath(includePath, activeInputs[activeInputs.length - 1].getTemplatePath());
		
		int baseLocationsStartIndex = 0;
		
		boolean recursiveInclude = templatePath.equals(activeInputs[activeInputs.length - 1].getTemplatePath());
		if(recursiveInclude) {
			final URI currentBaseURI = activeInputs[activeInputs.length - 1].getBaseLocation();
			
			baseLocationsStartIndex = 1 + findBaseLocationIndex(currentBaseURI);
		}
		
		for (int i = baseLocationsStartIndex; i < baseLocations.length; i++) {
			// check for recursive input
			for (int j = 0; j < activeInputs.length; j++) {
				if(templatePath.equals(activeInputs[j].getTemplatePath())
						&& baseLocations[i].equals(activeInputs[j].getBaseLocation())) {
					throw new RecursiveIncludeException(templatePath, baseLocations[i], activeInputs);
				}
			}
			if(inputExists(baseLocations[i], templatePath)) {
				return createTemplateInput(baseLocations[i], templatePath);
			}
		}
		return null;
	}

	/**
	 * Resolve an include path, which is relative to the current file
	 * into a template path, which is relative to the templates base locations
	 * @param includePath the include path
	 * @param templatePath the template path of the current file
	 * @return the templatePath of the include path.
	 * @throws IllegalArgumentException if templatePath cannot be converted to a URI
	 */
	private String resolveToTemplatePath(String includePath, String templatePath) {
		try {
			URI currentURI = new URI(null, templatePath, null);
			URI resolvedURI = currentURI.resolve(new URI(null, includePath, null));
			return resolvedURI.getSchemeSpecificPart();
		} catch (URISyntaxException e) {
			IllegalArgumentException wrapper = new IllegalArgumentException();
			wrapper.initCause(e);
			throw wrapper;
		}
	}

	/**
	 * Return the index of the base passed base location in {@link #baseLocations}.
	 * @param currentBaseURL the current base location
	 * @return the zero-based index.
	 * @throws IllegalArgumentException if currentBaseURL cannot be found
	 */
	private int findBaseLocationIndex(URI currentBaseURL) {
		for (int i = 0; i < baseLocations.length; i++) {
			if(currentBaseURL.equals(baseLocations[i])) {
				return i;
			}
		}
		throw new IllegalArgumentException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.parser.ITemplateResolver#getInput(java.lang.String)
	 */
	public final ITemplateInput getInput(String templatePath) {
		for (int i = 0; i < baseLocations.length; i++) {
			if(inputExists(baseLocations[i], templatePath)) {
				return createTemplateInput(baseLocations[i], templatePath);
			}
		}
		return null;
	}

	/**
	 * Test whether the template input exists. The default implementation opens in input
	 * stream to confirme the existance
	 * @param baseLocation the baseLocation
	 * @param templatePath the template path
	 * @return <code>true</code> if {@link #createTemplateInput(URI, String)}} will succeed, <code>false</code> otherwise.
	 */
	protected abstract boolean inputExists(URI baseLocation, String templatePath);
	
	/**
	 * Create a Template Input from the give base location and templatePath
	 * @param baseLocation a base loction
	 * @param templatePath a templatePath
	 * @return the template input.
	 */
	protected abstract ITemplateInput createTemplateInput(URI baseLocation, String templatePath);

}
