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
 * Describes a recursive inclusion.
 * 
 * @see ITemplateResolver#getIncludedInput(String, ITemplateInput[])
 *
 */
public final class RecursiveIncludeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3796819381247376434L;
	
	private final String templatePath;
	private final URI baseLocation;
	private final String[] activeTemplatePaths;
	private final URI[] activeBaseLocations;

	public RecursiveIncludeException(String templatePath, URI baseLocation, ITemplateInput[] activeInputs) {
		this.templatePath = templatePath;
		this.baseLocation = baseLocation;
		this.activeTemplatePaths = new String[activeInputs.length];
		this.activeBaseLocations = new URI[activeInputs.length];
		for (int i = 0; i < activeInputs.length; i++) {
			activeTemplatePaths[i] = activeInputs[i].getTemplatePath();
			activeBaseLocations[i] = activeInputs[i].getBaseLocation();
		}
	}

	/**
	 * Return the base locations of the active templates.
	 * Base locations are returned in order of inclusion. 
	 * That is, the base location of the most recently included
	 * template is the last element in the array.
	 * @return an array of base locations
	 * @see #getActiveTemplatePaths()
	 */
	public URI[] getActiveBaseLocations() {
		return activeBaseLocations;
	}

	/**
	 * Return the template paths of the active templates.
	 * Template paths are returned in the order of inclusion.
	 * That is, the template path of the most recently included
	 * template is the last element in the array.
	 * @return an array of template paths
	 */
	public String[] getActiveTemplatePaths() {
		return activeTemplatePaths;
	}

	/**
	 * Return the base location at which the recursion was detected.
	 * @return the base location.
	 */
	public URI getBaseLocation() {
		return baseLocation;
	}

	/**
	 * Return the template path that would have caused the recursion.
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}

}
