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

import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathUtil;

	/*
	 *  End custom imports
	 */

public class CamelCaseFunction implements XPathFunction {

	public CamelCaseFunction() {
		super();
	}

	public Object evaluate(List args) {
	/*
	 *  Begin function logic
	 */

		String buffer = XPathUtil.xpathString(args.get(0));

	    boolean first = true;
	    StringBuffer sb = new StringBuffer(buffer.length());
	    for (int index = 0; index < buffer.length(); index++) {
	    	char c = buffer.charAt(index);
	    	if (Character.isWhitespace(c)) { 
	    		first = true;
            } else if(Character.isJavaIdentifierPart(c)) {
//	    	} else {
	    		if (first) { c = Character.toUpperCase(c); }
	    		else { c = Character.toLowerCase(c); }
	    		sb.append(c); 
	    		first = false;
	    	}
	    }
	    buffer = sb.toString();

	    return sb.toString();
	
	/*
	 * End function logic
	 */
	}

}
