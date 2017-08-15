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
 * $Id: TagFactory.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.taglib;


/**
 * Factory used by compiled JET2 templates to instantiate 
 * custom XML Elements.
 * <p>
 * This class is not typically used directly by clients.
 * </p>
 */
public interface TagFactory
{

  RuntimeTagElement createRuntimeTag(String libraryId, String tagNCName, String tagQName, TagInfo tagInfo);

}
