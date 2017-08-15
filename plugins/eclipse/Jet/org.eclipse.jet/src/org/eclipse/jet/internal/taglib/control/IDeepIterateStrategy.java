/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: IDeepIterateStrategy.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import java.util.Collection;

import org.eclipse.jet.taglib.JET2TagException;

/**
 * @author pelder
 */
public interface IDeepIterateStrategy
{
  /**
   * perform the deep iterate search
   * @return a collection of {@link DeepIterateEntry} elements
   */
  Collection search();
  
  /**
   * Indicate whether the strategy supports &lt;c:deepContent&gt; tags.
   * @return <code>true</code> if such tags are supported, <code>false</code> otherwise
   */
  boolean supportsDeepContent();
  
  /**
   * Throw an appropriately detailed {@link JET2TagException} if deep content is not allowed, otherwise
   * return.
   * @throws JET2TagException if deepContent is not allowed.
   */
  void checkDeepContentAllowed() throws JET2TagException;
}
