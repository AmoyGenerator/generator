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

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.exceptions.MissingRequiredAttributeException;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.transform.TransformContextExtender;

/**
 * Implement the Contributed Control Tags tag 'loadContent'.
 *
 */
public class LoadContentTag extends AbstractFunctionTag {


	private String _var = null;

	private String _type = null;

	private String _loader = null;

	/**
	 *  Begin custom declarations
	 */

	/**
	 * End custom declarations
	 */

	public LoadContentTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
	 */
	public String doFunction(TagInfo tagInfo, JET2Context context, String bodyContent) throws JET2TagException {

		/**
		 * Get the "var" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_var = getAttribute("var"); //$NON-NLS-1$
		if (_var == null) {
			throw new MissingRequiredAttributeException("var"); //$NON-NLS-1$
		}

		/**
		 * Get the "type" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_type = getAttribute("type"); //$NON-NLS-1$

		/**
		 * Get the "loader" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_loader = getAttribute("loader"); //$NON-NLS-1$

		/**
	 	 *  Begin doStart logic
		 */

		try {
			Object modelRoot = TransformContextExtender.loadModelFromString(bodyContent, _loader , _type);
			context.setVariable(_var, modelRoot);
		} catch (Exception e) {
			throw new JET2TagException(e);
		}
		
	    return ""; //$NON-NLS-1$
		
		/**
		 * End doStart logic
		 */
	}

	/**
	 *  Begin tag-specific methods
	 */

	/**
	 * End tag-specific methods
	 */


}
