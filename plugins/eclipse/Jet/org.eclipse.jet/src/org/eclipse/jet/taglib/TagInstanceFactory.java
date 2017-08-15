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
 * $Id: TagInstanceFactory.java,v 1.1 2007/11/29 21:37:48 pelder Exp $
 */
package org.eclipse.jet.taglib;

/**
 * Define behavior of a factory for tags in a class library
 */
public interface TagInstanceFactory
{
  CustomTag createCustomTag(String name);
  
  String libraryId();
}
