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
package org.eclipse.jet.internal.core.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.TemplateInputException;
import org.eclipse.jet.internal.core.url.URLUtility;

/**
 * Standard inplementation of ITemplateInput
 *
 */
public class DefaultTemplateInput implements ITemplateInput {

	private final URI baseLocation;
	private final String templatePath;
	private final String encoding;
	
	
	public DefaultTemplateInput(URI baseLocation, String templatePath, String encoding) {
		this.baseLocation = baseLocation;
		this.templatePath = templatePath;
		this.encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.compiler.ITemplateInput#getBaseURL()
	 */
	public URI getBaseLocation() {
		return baseLocation;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.tools.compiler.ITemplateInput#getTemplatePath()
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	public Reader getReader() throws TemplateInputException {
		try {
			URI templateURI = new URI(null, templatePath, null);
			URI resolvedURI = baseLocation.resolve(templateURI);
			URL url = URLUtility.toURL(resolvedURI);
			InputStream urlStream = url.openStream();
			return new BufferedReader(new InputStreamReader(urlStream,
					getEncoding()));
		} catch (IOException e) {
			throw new TemplateInputException(e);
		} catch (URISyntaxException e) {
			IllegalArgumentException wrapper = new IllegalArgumentException();
			wrapper.initCause(e);
			throw wrapper;
		}
	}
	

	public String getEncoding() throws TemplateInputException {
		return encoding;
	}
}
