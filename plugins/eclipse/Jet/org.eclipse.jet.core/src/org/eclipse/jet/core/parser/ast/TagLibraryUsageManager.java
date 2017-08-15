/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.jet.core.parser.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.internal.taglib.TagLibraryReferenceImpl;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.taglib.TagLibraryReference;

/**
 * A class to manage usage of tag libraries in a JET2 template.
 * 
 */
public class TagLibraryUsageManager {

	private final Map tagLibraries = new HashMap();

	private final Map predefinedLibraryMap;

	private List tags = null;

	private final ITagLibraryResolver tagLibraryResolver;

	/**
	 * @param predefinedLibraryMap
	 *            a map of predefined prefixes and their tag libary ids.
	 * 
	 */
	public TagLibraryUsageManager(Map predefinedLibraryMap,
			ITagLibraryResolver tagLibraryResolver) {
		this.tagLibraryResolver = tagLibraryResolver;
		// make a defensive copy, we may change it...
		this.predefinedLibraryMap = new HashMap(predefinedLibraryMap);
	}

	/**
	 * Test whether a prefix can be created.
	 * <p>
	 * Conditions where this can succeed: <bl>
	 * <li>prefix is not defined in either the predefined library map or the
	 * library map.</li>
	 * </bl>
	 * </p>
	 * 
	 * @param prefix
	 *            a prefix
	 * @param id
	 *            the tag libary id.
	 * @return <code>true</code> if the prefix can be created.
	 */
	public boolean canDefinePrefix(String prefix, String id) {

		if (!tagLibraries.containsKey(prefix)
				&& !predefinedLibraryMap.containsKey(prefix)) {
			return true;
		}

		// check for duplicate definitions of predefined libraries, this is ok,
		// too.
		if (id.equals(predefinedLibraryMap.get(prefix))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLibraryDefined(String libaryId) {
		return tagLibraries.containsValue(libaryId);
	}

	public String getLibraryIdFromPrefix(String prefix) {
		String id = (String) tagLibraries.get(prefix);
		if (id == null) {
			id = (String) predefinedLibraryMap.get(prefix);
		}
		return id;
	}

	/**
	 * Defined a prefix for a tag library id. If a prefix for the tag library id
	 * is already defined in the predefined librarys map, then that prefix is
	 * removed.
	 * 
	 * @param prefix
	 *            a prefix
	 * @param libraryId
	 *            a library id
	 */
	public void add(String prefix, String libraryId) {
		tagLibraries.put(prefix, libraryId);

		for (Iterator i = predefinedLibraryMap.entrySet().iterator(); i
				.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			if (libraryId.equals(entry.getValue())) {
				i.remove();
			}
		}

		tags = null; // clear the tags list cache
	}

	/**
	 * Return the TagDefinition of the selected tag.
	 * 
	 * @param tagName
	 *            the QName of the tag
	 * @return the TagDefinition
	 */
	public TagDefinition getTagDefinition(String tagName) {
		int sepIndex = tagName.indexOf(':');
		String prefix = sepIndex == -1 ? "" : tagName.substring(0, sepIndex); //$NON-NLS-1$
		String tagNCName = sepIndex == -1 ? tagName : tagName
				.substring(sepIndex + 1);
		String id = getLibraryIdFromPrefix(prefix.toLowerCase());
		TagLibrary tagLibrary = tagLibraryResolver.getLibrary(id);
		return tagLibrary.getTagDefinition(tagNCName);
	}

	/**
	 * Test whether the tag name passed is know to this manager
	 * 
	 * @return <code>true</code> if the tag matches one of the libraries,
	 *         <code>false</code> otherwise.
	 */
	public boolean isKnownTag(String tagName) {
		boolean knownTag = false;

		int sepIndex = tagName.indexOf(':');
		String prefix = sepIndex == -1 ? "" : tagName.substring(0, sepIndex); //$NON-NLS-1$
		String tagNCName = sepIndex == -1 ? tagName : tagName
				.substring(sepIndex + 1);
		String id = getLibraryIdFromPrefix(prefix.toLowerCase());
		if (id != null) {
			TagLibrary tagLibrary = tagLibraryResolver.getLibrary(id);
			if (tagLibrary != null) {
				knownTag = tagLibrary.hasTag(tagNCName);
			}
		}
		return knownTag;
	}

	/**
	 * Test whether the tag name passed is known to be an invalid name by the
	 * manager. The tag name is known to be invalid if it has a known prefix,
	 * but an unknown unqualified tag name.
	 * 
	 * @return <code>true</code> if the tag matches one of the libraries,
	 *         <code>false</code> otherwise.
	 */
	public boolean isKnownInvalidTagName(String tagName) {
		boolean knownInvalidTagName = false;

		int sepIndex = tagName.indexOf(':');
		String prefix = sepIndex == -1 ? "" : tagName.substring(0, sepIndex); //$NON-NLS-1$
		String tagNCName = sepIndex == -1 ? tagName : tagName
				.substring(sepIndex + 1);
		String id = getLibraryIdFromPrefix(prefix.toLowerCase());
		if (id != null) {
			TagLibrary tagLibrary = tagLibraryResolver.getLibrary(id);
			if (tagLibrary != null && prefix.length() > 0) {
				knownInvalidTagName = !tagLibrary.hasTag(tagNCName);
			}
		}
		return knownInvalidTagName;

	}

	public TagLibraryReference[] getTagLibraryReferences() {
		List result = new ArrayList(predefinedLibraryMap.size()
				+ tagLibraries.size());
		for (Iterator i = predefinedLibraryMap.entrySet().iterator(); i
				.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			result.add(new TagLibraryReferenceImpl((String) entry.getKey(),
					(String) entry.getValue(), true));
		}
		for (Iterator i = tagLibraries.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			result.add(new TagLibraryReferenceImpl((String) entry.getKey(),
					(String) entry.getValue(), false));
		}
		return (TagLibraryReference[]) result
				.toArray(new TagLibraryReference[result.size()]);
	}

	public String[] getKnownTagNames() {
		List localTags = tags; // protect against concurrent access
		if (localTags == null) {
			localTags = new ArrayList();
			for (Iterator i = tagLibraries.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				String prefix = (String) entry.getKey();
				String id = (String) entry.getValue();
				TagLibrary tagLibrary = tagLibraryResolver.getLibrary(id);
				final String[] tagNames = tagLibrary.getTagNames();

				for (int j = 0; j < tagNames.length; j++) {
					localTags.add(prefix + "." + tagNames[j]); //$NON-NLS-1$
				}
			}
		}
		return (String[]) localTags.toArray(new String[localTags.size()]);
	}
}
