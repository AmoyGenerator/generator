/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: TagInfo.java,v 1.3 2009/04/06 17:55:06 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Define contextual information for a custom tag. The class is immutable. The
 * Tag context is the mechanism by which custom tag code accesses the tag
 * parameters code in a template.
 * <p>
 * Contextual information includes:
 * </p>
 * <bl> <li>the tag location in the source template (line # and start, end
 * offsets)</li> <li>the raw attribute values as specified in template. </bl>
 * <p>
 * <b>This class is instantiated in the compiled JET2 template. Clients would
 * not normally instantiate instances of this class.</b>
 * </p>
 */
public final class TagInfo {
	private final Map attrMap;

	private final String tagName;

	private final boolean expression;

	protected final int line;

	protected final int col;

	public TagInfo(String tagName, int line, int col, String[] attrNames,
			String attrValues[]) {
		
		this.line = line;
		this.col = col;
		this.expression = tagName.startsWith("${");
		this.tagName = tagName;

		if (attrNames == null || attrValues == null) {
			throw new NullPointerException();
		}
		if (attrNames.length != attrValues.length) {
			throw new IllegalArgumentException();
		}
		// use Linked has map so that attributes retain their original order.
		this.attrMap = new LinkedHashMap(attrNames.length);
		for (int i = 0; i < attrNames.length; i++) {
			attrMap.put(attrNames[i], attrValues[i]);
		}
	}

	/**
	 * Construct a TagInfo
	 * 
	 * @param tagName
	 * @param line
	 * @param start
	 * @param end
	 * @param attrNames
	 * @param attrValues
	 * @deprecated Use {@link #TagInfo(String, int, int, String[], String[])}
	 *             instead.
	 */
	public TagInfo(String tagName, int line, int start, int end,
			String[] attrNames, String[] attrValues) {
		this(tagName, line, 1, attrNames, attrValues);
	}

	/**
	 * Return the value of an attribute.
	 * 
	 * @param name
	 *            the attribute name. Cannot be <code>null</code>.
	 * @return the attribute value, or <code>null</code> if the attribute was
	 *         not set on the tag.
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>.
	 */
	public final String getAttribute(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		return (String) attrMap.get(name);
	}

	/**
	 * Test whether an attribute value was set.
	 * 
	 * @param name
	 *            the attribute name. Cannot be <code>null</code>.
	 * @return <code>true</code> if the attribute was set, <code>false</code>
	 *         otherwise.
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>.
	 */
	public final boolean hasAttribute(String name) {
		return attrMap.containsKey(name);
	}

	public String toString() {
		return "[" + line + ":" + col + " " + tagName + " " + attrMap + "]"; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	/**
	 * Return the tag name, as specified in the input, complete with namespace
	 * prefix.
	 * 
	 * @return Returns the tagName.
	 */
	public final String getTagName() {
		return tagName;
	}

	/**
	 * Return an array of attribute names
	 * 
	 * @return an possibly empty array of String values.
	 */
	public final String[] getAttributeNames() {
		return (String[]) attrMap.keySet().toArray(new String[attrMap.size()]);
	}

	public final String displayString() {
		if (expression) {
			return tagName;
		} else {
			StringBuffer displayString = new StringBuffer();
			displayString.append('<').append(tagName);
			for (Iterator i = attrMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				displayString.append(' ')
					.append((String) entry.getKey())
					.append('=')
					.append('\'')
					.append(((String) entry.getValue()).replaceAll("'",	"''"))
					.append('\'');
			}
			displayString.append('>');
			return displayString.toString();
		}
	}

	/**
	 * Return the one-based column number of the start of the tag withing the
	 * tag's start line.
	 * 
	 * @return Returns the col.
	 */
	public final int getCol() {
		return col;
	}

	/**
	 * Return the one-based line number of the start of the tag.
	 * 
	 * @return Returns the line.
	 */
	public final int getLine() {
		return line;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + line;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagInfo other = (TagInfo) obj;
		if (col != other.col)
			return false;
		if (line != other.line)
			return false;
		return true;
	}
	
}
