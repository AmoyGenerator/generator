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
package org.eclipse.jet.internal.xpath;


import org.eclipse.jet.xpath.IAnnotationManager;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathFactory;


/**
 * XPath Factory implementation
 *
 */
public class XPathFactoryImpl extends XPathFactory
{

  /**
   * 
   */
  public XPathFactoryImpl()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFactory#newXPath()
   */
  public XPath newXPath(IAnnotationManager annotationManager)
  {
    return new XPathImpl(annotationManager);
  }

}
