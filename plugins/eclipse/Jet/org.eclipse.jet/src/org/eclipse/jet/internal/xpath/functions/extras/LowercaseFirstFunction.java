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

public class LowercaseFirstFunction implements XPathFunction {

	public LowercaseFirstFunction() {
		super();
	}

	public Object evaluate(List args) {
	/*
	 *  Begin function logic
	 */

		String buffer = StringFunction.evaluate(args.get(0));
		buffer = buffer.length() > 0 ? buffer.substring(0,1).toLowerCase() + buffer.substring(1) : buffer;

		return buffer;
	
	/*
	 * End function logic
	 */
	}

}
