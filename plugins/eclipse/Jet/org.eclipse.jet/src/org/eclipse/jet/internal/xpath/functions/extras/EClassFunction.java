/**
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 */
package org.eclipse.jet.internal.xpath.functions.extras;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jet.xpath.Context;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.jet.xpath.XPathFunctionWithContext;

/**
 * Implement the XPath function 'emf.eClass( [Object object] )'
 *
 */
public class EClassFunction implements XPathFunction,
		XPathFunctionWithContext {

	private Context context;

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
	 */
	public Object evaluate(List args) {
		Object target = args.size() == 0 ? context.getContextNode() : getFirstObject(args.get(0));
		
		if(target instanceof EObject) {
			return Collections.singletonList(((EObject)target).eClass());
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	private Object getFirstObject(Object object) {
		if(object instanceof Collection) {
			final Collection c = (Collection)object;
			return c.size() > 0 ? c.iterator().next() : null;
		} else {
			return object;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.xpath.XPathFunctionWithContext#setContext(org.eclipse.jet.xpath.Context)
	 */
	public void setContext(Context context) {
		this.context = context;
		// TODO Auto-generated method stub

	}

}
