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
package org.eclipse.jet.xpath;


import org.eclipse.jet.internal.xpath.XPathFactoryImpl;


/**
 * Factory for instantiating XPath implementations
 *
 */
public abstract class XPathFactory
{

  /**
   * 
   */
  protected XPathFactory()
  {
    super();
  }

  public static XPathFactory newInstance()
  {
    return new XPathFactoryImpl();
  }

  public abstract XPath newXPath(IAnnotationManager annotationManager);
}
