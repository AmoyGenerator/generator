/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.taglib.control;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.exceptions.MissingRequiredAttributeException;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the Contributed Control Tags tag 'replaceStrings'.
 *
 */
public class ReplaceStringsTag extends AbstractFunctionTag {


	private String _replace = null;

	private String _with = null;

	/**
	 *  Begin custom declarations
	 */

	/**
	 * End custom declarations
	 */

	public ReplaceStringsTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
	 */
	public String doFunction(TagInfo tagInfo, JET2Context context, String bodyContent) throws JET2TagException {

		/**
		 * Get the "replace" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_replace = getAttribute("replace"); //$NON-NLS-1$
		if (_replace == null) {
			throw new MissingRequiredAttributeException("replace"); //$NON-NLS-1$
		}

		/**
		 * Get the "with" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_with = getAttribute("with"); //$NON-NLS-1$
		if (_with == null) {
			throw new MissingRequiredAttributeException("with"); //$NON-NLS-1$
		}

		/**
	 	 *  Begin doStart logic
		 */

		String[] replace = getStrings(_replace);
		String[] with = getStrings(_with);

		String buffer = replaceString(bodyContent,replace,with);
			
		return buffer;
		
		/**
		 * End doStart logic
		 */
	}

	/**
	 *  Begin tag-specific methods
	 */

	/*
	 * Parse the value of the specified attribute as a string separated list.
	 * 
	 * The following escapes are supported:  \r \n \t \, \\ \&lt; \&gt; \&amp; \amp; \apos; \quot;
	 */
	public String[] getStrings(String buffer) throws JET2TagException {
		List v = new ArrayList();
		StringBuffer sb = new StringBuffer();
		int offset = 0;
		while (offset < buffer.length()) {
			char c = buffer.charAt(offset);
			if (c == ',') {
				v.add(sb.toString());
				sb = new StringBuffer();
				offset++;
			} else if (c == '\\') {
				try {
					c = buffer.charAt(offset+1);
					if (c == 'r') {
						sb.append("\r"); //$NON-NLS-1$
						offset = offset + 2;
					} else if (c == 'n') {
						sb.append("\n"); //$NON-NLS-1$
						offset = offset + 2;
					} else if (c == 't') {
						sb.append("\t"); //$NON-NLS-1$
						offset = offset + 2;
					} else if (c == '\\') {
						sb.append("\\"); //$NON-NLS-1$
						offset = offset + 2;
					} else if (c == ',') {
						sb.append(","); //$NON-NLS-1$
						offset = offset + 2;
					} else if (c == '&') {
						if (buffer.substring(offset+1).startsWith("&lt;")) {  //$NON-NLS-1$
							sb.append("<"); //$NON-NLS-1$
							offset = offset + 5;
						} else if (buffer.substring(offset+1).startsWith("&gt;")) {  //$NON-NLS-1$
							sb.append(">"); //$NON-NLS-1$
							offset = offset + 5;
						} else if (buffer.substring(offset+1).startsWith("&amp;")) {  //$NON-NLS-1$
							sb.append("&"); //$NON-NLS-1$
							offset = offset + 6;
						} else if (buffer.substring(offset+1).startsWith("&apos;")) {  //$NON-NLS-1$
							sb.append("'"); //$NON-NLS-1$
							offset = offset + 7;
						} else if (buffer.substring(offset+1).startsWith("&quot;")) {  //$NON-NLS-1$
							sb.append("\""); //$NON-NLS-1$
							offset = offset + 7;
						} else {
							sb.append("\\&"); //$NON-NLS-1$
							offset = offset + 2;
						}						
					} else {
						sb.append("\\"); //$NON-NLS-1$
						offset++;
					}
				} catch (Exception e) {
					throw new JET2TagException(JET2Messages.ReplaceStringsTag_BadList+buffer);
				} 
			} else {
				sb.append(c);
				offset++;
			}
		}
		v.add(sb.toString());
		String[] result = (String[])v.toArray(new String[v.size()]);
		
		return result;
	}

	private String replaceString(String buffer, String[] replace, String[] with) throws JET2TagException {
		if (replace.length != with.length) {
			throw new JET2TagException(JET2Messages.ReplaceStringsTag_ListsNotSameLength);
		}
		for (int i = 0; i < replace.length; i++) {
			if (replace[i].length() == 0) {
				throw new JET2TagException(JET2Messages.ReplaceStringsTag_EmptyList);
			}
		}
		StringBuffer sb = new StringBuffer();
		int offset = 0;
		while (offset < buffer.length()) {
			boolean match = false;
			int i = 0;
			for (i = 0; (i < replace.length) & !match; i++) {
				if (buffer.substring(offset).startsWith(replace[i])) {
					match = true;
					sb.append(with[i]);
					offset = offset + replace[i].length();
				}
			}
			if (!match) {
				sb.append(buffer.charAt(offset));
				offset++;
			}
		}
		
		return sb.toString();		
	}

	/**
	 * End tag-specific methods
	 */


}
