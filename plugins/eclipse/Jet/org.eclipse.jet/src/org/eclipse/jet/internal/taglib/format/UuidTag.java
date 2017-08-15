/**
 * <copyright>
 *
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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

package org.eclipse.jet.internal.taglib.format;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Implement the Contributed Function Tags tag 'uuid'.
 *
 */
public class UuidTag extends AbstractEmptyTag {


	/*
	 *  Begin tag-specific declarations
	 */

	/*
	 * End tag-specific declarations
	 */
	
	public UuidTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
	 */
	public void doAction(TagInfo tagInfo, JET2Context context, JET2Writer out) throws JET2TagException {

	  if(!context.hasVariable(FormatContextExtender.FORMAT_TAGS_TEST_FLAG)) {
		out.write(EcoreUtil.generateUUID());
	  } else {
	    out.write(FormatContextExtender.getInstance(context).getPseudoUUID());
	  }
			
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
