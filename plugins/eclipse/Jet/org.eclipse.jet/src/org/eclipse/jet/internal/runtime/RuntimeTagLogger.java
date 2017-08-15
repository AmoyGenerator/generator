/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.jet.internal.runtime;


import org.eclipse.jet.taglib.TagInfo;


/**
 * A listener that can monitor the actions of runtime tags.
 */
public interface RuntimeTagLogger
{
 
  void log(String message, TagInfo td, String templatePath, int level);

}
