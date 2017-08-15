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
 * $Id: Messages.java,v 1.1 2008/04/23 01:16:17 pelder Exp $
 */
package org.eclipse.jet.internal.runtime.model;


import org.eclipse.osgi.util.NLS;


public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.jet.internal.runtime.model.messages"; //$NON-NLS-1$

  public static String EntityResolverFactory_BundleNotResolved;

  public static String EntityResolverFactory_FileNotFound;
  static
  {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
