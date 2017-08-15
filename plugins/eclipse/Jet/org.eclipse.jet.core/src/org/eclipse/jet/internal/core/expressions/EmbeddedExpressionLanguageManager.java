/*
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet.internal.core.expressions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A manager of embedded language implementations available to the JET parser
 *
 */
public class EmbeddedExpressionLanguageManager {
	
	private static EmbeddedExpressionLanguageManager instance;
	private final Map languageRegistry = Collections.synchronizedMap(new HashMap());
	
	private EmbeddedExpressionLanguageManager() {
		
	}
	
	/**
	 * Define an embedded language
	 * @param language the language identifier
	 * @param embeddedLanguage the implementation of the language
	 * @throws NullPointerException if either argument is <code>null</code>
	 */
	public void addLanguage(String language, IEmbeddedLanguage embeddedLanguage) {
		if(language == null || embeddedLanguage == null) {
			throw new NullPointerException();
		}
		languageRegistry.put(language, embeddedLanguage);
	}
	
	/**
	 * return a map of the registered embedded languages. The map key is the language's String identifier, while
	 * the map value is the language implementation, an {@link IEmbeddedLanguage}.
	 * @return a map of registered embedded languages.
	 */
	public Map getLanguages() {
		return new HashMap(languageRegistry);
	}

	/**
	 * Return the default instance of the embedded language manager.
	 * @return an {@link EmbeddedExpressionLanguageManager} instance
	 */
	public static synchronized EmbeddedExpressionLanguageManager getInstance() {
		if(instance == null) {
			instance = new EmbeddedExpressionLanguageManager();
		}
		return instance;
	}

	/**
	 * Return the embedded language implementation corresponding to the passed language identifier.
	 * @param language a embedded langauge's String identifier.
	 * @return the corresponding embedded language implementation or <code>null</code>
	 */
	public IEmbeddedLanguage getLanguage(String language) {
		return (IEmbeddedLanguage) languageRegistry.get(language);
	}

}
