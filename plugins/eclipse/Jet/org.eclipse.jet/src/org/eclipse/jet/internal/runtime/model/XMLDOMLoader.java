/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jet.runtime.model.IModelLoader;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Implement a JET Loader that loads XML documents as XML DOM, and resolves any XML Entities using the
 * Web Tools projects URI Resolver capabilities and UI.
 *
 */
public class XMLDOMLoader implements IModelLoader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#canLoad(java.lang.String)
	 */
	public boolean canLoad(String kind) {
		// will attempt to load anything...
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL)
	 */
	public Object load(URL modelUrl) throws IOException {
		InputStream inputStream = modelUrl.openStream();
		InputSource inputSource = new InputSource(inputStream);

		Document document = parse(inputSource, modelUrl.toExternalForm());
		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL,
	 *      java.lang.String)
	 */
	public Object load(URL modelUrl, String kind) throws IOException {
		// ignore the kind ...
		return load(modelUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#loadFromString(java.lang.String,
	 *      java.lang.String)
	 */
	public Object loadFromString(String serializedModel, String kind)
			throws IOException {
		InputSource inputSource = new InputSource(new StringReader(
				serializedModel));
		return parse(inputSource, null);
	}

	/**
	 * @param inputSource
	 * @param baseLocation TODO
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document parse(InputSource inputSource, final String baseLocation) throws IOException {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			builderFactory.setNamespaceAware(true);

			DocumentBuilder builder = builderFactory.newDocumentBuilder();

			// install an entity resolver to handle resolution of external DOCTYPE, and ENTITY references
			EntityResolver entityResolver = EntityResolverFactory.getEntityResolver(baseLocation);
			if(entityResolver != null) {
				builder.setEntityResolver(entityResolver);
			}

			Document document = builder.parse(inputSource);
			return document;
		} catch (ParserConfigurationException e) {
			IOException ioex = new IOException();
			ioex.initCause(e);
			throw ioex;
		} catch (SAXException e) {
			IOException ioex = new IOException();
			ioex.initCause(e);
			throw ioex;
		}
	}


}
