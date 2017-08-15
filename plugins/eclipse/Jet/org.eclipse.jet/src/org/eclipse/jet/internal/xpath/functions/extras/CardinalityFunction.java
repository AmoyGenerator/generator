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

import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathUtil;

	/*
	 *  End custom imports
	 */

public class CardinalityFunction implements XPathFunction {

	public CardinalityFunction() {
		super();
	}

	public Object evaluate(List args) {
	/*
	 *  Begin function logic
	 */

		NodeSet list = (NodeSet) args.get(0);
		int count = list.size();
		String buffer = XPathUtil.xpathString(args.get(1)).toLowerCase();
		
		boolean result = false;
		if ((count == 0) && (buffer.indexOf('0') > -1)) { result = true; }
		if ((count == 1) && (buffer.indexOf('1') > -1)) { result = true; }
		if ((count  > 1) && (buffer.indexOf('m') > -1)) { result = true; }

	return new Boolean(result);

	/*
	 * End function logic
	 */
	}

}
