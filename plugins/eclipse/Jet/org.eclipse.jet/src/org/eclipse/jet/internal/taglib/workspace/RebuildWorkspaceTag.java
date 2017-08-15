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

package org.eclipse.jet.internal.taglib.workspace;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * Implement the Contributed Workspace Tags tag 'rebuildWorkspace'.
 *
 */
public class RebuildWorkspaceTag extends AbstractEmptyTag {


	/*
	 *  Begin tag-specific declarations
	 */

	/*
	 * End tag-specific declarations
	 */
	
	public RebuildWorkspaceTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
	 */
	public void doAction(TagInfo tagInfo, JET2Context context, JET2Writer out) throws JET2TagException {

		TransformContextExtender tce = TransformContextExtender.getInstance(context);
		String templatePath = tce.getTemplatePath();

		WorkspaceContextExtender wsExtender = WorkspaceContextExtender.getInstance(context);
		wsExtender.addAction(new RebuildWorkspaceAction(tagInfo, templatePath));
			
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
