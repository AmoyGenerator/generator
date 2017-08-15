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

package org.eclipse.jet.internal.taglib.format;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.exceptions.MissingRequiredAttributeException;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;



/**
 * Implement the Contributed Function Tags tag 'formatNow'.
 *
 */
public class FormatNowTag extends AbstractEmptyTag {


	private String _pattern = null;

	/*
	 *  Begin tag-specific declarations
	 */

	/*
	 * End tag-specific declarations
	 */
	
	public FormatNowTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
	 */
	public void doAction(TagInfo tagInfo, JET2Context context, JET2Writer out) throws JET2TagException {

		/**
		 * Get the "pattern" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_pattern = getAttribute("pattern"); //$NON-NLS-1$
		if (_pattern == null) {
			throw new MissingRequiredAttributeException("pattern"); //$NON-NLS-1$
		}

		/**
	 	 *  Begin doAction logic
		 */

		SimpleDateFormat f = new SimpleDateFormat(_pattern);
		String buf = f.format(new Date());
		out.write(buf);
			
		/**
		 * End doAction logic
		 */

	}

	/**
	 *  Begin custom methods
	 */

	/**
	 * End custom methods
	 */

}
