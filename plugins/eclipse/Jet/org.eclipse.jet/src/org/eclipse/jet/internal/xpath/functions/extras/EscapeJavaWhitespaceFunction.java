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

import org.eclipse.jet.internal.JavaUtil;
import org.eclipse.jet.internal.xpath.functions.StringFunction;
import org.eclipse.jet.xpath.XPathFunction;

	/*
	 *  End custom imports
	 */

public class EscapeJavaWhitespaceFunction implements XPathFunction {

	public EscapeJavaWhitespaceFunction() {
		super();
	}

	public Object evaluate(List args) {
	/*
	 *  Begin function logic
	 */

		String buffer = StringFunction.evaluate(args.get(0));

		return JavaUtil.escapeJavaString(buffer.toCharArray());

	/*
	 * End function logic
	 */
	}

}
