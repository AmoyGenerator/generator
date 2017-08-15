/**
 * <copyright>
 *
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
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
 */

package org.eclipse.jet.internal.xpath.functions.extras;

import java.util.List;

import org.eclipse.jet.internal.xpath.functions.StringFunction;
import org.eclipse.jet.xpath.XPathFunction;

	/*
	 *  End custom imports
	 */

public class XmlEncodeFunction implements XPathFunction {

	public XmlEncodeFunction() {
		super();
	}

	public Object evaluate(List args) {
	/*
	 *  Begin function logic
	 */

		String buffer = StringFunction.evaluate(args.get(0));

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if (c == '&') { sb.append("&amp;"); }  //$NON-NLS-1$
			else if (c == '<') { sb.append("&lt;"); } //$NON-NLS-1$
			else if (c == '>') { sb.append("&gt;"); } //$NON-NLS-1$
			else if (c == '\'') { sb.append("&apos;"); } //$NON-NLS-1$
			else if (c == '"') { sb.append("&quot;"); } //$NON-NLS-1$
			else {sb.append(c); }
		}
		return sb.toString();
	
	/*
	 * End function logic
	 */
	}

}
