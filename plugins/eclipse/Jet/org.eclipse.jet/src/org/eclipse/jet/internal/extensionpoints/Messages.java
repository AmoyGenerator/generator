/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
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
 * $Id: Messages.java,v 1.2 2008/09/12 12:56:28 pelder Exp $
 */
package org.eclipse.jet.internal.extensionpoints;


import org.eclipse.osgi.util.NLS;


public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.jet.internal.extensionpoints.messages"; //$NON-NLS-1$

  public static String StaleXPathFunctionWrapper_DefiningPluginNotAvailable;

  public static String UnableToLoadXPathFunctionWrapper_ErrorLoadingFunction;

  public static String XPathFunctionLazyLoadWrapper_XPathFunctionNotImplemented;

  public static String XPathFunctionNotImplementedWrapper_XPathFunctionNotImplemented;
  static
  {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
