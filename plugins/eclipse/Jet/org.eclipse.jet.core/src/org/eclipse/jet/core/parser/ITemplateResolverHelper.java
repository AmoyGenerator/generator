/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: ITemplateResolverHelper.java,v 1.1 2007/04/04 14:53:54 pelder Exp $
 */
package org.eclipse.jet.core.parser;


/**
 * Protocal for template resolver that is responsible for resolving templates from 
 * a single base location which is communicated to the helper on creation. 
 * Helpers are called by {@link DefaultTemplateResolver} implementations.
 * @see ITemplateResolverHelperFactory
 */
public interface ITemplateResolverHelper {

	/**
	 * Test if templatePath exists
	 * @param templatePath a template Path
	 * @return <code>true</code>
	 */
	boolean inputExists(String templatePath);

	/**
	 * Create a template input for the given templatePath. The templatePath
	 * will have been valided with {@link #inputExists(String)} prior to
	 * calling this method
	 * @param templatePath a template path
	 * @return the template input object
	 */
	ITemplateInput createTemplateInput(String templatePath);

}
